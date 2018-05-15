package com.wateron.smartrhomes.models;

/**
 * Created by Paranjay on 08-12-2017.
 */

public class Apartment {

    private String name;
    private int id;
    private String society;
    private double fixedcharge;
    private double billAmount;
    private String billDate;
    private String billPaid;
    private String unitText;
    private String unitAbbrev;
    private String currencyText;
    private String currencyAbbrev;
    private String unitSymbol;
    private String currencySymbol;

    public Apartment() {
    }

    public Apartment(String name, int id, String society, double fixedcharge, double billAmount, String billDate, String billPaid, String unitText, String unitAbbrev, String currencyText, String currencyAbbrev, String unitSymbol, String currencySymbol) {
        this.name = name;
        this.id = id;
        this.society = society;
        this.fixedcharge = fixedcharge;
        this.billAmount = billAmount;
        this.billDate = billDate;
        this.billPaid = billPaid;
        this.unitText = unitText;
        this.unitAbbrev = unitAbbrev;
        this.currencyText = currencyText;
        this.currencyAbbrev = currencyAbbrev;
        this.unitSymbol = unitSymbol;
        this.currencySymbol = currencySymbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    public double getFixedcharge() {
        return fixedcharge;
    }

    public void setFixedcharge(double fixedcharge) {
        this.fixedcharge = fixedcharge;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillPaid() {
        return billPaid;
    }

    public void setBillPaid(String billPaid) {
        this.billPaid = billPaid;
    }

    public String getUnitText() {
        return unitText;
    }

    public void setUnitText(String unitText) {
        this.unitText = unitText;
    }

    public String getUnitAbbrev() {
        return unitAbbrev;
    }

    public void setUnitAbbrev(String unitAbbrev) {
        this.unitAbbrev = unitAbbrev;
    }

    public String getCurrencyText() {
        return currencyText;
    }

    public void setCurrencyText(String currencyText) {
        this.currencyText = currencyText;
    }

    public String getCurrencyAbbrev() {
        return currencyAbbrev;
    }

    public void setCurrencyAbbrev(String currencyAbbrev) {
        this.currencyAbbrev = currencyAbbrev;
    }

    public String getUnitSymbol() {
        return unitSymbol;
    }

    public void setUnitSymbol(String unitSymbol) {
        this.unitSymbol = unitSymbol;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", society='" + society + '\'' +
                ", fixedcharge=" + fixedcharge +
                ", billAmount=" + billAmount +
                ", billDate='" + billDate + '\'' +
                ", billPaid='" + billPaid + '\'' +
                ", unitText='" + unitText + '\'' +
                ", unitAbbrev='" + unitAbbrev + '\'' +
                ", currencyText='" + currencyText + '\'' +
                ", currencyAbbrev='" + currencyAbbrev + '\'' +
                ", unitSymbol='" + unitSymbol + '\'' +
                ", currencySymbol='" + currencySymbol + '\'' +
                '}';
    }
}
