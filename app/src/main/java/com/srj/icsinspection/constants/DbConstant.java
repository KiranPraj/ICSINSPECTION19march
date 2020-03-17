package com.srj.icsinspection.constants;

import android.provider.BaseColumns;

public class DbConstant {
    public class location_entry implements BaseColumns{
        //table name
        public static final String TABLE_LOCATION="LOCATON";

        //fields
        public static final String LOCATION="location";
    }
    public class  Description_Entry implements  BaseColumns{
        //TABLE DESCRIPTION NAME
        public static final String TABLE_DESCRIPTION="DESCRIPTION";

        //FEILDS
        public static final String CLIENTREG_NO="ClientregNum";
        public static final String DESCRIPTION="Description";
        public static final String CFIRD="cfird";



    }

    public class  QuantityDescription_Entry implements  BaseColumns{
        //TABLE DESCRIPTION NAME
        public static final String TABLE_QUANTITY_DESCRIPTION="QUANTITY_DESCRIPTION";

        //FEILDS
        public static final String CLIENTREG_NO="ClientregNum";
        public static final String DESCRIPTION="Description";
        public static final String CFIRD="cfird";
        public static final String UNIT="unit";
        public static final String PONUMBER="po_no";

    }

    public class  SiteIncharge_Entry implements  BaseColumns{
        //TABLE SITEINCHARGE NAME
        public static final String TABLE_SITEINCHARGE="SITEINCHARGE";

        //FEILDS
        public static final String REGNOCLIENT="ClientRegNumber";
        public static final  String SITEINCHARGENAME="SITEINCHARGENAME";

    }
    public class Inspection_Entry implements BaseColumns {
        //table name
        public static final String TABLE_INSPECTION = "inspection";

        // fields
        public static final String _id = BaseColumns._ID;
        public static final String EMP_CODE = "emp_code";
        public static final String EMP_NAME = "emp_name";
        public static final String ICS_REG_NUM = "ics_reg_num";
        public static final String INSPECTION_ID = "inspection_id";
        public static final String CUSTOMER_NAEM = "customer_name";
        public static final String CONSULTANT_NAEM = "consultant_name";
        public static final String MF_SUPPLIER_NAEM = "mf_supplier_name";
        public static final String PROJECT_VEND = "project_vend";
        //new
        public static final String SUB_VENDOR_PO_NUMBER = "sub_ven_po_number";

        public static final String ITEM = "item";
        public static final String BATCH_NO = "batch_no";
        public static final String QUANTITY = "quantity";
        public static final String SPEC_DRAWINGS = "spec_drawings";
        public static final String CODES_STANDARD = "codes_standard";
        public static final String DATE_OF_INSP = "date_of_insp";
        public static final String INSP_TYPE = "insp_type";
        public static final String OBSERVATIION = "observation";
        public static final String EMP_STATION = "emp_station";
        public static final String PHOTO_2 = "pdf_name";
        public static final String ANNEXURE_A = "annexure_a";
        public static final String VISUALA = "visualA";
        public static final String VISUALA_FILE = "visualA_file";
        public static final String VISUALB = "visualB";
        public static final String VISUALB_FILE = "visualB_file";
        public static final String Dimensional = "Dimensional";
        public static final String Dimensional_file = "Dimensional_file";
        public static final String Calibration = "Calibration";
        public static final String Calibration_file = "Calibration_file";
        public static final String ReportsR = "ReportsR";
        public static final String ReportsR_file = "ReportsR_file";
        public static final String TestWitness = "TestWitness";
        public static final String TestWitness_File = "TestWitness_File";
        public static final String Identifation = "Identifation";
        public static final String Identifation_file = "Identifation_file";
        //new
        public static final String InspectionResult = "InspectionResult";
        public static final String InspectionResultFile = "InspectionResult_File";
        public static final String Photograph = "Photograph";
        public static final String Photograph_file = "Photograph_File";
        public static final String QUANTITY_DESC = "Quantity_Desc";
        public static final String QUANTITY_QTY = "Quantity_Qty";
        public static final String QUANTITY_UNIT = "Quantity_Unit";
        public static final String QUANTITY_FILE = "Quantity_File";


