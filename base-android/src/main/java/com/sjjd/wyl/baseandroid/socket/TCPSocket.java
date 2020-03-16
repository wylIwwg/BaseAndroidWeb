package com.sjjd.wyl.baseandroid.socket;
/**
 * Created by wyl on 2019/12/17.
 */

import android.content.Context;

import com.sjjd.wyl.baseandroid.tools.IConfigs;
import com.sjjd.wyl.baseandroid.tools.ToolLog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wyl on 2018/11/22.
 */
public class TCPSocket {
    private static final String TAG = " TCPSocket ";
    private String LABEL = "\n";
    private ExecutorService mThreadPool;
    private Socket mSocket;
    private BufferedReader br;
    private PrintWriter pw;
    private HeartbeatTimer timer;
    private long lastReceiveTime = 0;
    private Context mContext;
    private final Object mObject = new Object();
    private OnConnectionStateListener mListener;
    private OnMessageReceiveListener mMessageListener;

    private static final long HEARTBEAT_MESSAGE_DURATION = 2 * 1000;//心跳反应时间
    private boolean alive = false;




    private MsgThread mMsgThread;

    public TCPSocket(Context context) {
        mContext = context;
        int cpuNumbers = Runtime.getRuntime().availableProcessors();
        // 根据CPU数目初始化线程池
        mThreadPool = Executors.newFixedThreadPool(cpuNumbers * IConfigs.POOL_SIZE);
        // 记录创建对象时的时间
        lastReceiveTime = System.currentTimeMillis();
        mMsgThread = new MsgThread();

    }


    public void startTcpSocket(final String ip, final String port) {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (startTcpConnection(ip, Integer.valueOf(port))) {// 尝试建立 TCP 连接
                    if (mListener != null) {
                        mListener.onSuccess();
                    }
                    alive = true;
                    startReceiveTcpThread();//TCP创建成功 开启数据接收线程
                    startHeartbeatTimer();//发送心跳
                } else {
                    if (mListener != null) {
                        alive = false;
                        stopTcpConnection();//创建失败 关闭资源
                        mListener.onFailed(IConfigs.MSG_CREATE_TCP_ERROR);
                    }
                }
            }
        });
    }

    public void setOnConnectionStateListener(OnConnectionStateListener listener) {
        this.mListener = listener;
    }

    public void addOnMessageReceiveListener(OnMessageReceiveListener listener) {
        mMessageListener = listener;
    }

    /**
     * 创建接收线程
     */
    class MsgThread implements Runnable {

        @Override
        public void run() {
            if (null != mSocket)
                try {
                    byte[] buffer = new byte[1024 * 1024 * 10];
                    int length = 0;
                    String message = "";
                    InputStream is = mSocket.getInputStream();
                    synchronized (mObject) {
                        while (alive && mSocket != null && mSocket.isConnected() && !mSocket.isClosed() && ((length = is.read(buffer)) != -1)) {
                            if (length > 0) {
                                message = new String(Arrays.copyOf(buffer, length));
                                handleReceiveTcpMessage(message);
                            }
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


        }

    }

    private void startReceiveTcpThread() {

        if (mMsgThread != null)
            mThreadPool.execute(mMsgThread);
    }

    /**
     * 处理 tcp 收到的消息
     *
     * @param line
     */
    String result = "";
    String remainder = "";

    private void handleReceiveTcpMessage(String line) {
        ToolLog.e(TAG, "\n接收 tcp 消息：" + line + "\n");
        lastReceiveTime = System.currentTimeMillis();

        if (line.contains(LABEL)) {
            int mIndex = line.indexOf(LABEL);//获取标识符索引
            ToolLog.e(TAG, "【mIndex】 : " + mIndex);
            // result += line.replace(LABEL, "");

            // result += line.replace(LABEL, "");


            result += line.substring(0, mIndex);//获取标识符前段字符
            // remainder = line.substring(mIndex, line.length() - 1);//获取标识符后段字符
            ToolLog.e(TAG, "【找到结束符】 : " + result);
            if (result.startsWith("{") && result.endsWith("}")) {
                if (mMessageListener != null) {
                    mMessageListener.onMessageReceived(result);
                    result="";
                    String more =line.substring(mIndex+1);
                    if(more.length()>0){
                        ToolLog.e(TAG, "【more】 : " + more);
                        handleReceiveTcpMessage(more);
                    }

                }
            }

        } else {
            result += line;
            ToolLog.e(TAG, "【等待结束符】: " + result);
        }


    }

    public void sendTcpMessage(String json) {
        if (pw != null)
            pw.println(json);
        ToolLog.e(TAG, "tcp 消息发送成功..." + json);
    }

    /**
     * 启动心跳
     */
    private void startHeartbeatTimer() {
        if (timer == null) {
            timer = new HeartbeatTimer();
        }
        timer.setOnScheduleListener(new HeartbeatTimer.OnScheduleListener() {
            @Override
            public void onSchedule() {

                if (SocketManager.getInstance(mContext).getPING() != null && SocketManager.getInstance(mContext).getPING().length() > 0) {
                    //如果ping不为null 则发送心跳ping
                    long duration = System.currentTimeMillis() - lastReceiveTime;
                    ToolLog.e(TAG, "timer is onSchedule..." + " duration:" + duration);
                    if (duration > SocketManager.getInstance(mContext).getTIME_OUT()) {//若超过十五秒都没收到我的心跳包，则认为对方不在线。
                        ToolLog.e(TAG, "tcp ping 超时， 断开连接");
                        stopTcpConnection();
                        if (mListener != null) {
                            alive = false;
                            mListener.onFailed(IConfigs.MSG_PING_TCP_TIMEOUT);
                        }
                    } else if (duration > HEARTBEAT_MESSAGE_DURATION) {//若超过两秒他没收到我的心跳包，则重新发一个。
                        sendTcpMessage(SocketManager.getInstance(mContext).getPING());
                        //sendTcpMessage(jsonObject.toString());
                    }
                } else {
                    ToolLog.e(TAG, "onSchedule: 心跳为空 将不会发送");
                }

            }

        });
        timer.startTimer(10, SocketManager.getInstance(mContext).getHEARTBEAT_RATE());
    }

    public void stopHeartbeatTimer() {
        if (timer != null) {
            timer.exit();
            timer = null;
        }
    }

    /**
     * 尝试建立tcp连接
     *
     * @param ip
     * @param port
     */
    public boolean startTcpConnection(final String ip, final int port) {
        try {
            if (mSocket == null) {
                mSocket = new Socket(ip, port);
                mSocket.setKeepAlive(true);
                mSocket.setTcpNoDelay(true);
                mSocket.setReuseAddress(true);
            }
            InputStream is = mSocket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            OutputStream os = mSocket.getOutputStream();
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)), true);
            ToolLog.e(TAG, "tcp 创建成功...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        ToolLog.e(TAG, "tcp 创建失败...");
        return false;
    }

    public void stopTcpConnection() {
        try {
            alive = false;
            stopHeartbeatTimer();
            if (br != null) {
                br.close();
            }
            if (pw != null) {
                pw.close();
            }
            if (mThreadPool != null) {
                mThreadPool.shutdown();
                mThreadPool = null;
            }

            if (mSocket != null) {
                mSocket.close();
                mSocket = null;
            }

            if (mMsgThread != null) {
                mMsgThread = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
