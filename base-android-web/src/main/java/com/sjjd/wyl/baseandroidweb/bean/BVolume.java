package com.sjjd.wyl.baseandroidweb.bean;

/**
 * Created by wyl on 2019/9/4.
 */
public class BVolume {
    private String type;
    private Data data;

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        private String size;

        public String getSize() {
            return size == null ? "" : size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }
}
