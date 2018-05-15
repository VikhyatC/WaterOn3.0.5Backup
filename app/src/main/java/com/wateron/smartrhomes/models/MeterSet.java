package com.wateron.smartrhomes.models;

import android.content.ContentValues;

/**
 * Created by paran on 9/20/2016.
 */
public class MeterSet {
    String id;
    String location;
    boolean has_valve;
    String current_status;
    String last_operated;
    String aptid;

    boolean editOn=false;

    public boolean isEditOn() {
        return editOn;
    }

    public void setEditOn(boolean editOn) {
        this.editOn = editOn;
    }

    public MeterSet(String id, String location, boolean has_valve, String current_status, String last_operated, String aptid, boolean editOn) {
        this.id = id;
        this.location = location;
        this.has_valve = has_valve;
        this.current_status = current_status;
        this.last_operated = last_operated;
        this.aptid = aptid;
        this.editOn = editOn;
    }

    public ContentValues getContentValues(){
        ContentValues contentValues=new ContentValues();
        contentValues.put("meter_id",id);
        contentValues.put("location",location);
        contentValues.put("has_valve",has_valve?"Yes":"No");
        contentValues.put("current_valve_status",current_status);
        contentValues.put("last_operated",last_operated);
        contentValues.put("aptid_meter",aptid);
        return contentValues;
    }

    public MeterSet(Meter m, boolean editOn) {
        this.id = String.valueOf(m.getId());
        this.location = m.getLocationUser();
        this.has_valve = m.getHasValve()==1;
        this.current_status = m.getValveCurrentStatus();
        this.last_operated = m.getValveLastOperated();
        this.aptid = String.valueOf(m.getAptId());
        this.editOn = editOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isHas_valve() {
        return has_valve;
    }

    public void setHas_valve(boolean has_valve) {
        this.has_valve = has_valve;
    }

    public String getCurrent_status() {
        return current_status;
    }

    public void setCurrent_status(String current_status) {
        this.current_status = current_status;
    }

    public String getLast_operated() {
        return last_operated;
    }

    public void setLast_operated(String last_operated) {
        this.last_operated = last_operated;
    }

    public String getAptid() {
        return aptid;
    }

    public void setAptid(String aptid) {
        this.aptid = aptid;
    }
}
