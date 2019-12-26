package com.srj.icsinspection.model;

import android.os.Parcel;
import android.os.Parcelable;

public class InspectionMoodel implements Parcelable {


    private String cust_name;
    private String conslt_name;
    private String mf_supplier_naem;
    private String project_vend;
    private String item;
    private String batch_no;
    private String spec_drawings;
    private String codes_standard;
    private String date_of_insp;
    private String insp_type;
    private String po_number;

    public String getSub_ven_po_number() {
        return sub_ven_po_number;
    }

    public void setSub_ven_po_number(String sub_ven_po_number) {
        this.sub_ven_po_number = sub_ven_po_number;
    }

    private String sub_ven_po_number;
    private String ics_reg_num;

    private String uuid;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSiteIncharge() {
        return siteIncharge;
    }

    public void setSiteIncharge(String siteIncharge) {
        this.siteIncharge = siteIncharge;
    }

    public String getNo_of_jobs() {
        return no_of_jobs;
    }

    public void setNo_of_jobs(String no_of_jobs) {
        this.no_of_jobs = no_of_jobs;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getProject_type() {
        return project_type;
    }

    public void setProject_type(String project_type) {
        this.project_type = project_type;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    private String location;
    private String description;
    private String no_of_jobs;
    private String range;
    private String project_type;
    private String siteIncharge;
    private String quantity;


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cust_name);
        dest.writeString(conslt_name);
        dest.writeString(mf_supplier_naem);
        dest.writeString(project_vend);
        dest.writeString(item);
        dest.writeString(batch_no);
        dest.writeString(ics_reg_num);
        dest.writeString(spec_drawings);
        dest.writeString(codes_standard);
        dest.writeString(date_of_insp);
        dest.writeString(insp_type);
        dest.writeString(quantity);
        dest.writeString(po_number);
        dest.writeString(sub_ven_po_number);
        dest.writeString(uuid);
    }

    public InspectionMoodel(String cust_name, String conslt_name, String mf_supplier_naem, String project_vend,
                            String item, String batch_no, String spec_drawings, String codes_standard,
                            String date_of_insp, String insp_type,String ics_reg_num, String po_number,String sub_ven_po_number,
                            String quantity,String uuid,String location,String description,String siteIncharge,String no_of_jobs,String range,String project_type) {
        this.cust_name = cust_name;
        this.conslt_name = conslt_name;
        this.mf_supplier_naem = mf_supplier_naem;
        this.project_vend = project_vend;
        this.item = item;
        this.batch_no = batch_no;
        this.spec_drawings = spec_drawings;
        this.codes_standard = codes_standard;
        this.date_of_insp = date_of_insp;
        this.insp_type = insp_type;
        this.ics_reg_num = ics_reg_num;
        this.po_number = po_number;
        this.sub_ven_po_number = sub_ven_po_number;
        this.quantity = quantity;
        this.uuid = uuid;
        this.location=location;
        this.description=description;
        this.siteIncharge=siteIncharge;
        this.no_of_jobs=no_of_jobs;
        this.range=range;
        this.project_type=project_type;
    }

    private InspectionMoodel(Parcel in) {
        cust_name = in.readString();
        conslt_name = in.readString();
        mf_supplier_naem = in.readString();
        project_vend = in.readString();
        item = in.readString();
        batch_no = in.readString();
        po_number = in.readString();
        spec_drawings = in.readString();
        codes_standard = in.readString();
        date_of_insp = in.readString();
        insp_type = in.readString();
        ics_reg_num = in.readString();
        sub_ven_po_number = in.readString();
        quantity = in.readString();
        uuid = in.readString();

    }

    public static final Creator<InspectionMoodel> CREATOR = new Creator<InspectionMoodel>() {
        @Override
        public InspectionMoodel createFromParcel(Parcel in) {
            return new InspectionMoodel(in);
        }

        @Override
        public InspectionMoodel[] newArray(int size) {
            return new InspectionMoodel[size];
        }
    };

    public String getCust_name() {
        return cust_name;
    }

    public String getConslt_name() {
        return conslt_name;
    }

    public String getMf_supplier_naem() {
        return mf_supplier_naem;
    }

    public String getProject_vend() {
        return project_vend;
    }

    public String getItem() {
        return item;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public String getSpec_drawings() {
        return spec_drawings;
    }

    public String getIcs_reg_num() {
        return ics_reg_num;
    }

    public void setIcs_reg_num(String ics_reg_num) {
        this.ics_reg_num = ics_reg_num;
    }

    public String getCodes_standard() {
        return codes_standard;
    }

    public String getDate_of_insp() {
        return date_of_insp;
    }


    public String getInsp_type() {
        return insp_type;
    }

    public String getPo_number() {
        return po_number;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUuid() { return uuid; }

    @Override
    public int describeContents() {
        return 0;
    }


}
