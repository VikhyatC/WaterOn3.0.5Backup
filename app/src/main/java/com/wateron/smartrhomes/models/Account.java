package com.wateron.smartrhomes.models;

/**
 * Created by Paranjay on 11-01-2018.
 */

public class Account {

    String number;
    boolean editable;

    public Account (){

    }

    public Account(String number, boolean editable) {
        this.number = number;
        this.editable = editable;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
