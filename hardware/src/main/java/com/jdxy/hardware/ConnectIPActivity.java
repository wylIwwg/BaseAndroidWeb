package com.jdxy.hardware;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lvrenyang.io.NETPrinting;
import com.lvrenyang.io.Pos;
import com.lvrenyang.io.base.IOCallBack;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConnectIPActivity extends Activity implements OnClickListener, IOCallBack {

	//private static final String TAG = "ConnectIPActivity";
	
	ExecutorService es = Executors.newScheduledThreadPool(30);
	Pos mPos = new Pos();
	NETPrinting mNet = new NETPrinting();
	
	EditText inputIp, inputPort;
	Button btnConnect,btnDisconnect,btnPrint;
	
	ConnectIPActivity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connectip);
		
		mActivity = this;
		
		btnConnect = (Button) findViewById(R.id.buttonConnect);
		btnDisconnect = (Button) findViewById(R.id.buttonDisconnect);
		btnPrint = (Button) findViewById(R.id.buttonPrint);
		inputIp = (EditText) findViewById(R.id.editTextInputIp);
		inputPort = (EditText) findViewById(R.id.editTextInputPort);

		btnConnect.setOnClickListener(this);
		btnDisconnect.setOnClickListener(this);
		btnPrint.setOnClickListener(this);
		
		inputIp.setText("192.168.1.80");
		inputPort.setText("9100");
		btnConnect.setEnabled(true);
		btnDisconnect.setEnabled(false);
		btnPrint.setEnabled(false);
		
		mPos.Set(mNet);
		mNet.SetCallBack(this);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		btnDisconnect.performClick();
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {

		case R.id.buttonConnect:
			boolean valid = false;
			int port = 9100;
			String ip = "";
			try {
				ip = inputIp.getText().toString();
				if (null == IsIPValid(ip))
					throw new Exception("Invalid IP Address");
				port = Integer.parseInt(inputPort.getText().toString());
				valid = true;
			} catch (NumberFormatException e) {
				Toast.makeText(this, "Invalid Port Number", Toast.LENGTH_LONG)
						.show();
				valid = false;
			} catch (Exception e) {
				Toast.makeText(this, "Invalid IP Address", Toast.LENGTH_LONG)
						.show();
				valid = false;
			}
			if (valid) {
				// 进行下一步连接操作。
				Toast.makeText(this, "Connecting...", Toast.LENGTH_SHORT).show();
				btnConnect.setEnabled(false);
				btnDisconnect.setEnabled(false);
				btnPrint.setEnabled(false);
				es.submit(new TaskOpen(mNet,ip,port,mActivity));
			}
			break;

		case R.id.buttonDisconnect:
			es.submit(new TaskClose(mNet));
			break;

		case R.id.buttonPrint:
			btnPrint.setEnabled(false);
			es.submit(new TaskPrint(mPos));
			break;
			
		}
	}
	
	public static byte[] IsIPValid(String ip)
	{
		byte[] ipbytes = new byte[4];
		int valid = 0;
		int s,e;
		String ipstr = ip + ".";
		s = 0;
		for(e = 0; e < ipstr.length(); e++)
		{
			if ('.' == ipstr.charAt(e))
			{
				if ((e - s > 3) || (e - s) <= 0)	// 最长3个字符
					return null;
				
				int ipbyte = -1;
				try{
					ipbyte = Integer.parseInt(ipstr.substring(s, e));
					if (ipbyte < 0 || ipbyte > 255)
						return null;
					else
						ipbytes[valid] = (byte) ipbyte;
				}
				catch(NumberFormatException exce)
				{
					return null;
				}
				s = e + 1;
				valid++;
			}
		}
		if (valid == 4)
			return ipbytes;
		else
			return null;
	}
	
	public class TaskOpen implements Runnable
	{
		NETPrinting net = null;
		String ip = null;
		int port;
		Context context;
		
		public TaskOpen(NETPrinting net, String ip, int port, Context context)
		{
			this.net = net;
			this.ip = ip;
			this.port = port;
			this.context = context;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			net.Open(ip, port, 5000, context);
		}
	}
	
	static int dwWriteIndex = 1;
	public class TaskPrint implements Runnable
	{
		Pos pos = null;
		
		public TaskPrint(Pos pos)
		{
			this.pos = pos;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			final int bPrintResult = Prints.PrintTicket(getApplicationContext(), pos, AppStart.nPrintWidth, AppStart.bCutter, AppStart.bDrawer, AppStart.bBeeper, AppStart.nPrintCount, AppStart.nPrintContent, AppStart.nCompressMethod);
			final boolean bIsOpened = pos.GetIO().IsOpened();
			
			mActivity.runOnUiThread(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(mActivity.getApplicationContext(), (bPrintResult == 0) ? getResources().getString(R.string.printsuccess) : getResources().getString(R.string.printfailed) + " " + Prints.ResultCodeToString(bPrintResult), Toast.LENGTH_SHORT).show();
					mActivity.btnPrint.setEnabled(bIsOpened);
				}
			});

		}
		
	
	}
	
	public class TaskClose implements Runnable
	{
		NETPrinting net = null;
		
		public TaskClose(NETPrinting net)
		{
			this.net = net;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			net.Close();
		}
		
	}

	@Override
	public void OnOpen() {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				btnConnect.setEnabled(false);
				btnDisconnect.setEnabled(true);
				btnPrint.setEnabled(true);
			}
		});
	}

	@Override
	public void OnOpenFailed() {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				btnConnect.setEnabled(true);
				btnDisconnect.setEnabled(false);
				btnPrint.setEnabled(false);
			}
		});
	}
	
	@Override
	public void OnClose() {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				btnConnect.setEnabled(true);
				btnDisconnect.setEnabled(false);
				btnPrint.setEnabled(false);
			}
		});
	}
	
}