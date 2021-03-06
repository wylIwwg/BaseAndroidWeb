package com.sjjd.wyl.baseandroidweb.anr;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wyl on 2019/2/26.
 */
public class ANRException extends RuntimeException {
    public ANRException() {
        super("ANR...");
        Thread mThread = Looper.getMainLooper().getThread();
        setStackTrace(mThread.getStackTrace());
        writeFile(mThread.getStackTrace());
    }

    private void writeFile(final StackTraceElement[] stackTrace) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/sjjd/anr/");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String name = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date(System.currentTimeMillis())) + ".txt";
                File anr = new File(dir, name);
                try {
                    OutputStreamWriter write = null;
                    write = new OutputStreamWriter(new FileOutputStream(anr), Charset.defaultCharset());
                    for (StackTraceElement trace : stackTrace) {
                        write.write(trace.toString() + "\n");
                    }
                    write.flush();
                    write.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //抛出空指针异常  来重启应用
                Context mContext = null;
                Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
            }
        }).start();

    }
}
