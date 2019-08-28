package com.iiysoftware.localtransport;

public class Students {

    private boolean isAssigned;
    private String driver_id;
    private String fair;
    private String student_address;
    private String student_admno;
    private String student_city;
    private String student_class;
    private String student_contact;
    private String student_dob;
    private String student_email;
    private String student_fname;
    private String student_focc;
    private String student_gender;
    private String student_mname;
    private String student_mocc;
    private String student_name;
    private String student_password;
    private String student_state;
    private String uid;

    public Students(){

    }

    public Students(boolean isAssigned, String driver_id, String fair, String student_address, String student_admno, String student_city, String student_class, String student_contact, String student_dob, String student_email, String student_fname, String student_focc, String student_gender, String student_mname, String student_mocc, String student_name, String student_password, String student_state, String uid) {
        this.isAssigned = isAssigned;
        this.driver_id = driver_id;
        this.fair = fair;
        this.student_address = student_address;
        this.student_admno = student_admno;
        this.student_city = student_city;
        this.student_class = student_class;
        this.student_contact = student_contact;
        this.student_dob = student_dob;
        this.student_email = student_email;
        this.student_fname = student_fname;
        this.student_focc = student_focc;
        this.student_gender = student_gender;
        this.student_mname = student_mname;
        this.student_mocc = student_mocc;
        this.student_name = student_name;
        this.student_password = student_password;
        this.student_state = student_state;
        this.uid = uid;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getFair() {
        return fair;
    }

    public void setFair(String fair) {
        this.fair = fair;
    }

    public String getStudent_address() {
        return student_address;
    }

    public void setStudent_address(String student_address) {
        this.student_address = student_address;
    }

    public String getStudent_admno() {
        return student_admno;
    }

    public void setStudent_admno(String student_admno) {
        this.student_admno = student_admno;
    }

    public String getStudent_city() {
        return student_city;
    }

    public void setStudent_city(String student_city) {
        this.student_city = student_city;
    }

    public String getStudent_class() {
        return student_class;
    }

    public void setStudent_class(String student_class) {
        this.student_class = student_class;
    }

    public String getStudent_contact() {
        return student_contact;
    }

    public void setStudent_contact(String student_contact) {
        this.student_contact = student_contact;
    }

    public String getStudent_dob() {
        return student_dob;
    }

    public void setStudent_dob(String student_dob) {
        this.student_dob = student_dob;
    }

    public String getStudent_email() {
        return student_email;
    }

    public void setStudent_email(String student_email) {
        this.student_email = student_email;
    }

    public String getStudent_fname() {
        return student_fname;
    }

    public void setStudent_fname(String student_fname) {
        this.student_fname = student_fname;
    }

    public String getStudent_focc() {
        return student_focc;
    }

    public void setStudent_focc(String student_focc) {
        this.student_focc = student_focc;
    }

    public String getStudent_gender() {
        return student_gender;
    }

    public void setStudent_gender(String student_gender) {
        this.student_gender = student_gender;
    }

    public String getStudent_mname() {
        return student_mname;
    }

    public void setStudent_mname(String student_mname) {
        this.student_mname = student_mname;
    }

    public String getStudent_mocc() {
        return student_mocc;
    }

    public void setStudent_mocc(String student_mocc) {
        this.student_mocc = student_mocc;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_password() {
        return student_password;
    }

    public void setStudent_password(String student_password) {
        this.student_password = student_password;
    }

    public String getStudent_state() {
        return student_state;
    }

    public void setStudent_state(String student_state) {
        this.student_state = student_state;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
