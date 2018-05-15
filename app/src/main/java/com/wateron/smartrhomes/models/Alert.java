package com.wateron.smartrhomes.models;

/**
 * Created by Paranjay on 08-12-2017.
 */

public class Alert {


    private double qty;
    private String time;
    private int meterId;
    private int aptId;
    private String alarm_id;

    public String getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(String alarm_id) {
        this.alarm_id = alarm_id;
    }



    public Alert() {
    }

    public Alert(double qty, String time, int meterId, int aptId) {
        this.qty = qty;
        this.time = time;
        this.meterId = meterId;
        this.aptId = aptId;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMeterId() {
        return meterId;
    }

    public void setMeterId(int meterId) {
        this.meterId = meterId;
    }

    public int getAptId() {
        return aptId;
    }

    public void setAptId(int aptId) {
        this.aptId = aptId;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "qty=" + qty +
                ", time='" + time + '\'' +
                ", meterId=" + meterId +
                ", aptId=" + aptId +
                '}';
    }
}
