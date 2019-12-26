package com.srj.icsinspection.model;


public class SingleRowModel {


    private String description;
    private String quantity;
    private String unit;
    private int type;
    public static final int QTY_TYPE=1;
    public static final int COMON_TYPE=0;
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    private String file;

    public void setSrno(String srno) {
        this.srno = srno;
    }



    public void setObs(String obs) {
        this.obs = obs;
    }



    public void setUpload(String upload) {
        this.upload = upload;
    }

    public String getSrno() {
        return srno;
    }

    public String getObs() {
        return obs;
    }

    public String getUpload() {
        return upload;
    }

    public String getDeletedId() {
        return deletedId;
    }

    public void setDeletedId(String deletedId) {
        this.deletedId = deletedId;
    }

    private String srno,obs,upload,deletedId;




    public SingleRowModel(int type,String srno, String obs, String upload, String deletedId) {
        this.type=type;
        this.srno = srno;
        this.obs = obs;
        this.upload=upload;
        this.deletedId = deletedId;
    }


    public SingleRowModel(int type,String srno, String description, String quantity, String unit,String file) {
       this.type=type;
        this.srno = srno;
        this.description = description;
        this.quantity=quantity;
        this.unit = unit;
        this.file=file;
    }


}