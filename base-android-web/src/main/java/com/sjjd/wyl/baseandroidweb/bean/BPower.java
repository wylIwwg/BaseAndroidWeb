package com.sjjd.wyl.baseandroidweb.bean;

/**
 * Created by wyl on 2019/9/4.
 */
public class BPower {
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

        private String starTime;//开机时间
        private String endTime;//关机时间

        public String getStarTime() {
            return starTime == null ? "" : starTime;
        }

        public void setStarTime(String starTime) {
            this.starTime = starTime;
        }

        public String getEndTime() {
            return endTime == null ? "" : endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }
}
