package com.iiysoftware.localtransport;

public class Vehicles {

    private String uid;
    private String name_plate;
    private String image;
    private boolean isAssigned;

    public Vehicles(){

    }

    public Vehicles(String uid, String name_plate, String image, boolean isAssigned) {
        this.uid = uid;
        this.name_plate = name_plate;
        this.image = image;
        this.isAssigned = isAssigned;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName_plate() {
        return name_plate;
    }

    public void setName_plate(String name_plate) {
        this.name_plate = name_plate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
