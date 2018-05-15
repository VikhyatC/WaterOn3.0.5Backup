package com.wateron.smartrhomes.models;

/**
 * Created by Paranjay on 13-12-2017.
 */

public class HAlert {

    private String date;
    private int value;
    private int aptId;
    public HAlert() {
    }

    public HAlert(String date, int value, int aptId) {
        this.date = date;
        this.value = value;
        this.aptId = aptId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getAptId() {
        return aptId;
    }

    public void setAptId(int aptId) {
        this.aptId = aptId;
    }
}
