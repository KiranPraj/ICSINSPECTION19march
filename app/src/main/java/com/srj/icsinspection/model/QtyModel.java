package com.srj.icsinspection.model;

public class QtyModel {
    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    public String getDeletedId() {
        return deletedId;
    }

    public void setDeletedId(String deletedId) {
        this.deletedId = deletedId;
    }

    private String srno,desc,qty,unit,upload,deletedId;

    public QtyModel(String srno, String desc, String qty, String unit, String upload, String deletedId) {
        this.srno = srno;
        this.desc = desc;
        this.qty = qty;
        this.unit = unit;
        this.upload = upload;
        this.deletedId = deletedId;
    }
}
