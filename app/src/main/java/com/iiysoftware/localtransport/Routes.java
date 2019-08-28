package com.iiysoftware.localtransport;

public class Routes {

    private String from;
    private String to;
    private String dist;
    private String stops;
    private String uid;
    private boolean isAssigend;

    public Routes(){

    }


    public Routes(String from, String to, String dist, String stops, String uid, boolean isAssigend) {
        this.from = from;
        this.to = to;
        this.dist = dist;
        this.stops = stops;
        this.uid = uid;
        this.isAssigend = isAssigend;
    }

    public boolean isAssigend() {
        return isAssigend;
    }

    public void setAssigend(boolean assigend) {
        isAssigend = assigend;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getStops() {
        return stops;
    }

    public void setStops(String stops) {
        this.stops = stops;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
