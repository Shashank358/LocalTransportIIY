package com.iiysoftware.localtransport.NewPackage;

public class Request {

    private String date;
    private String push;
    private String reason;
    private String user_id;

    public Request(String date, String push, String reason, String user_id) {
        this.date = date;
        this.push = push;
        this.reason = reason;
        this.user_id = user_id;
    }

    public Request(){

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPush() {
        return push;
    }

    public void setPush(String push) {
        this.push = push;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
