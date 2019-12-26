package com.srj.icsinspection.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReportDetailsModel {

    @SerializedName("Ol_Name")
    @Expose
     val olName: String? = null
    @SerializedName("ClientRegnum")
    @Expose
     val clientRegnum: String? = null
    @SerializedName("ConsultantName")
    @Expose
    val consultantname: String? = null
    @SerializedName("VendorName")
    @Expose
     val vendorName: String? = null
    @SerializedName("PONumber")
    @Expose
     val pONumber: String? = null
    @SerializedName("Emp_Dt")
    @Expose
     val empDt: String? = null
    @SerializedName("ReportNo")
    @Expose
     val reportNo: String? = null
    @SerializedName("Emp_Code")
    @Expose
     val empCode: String? = null
    @SerializedName("QAPCopy")
    @Expose
     val qAPCopy: String? = null
    @SerializedName("POCopy")
    @Expose
     val pOCopy: String? = null
    @SerializedName("StandardCopy")
    @Expose
     val standardCopy: String? = null

    @SerializedName("Emp_Name")
    @Expose
    val empName: String? = null
    @SerializedName("Station")
    @Expose
    var station: String? = null
    @SerializedName("ProjectType")
    @Expose
    var projectType: String? = null
    inner class EmpDt {

        @SerializedName("date")
        @Expose
         val date: String? = null
        @SerializedName("timezone_type")
        @Expose
         val timezoneType: Int? = null
        @SerializedName("timezone")
        @Expose
         val timezone: String? = null
    }


}
