package com.srj.icsinspection.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DescriptionModel {
    @SerializedName("Rowindex")
    @Expose
    private Integer rowindex;
    @SerializedName("ClientregNum")
    @Expose
    private String clientregNum;
    @SerializedName("cfrid")
    @Expose
    private Integer cfrid;
    @SerializedName("Description")
    @Expose
    private String description;

    public Integer getRowindex() {
        return rowindex;
    }

    public void setRowindex(Integer rowindex) {
        this.rowindex = rowindex;
    }

    public String getClientregNum() {
        return clientregNum;
    }

    public void setClientregNum(String clientregNum) {
        this.clientregNum = clientregNum;
    }

    public Integer getCfrid() {
        return cfrid;
    }

    public void setCfrid(Integer cfrid) {
        this.cfrid = cfrid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}

