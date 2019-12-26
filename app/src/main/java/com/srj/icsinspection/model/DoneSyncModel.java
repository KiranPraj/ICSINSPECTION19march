package com.srj.icsinspection.model;

public class DoneSyncModel {

    String Date;
    String Ponumber;

    public DoneSyncModel(String date, String ponumber, String date_of_filled_data) {
        Date = date;
        Ponumber = ponumber;
        this.date_of_filled_data = date_of_filled_data;
    }

    String date_of_filled_data;

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

    public String getDate_of_filled_data() {
        return date_of_filled_data;
    }

    public void setDate_of_filled_data(String date_of_filled_data) {
        this.date_of_filled_data = date_of_filled_data;
    }
}
