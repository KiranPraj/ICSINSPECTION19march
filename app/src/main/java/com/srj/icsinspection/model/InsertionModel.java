package com.srj.icsinspection.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsertionModel {

    @SerializedName("sts")
    @Expose
    public Integer sts;

    public Integer getSts() {
        return sts;
    }

}


