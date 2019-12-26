package com.srj.icsinspection.handler;

import androidx.lifecycle.ReportFragment;

import com.srj.icsinspection.constants.DbConstant;
import com.srj.icsinspection.model.CommonDetailsModel;
import com.srj.icsinspection.model.DescriptionDataModel;
import com.srj.icsinspection.model.DescriptionModel;
import com.srj.icsinspection.model.DescriptionQuantityModel;
import com.srj.icsinspection.model.EmpDateModel;
import com.srj.icsinspection.model.GetFinalDescModel;
import com.srj.icsinspection.model.InsertionModel;
import com.srj.icsinspection.model.Location;
import com.srj.icsinspection.model.MainMOdel;
import com.srj.icsinspection.model.PoModel;
import com.srj.icsinspection.model.ReportDetailsModel;
import com.srj.icsinspection.model.SectionModel;
import com.srj.icsinspection.model.SiteInchargeModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ApiService {

    @FormUrlEncoded
    @POST("PostDetails")
        //  @POST("login.php")
    Observable<List<InsertionModel>> login(@Field("id") String id, @Field("pwd") String pass);

    @FormUrlEncoded
    @POST("section.php")
    Observable<SectionModel> section(@Field("emp_code") String id);

    @FormUrlEncoded
    // @POST("getreportsp.php")
    @POST("GetDetails?")
    Observable<List<ReportDetailsModel>> getreportsp(@Field("emp_code") String emp_code);

    //    @FormUrlEncoded
//    // @POST("getreportsp.php")
//    @POST("GetDetails?")
//    Call<List<ReportDetailsModel>> getreportSp(@Field("emp_code") String emp_code);
    @FormUrlEncoded
    // @POST("getreportsp.php")
    @POST("GetAuditPlanDetails")
    Call<List<ReportDetailsModel>> getreportSp(@Field("emp_code") String emp_code);

    @FormUrlEncoded
    // @POST("getreportsp.php")
    @POST("GetAuditPlanDesc")
    Call<List<DescriptionModel>> getdescription(@Field("emp_code") String emp_code);

    @FormUrlEncoded
    // @POST("getreportsp.php")
    @POST("GetDescQuantity")
    Call<DescriptionQuantityModel> getquantitydescription(@Field("emp_code") String emp_code);



    @FormUrlEncoded
    // @POST("getreportsp.php")
    @POST("GetSiteIncharge")
    Call<List<SiteInchargeModel>> getSiteIncharge(@Field("emp_code") String emp_code);

    @FormUrlEncoded
    // @POST("getreportsp.php")
    @POST("getreportdetails")
    Call<List<GetFinalDescModel>> getFinalDescription(@Field("emp_code") String emp_code);

    @FormUrlEncoded
    @POST("sendCommonDetails.php")
    Observable<List<CommonDetailsModel>> getCommonDetails(@Field("emp_code") String id);

    @FormUrlEncoded
    @POST("sendPoNumber.php")
    Observable<List<PoModel>> getPoNumber(@Field("emp_code") String emp_code, @Field("cust_name") String cust_name);

    @FormUrlEncoded
    @POST("sendEmpDate.php")
    Observable<List<EmpDateModel>> getDateModel(@Field("emp_code") String emp_code,
                                                @Field("cust_name") String cust_name,
                                                @Field("po") String po,
                                                @Field("v_name") String v_name);

    @FormUrlEncoded
    @POST("sendBindingDetails.php")
    Observable<List<MainMOdel>> getAllMaindata(@Field("emp_code") String emp_code,
                                               @Field("cust_name") String cust_name,
                                               @Field("v_name") String vendor_name);

    @FormUrlEncoded
    @POST("sendDescriptions.php")
    Observable<DescriptionDataModel> getDescriptions(@Field("po") String po,
                                                     @Field("ics_reg") String ics_reg);

//    @FormUrlEncoded
//    @POST("irnquantity")
//    Call<List<InsertionModel>> irn(@Field("bal_qty") String In,
//                                   @Field("pONumber") String po,
//                                   @Field("iCSRegNumber") String reg_num,
//                                   @Field("description") String desc,
//                                   @Field("reportNumber") String report_no,
//                                   @Field("iCSClientName") String client_name,
//                                   @Field("vendorName") String vendor_name,
//                                   @Field("inspectiondate") String insp_date,
//                                   @Field("emp_name") String emp_name,
//                                   @Field("emp_code") String emp_code,
//                                   @Field("emp_station") String emp_station,
//                                   @Field("po_qty") String po_qty,
//                                   @Field("rels_qty") String rel_qty,
//                                   @Field("rejected_qty") String rej_qty,
//                                   @Field("nos") String Nos,
//                                   @Field("status") String Status,
//                                   @Field("inspstatus") String Inspstatus,
//                                   @Field("inspected_qty") String insp_qty);
@FormUrlEncoded
@POST("irnquantity")
Observable<List<InsertionModel>> irn(@Field("bal_qty") String In,
                               @Field("pONumber") String po,
                               @Field("iCSRegNumber") String reg_num,
                               @Field("description") String desc,
                               @Field("reportNumber") String report_no,
                               @Field("iCSClientName") String client_name,
                               @Field("vendorName") String vendor_name,
                               @Field("inspectiondate") String insp_date,
                               @Field("emp_name") String emp_name,
                               @Field("emp_code") String emp_code,
                               @Field("emp_station") String emp_station,
                               @Field("po_qty") String po_qty,
                               @Field("rels_qty") String rel_qty,
                               @Field("rejected_qty") String rej_qty,
                               @Field("nos") String Nos,
                               @Field("status") String Status,
                               @Field("inspstatus") String Inspstatus,
                               @Field("inspected_qty") String insp_qty);


    @FormUrlEncoded
    @POST("getDescription1.php")
    Observable<InsertionModel> sendQty(@Field("bal_qty") Double In,
                                       @Field("po") String po,
                                       @Field("reg_num") String reg_num,
                                       @Field("desc") String desc,
                                       @Field("report_no") String report_no,
                                       @Field("client_name") String client_name,
                                       @Field("vendor_name") String vendor_name,
                                       @Field("insp_date") String insp_date,
                                       @Field("emp_name") String emp_name,
                                       @Field("emp_code") String emp_code,
                                       @Field("emp_station") String emp_station,
                                       @Field("po_qty") Double po_qty,
                                       @Field("rel_qty") Double rel_qty,
                                       @Field("rej_qty") Double rej_qty,
                                       @Field("insp_qty") Double insp_qty
    );


    @Multipart
    // @POST("get_OfflineI_R.php")
    @POST("NewIrIrnMultipleData")
    Observable<List<InsertionModel>> sendirirndata(@Part("vendor_name") RequestBody vendor_name,
                                                   @Part("po_number") RequestBody po_num,
                                                   @Part("cunsltent_name") RequestBody $cunsltant_name,
                                                   @Part("client_name") RequestBody client_name,
                                                   @Part("ics_reg_num") RequestBody ics_reg_num,
                                                   @Part("emp_code") RequestBody emp_code,
                                                   @Part("station") RequestBody station,
                                                   @Part("v_A") RequestBody v_a,
                                                   @Part("v_b") RequestBody v_b,
                                                   @Part("fac_insp") RequestBody fac_insp, // test_a (list of instrument/equipment..)
                                                   @Part("desc_fac") RequestBody desc_fac, // test_b  (desc of any jig/fixture...)
                                                   @Part("dimmension") RequestBody dim,
                                                   @Part("callib") RequestBody calib,
                                                   @Part("report_r") RequestBody report_r,
                                                   @Part("test_w") RequestBody test_w,
                                                   @Part("status") RequestBody status,
                                                   @Part("insp_status") RequestBody insp_status,
                                                   @Part MultipartBody.Part v_a_file,
                                                   @Part MultipartBody.Part v_b_file,
                                                   @Part MultipartBody.Part dim_file,
                                                   @Part MultipartBody.Part calib_file,
                                                   @Part MultipartBody.Part report_r_file,
                                                   @Part MultipartBody.Part test_w_file,
                                                   @Part("insp_date") RequestBody insp_date,
                                                   @Part("item_material") RequestBody item_material,
                                                   @Part("quantity") RequestBody quantity,
                                                   @Part("spec_drw") RequestBody spec_drw,
                                                   @Part("code_standard") RequestBody code_stnd,
                                                   @Part("other") RequestBody other,
                                                   @Part("deviation") RequestBody deviationother,
                                                   @Part("identification") RequestBody identification,
                                                   @Part("emp_name") RequestBody emp_name,
                                                   @Part("emp_station") RequestBody emp_station,
                                                   @Part("batch_no") RequestBody batch_no,
                                                   @Part("report_no") RequestBody report_no,
                                                   @Part MultipartBody.Part v_slip,
                                                   @Part MultipartBody.Part tc_slip,
                                                   @Part("stand") RequestBody stand,
                                                   @Part MultipartBody.Part qaqc,
                                                   @Part("qsp") RequestBody qsp,
                                                   @Part("Upload_po") RequestBody upload_po,
                                                   @Part MultipartBody.Part upload_calib,
                                                   @Part MultipartBody.Part other_doc,
                                                   @Part("desc_num") RequestBody desc_num,
                                                   @Part("tag_type") RequestBody tag_type,
                                                   @Part("create_date") RequestBody create_date,
                                                   @Part("max_num") RequestBody max_num,
                                                   @Part("insp_visit") RequestBody insp_visit,
                                                   @Part("annex") RequestBody annex,
                                                   @Part("TS_Location") RequestBody location,
                                                   @Part("TS_Range") RequestBody range,
                                                   @Part("TS_CFRDesc") RequestBody description,
                                                   @Part("TS_projectType") RequestBody project_type,
                                                   @Part("TS_Qty") RequestBody no_of_jobs,
                                                   @Part("TS_Workhrs") RequestBody donehrs,
                                                   @Part("TS_Extrahrs") RequestBody extrahrs,
                                                   @Part("TS_CFRId") RequestBody cfrid,
                                                   @Part("TS_siteIncharge") RequestBody siteincharge,
                                                   //new
                                                   @Part("InspectionResult") RequestBody insepectionresult,
                                                   @Part MultipartBody.Part  insepectionresult_file,
                                                   @Part("Photograph") RequestBody photograph,
                                                   @Part MultipartBody.Part photograph_file,
                                                   @Part("Quantity_Desc") RequestBody quantity_desc,
                                                   @Part("Quantity_Qty") RequestBody Quantity_qty,
                                                   @Part("Quantity_Unit") RequestBody Quantity_unit,
                                                   @Part MultipartBody.Part Quantity_file,
                                                   @Part("Qtydescription") RequestBody Qtydescription,
                                                   @Part("Qty") RequestBody Qty,
                                                   @Part("Qty_Unit") RequestBody Qty_Unit,
                                                   @Part MultipartBody.Part DescriptionFile,
                                                   @Part("subvendorname_po") RequestBody subvendorponame,
                                                   @Part MultipartBody.Part Other_File,
                                                   @Part MultipartBody.Part Deviation_File,
                                                   @Part MultipartBody.Part Identification_File,
                                                   @Part ("noofdesc") RequestBody countnumbr


                                                   );

    @GET("GetLocation")
    Call<List<Location>>
    getlocation(
            @Query("city") String city
    );

}


