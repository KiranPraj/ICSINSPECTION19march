package com.srj.icsinspection.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SectionModel {

    @SerializedName("Emp_Name")
    @Expose
    private String empName;
    @SerializedName("station")
    @Expose
    private String station;

    public String getEmpName() {
        return empName;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
