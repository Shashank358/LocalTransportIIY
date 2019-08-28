package com.iiysoftware.localtransport.NewPackage;

public class Location {

    String longitude, latittude;

    public Location() {
    }

    public Location(String longitude, String latittude) {
        this.longitude = longitude;
        this.latittude = latittude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatittude() {
        return latittude;
    }

    public void setLatittude(String latittude) {
        this.latittude = latittude;
    }
}
