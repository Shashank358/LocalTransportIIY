package com.iiysoftware.localtransport.NewPackage;

import com.iiysoftware.localtransport.AttendanceFrag.AssignedStudFrag;

public class AssignedRoutes {
    private String route;

    public AssignedRoutes(String route) {
        this.route = route;
    }

    public AssignedRoutes(){

    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
