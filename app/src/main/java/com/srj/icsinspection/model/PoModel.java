package com.srj.icsinspection.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PoModel {

    @SerializedName("Ol_CustID")
    @Expose
    private String olCustID;
    @SerializedName("Ol_Region")
    @Expose
    private String olRegion;

    public String getCust_reg_num() {
        return cust_reg_num;
    }

    @SerializedName("ClientRegnum")
    @Expose
    private String cust_reg_num;

    public String getOlCustID() {
        return olCustID;
    }

    public String getOlRegion() {
        return olRegion;
    }



}
