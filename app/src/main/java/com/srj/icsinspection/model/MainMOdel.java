package com.srj.icsinspection.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainMOdel {

    @SerializedName("POidentitynum")
    @Expose
    private String pOidentitynum;
    @SerializedName("ICSClientName")
    @Expose
    private Object iCSClientName;
    @SerializedName("ICSRegNumber")
    @Expose
    private String iCSRegNumber;
    @SerializedName("ICSPONumber")
    @Expose
    private String iCSPONumber;
    @SerializedName("PONumber")
    @Expose
    private String pONumber;
    @SerializedName("VendorName")
    @Expose
    private String vendorName;
    @SerializedName("VendorEmail")
    @Expose
    private String vendorEmail;
    @SerializedName("VendorAddress")
    @Expose
    private String vendorAddress;
    @SerializedName("ClientName")
    @Expose
    private String clientName;
    @SerializedName("ClientEmail")
    @Expose
    private String clientEmail;
    @SerializedName("QAP")
    @Expose
    private String qAP;
    @SerializedName("QAPCopy")
    @Expose
    private String qAPCopy;
    @SerializedName("POCopy")
    @Expose
    private String pOCopy;
    @SerializedName("StandardCopy")
    @Expose
    private String standardCopy;
    @SerializedName("Calldate")
    @Expose
    private Calldate calldate;
    @SerializedName("Qtyoff")
    @Expose
    private String qtyoff;
    @SerializedName("Partdtl")
    @Expose
    private String partdtl;
    @SerializedName("Station")
    @Expose
    private String station;
    @SerializedName("Registerdt")
    @Expose
    private Registerdt registerdt;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("vendrPODate")
    @Expose
    private VendrPODate vendrPODate;
    @SerializedName("vendrRcvddt")
    @Expose
    private VendrRcvddt vendrRcvddt;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Inspectionloc")
    @Expose
    private String inspectionloc;
    @SerializedName("Contactperson")
    @Expose
    private String contactperson;
    @SerializedName("Contactpersonnumber")
    @Expose
    private String contactpersonnumber;
    @SerializedName("ContactPersonEmail")
    @Expose
    private String contactPersonEmail;
    @SerializedName("plannumber")
    @Expose
    private Integer plannumber;
    @SerializedName("sic1")
    @Expose
    private String sic1;
    @SerializedName("ConsultantName")
    @Expose
    private String consultantName;
    @SerializedName("MailCopy")
    @Expose
    private Object mailCopy;


    public String getpOidentitynum() {
        return pOidentitynum;
    }

    public Object getiCSClientName() {
        return iCSClientName;
    }

    public String getiCSRegNumber() {
        return iCSRegNumber;
    }

    public String getiCSPONumber() {
        return iCSPONumber;
    }

    public String getpONumber() {
        return pONumber;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getVendorEmail() {
        return vendorEmail;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public String getqAP() {
        return qAP;
    }

    public String getqAPCopy() {
        return qAPCopy;
    }

    public String getpOCopy() {
        return pOCopy;
    }

    public String getStandardCopy() {
        return standardCopy;
    }

    public Calldate getCalldate() {
        return calldate;
    }

    public String getQtyoff() {
        return qtyoff;
    }

    public String getPartdtl() {
        return partdtl;
    }

    public String getStation() {
        return station;
    }

    public Registerdt getRegisterdt() {
        return registerdt;
    }

    public String getNote() {
        return note;
    }

    public VendrPODate getVendrPODate() {
        return vendrPODate;
    }

    public VendrRcvddt getVendrRcvddt() {
        return vendrRcvddt;
    }

    public String getStatus() {
        return status;
    }

    public String getInspectionloc() {
        return inspectionloc;
    }

    public String getContactperson() {
        return contactperson;
    }

    public String getContactpersonnumber() {
        return contactpersonnumber;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public Integer getPlannumber() {
        return plannumber;
    }

    public String getSic1() {
        return sic1;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public Object getMailCopy() {
        return mailCopy;
    }

    public class Registerdt {

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

        public void setDate(String date) {
            this.date = date;
        }

        public Integer getTimezoneType() {
            return timezoneType;
        }

        public void setTimezoneType(Integer timezoneType) {
            this.timezoneType = timezoneType;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

    }

    public class VendrPODate {

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

        public void setDate(String date) {
            this.date = date;
        }

        public Integer getTimezoneType() {
            return timezoneType;
        }

        public void setTimezoneType(Integer timezoneType) {
            this.timezoneType = timezoneType;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

    }

    public class VendrRcvddt {

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

        public void setDate(String date) {
            this.date = date;
        }

        public Integer getTimezoneType() {
            return timezoneType;
        }

        public void setTimezoneType(Integer timezoneType) {
            this.timezoneType = timezoneType;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

    }

    public class Calldate {

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

        public void setDate(String date) {
            this.date = date;
        }

        public Integer getTimezoneType() {
            return timezoneType;
        }

        public void setTimezoneType(Integer timezoneType) {
            this.timezoneType = timezoneType;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

    }

}
