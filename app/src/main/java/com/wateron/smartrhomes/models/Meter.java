package com.wateron.smartrhomes.models;

/**
 * Created by Paranjay on 08-12-2017.
 */

public class Meter {

    public int getApartment_index() {
        return apartment_index;
    }

    public void setApartment_index(int apartment_index) {
        this.apartment_index = apartment_index;
    }

    private int apartment_index;
    private int id;
    private String locationUser;
    private String locationDefault;
    private int hasValve;
    private String valveCurrentStatus;
    private String valveLastOperated;
    private int aptId;

    public Meter() {
    }

    public Meter(int id, String locationUser, String locationDefault, int hasValve, String valveCurrentStatus, String valveLastOperated, int aptId) {
        this.id = id;
        this.locationUser = locationUser;
        this.locationDefault = locationDefault;
        this.hasValve = hasValve;
        this.valveCurrentStatus = valveCurrentStatus;
        this.valveLastOperated = valveLastOperated;
        this.aptId = aptId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocationUser() {
        return locationUser;
    }

    public void setLocationUser(String locationUser) {
        this.locationUser = locationUser;
    }

    public String getLocationDefault() {
        return locationDefault;
    }

    public void setLocationDefault(String locationDefault) {
        this.locationDefault = locationDefault;
    }

    public int getHasValve() {
        return hasValve;
    }

    public void setHasValve(int hasValve) {
        this.hasValve = hasValve;
    }

    public String getValveCurrentStatus() {
        return valveCurrentStatus;
    }

    public void setValveCurrentStatus(String valveCurrentStatus) {
        this.valveCurrentStatus = valveCurrentStatus;
    }

    public String getValveLastOperated() {
        return valveLastOperated;
    }

    public void setValveLastOperated(String valveLastOperated) {
        this.valveLastOperated = valveLastOperated;
    }

    public int getAptId() {
        return aptId;
    }

    public void setAptId(int aptId) {
        this.aptId = aptId;
    }

    @Override
    public String toString() {
        return "Meter{" +
                "id=" + id +
                ", locationUser='" + locationUser + '\'' +
                ", locationDefault='" + locationDefault + '\'' +
                ", hasValve=" + hasValve +
                ", valveCurrentStatus='" + valveCurrentStatus + '\'' +
                ", valveLastOperated='" + valveLastOperated + '\'' +
                ", aptId=" + aptId +
                '}';
    }
}
