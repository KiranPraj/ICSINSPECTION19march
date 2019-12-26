package com.srj.icsinspection.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CommonDetailsModel {

    @SerializedName("Emp_Name")
    @Expose
    val empName: String? = null
    @SerializedName("Station")
    @Expose
    val station: String? = null
    @SerializedName("Ol_Name")
    @Expose
    val olName: String? = null
    @SerializedName("Ol_Region")
    @Expose
    val olRegion: String? = null
    @SerializedName("Ol_CustID")
    @Expose
    val olCustID: String? = null
    @SerializedName("ReportNo")
    @Expose
    val reportNo: String? = null

    @SerializedName("ClientRegnum")
    @Expose
    var ics_reg_num: String? = null
}