        public static final String Deviation = "Deviation";
        public static final String Deviation_file = "Deviation_file";
        public static final String Other = "Other";
        public static final String Other_file = "Other_file";
        public static final String TEST_A = "test_a";
        public static final String TEST_A_FILE = "test_a_file";
        public static final String TEST_B = "test_b";
        public static final String TEST_B_FILE = "test_b_file";
        public static final String TAG_TYPE = "tag_type";
        public static final String COUNTDOUN = "count_down";
        public static final String COUNT_A = "count_a";
        public static final String COUNT_B = "count_b";
        public static final String COUNT_C = "count_c";
        public static final String COUNT_D = "count_d";
        public static final String COUNT_E = "count_e";
        public static final String COUNT_F = "count_f";
        public static final String PHOTO_1 = "photo_1";
        public static final String PHOTOS = "photos";
        public static final String LOCATION="location";
        public static final String DESCRIPTION="description";
        public static final String SITEINCHARGE="siteincharge";
        public static final String RANGE="range";
        public static final String PROJECT_TYPE="project_type";
        public static final String NO_OF_JOBS="no_of_jobs";
        public static final String DONE_HOURS="done_hours";
        public static final String EXTRA_HOURS="extra_hours";
        public static final String CFRID="cfrid";
    }

    public static final class Login_Entry implements BaseColumns {

        //table name
        public static final String TABLE_LOGIN = "user";

        // fields
        public static final String _id = BaseColumns._ID;
        public static final String USERNAME = "username";
        public static final String PASS = "pass";
        public static final String STATION = "station";
        public static final String FULL_NAME = "full_name";
    }


    public static final class Material_Data_Entry implements BaseColumns {

        //table name
        public static final String TABLE_MATERIAL_DATA = "materials";

        // fields
        public static final String _id = BaseColumns._ID;
        public static final String NAME = "name";
        public static final String QTY = "qty";
        public static final String CUSTOMER_NAME = "customer_name";
        public static final String ADDED_DATE = "added_date";
        public static final String MODIFIED_DATE = "modified_date";
    }

    public static final class Final_DATA_ENTRY implements BaseColumns {

        //table name
        public static final String TABLE_FINAL_DATA = "final_qty";

        // fields
        public static final String INSPECTION_ID = "inspection_id";
        public static final String POidentitynum = "poidentitynum";
        public static final String ICSClientName = "icsclientname";
        public static final String PONumber = "ponumber";
        public static final String VendorName = "vendorname";
        public static final String ClientName = "clientname";
        public static final String INSPECTION_DATE = "inspection_date";
        public static final String QAP = "qap";
        public static final String ICS_REG_NUMBER = "ics_reg_number";
        public static final String REPORT_NO = "report_no";
        public static final String EMP_CODE = "emp_code";
        public static final String EMP_STATION = "emp_station";
        public static final String USERNAME = "username";
        public static final String BALANCE_QTY = "balance_qty";
        public static final String QTY_DESCRIPTION = "qty_description";
        public static final String PO_QTY = "po_qty";
        public static final String REL_QTY = "rel_qty";
        public static final String REJ_QTY = "rej_qty";
        public static final String INSP_QTY = "insp_qty";
        public static final String INSERTION_DATE = "insertion_date";
        public static final String STATUS = "status";
    }


    public static final class IrIrn_Data_Entry implements BaseColumns {
        //table name
        public static final String TABLE_IR_IRN = "IR_IRN";
        public static final String TABLE_TEMP_2ND_PAGE = "temp_2nd_page";

        public static final String _id = BaseColumns._ID;
        public static final String INSPECTION_ID = "inspection_id";
        public static final String PROJECT_VEND = "project_vend";
        public static final String PO_NUM = "po_num";
        //new
        public static final String SUB_VENDOR_PO_NUM = "sub_ven_po_num";

        public static final String CONSULTANT_NAME = "consultant_name";
        public static final String CUSTOMER_NAME = "customer_name";
        public static final String ICS_REG_NUMBER = "ics_reg_number";
        public static final String EMP_CODE = "emp_code";
        public static final String EMP_STATION = "emp_station";
        public static final String INSP_TYPE = "insp_type";
        public static final String FeInspection = "fe_inspection";
        public static final String FnInspection = "fn_inspection";
        public static final String VISUALA = "visualA";
        public static final String VISUALA_FILE = "visualA_file";
        public static final String VISUALB = "visualB";
        public static final String VISUALB_FILE = "visualB_file";
        public static final String Dimensional = "Dimensional";
        public static final String Dimensional_file = "Dimensional_file";
        public static final String Calibration = "Calibration";
        public static final String Calibration_file = "Calibration_file";
        public static final String ReportsR = "ReportsR";
        public static final String ReportsR_file = "ReportsR_file";
        public static final String TestWitness = "TestWitness";
        public static final String TestWitness_File = "TestWitness_File";
        //new
        public static final String INSPECTION_RESULTS = "InspectionResult";
        public static final String INSPECTION_RESULTS_File = "InspectionResult_File";
        public static final String PHOTOGRAPH = "Photograph";
        public static final String PHOTOGRAPH_File = "Photograph_File";
        public static final String QUANTITY_DESC = "Quantity_Desc";
        public static final String QUANTITY_QTY = "Quantity_Qty";
        public static final String QUANTITY_UNIT = "Quantity_Unit";
        public static final String QUANTITY_FILE = "Quantity_File";

