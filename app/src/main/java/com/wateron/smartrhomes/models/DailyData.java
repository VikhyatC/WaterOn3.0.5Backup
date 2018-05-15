package com.wateron.smartrhomes.models;

/**
 * Created by Paranjay on 08-12-2017.
 */

public class DailyData {

    private String date;
    private double value;
    private int meterId;
    private int aptId;

    public DailyData() {
    }

    public DailyData(String date, double value, int meterId, int aptId) {
        this.date = date;
        this.value = value;
        this.meterId = meterId;
        this.aptId = aptId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
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
        return "DailyData{" +
                "date=" + date +
                ", value=" + value +
                ", meterId=" + meterId +
                ", aptId=" + aptId +
                '}';
    }
}
