package com.sjjd.wyl.baseandroidweb.bean;

import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;

import java.nio.charset.Charset;

/**
 * Created by wyl on 2020/5/6.
 */
public class BPulse implements IPulseSendable {
    private String ping = "{\"type\":\"ping\"}";

    public void setPing(String ping) {
        this.ping = ping;
    }

    @Override
    public byte[] parse() {
        byte[] body = ping.getBytes(Charset.forName("utf-8"));
        byte[] result = new byte[8 + body.length];

        System.arraycopy(intToBytes(result.length), 0, result, 0, 4);
        result[4] = 1;
        result[5] = 1;
        result[6] = 1;
        result[7] = 1;
        System.arraycopy(body, 0, result, 8, body.length);
        return result;
        //[0, 0, 0, 15, 0, 0, 0, 123, 34, 116, 121, 112, 101, 34, 58, 34, 112, 105, 110, 103, 34, 125]
    }

    public static byte[] intToBytes(int value) {
        byte[] byte_src = new byte[4];
        byte_src[3] = (byte) ((value & 0xFF000000) >> 24);
        byte_src[2] = (byte) ((value & 0x00FF0000) >> 16);
        byte_src[1] = (byte) ((value & 0x0000FF00) >> 8);
        byte_src[0] = (byte) ((value & 0x000000FF));
        return byte_src;
    }
}