        public static final String DATE_OF_INSP = "date_of_insp";
        public static final String ITEM_METARIAL = "item_metarial";
        public static final String QUANTITY = "quantity";
        public static final String SPEC_DRAWINGS = "spec_drawings";
        public static final String CODES_STANDARD = "codes_standard";
        public static final String Other = "Other";

        public static final String Other_file = "Other_file";

        public static final String Deviation = "Deviation";

        public static final String Deviation_file = "Deviation_file";

        public static final String Identification = "Identification";

        public static final String Identifation_file = "Identifation_file";

        public static final String EMP_NAME = "emp_name";
        public static final String STATION = "station";
        public static final String BATCH_NO = "batch_no";
        public static final String REPORT_NO = "report_no";
        public static final String PART_VISIT_SLIP = "part_visit_slip";
        public static final String PART_TC_SLIP = "part_tc_slip";
        public static final String UPLOAD_STAND = "upload_stand";
        public static final String PART_QAQC = "part_qaqc";
        public static final String UPLOAD_QAP = "upload_qap";
        public static final String UPLOAD_PO = "upload_po";
        public static final String PART_CALIB_PART_QAQC = "part_calib_part_qaqc";
        public static final String PART_OTHER = "part_other";
        public static final String DESC_NUM = "desc_num";
        public static final String TAG_TYPE = "tag_type";

        public static final String BALANCE_QTY = "balance_qty";
        public static final String QTY_DESCRIPTION = "qty_description";
        public static final String PO_QTY = "po_qty";
        public static final String REL_QTY = "rel_qty";
        public static final String REJ_QTY = "rej_qty";
        public static final String INSP_QTY = "insp_qty";

        public static final String MF_SUPPLIER_NAME = "mf_supplier_name";
        public static final String INSP_VISIT = "insp_visit";
        public static final String ANNEX = "annex";
        public static final String CUST_ID = "cust_ID";

        public static final String DATE_OF_FILLED_DATA = "date_of_filled_data";

        public static final String LOCATION="location";
        public static final String DESCRIPTION="description";
        public static final String SITEINCHARGE="siteincharge";
        public static final String RANGE="range";
        public static final String PROJECT_TYPE="project_type";
        public static final String NO_OF_JOBS="no_of_jobs";
        public static final String DONE_HOURS="done_hours";
        public static final String EXTRA_HOURS="extra_hours";
        public static final String CFRID="cfrid";
        public static final String SERVER_STATUS = "server_status";


    }

    // SP to Model Offline store database
    public static final class REPORT_SP_ENTRY implements BaseColumns {

        //table name
        public static final String TABLE_REPORT_SP = "report_sp";

        // fields
        public static final String INSPECTION_ID = "inspection_id";
        public static final String Ol_Name = "Ol_Name";
        public static final String ClientRegnum = "ClientRegnum";
        public static final String ConsultantName = "consultantname";
        public static final String VendorName = "VendorName";
        public static final String PONumber = "PONumber";
        public static final String Emp_Dt = "Emp_Dt";
        public static final String ReportNo = "ReportNo";
        public static final String Emp_Code = "Emp_Code";
        public static final String QAPCopy = "QAPCopy";
        public static final String POCopy = "POCopy";
        public static final String StandardCopy = "StandardCopy";
        public static final String EmpName = "EmpName";
        public static final String Station = "Station";
        public static final String Server_Status = "Server_Status";
        public static final String Project_Type ="Project_Type";
    }

    public static final class TEMP_1ST_DATA_ENTRY implements BaseColumns {
        //table name
        public static final String TABLE_TEMP_1ST = "temp_1st_page";

        // fields
        public static final String _id = BaseColumns._ID;
        public static final String INSPECTION_ID = "inspection_id";
        public static final String POidentitynum = "poidentitynum";
        public static final String ICSClientName = "icsclientname";
        public static final String PONumber = "ponumber";
        public static final String VendorName = "vendorname";
        public static final String PROJECT_VEND = "project_vend";
        public static final String PO_NUM = "po_num";

