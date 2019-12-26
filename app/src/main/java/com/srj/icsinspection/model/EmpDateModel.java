package com.srj.icsinspection.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmpDateModel {

    @SerializedName("Emp_Dt")
    @Expose
    private EmpDt empDt;
    @SerializedName("ReportNo")
    @Expose
    private String reportNo;

    public EmpDt getEmpDt() {
        return empDt;
    }

    public String getReportNo() {
        return reportNo;
    }

    public class EmpDt {

        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("timezone_type")
        @Expose
        private Integer timezoneType;
        @SerializedName("timezone")
        @Expose
        private String timezone;

        public String getDate() {
            return date;
        }

        public Integer getTimezoneType() {
            return timezoneType;
        }

        public String getTimezone() {
            return timezone;
        }
    }
}
