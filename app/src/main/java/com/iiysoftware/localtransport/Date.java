package com.iiysoftware.localtransport;

public class Date {

    private String date;
    private String pushId;

    public Date(){

    }

    public Date(String date, String pushId) {
        this.date = date;
        this.pushId = pushId;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
