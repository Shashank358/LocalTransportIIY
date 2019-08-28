package com.iiysoftware.localtransport;

public class AssignedStudents {

    private String name;
    private String id;
    private boolean attendance;
    private String _class;

    public AssignedStudents(String name, String id, boolean attendance, String _class) {
        this.name = name;
        this.id = id;
        this.attendance = attendance;
        this._class = _class;
    }

    public AssignedStudents(){

    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAttendance() {
        return attendance;
    }

    public void setAttendance(boolean attendance) {
        this.attendance = attendance;
    }
}
