package com.srj.icsinspection.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SiteInchargeModel {

    @SerializedName("Rowindex")
    @Expose
    private Integer rowindex;
    @SerializedName("Emp_Code")
    @Expose
    private Object empCode;
    @SerializedName("ClientregNum")
    @Expose
    private String clientregNum;
    @SerializedName("SiteIncharge")
    @Expose
    private String siteIncharge;

    public Integer getRowindex() {
        return rowindex;
    }

    public void setRowindex(Integer rowindex) {
        this.rowindex = rowindex;
    }

    public Object getEmpCode() {
        return empCode;
    }

    public void setEmpCode(Object empCode) {
        this.empCode = empCode;
    }

    public String getClientregNum() {
        return clientregNum;
    }

    public void setClientregNum(String clientregNum) {
        this.clientregNum = clientregNum;
    }

    public String getSiteIncharge() {
        return siteIncharge;
    }

    public void setSiteIncharge(String siteIncharge) {
        this.siteIncharge = siteIncharge;
    }


}