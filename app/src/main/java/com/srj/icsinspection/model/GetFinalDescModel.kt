package com.srj.icsinspection.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetFinalDescModel {
    @SerializedName("POidentitynum")
    @Expose
    var pOidentitynum: String? = null
    @SerializedName("ICSClientName")
    @Expose
    var icsClientName: String? = null
    @SerializedName("ICSRegNumber")
    @Expose
    var icsRegNumber: String? = null
    @SerializedName("PONumber")
    @Expose
    var poNumber: String? = null
    @SerializedName("VendorName")
    @Expose
    var vendorName: String? = null
    @SerializedName("ClientName")
    @Expose
    var clientName: String? = null
    @SerializedName("QAP")
    @Expose
    var qap: String? = null
    @SerializedName("Description")
    @Expose
    var description: String? = null
    @SerializedName("Nos")
    @Expose
    var nos: String? = null
    @SerializedName("po_qty")
    @Expose
    var poQty: String? = null
    @SerializedName("rels_qty")
    @Expose
    var relsQty: Int? = null
    @SerializedName("rejected_qty")
    @Expose
    var rejectedQty: Int? = null
    @SerializedName("bal_qty")
    @Expose
    var balQty: Int? = null
    @SerializedName("inspected_qty")
    @Expose
    var inspectedQty: Int? = null

}
