package com.srj.icsinspection.model;

public class SyncModel {

    String Date;

    public SyncModel(String date, String ponumber, String date_of_fill) {
        Date = date;
        Ponumber = ponumber;
        this.date_of_fill = date_of_fill;
    }

    String Ponumber;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPonumber() {
        return Ponumber;
    }

    public void setPonumber(String ponumber) {
        Ponumber = ponumber;
    }

    public String getDate_of_fill() {
        return date_of_fill;
    }

    public void setDate_of_fill(String date_of_fill) {
        this.date_of_fill = date_of_fill;
    }

    String date_of_fill;

}
