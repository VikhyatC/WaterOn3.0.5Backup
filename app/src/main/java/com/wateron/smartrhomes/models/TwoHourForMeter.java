package com.wateron.smartrhomes.models;

/**
 * Created by Paranjay on 08-12-2017.
 */

public class TwoHourForMeter {

    private int meterId;
    private int aptId;
    private double value;
    private int slot;
    private String date;

    public TwoHourForMeter() {
    }

    public TwoHourForMeter(int meterId, int aptId, double value, int slot, String date) {
        this.meterId = meterId;
        this.aptId = aptId;
        this.value = value;
        this.slot = slot;
        this.date = date;
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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TwoHourForMeter{" +
                "meterId=" + meterId +
                ", aptId=" + aptId +
                ", value=" + value +
                ", slot=" + slot +
                ", date=" + date +
                '}';
    }
}
