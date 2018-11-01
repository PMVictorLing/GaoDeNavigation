package com.lwc.gaodetest;

import java.util.List;

/**
 * Created by lingwancai on
 * 2018/10/15 17:45
 */
public class JsonBeanTest {

    /**
     * key : c0ba49e4b5981bb66fa90b40f67afacf
     * sid : 5716
     * tid : 1614720
     * trid : 100
     * points : [{"location":"28.19786,112.969687","locatetime":"1539595910000"},{"location":"27.833333,113.166666","locatetime":"1539595970000"}]
     */

    private String key;
    private int sid;
    private int tid;
    private int trid;
    private List<PointsBean> points;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getTrid() {
        return trid;
    }

    public void setTrid(int trid) {
        this.trid = trid;
    }

    public List<PointsBean> getPoints() {
        return points;
    }

    public void setPoints(List<PointsBean> points) {
        this.points = points;
    }

    public static class PointsBean {
        /**
         * location : 28.19786,112.969687
         * locatetime : 1539595910000
         */

        private String location;
        private String locatetime;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLocatetime() {
            return locatetime;
        }

        public void setLocatetime(String locatetime) {
            this.locatetime = locatetime;
        }
    }
}