        public static final String SUB_VENDOR_PO_NUM = "sub_ven_po_num";

        public static final String CONSULTANT_NAME = "consultant_name";
        public static final String CUSTOMER_NAME = "customer_name";
        public static final String ICS_REG_NUMBER = "ics_reg_number";
        public static final String EMP_CODE = "emp_code";
        public static final String EMP_STATION = "emp_station";
        public static final String INSP_TYPE = "insp_type";
        public static final String DATE_OF_INSP = "date_of_insp";
        public static final String ITEM_METARIAL = "item_metarial";
        public static final String BATCH_NO = "batch_no";
        public static final String QUANTITY = "quantity";
        public static final String SPEC_DRAWINGS = "spec_drawings";
        public static final String CODES_STANDARD = "codes_standard";
        public static final String ClientName = "clientname";

        public static final String QAP = "qap";
        public static final String REPORT_NO = "report_no";
        public static final String USERNAME = "username";
        public static final String BALANCE_QTY = "balance_qty";
        public static final String QTY_DESCRIPTION = "qty_description";
        public static final String PO_QTY = "po_qty";
        public static final String REL_QTY = "rel_qty";
        public static final String REJ_QTY = "rej_qty";
        public static final String INSP_QTY = "insp_qty";
        public static final String INSERTION_DATE = "insertion_date";
        public static final String STATUS = "status";
        public static final String LOCATION = "location";
        public static final String RANGE = "range";
        public static final String No_OF_INSPECTION = "no_of_inspection";
        public static final String DESCRIPTION="description";
        public static final String SITEINCHARGE="siteincharge";



    }

    public static final class TEMP_ENTRY implements BaseColumns {

        //table name
        public static final String TABLE_A_TEMP = "A_temp";
        public static final String TABLE_AA_TEMP = "aa_temp";
        public static final String TABLE_B_TEMP = "b_temp";
        public static final String TABLE_C_TEMP = "c_temp";
        public static final String TABLE_D_TEMP = "d_temp";
        public static final String TABLE_E_TEMP = "e_temp";
        public static final String TABLE_F_TEMP = "f_temp";
        public static final String TABLE_G_TEMP = "g_temp";
        public static final String TABLE_H_TEMP = "h_temp";
        public static final String TABLE_I_TEMP = "i_temp";
        public static final String TABLE_J_TEMP = "j_temp";
        public static final String TABLE_H_TEMP_NEW = "h_temp_new";
        public static final String TABLE_J_TEMP_NEW = "j_temp_new";
        public static final String TABLE_K_TEMP = "k_temp";

        public static final String _id = BaseColumns._ID;
        public static final String INSPECTION_ID = "inspection_id";
        public static final String OBS = "obs";
        public static final String OBS_FILE = "obs_file";
        public static final String DESC_NUM = "desc_num";
        public static final String STATUS= "status";

        //new table for quantity
        public static final String TYPE= "TYPE";
        public static final String DESCRIPTION= "description";
        public static final String QTY= "qty";
        public static final String UNIT= "unit";
        public static final String QTY_FILE= "qty_file";

    }

    // added 11 march 2020
    public static final class OUTSTATION_LOCAL implements  BaseColumns
    {
        public static final String TABLE_OUTSTATION_LOCALEXPENSES="table_outstation_localexpenses";
        public static final String FROM_LOCAL ="from_local";
        public static final String TO_LOCAL ="to_local";
        public static final String EXPENSES ="expenses";
        public static final String IGST ="igst";
        public static final String CGST ="cgst";
        public static final String SGST ="sgst";
        public static final String GST_NO ="gst_no";
        public static final String SERVICE_PROVIDER ="service_provider";
        public static final String PAID_BY ="paid_by";
        public static final String INVOICEABLE ="invoiceable";
        public static final String NARRATION ="narration";
        public static final String CHOOSE_FILE ="choose_file";
    }
    public static final class OUTSTATION_BOARDING implements  BaseColumns
    {
        public static final String TABLE_OUTSTATION_BOARDINGEXPENSES="table_outstation_boardingexpenses";
        public static final String LOCATION ="location";
        public static final String EXPENSES ="expenses";
        public static final String IGST ="igst";
        public static final String CGST ="cgst";
        public static final String SGST ="sgst";
        public static final String GST_NO ="gst_no";
        public static final String SERVICE_PROVIDER ="service_provider";
        public static final String PAID_BY ="paid_by";
        public static final String INVOICEABLE ="invoiceable";
        public static final String NARRATION ="narration";
        public static final String CHOOSE_FILE ="choose_file";
    }

}
