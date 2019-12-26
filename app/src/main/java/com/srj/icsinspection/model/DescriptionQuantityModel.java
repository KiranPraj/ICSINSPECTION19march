package com.srj.icsinspection.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
public class DescriptionQuantityModel {


    @SerializedName("dtqty")
    @Expose
    public ArrayList<Dtqty> dtqty = null;

    public List<Dtqty> getDtqty() {
        return dtqty;
    }

    public void setDtqty(ArrayList<Dtqty> dtqty) {
        this.dtqty = dtqty;
    }

public  class Dtqty
{

    @SerializedName("POidentitynum")
    @Expose
    public String pOidentitynum;
    @SerializedName("ICSClientName")
    @Expose
    public String iCSClientName;
    @SerializedName("ICSRegNumber")
    @Expose
    public String iCSRegNumber;
    @SerializedName("VendorName")
    @Expose
    public String vendorName;
    @SerializedName("PONumber")
    @Expose
    public String pONumber;
    @SerializedName("ClientName")
    @Expose
    public String clientName;
    @SerializedName("QAP")
    @Expose
    public String qAP;
    @SerializedName("Description")
    @Expose
    public String description;
    @SerializedName("Nos")
    @Expose
    public String nos;
    @SerializedName("po_qty")
    @Expose
    public Double poQty;
    @SerializedName("rels_qty")
    @Expose
    public Object relsQty;
    @SerializedName("rejected_qty")
    @Expose
    public Object rejectedQty;
    @SerializedName("bal_qty")
    @Expose
    public Double balQty;
    @SerializedName("inspected_qty")
    @Expose
    public Object inspectedQty;
    @SerializedName("Units")
    @Expose
    public String units;

    public String getPOidentitynum() {
        return pOidentitynum;
    }

    public void setPOidentitynum(String pOidentitynum) {
        this.pOidentitynum = pOidentitynum;
    }

    public String getICSClientName() {
        return iCSClientName;
    }

    public void setICSClientName(String iCSClientName) {
        this.iCSClientName = iCSClientName;
    }

    public String getICSRegNumber() {
        return iCSRegNumber;
    }

    public void setICSRegNumber(String iCSRegNumber) {
        this.iCSRegNumber = iCSRegNumber;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getPONumber() {
        return pONumber;
    }

    public void setPONumber(String pONumber) {
        this.pONumber = pONumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getQAP() {
        return qAP;
    }

    public void setQAP(String qAP) {
        this.qAP = qAP;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNos() {
        return nos;
    }

    public void setNos(String nos) {
        this.nos = nos;
    }

    public Double getPoQty() {
        return poQty;
    }

    public void setPoQty(Double poQty) {
        this.poQty = poQty;
    }

    public Object getRelsQty() {
        return relsQty;
    }

    public void setRelsQty(Object relsQty) {
        this.relsQty = relsQty;
    }

    public Object getRejectedQty() {
        return rejectedQty;
    }

    public void setRejectedQty(Object rejectedQty) {
        this.rejectedQty = rejectedQty;
    }

    public Double getBalQty() {
        return balQty;
    }

    public void setBalQty(Double balQty) {
        this.balQty = balQty;
    }

    public Object getInspectedQty() {
        return inspectedQty;
    }

    public void setInspectedQty(Object inspectedQty) {
        this.inspectedQty = inspectedQty;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

}





}