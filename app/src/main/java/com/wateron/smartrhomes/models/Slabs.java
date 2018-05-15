package com.wateron.smartrhomes.models;

/**
 * Created by Paranjay on 08-12-2017.
 */

public class Slabs {

    private int apTid;
    private double slabKl;
    private double slabPrice;
    private int slabLevel;

    public Slabs() {
    }

    public Slabs(int apTid, double slabKl, double slabPrice, int slabLevel) {
        this.apTid = apTid;
        this.slabKl = slabKl;
        this.slabPrice = slabPrice;
        this.slabLevel = slabLevel;
    }

    public int getApTid() {
        return apTid;
    }

    public void setApTid(int apTid) {
        this.apTid = apTid;
    }

    public double getSlabKl() {
        return slabKl;
    }

    public void setSlabKl(double slabKl) {
        this.slabKl = slabKl;
    }

    public double getSlabPrice() {
        return slabPrice;
    }

    public void setSlabPrice(double slabPrice) {
        this.slabPrice = slabPrice;
    }

    public int getSlabLevel() {
        return slabLevel;
    }

    public void setSlabLevel(int slabLevel) {
        this.slabLevel = slabLevel;
    }

    @Override
    public String toString() {
        return "Slabs{" +
                "apTid=" + apTid +
                ", slabKl=" + slabKl +
                ", slabPrice=" + slabPrice +
                ", slabLevel=" + slabLevel +
                '}';
    }
}
