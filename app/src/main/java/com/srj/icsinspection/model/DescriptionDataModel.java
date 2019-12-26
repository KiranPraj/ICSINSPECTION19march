package com.srj.icsinspection.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DescriptionDataModel  {

    @SerializedName("data")
    @Expose
    private List<Data> data = null;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }



    public class Data {

        public Data(String description, Double poQty, Double relsQty, Double rejectedQty, Double balQty,Double inspQty) {
            this.description = description;
            this.poQty = poQty;
            this.relsQty = relsQty;
            this.rejectedQty = rejectedQty;
            this.balQty = balQty;
            this.inspQty = inspQty;
        }

        @SerializedName("Description")
        @Expose
        private String description;
        @SerializedName("po_qty")
        @Expose
        private Double poQty;
        @SerializedName("rels_qty")
        @Expose
        private Double relsQty;
        @SerializedName("rejected_qty")
        @Expose
        private Double rejectedQty;
        @SerializedName("bal_qty")
        @Expose
        private Double balQty;
        private Double inspQty;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Double getPoQty() {
            return poQty;
        }

        public void setPoQty(Double poQty) {
            this.poQty = poQty;
        }

        public Double getRelsQty() {
            return relsQty;
        }

        public void setRelsQty(Double relsQty) {
            this.relsQty = relsQty;
        }

        public Double getRejectedQty() {
            return rejectedQty;
        }

        public void setRejectedQty(Double rejectedQty) {
            this.rejectedQty = rejectedQty;
        }

        public Double getBalQty() {
            return balQty;
        }

        public void setBalQty(Double balQty) {
            this.balQty = balQty;
        }

        public void setInspQty(Double inspQty) {
            this.inspQty = inspQty;
        }

        public Double getInspQty() {

            return inspQty;
        }
    }

}
