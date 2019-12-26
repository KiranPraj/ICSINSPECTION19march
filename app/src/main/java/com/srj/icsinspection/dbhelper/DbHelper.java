package com.srj.icsinspection.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.srj.icsinspection.constants.DbConstant;
import com.srj.icsinspection.model.DescriptionModel;
import com.srj.icsinspection.model.DescriptionQuantityModel;
import com.srj.icsinspection.model.FirstPageTempModel;
import com.srj.icsinspection.model.GetFinalDescModel;
import com.srj.icsinspection.model.Location;
import com.srj.icsinspection.model.ReportDetailsModel;
import com.srj.icsinspection.model.SingleRowModel;
import com.srj.icsinspection.model.SiteInchargeModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbHelper";
    private static final String DB_NAME = "IOCL_CALIBRATION.db";
    private static final int DB_VERSION = 2;
    private String[] temp_tables = {DbConstant.TEMP_ENTRY.TABLE_A_TEMP,
            DbConstant.TEMP_ENTRY.TABLE_AA_TEMP,
            DbConstant.TEMP_ENTRY.TABLE_B_TEMP,
            DbConstant.TEMP_ENTRY.TABLE_C_TEMP,
            DbConstant.TEMP_ENTRY.TABLE_D_TEMP,
            DbConstant.TEMP_ENTRY.TABLE_E_TEMP,
            DbConstant.TEMP_ENTRY.TABLE_F_TEMP,
            DbConstant.TEMP_ENTRY.TABLE_G_TEMP,
            DbConstant.TEMP_ENTRY.TABLE_H_TEMP,
            DbConstant.TEMP_ENTRY.TABLE_I_TEMP,
            DbConstant.TEMP_ENTRY.TABLE_J_TEMP,
            DbConstant.TEMP_ENTRY.TABLE_H_TEMP_NEW,
            DbConstant.TEMP_ENTRY.TABLE_J_TEMP_NEW};

    // private static String on_foreign_key = "PRAGMA foreign_keys=ON;";


    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(CREATE_REPORT_SP);
        db.execSQL(CREATE_FINAL);
        db.execSQL(CREATE_IR_IR);
        db.execSQL(CREATE_TEMP_IR_IR);
        db.execSQL(TEMP_1ST_PAGE_DATA);

        for (String temp_table : temp_tables) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + temp_table + " ("
                    + DbConstant.TEMP_ENTRY._ID + " integer primary key autoincrement" + " , "
                    + DbConstant.TEMP_ENTRY.INSPECTION_ID + " VARCHAR(250) " + " , "
                    + DbConstant.TEMP_ENTRY.OBS + " VARCHAR(250) " + " , "
                    + DbConstant.TEMP_ENTRY.OBS_FILE + " VARCHAR(250) " + " , "
                    + DbConstant.TEMP_ENTRY.DESC_NUM + " integer " + " , "
                    + DbConstant.TEMP_ENTRY.STATUS + " VARCHAR(250)" + "); ");

        }
        //quantity table
        db.execSQL("CREATE TABLE "+ DbConstant.TEMP_ENTRY.TABLE_K_TEMP+"("
                + DbConstant.TEMP_ENTRY._ID + " integer primary key autoincrement" + " , "
                + DbConstant.TEMP_ENTRY.INSPECTION_ID + " VARCHAR(250) " + " , "
                + DbConstant.TEMP_ENTRY.DESCRIPTION + " VARCHAR(250) " + " , "
                + DbConstant.TEMP_ENTRY.QTY + " VARCHAR(250) " + " , "
                + DbConstant.TEMP_ENTRY.UNIT + " VARCHAR(250) " + " , "
                + DbConstant.TEMP_ENTRY.QTY_FILE + " VARCHAR(250) " + " , "
                + DbConstant.TEMP_ENTRY.DESC_NUM + " integer " + " , "
                + DbConstant.TEMP_ENTRY.STATUS + " VARCHAR(250)" + "); ");
        //loading location
        db.execSQL("CREATE TABLE " + DbConstant.location_entry.TABLE_LOCATION + "(" + DbConstant.location_entry.LOCATION + " VARCHAR(200))");
//Creating discription Table
        db.execSQL("CREATE TABLE " + DbConstant.Description_Entry.DESCRIPTION + "(" + DbConstant.Description_Entry.CLIENTREG_NO + " VARCHAR(250),"
                + DbConstant.Description_Entry.DESCRIPTION + " VARCHAR(200) " + "," + DbConstant.Description_Entry.CFIRD + " VARCHAR(200))");
//Creating quantity_discription Table
        db.execSQL("CREATE TABLE " + DbConstant.QuantityDescription_Entry.TABLE_QUANTITY_DESCRIPTION + "(" + DbConstant.QuantityDescription_Entry.CLIENTREG_NO + " VARCHAR(250),"
                + DbConstant.QuantityDescription_Entry.DESCRIPTION + " VARCHAR(200) " + "," + DbConstant.QuantityDescription_Entry.UNIT + " VARCHAR(200)"+ "," + DbConstant.QuantityDescription_Entry.CFIRD + " VARCHAR(200)"+ "," + DbConstant.QuantityDescription_Entry.PONUMBER + " VARCHAR(200))");
//CREATING SITEINCHARGE TABLE
        db.execSQL("CREATE TABLE " + DbConstant.SiteIncharge_Entry.TABLE_SITEINCHARGE + "(" + DbConstant.SiteIncharge_Entry.REGNOCLIENT + " VARCHAR(250),"
                + DbConstant.SiteIncharge_Entry.SITEINCHARGENAME + " VARCHAR(200))");

        // creating CATEGORY table
        db.execSQL("CREATE TABLE " + DbConstant.Inspection_Entry.TABLE_INSPECTION + "("
                + DbConstant.Inspection_Entry._ID + " integer primary key autoincrement" + " , "
                + DbConstant.Inspection_Entry.INSPECTION_ID + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.CUSTOMER_NAEM + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.ICS_REG_NUM + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.CONSULTANT_NAEM + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.MF_SUPPLIER_NAEM + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.PROJECT_VEND + " VARCHAR(250) " + " , "
                //new
                + DbConstant.Inspection_Entry.SUB_VENDOR_PO_NUMBER + " VARCHAR(250) " + " , "

                + DbConstant.Inspection_Entry.ITEM + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.BATCH_NO + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.QUANTITY + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.SPEC_DRAWINGS + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.CODES_STANDARD + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.DATE_OF_INSP + " DATE" + " , "
                + DbConstant.Inspection_Entry.INSP_TYPE + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.ANNEXURE_A + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.PHOTO_1 + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.PHOTO_2 + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.EMP_NAME + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.EMP_CODE + " VARCHAR(250) " + " , "

                + DbConstant.Inspection_Entry.VISUALA + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.VISUALA_FILE + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.VISUALB + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.VISUALB_FILE + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.Dimensional + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.Dimensional_file + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.Calibration + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.Calibration_file + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.ReportsR + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.ReportsR_file + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.TestWitness + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.TestWitness_File + " VARCHAR(250) " + " , "
                //new
                + DbConstant.Inspection_Entry.InspectionResult + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.InspectionResultFile + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.Photograph + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.Photograph_file + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.QUANTITY_DESC + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.QUANTITY_QTY + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.QUANTITY_UNIT + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.QUANTITY_FILE + " VARCHAR(250) " + " , "

                + DbConstant.Inspection_Entry.Identifation + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.Identifation_file + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.Deviation + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.Deviation_file + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.Other + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.Other_file + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.TEST_A + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.TEST_A_FILE + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.TEST_B + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.TEST_B_FILE + " VARCHAR(250) " + " , "

                + DbConstant.Inspection_Entry.EMP_STATION + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.OBSERVATIION + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.TAG_TYPE + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.COUNTDOUN + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.COUNT_A + " integer " + " , "
                + DbConstant.Inspection_Entry.COUNT_B + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.COUNT_C + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.COUNT_D + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.COUNT_E + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.COUNT_F + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.PHOTOS + " VARCHAR(250) " + " , "

                //new entry
                + DbConstant.Inspection_Entry.LOCATION + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.DESCRIPTION + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.SITEINCHARGE + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.RANGE + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.PROJECT_TYPE + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.NO_OF_JOBS + " VARCHAR(250) " + " , "
                + DbConstant.Inspection_Entry.DONE_HOURS + " VARCHAR(250)" + " , "
                + DbConstant.Inspection_Entry.EXTRA_HOURS + " VARCHAR(250)" + "); "


        );

        // creating LOGIN table
        db.execSQL("CREATE TABLE " + DbConstant.Login_Entry.TABLE_LOGIN + "("
                + DbConstant.Login_Entry._ID + " integer primary key autoincrement" + " , "
                + DbConstant.Login_Entry.USERNAME + " VARCHAR(250) " + " , "
                + DbConstant.Login_Entry.PASS + " VARCHAR(250) " + " , "
                + DbConstant.Login_Entry.STATION + " VARCHAR(250) " + "); "
        );

        // creating MATERIAL DATA table
        db.execSQL("CREATE TABLE " + DbConstant.Material_Data_Entry.TABLE_MATERIAL_DATA + "("
                + DbConstant.Material_Data_Entry._ID + " integer primary key autoincrement" + " , "
                + DbConstant.Material_Data_Entry.NAME + " VARCHAR(250) " + " , "
                + DbConstant.Material_Data_Entry.CUSTOMER_NAME + " VARCHAR(250) " + " , "
                + DbConstant.Material_Data_Entry.QTY + " integer " + " , "
                + DbConstant.Material_Data_Entry.ADDED_DATE + " DATETIME " + " , "
                + DbConstant.Material_Data_Entry.MODIFIED_DATE + " DATETIME   " + "); "
        );

        // insertMaterialData(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade: old version is :" + oldVersion);

        /*
        if (oldVersion < 2) {
            db.execSQL(CREATE_REPORT_SP);

            db.execSQL(CREATE_FINAL);
            db.execSQL(CREATE_IR_IR);
            db.execSQL(TEMP_1ST_PAGE_DATA);
        }
        if (oldVersion < 3) {
            Log.i(TAG, "onUpgrade: old version is 2");
            db.execSQL(TEMP_1ST_PAGE_DATA);
        }*/
        if(newVersion>oldVersion){
            String query="ALTER TABLE "+DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN +" ADD COLUMN "+DbConstant.IrIrn_Data_Entry.SUB_VENDOR_PO_NUM+ " VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN +" ADD COLUMN "+DbConstant.IrIrn_Data_Entry.INSPECTION_RESULTS+" VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN +" ADD COLUMN "+DbConstant.IrIrn_Data_Entry.INSPECTION_RESULTS_File+" VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN +" ADD COLUMN "+DbConstant.IrIrn_Data_Entry.PHOTOGRAPH+" VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN +" ADD COLUMN "+DbConstant.IrIrn_Data_Entry.PHOTOGRAPH_File+" VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN +" ADD COLUMN "+DbConstant.IrIrn_Data_Entry.QUANTITY_DESC+" VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN +" ADD COLUMN "+DbConstant.IrIrn_Data_Entry.QUANTITY_QTY+" VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN +" ADD COLUMN "+DbConstant.IrIrn_Data_Entry.QUANTITY_UNIT+" VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN +" ADD COLUMN "+DbConstant.IrIrn_Data_Entry.QUANTITY_FILE+" VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN +" ADD COLUMN "+DbConstant.IrIrn_Data_Entry.Other_file+" VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN +" ADD COLUMN "+DbConstant.IrIrn_Data_Entry.Deviation_file+" VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN +" ADD COLUMN "+DbConstant.IrIrn_Data_Entry.Identifation_file+" VARCHAR(250) ;";
            db.execSQL(query);

            query="ALTER TABLE "+DbConstant.Inspection_Entry.TABLE_INSPECTION +" ADD COLUMN "+DbConstant.Inspection_Entry.SUB_VENDOR_PO_NUMBER+ " VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.Inspection_Entry.TABLE_INSPECTION  +" ADD COLUMN "+DbConstant.Inspection_Entry.InspectionResult+" VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.Inspection_Entry.TABLE_INSPECTION  +" ADD COLUMN "+DbConstant.Inspection_Entry.InspectionResultFile+" VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.Inspection_Entry.TABLE_INSPECTION  +" ADD COLUMN "+DbConstant.Inspection_Entry.Photograph+" VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.Inspection_Entry.TABLE_INSPECTION  +" ADD COLUMN "+DbConstant.Inspection_Entry.Photograph_file+" VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.Inspection_Entry.TABLE_INSPECTION  +" ADD COLUMN "+DbConstant.Inspection_Entry.QUANTITY_DESC+" VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.Inspection_Entry.TABLE_INSPECTION  +" ADD COLUMN "+DbConstant.Inspection_Entry.QUANTITY_QTY+" VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.Inspection_Entry.TABLE_INSPECTION +" ADD COLUMN "+DbConstant.Inspection_Entry.QUANTITY_UNIT+" VARCHAR(250) ;";
            db.execSQL(query);
            query="ALTER TABLE "+DbConstant.Inspection_Entry.TABLE_INSPECTION +" ADD COLUMN "+DbConstant.Inspection_Entry.QUANTITY_FILE+" VARCHAR(250) ;";
            db.execSQL(query);

            query="ALTER TABLE "+DbConstant.TEMP_1ST_DATA_ENTRY.TABLE_TEMP_1ST +" ADD COLUMN "+DbConstant.TEMP_1ST_DATA_ENTRY.SUB_VENDOR_PO_NUM+ " VARCHAR(250) ;";
            db.execSQL(query);

            for (String temp_table : temp_tables) {
                db.execSQL("CREATE TABLE IF NOT EXISTS " + temp_table + " ("
                        + DbConstant.TEMP_ENTRY._ID + " integer primary key autoincrement" + " , "
                        + DbConstant.TEMP_ENTRY.INSPECTION_ID + " VARCHAR(250) " + " , "
                        + DbConstant.TEMP_ENTRY.OBS + " VARCHAR(250) " + " , "
                        + DbConstant.TEMP_ENTRY.OBS_FILE + " VARCHAR(250) " + " , "
                        + DbConstant.TEMP_ENTRY.DESC_NUM + " integer " + " , "
                        + DbConstant.TEMP_ENTRY.STATUS + " VARCHAR(250)" + "); ");

            }
            //quantity table
            db.execSQL("CREATE TABLE "+ DbConstant.TEMP_ENTRY.TABLE_K_TEMP+"("
                    + DbConstant.TEMP_ENTRY._ID + " integer primary key autoincrement" + " , "
                    + DbConstant.TEMP_ENTRY.INSPECTION_ID + " VARCHAR(250) " + " , "
                    + DbConstant.TEMP_ENTRY.DESCRIPTION + " VARCHAR(250) " + " , "
                    + DbConstant.TEMP_ENTRY.QTY + " VARCHAR(250) " + " , "
                    + DbConstant.TEMP_ENTRY.UNIT + " VARCHAR(250) " + " , "
                    + DbConstant.TEMP_ENTRY.QTY_FILE + " VARCHAR(250) " + " , "
                    + DbConstant.TEMP_ENTRY.DESC_NUM + " integer " + " , "
                    + DbConstant.TEMP_ENTRY.STATUS + " VARCHAR(250)" + "); ");

            db.execSQL("CREATE TABLE " + DbConstant.QuantityDescription_Entry.TABLE_QUANTITY_DESCRIPTION + "(" + DbConstant.QuantityDescription_Entry.CLIENTREG_NO + " VARCHAR(250),"
                    + DbConstant.QuantityDescription_Entry.DESCRIPTION + " VARCHAR(200) " + "," + DbConstant.QuantityDescription_Entry.UNIT + " VARCHAR(200)"+ "," + DbConstant.QuantityDescription_Entry.CFIRD + " VARCHAR(200)"+ "," + DbConstant.QuantityDescription_Entry.PONUMBER + " VARCHAR(200))");

        }

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    private static final String CREATE_FINAL =
            "CREATE TABLE " + DbConstant.Final_DATA_ENTRY.TABLE_FINAL_DATA + "("
                    + DbConstant.Final_DATA_ENTRY.INSPECTION_ID + " integer primary key autoincrement" + " , "
                    + DbConstant.Final_DATA_ENTRY.POidentitynum + " VARCHAR(250) " + " , "
                    + DbConstant.Final_DATA_ENTRY.ICSClientName + " VARCHAR(250) " + " , "
                    + DbConstant.Final_DATA_ENTRY.PONumber + " VARCHAR(250) " + " , "
                    + DbConstant.Final_DATA_ENTRY.VendorName + " VARCHAR(250) " + " , "
                    + DbConstant.Final_DATA_ENTRY.ClientName + " VARCHAR(250) " + " , "
                    + DbConstant.Final_DATA_ENTRY.INSPECTION_DATE + " VARCHAR(250) " + " , "
                    + DbConstant.Final_DATA_ENTRY.QAP + " VARCHAR(250) " + " , "
                    + DbConstant.Final_DATA_ENTRY.ICS_REG_NUMBER + " VARCHAR(250) " + " , "
                    + DbConstant.Final_DATA_ENTRY.REPORT_NO + " VARCHAR(200) " + " , "
                    + DbConstant.Final_DATA_ENTRY.EMP_CODE + " VARCHAR(40) " + " , "
                    + DbConstant.Final_DATA_ENTRY.EMP_STATION + " VARCHAR(150) " + " , "
                    + DbConstant.Final_DATA_ENTRY.USERNAME + " VARCHAR(250) " + " , "
                    + DbConstant.Final_DATA_ENTRY.BALANCE_QTY + " integer " + " , "
                    + DbConstant.Final_DATA_ENTRY.QTY_DESCRIPTION + " VARCHAR(250) " + " , "
                    + DbConstant.Final_DATA_ENTRY.PO_QTY + " integer " + " , "
                    + DbConstant.Final_DATA_ENTRY.REL_QTY + " integer " + " , "
                    + DbConstant.Final_DATA_ENTRY.REJ_QTY + " integer " + " , "
                    + DbConstant.Final_DATA_ENTRY.INSP_QTY + " integer " + " , "
                    + DbConstant.Final_DATA_ENTRY.INSERTION_DATE + " VARCHAR(250) " + " , "
                    + DbConstant.Final_DATA_ENTRY.STATUS + " VARCHAR(250)   " + "); ";


    // Main table for syncing data to server in offline
    private static final String CREATE_IR_IR = "CREATE TABLE " + DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN + "("
            + DbConstant.IrIrn_Data_Entry._ID + " integer primary key autoincrement" + " , "
            + DbConstant.IrIrn_Data_Entry.INSPECTION_ID + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PROJECT_VEND + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PO_NUM + " VARCHAR(250) " + " , "
            //new
            + DbConstant.IrIrn_Data_Entry.SUB_VENDOR_PO_NUM + " VARCHAR(250) " + " , "

            + DbConstant.IrIrn_Data_Entry.CONSULTANT_NAME + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.CUSTOMER_NAME + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.ICS_REG_NUMBER + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.EMP_CODE + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.EMP_STATION + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.INSP_TYPE + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.FeInspection + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.FnInspection + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.VISUALA + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.VISUALA_FILE + " DATE" + " , "
            + DbConstant.IrIrn_Data_Entry.VISUALB + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.VISUALB_FILE + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.Dimensional + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.Dimensional_file + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.Calibration + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.Calibration_file + " VARCHAR(250) " + " , "

            + DbConstant.IrIrn_Data_Entry.ReportsR + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.ReportsR_file + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.TestWitness + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.TestWitness_File + " VARCHAR(250) " + " , "
           //new
            + DbConstant.IrIrn_Data_Entry.INSPECTION_RESULTS + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.INSPECTION_RESULTS_File + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PHOTOGRAPH + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PHOTOGRAPH_File + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.QUANTITY_DESC + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.QUANTITY_QTY + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.QUANTITY_UNIT + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.QUANTITY_FILE + " VARCHAR(250) " + " , "

            + DbConstant.IrIrn_Data_Entry.DATE_OF_INSP + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.ITEM_METARIAL + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.QUANTITY + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.SPEC_DRAWINGS + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.CODES_STANDARD + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.Other + " VARCHAR(250) " + " , "

            + DbConstant.IrIrn_Data_Entry.Other_file + " VARCHAR(250) " + " , "

            + DbConstant.IrIrn_Data_Entry.Deviation + " VARCHAR(250) " + " , "

            + DbConstant.IrIrn_Data_Entry.Deviation_file + " VARCHAR(250) " + " , "

            + DbConstant.IrIrn_Data_Entry.Identification + " VARCHAR(250) " + " , "

            + DbConstant.IrIrn_Data_Entry.Identifation_file + " VARCHAR(250) " + " , "

            + DbConstant.IrIrn_Data_Entry.EMP_NAME + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.STATION + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.BATCH_NO + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.REPORT_NO + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PART_VISIT_SLIP + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PART_TC_SLIP + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.UPLOAD_STAND + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PART_QAQC + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.UPLOAD_QAP + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.UPLOAD_PO + " VARCHAR(250) " + " , "

            + DbConstant.IrIrn_Data_Entry.PART_CALIB_PART_QAQC + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PART_OTHER + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.DESC_NUM + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.TAG_TYPE + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.BALANCE_QTY + " integer " + " , "
            + DbConstant.IrIrn_Data_Entry.QTY_DESCRIPTION + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PO_QTY + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.REL_QTY + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.REJ_QTY + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.INSP_QTY + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.MF_SUPPLIER_NAME + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.INSP_VISIT + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.ANNEX + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.CUST_ID + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.DATE_OF_FILLED_DATA + " VARCHAR(250) " + " , "
            + DbConstant.Inspection_Entry.LOCATION + " VARCHAR(250) " + " , "
            + DbConstant.Inspection_Entry.DESCRIPTION + " VARCHAR(250) " + " , "
            + DbConstant.Inspection_Entry.SITEINCHARGE + " VARCHAR(250) " + " , "
            + DbConstant.Inspection_Entry.RANGE + " VARCHAR(250) " + " , "
            + DbConstant.Inspection_Entry.PROJECT_TYPE + " VARCHAR(250) " + " , "
            + DbConstant.Inspection_Entry.NO_OF_JOBS + " VARCHAR(250)" + " , "
            + DbConstant.Inspection_Entry.DONE_HOURS + " VARCHAR(250)" + " , "
            + DbConstant.Inspection_Entry.EXTRA_HOURS + " VARCHAR(250)" + " , "
            + DbConstant.Inspection_Entry.CFRID + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.SERVER_STATUS + " VARCHAR(250)" + "); ";


    // Temp table for saving data to server in offline
    private static final String CREATE_TEMP_IR_IR = "CREATE TABLE " + DbConstant.IrIrn_Data_Entry.TABLE_TEMP_2ND_PAGE + "("
            + DbConstant.IrIrn_Data_Entry._ID + " integer primary key autoincrement" + " , "
            + DbConstant.IrIrn_Data_Entry.INSPECTION_ID + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PROJECT_VEND + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PO_NUM + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.SUB_VENDOR_PO_NUM + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.CONSULTANT_NAME + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.CUSTOMER_NAME + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.ICS_REG_NUMBER + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.EMP_CODE + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.EMP_STATION + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.INSP_TYPE + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.FeInspection + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.FnInspection + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.VISUALA + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.VISUALA_FILE + " DATE" + " , "
            + DbConstant.IrIrn_Data_Entry.VISUALB + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.VISUALB_FILE + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.Dimensional + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.Dimensional_file + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.Calibration + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.Calibration_file + " VARCHAR(250) " + " , "

            + DbConstant.IrIrn_Data_Entry.ReportsR + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.ReportsR_file + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.TestWitness + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.TestWitness_File + " VARCHAR(250) " + " , "
            //new
            + DbConstant.IrIrn_Data_Entry.INSPECTION_RESULTS + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.INSPECTION_RESULTS_File + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PHOTOGRAPH + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PHOTOGRAPH_File + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.QUANTITY_DESC + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.QUANTITY_QTY + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.QUANTITY_UNIT + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.QUANTITY_FILE + " VARCHAR(250) " + " , "

            + DbConstant.IrIrn_Data_Entry.DATE_OF_INSP + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.ITEM_METARIAL + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.QUANTITY + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.SPEC_DRAWINGS + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.CODES_STANDARD + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.Other + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.Other_file + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.Deviation + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.Deviation_file + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.Identification + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.Identifation_file + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.EMP_NAME + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.STATION + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.BATCH_NO + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.REPORT_NO + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PART_VISIT_SLIP + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PART_TC_SLIP + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.UPLOAD_STAND + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PART_QAQC + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.UPLOAD_QAP + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.UPLOAD_PO + " VARCHAR(250) " + " , "

            + DbConstant.IrIrn_Data_Entry.PART_CALIB_PART_QAQC + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PART_OTHER + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.DESC_NUM + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.TAG_TYPE + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.BALANCE_QTY + " integer " + " , "
            + DbConstant.IrIrn_Data_Entry.QTY_DESCRIPTION + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.PO_QTY + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.REL_QTY + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.REJ_QTY + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.INSP_QTY + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.MF_SUPPLIER_NAME + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.DATE_OF_FILLED_DATA + " VARCHAR(250) " + " , "
            + DbConstant.IrIrn_Data_Entry.SERVER_STATUS + " VARCHAR(250)" + "); ";
    //  + "FOREIGN KEY(" + DbConstant.IrIrn_Data_Entry.INSPECTION_ID + ")" +
    //  " REFERENCES " + DbConstant.Inspection_Entry.TABLE_INSPECTION + "(" + DbConstant.Inspection_Entry.INSPECTION_ID + ")" + "); ";
//0
    // Main table for syncing data to server in offline
    private static final String CREATE_REPORT_SP = "CREATE TABLE " + DbConstant.REPORT_SP_ENTRY.TABLE_REPORT_SP + "("
            + DbConstant.REPORT_SP_ENTRY._ID + " integer primary key autoincrement" + " , "
            + DbConstant.REPORT_SP_ENTRY.INSPECTION_ID + " VARCHAR(250) " + " , "
            + DbConstant.REPORT_SP_ENTRY.Ol_Name + " VARCHAR(250) " + " , "
            + DbConstant.REPORT_SP_ENTRY.ClientRegnum + " VARCHAR(250) " + " , "
            + DbConstant.REPORT_SP_ENTRY.ConsultantName + " VARCHAR(250) " + " , "
            + DbConstant.REPORT_SP_ENTRY.VendorName + " VARCHAR(250) " + " , "
            + DbConstant.REPORT_SP_ENTRY.PONumber + " VARCHAR(250) " + " , "
            + DbConstant.REPORT_SP_ENTRY.Emp_Dt + " VARCHAR(250) " + " , "
            + DbConstant.REPORT_SP_ENTRY.ReportNo + " VARCHAR(250) " + " , "
            + DbConstant.REPORT_SP_ENTRY.Emp_Code + " VARCHAR(250) " + " , "
            + DbConstant.REPORT_SP_ENTRY.POCopy + " VARCHAR(250) " + " , "
            + DbConstant.REPORT_SP_ENTRY.QAPCopy + " VARCHAR(250) " + " , "
            + DbConstant.REPORT_SP_ENTRY.StandardCopy + " VARCHAR(250) " + " , "
            + DbConstant.REPORT_SP_ENTRY.EmpName + " VARCHAR(250) " + " , "
            + DbConstant.REPORT_SP_ENTRY.Station + " VARCHAR(250) " + " , "
            + DbConstant.REPORT_SP_ENTRY.Server_Status + " VARCHAR(250)" + ","
            + DbConstant.REPORT_SP_ENTRY.Project_Type + " VARCHAR(250)" + "); ";

    // Temproray table to save data of 1st fragment inspection
    private static final String TEMP_1ST_PAGE_DATA = "CREATE TABLE " + DbConstant.TEMP_1ST_DATA_ENTRY.TABLE_TEMP_1ST + "("
            + DbConstant.TEMP_1ST_DATA_ENTRY._ID + " integer primary key autoincrement" + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.INSPECTION_ID + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.POidentitynum + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.ICSClientName + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.PONumber + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.VendorName + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.PROJECT_VEND + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.PO_NUM + " VARCHAR(250) " + " , "
            //new
            + DbConstant.TEMP_1ST_DATA_ENTRY.SUB_VENDOR_PO_NUM + " VARCHAR(250) " + " , "

            + DbConstant.TEMP_1ST_DATA_ENTRY.CONSULTANT_NAME + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.CUSTOMER_NAME + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.ICS_REG_NUMBER + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.EMP_CODE + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.EMP_STATION + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.INSP_TYPE + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.DATE_OF_INSP + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.ITEM_METARIAL + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.QUANTITY + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.BATCH_NO + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.SPEC_DRAWINGS + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.CODES_STANDARD + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.ClientName + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.REPORT_NO + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.USERNAME + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.BALANCE_QTY + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.QTY_DESCRIPTION + " VARCHAR(250) " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.PO_QTY + " REAL " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.REL_QTY + " REAL " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.INSP_QTY + " REAL " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.INSERTION_DATE + " DATE " + " , "
            + DbConstant.TEMP_1ST_DATA_ENTRY.STATUS + " VARCHAR(250)" + ","
            + DbConstant.TEMP_1ST_DATA_ENTRY.LOCATION + " VARCHAR(100)" + ","
            + DbConstant.TEMP_1ST_DATA_ENTRY.RANGE + " VARCHAR(250)" + ","
            + DbConstant.TEMP_1ST_DATA_ENTRY.No_OF_INSPECTION + " VARCHAR(250)" + ","
            + DbConstant.TEMP_1ST_DATA_ENTRY.DESCRIPTION + " VARCHAR(250)" + ","
            + DbConstant.TEMP_1ST_DATA_ENTRY.SITEINCHARGE + " VARCHAR(250)" +
            "); ";

    public long insertImagePath(String annexure_a, String Observe_C, String et_single_obs,
                                String file_C, String calibration_file, String inspection_id, int i) {
        ContentValues mValues = new ContentValues();
        SQLiteDatabase mSqLiteDatabase = this.getWritableDatabase();
        mValues.put(DbConstant.Inspection_Entry.ANNEXURE_A, annexure_a);
        mValues.put(Observe_C, et_single_obs);
        mValues.put(file_C, calibration_file);

        return mSqLiteDatabase.updateWithOnConflict(DbConstant.Inspection_Entry.TABLE_INSPECTION, mValues,
                DbConstant.Inspection_Entry.INSPECTION_ID + "=? AND count_down=?",
                new String[]{inspection_id, String.valueOf(i)}, SQLiteDatabase.CONFLICT_REPLACE);

    }
    public long insertImagePathQTY(String annexure_a, String DESC, String tv_desc,
                                   String Observe_C, String et_qty,
                                   String unit, String tv_unit,
                                   String file_C, String calibration_file, String inspection_id, int i) {
        ContentValues mValues = new ContentValues();
        SQLiteDatabase mSqLiteDatabase = this.getWritableDatabase();
        mValues.put(DbConstant.Inspection_Entry.ANNEXURE_A, annexure_a);
        mValues.put(DESC, tv_desc);
        mValues.put(Observe_C, et_qty);
        mValues.put(unit, tv_unit);
        mValues.put(file_C, calibration_file);

        return mSqLiteDatabase.updateWithOnConflict(DbConstant.Inspection_Entry.TABLE_INSPECTION, mValues,
                DbConstant.Inspection_Entry.INSPECTION_ID + "=? AND count_down=?",
                new String[]{inspection_id, String.valueOf(i)}, SQLiteDatabase.CONFLICT_REPLACE);

    }

    public long location(ArrayList<Location> mList) {
        ContentValues mValues = new ContentValues();
        SQLiteDatabase mSqLiteDatabase = this.getWritableDatabase();

        long id = 0;
        for (Location model : mList) {
            mSqLiteDatabase.beginTransaction();
            try {

                String city = model.getCity().toString().replace("[", "");
                city = city.replace("]", "");
                List<String> values = Arrays.asList(city.split(","));
                for (int i = 0; i < values.size(); i++) {
                    mValues.put(DbConstant.location_entry.LOCATION, values.get(i).trim());
                    id = mSqLiteDatabase.insert(DbConstant.location_entry.TABLE_LOCATION, null, mValues);
                }

                mSqLiteDatabase.setTransactionSuccessful();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mSqLiteDatabase.endTransaction();
            }

        }
        return id;
    }

    public long SiteIncharge(ArrayList<SiteInchargeModel> mList) {
        ContentValues mValues = new ContentValues();
        SQLiteDatabase mSqLiteDatabase = this.getWritableDatabase();
        long id = 0;
        for (SiteInchargeModel model : mList) {
            mSqLiteDatabase.beginTransaction();

            try {
                mValues.put(DbConstant.SiteIncharge_Entry.REGNOCLIENT, model.getClientregNum());
                mValues.put(DbConstant.SiteIncharge_Entry.SITEINCHARGENAME, model.getSiteIncharge());
                id = mSqLiteDatabase.insert(DbConstant.SiteIncharge_Entry.TABLE_SITEINCHARGE, null, mValues);
                mSqLiteDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mSqLiteDatabase.endTransaction();
            }
        }
        return id;
    }

    public long Description(ArrayList<DescriptionModel> mList) {
        ContentValues mValues = new ContentValues();
        SQLiteDatabase mSqLiteDatabase = this.getWritableDatabase();
        long id = 0;


        for (DescriptionModel model : mList) {
            mSqLiteDatabase.beginTransaction();

            try {
                mValues.put(DbConstant.Description_Entry.CLIENTREG_NO, model.getClientregNum());
                mValues.put(DbConstant.Description_Entry.DESCRIPTION, model.getDescription());
                mValues.put(DbConstant.Description_Entry.CFIRD, model.getCfrid());
                id = mSqLiteDatabase.insert(DbConstant.Description_Entry.TABLE_DESCRIPTION, null, mValues);
                mSqLiteDatabase.setTransactionSuccessful();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mSqLiteDatabase.endTransaction();
            }
        }
        return id;
    }
    public long QuantityDescription( ArrayList<DescriptionQuantityModel.Dtqty> mList) {
        ContentValues mValues = new ContentValues();
        SQLiteDatabase mSqLiteDatabase = this.getWritableDatabase();
        long id = 0;

        for (DescriptionQuantityModel.Dtqty model : mList) {
            mSqLiteDatabase.beginTransaction();

            try {
                mValues.put(DbConstant.QuantityDescription_Entry.CLIENTREG_NO, model.getICSRegNumber().toString());
                mValues.put(DbConstant.QuantityDescription_Entry.DESCRIPTION, model.description);
                mValues.put(DbConstant.QuantityDescription_Entry.CFIRD, model.getPOidentitynum());
                mValues.put(DbConstant.QuantityDescription_Entry.PONUMBER, model.getPONumber());
                mValues.put(DbConstant.QuantityDescription_Entry.UNIT, model.getUnits());
                id = mSqLiteDatabase.insert(DbConstant.QuantityDescription_Entry.TABLE_QUANTITY_DESCRIPTION, null, mValues);
                mSqLiteDatabase.setTransactionSuccessful();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mSqLiteDatabase.endTransaction();
            }
        }
        return id;
    }
    public long insertReportSP(String ol_name, String clientregnum, String reportNo, String VendorName, String PONumber,
                               String Emp_Dt, String emp_code, String POCopy, String consultantname,
                               String qapCopy, String StandardCopy, String EmpName, String Station, String ProjectType) {

        ContentValues mValues = new ContentValues();
        SQLiteDatabase mSqLiteDatabase = this.getWritableDatabase();
        long id = 0;
        mSqLiteDatabase.beginTransaction();
        try {
            mValues.put(DbConstant.REPORT_SP_ENTRY.Ol_Name, ol_name);
            mValues.put(DbConstant.REPORT_SP_ENTRY.ClientRegnum, clientregnum);
            mValues.put(DbConstant.REPORT_SP_ENTRY.VendorName, VendorName);
            mValues.put(DbConstant.REPORT_SP_ENTRY.PONumber, PONumber);
            mValues.put(DbConstant.REPORT_SP_ENTRY.ConsultantName, consultantname);
            mValues.put(DbConstant.REPORT_SP_ENTRY.Emp_Dt, Emp_Dt);
            mValues.put(DbConstant.REPORT_SP_ENTRY.ReportNo, reportNo);
            mValues.put(DbConstant.REPORT_SP_ENTRY.Emp_Code, emp_code);
            mValues.put(DbConstant.REPORT_SP_ENTRY.POCopy, POCopy);
            mValues.put(DbConstant.REPORT_SP_ENTRY.QAPCopy, qapCopy);
            mValues.put(DbConstant.REPORT_SP_ENTRY.StandardCopy, StandardCopy);
            mValues.put(DbConstant.REPORT_SP_ENTRY.EmpName, EmpName);
            mValues.put(DbConstant.REPORT_SP_ENTRY.Station, Station);
            mValues.put(DbConstant.REPORT_SP_ENTRY.Server_Status, "pending");
            mValues.put(DbConstant.REPORT_SP_ENTRY.Project_Type, ProjectType);

            id = mSqLiteDatabase.insert(DbConstant.REPORT_SP_ENTRY.TABLE_REPORT_SP, null, mValues);
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSqLiteDatabase.endTransaction();
        }
        return id;

    }

    // get Customer names list in Spinner
    public ArrayList<String> getCustomerName(String date) {
        ArrayList<String> mList = new ArrayList<>();
        mList.add("Select");

        SQLiteDatabase mDatabase = this.getReadableDatabase();
        Cursor mCursor = mDatabase.query(DbConstant.REPORT_SP_ENTRY.TABLE_REPORT_SP,
                new String[]{DbConstant.REPORT_SP_ENTRY.Ol_Name},
                "Server_Status=? AND Emp_Dt=?", new String[]{"pending", date}/*new String[]{"done"}*/, null, null, DbConstant.REPORT_SP_ENTRY.Emp_Dt);
        if (mCursor != null && mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                if (!mList.contains(mCursor.getString(0)))
                    mList.add(mCursor.getString(0));
            }
        }

        if (mCursor != null) {
            mCursor.close();
        }
        mDatabase.close();
        return mList;

    }

    //get Location
    public ArrayList<String> geLocations() {
        ArrayList<String> mList = new ArrayList<>();
        mList.add("Select");

        SQLiteDatabase mDatabase = this.getReadableDatabase();
        Cursor mCursor = mDatabase.rawQuery("SELECT * FROM " + DbConstant.location_entry.TABLE_LOCATION, null);
        if (mCursor != null && mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                if (!mList.contains(mCursor.getString(0)))
                    mList.add(mCursor.getString(0));
            }
        }

        if (mCursor != null) {
            mCursor.close();
        }
        mDatabase.close();
        return mList;
    }

    //Get Description
    public ArrayList<String> getdescription(String regno) {
        ArrayList<String> mList = new ArrayList<>();
        mList.add("Select");
        SQLiteDatabase mDatabase = this.getReadableDatabase();
        Cursor mCursor = mDatabase.rawQuery("SELECT * FROM " + DbConstant.Description_Entry.TABLE_DESCRIPTION + " WHERE " + DbConstant.Description_Entry.CLIENTREG_NO + "=\"" + regno + "\"", null);
        if (mCursor != null && mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                if (!mList.contains(mCursor.getString(1)))
                    mList.add(mCursor.getString(1));
            }
        }

        if (mCursor != null) {
            mCursor.close();
        }
        mDatabase.close();
        return mList;


    }
    //Get Quantity Description
    public Cursor getQuantitydescription(String regno) {
        ArrayList<DescriptionQuantityModel> mList = new ArrayList<DescriptionQuantityModel>();
        //mList.add("Select");
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        Cursor mCursor = mDatabase.rawQuery("SELECT * FROM " + DbConstant.QuantityDescription_Entry.TABLE_QUANTITY_DESCRIPTION + " WHERE " + DbConstant.QuantityDescription_Entry.CLIENTREG_NO + "=\"" + regno + "\"", null);



        return mCursor;


    }

    public String getcfrid(String Description) {
        String cfrid = "";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //Cursor cursor= sqLiteDatabase.rawQuery("SELECT * FROM "+DbConstant.Description_Entry.TABLE_DESCRIPTION+" WHERE "+ DbConstant.Description_Entry.DESCRIPTION +"=\""+Description+"\"",null);
        //cfrid=cursor.getString(cursor.getColumnIndex("cfird"));


        Cursor cursor = null;
        String Cfird = "";
        try {
            String selectQuery = "SELECT  * FROM " + DbConstant.Description_Entry.TABLE_DESCRIPTION + " WHERE "
                    + DbConstant.Description_Entry.DESCRIPTION + " = \"" + Description + "\"";
            cursor = sqLiteDatabase.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                cfrid = cursor.getString(cursor.getColumnIndex("cfird"));
            }

            return cfrid;
        } finally {

            cursor.close();
        }

    }

    //Get Site Incharge
    public ArrayList<String> getsiteincharge(String regno) {
        ArrayList<String> mList = new ArrayList<>();
        mList.add("Select");
        SQLiteDatabase mDatabase = this.getReadableDatabase();
        Cursor mCursor = mDatabase.rawQuery("SELECT * FROM " + DbConstant.SiteIncharge_Entry.TABLE_SITEINCHARGE + " WHERE " + DbConstant.SiteIncharge_Entry.REGNOCLIENT + "=\"" + regno + "\"", null);
        if (mCursor != null && mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                if (!mList.contains(mCursor.getString(1)))
                    mList.add(mCursor.getString(1));
            }
        }

        if (mCursor != null) {
            mCursor.close();
        }
        mDatabase.close();
        return mList;

    }

    // get PoNumber (CustomId) lists
    public ArrayList<String> getVendorsName(String customer_name) {
        ArrayList<String> mList = new ArrayList<>();
        mList.add("Select");
        SQLiteDatabase mDatabase = this.getReadableDatabase();
        Cursor mCursor = mDatabase.query(DbConstant.REPORT_SP_ENTRY.TABLE_REPORT_SP,
                new String[]{DbConstant.REPORT_SP_ENTRY.VendorName}, DbConstant.REPORT_SP_ENTRY.Ol_Name + "=? AND Server_Status=? ",
                new String[]{customer_name, "pending"}, null, null, DbConstant.REPORT_SP_ENTRY.Emp_Dt);
        if (mCursor != null && mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                Log.i(TAG, "getVendorsName: " + mCursor.getString(0));
                if (!mList.contains(mCursor.getString(0)))
                    mList.add(mCursor.getString(0));
            }
        }

        if (mCursor != null) {
            mCursor.close();
        }
        mDatabase.close();
        return mList;

    }

    // get PoNumber (CustomId) lists
    public Cursor getbindPoata(String customerName, String vendorName) {
        SQLiteDatabase mDatabase = this.getReadableDatabase();

        Log.i(TAG, "bindPo_$_Data: ");
        return mDatabase.query(DbConstant.REPORT_SP_ENTRY.TABLE_REPORT_SP, null,
                DbConstant.REPORT_SP_ENTRY.Ol_Name + "=?  AND " +
                        DbConstant.REPORT_SP_ENTRY.VendorName + "=? AND " +
                        DbConstant.REPORT_SP_ENTRY.Server_Status + "=? ",
                new String[]{customerName, vendorName, "pending"}, null, null, DbConstant.REPORT_SP_ENTRY.Emp_Dt);


    }

    // insert final DESCRIPTIONS of QTY
    public void insertFinalDescription(ArrayList<GetFinalDescModel> mList) {

        SQLiteDatabase mDatabase = this.getWritableDatabase();
        for (GetFinalDescModel model : mList) {
            mDatabase.beginTransaction();
            try {
                ContentValues mValues = new ContentValues();
                mValues.put(DbConstant.Final_DATA_ENTRY.POidentitynum, model.getPOidentitynum());
                mValues.put(DbConstant.Final_DATA_ENTRY.ICSClientName, model.getIcsClientName());
                mValues.put(DbConstant.Final_DATA_ENTRY.PONumber, model.getPoNumber());
                mValues.put(DbConstant.Final_DATA_ENTRY.VendorName, model.getVendorName());
                mValues.put(DbConstant.Final_DATA_ENTRY.ClientName, model.getClientName());
                mValues.put(DbConstant.Final_DATA_ENTRY.QAP, model.getQap());
                mValues.put(DbConstant.Final_DATA_ENTRY.ICS_REG_NUMBER, model.getIcsRegNumber());
                mValues.put(DbConstant.Final_DATA_ENTRY.QTY_DESCRIPTION, model.getDescription());
                mValues.put(DbConstant.Final_DATA_ENTRY.PO_QTY, model.getPoQty() != null ? model.getPoQty() : "0");
                mValues.put(DbConstant.Final_DATA_ENTRY.REL_QTY, (model.getRelsQty() != null) ? model.getRelsQty() : 0);
                mValues.put(DbConstant.Final_DATA_ENTRY.REJ_QTY, (model.getRejectedQty() != null) ? model.getRejectedQty() : 0);
                mValues.put(DbConstant.Final_DATA_ENTRY.BALANCE_QTY, (model.getBalQty() != null) ? model.getBalQty() : 0);
                mValues.put(DbConstant.Final_DATA_ENTRY.INSP_QTY, model.getInspectedQty() != null ? model.getInspectedQty() : 0);
                mValues.put(DbConstant.Final_DATA_ENTRY.STATUS, "fetched");
                mValues.put(DbConstant.Final_DATA_ENTRY.INSERTION_DATE, String.valueOf(System.currentTimeMillis()));


                mDatabase.insert(DbConstant.Final_DATA_ENTRY.TABLE_FINAL_DATA, null, mValues);
                mDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDatabase.endTransaction();
            }
        }
    }

    // get final recycler view data list
    public Cursor getFinalList(String poNumber, String vendorName) {
        SQLiteDatabase mDatabase = this.getReadableDatabase();
        return mDatabase.query(DbConstant.Final_DATA_ENTRY.TABLE_FINAL_DATA, null,
                DbConstant.Final_DATA_ENTRY.PONumber + "=? AND " + DbConstant.Final_DATA_ENTRY.VendorName
                        + " =? AND " + DbConstant.Final_DATA_ENTRY.STATUS + " =? ",
                new String[]{poNumber, vendorName, "fetched"}, null, null, null, null);
    }

    /* public String saveAllFinalData() {
         String selectQuery = "Select * from " + DbConstant.Final_DATA_ENTRY.TABLE_FINAL_DATA;
         SQLiteDatabase db = this.getReadableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);
         JSONObject mObject;
         JSONArray arr = new JSONArray();
         while (cursor.moveToNext()) {
             mObject = new JSONObject();
             try {
                 mObject.put("POidentitynum", cursor.getString(cursor.getColumnIndex("poidentitynum")));
                 mObject.put("we", cursor.getString(cursor.getColumnIndex("icsclientname")));
                 mObject.put("PONumber", cursor.getString(cursor.getColumnIndex("ponumber")));
                 mObject.put("VendorName", cursor.getString(cursor.getColumnIndex("vendorname")));
                 mObject.put("ClientName", cursor.getString(cursor.getColumnIndex("clientname")));
                 mObject.put("QAP", cursor.getString(cursor.getColumnIndex("qap")));
                 mObject.put("ICSRegNumber", cursor.getString(cursor.getColumnIndex("ics_reg_number")));
                 mObject.put("POidentitynum", cursor.getString(cursor.getColumnIndex("report_no")));
                 mObject.put("emp_code", cursor.getString(cursor.getColumnIndex("emp_code")));
                 mObject.put("emp_station", cursor.getString(cursor.getColumnIndex("emp_station")));
                 mObject.put("username", cursor.getString(cursor.getColumnIndex("username")));
                 mObject.put("bal_qty", cursor.getString(cursor.getColumnIndex("balance_qty")));
                 mObject.put("qty_description", cursor.getString(cursor.getColumnIndex("qty_description")));
                 mObject.put("po_qty", cursor.getString(cursor.getColumnIndex("po_qty")));
                 mObject.put("rels_qty", cursor.getInt(cursor.getColumnIndex("rel_qty")));
                 mObject.put("rejected_qty", cursor.getInt(cursor.getColumnIndex("rej_qty")));
                 mObject.put("inspected_qty", cursor.getInt(cursor.getColumnIndex("insp_qty")));
                 mObject.put("insertion_date", cursor.getString(cursor.getColumnIndex("insertion_date")));
                 mObject.put("status", cursor.getString(cursor.getColumnIndex("status")));
             } catch (JSONException e) {
                 e.printStackTrace();
             }

             arr.put(mObject);
         }

         mObject = new JSONObject();
         try {
             mObject.put("data", arr);
             cursor.close();
         } catch (JSONException e) {
             e.printStackTrace();
         }

         return mObject.toString();
     }*/

    public Cursor getPendingIRIRN() {
        SQLiteDatabase mDatabase = this.getReadableDatabase();
//       return mDatabase.query(DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN, null,
//               "server_status=?", new String[]{"pending"}, null, null,
//               DbConstant.IrIrn_Data_Entry.DESC_NUM + " ASC");
        return mDatabase.rawQuery("SELECT * FROM " + DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN + " WHERE " + DbConstant.IrIrn_Data_Entry.SERVER_STATUS + "='pending' GROUP BY "+ DbConstant.IrIrn_Data_Entry.DATE_OF_INSP + "," + DbConstant.IrIrn_Data_Entry.PO_NUM   + "  ORDER BY " + DbConstant.IrIrn_Data_Entry.DATE_OF_INSP, null);
    }
    public Cursor getDoneIRIRN() {
        SQLiteDatabase mDatabase = this.getReadableDatabase();
//       return mDatabase.query(DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN, null,
//               "server_status=?", new String[]{"pending"}, null, null,
//               DbConstant.IrIrn_Data_Entry.DESC_NUM + " ASC");
        return mDatabase.rawQuery("SELECT * FROM " + DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN + " WHERE " + DbConstant.IrIrn_Data_Entry.SERVER_STATUS + "='done' GROUP BY "+ DbConstant.IrIrn_Data_Entry.DATE_OF_INSP + "," + DbConstant.IrIrn_Data_Entry.PO_NUM  + " ORDER BY " + DbConstant.IrIrn_Data_Entry.DATE_OF_INSP +" DESC", null);
    }

    public Cursor getPendingdata() {
        SQLiteDatabase mDatabase = this.getReadableDatabase();
//       return mDatabase.query(DbConstant.Final_DATA_ENTRY.TABLE_FINAL_DATA, null,
//               "status=?", new String[]{"pending"}, null, null,
//               null);
        String Query="SELECT DISTINCT " + DbConstant.Final_DATA_ENTRY.INSPECTION_DATE + "," + DbConstant.Final_DATA_ENTRY.PONumber + " FROM " + DbConstant.Final_DATA_ENTRY.TABLE_FINAL_DATA + " WHERE " + DbConstant.Final_DATA_ENTRY.STATUS + "='pending'" + " ORDER BY " + DbConstant.Final_DATA_ENTRY.INSPECTION_DATE;
        return mDatabase.rawQuery(Query, null);
    }

    public Cursor getAllFinalData(String date, String ponumber) {
        SQLiteDatabase mDatabase = this.getReadableDatabase();
        return mDatabase.query(DbConstant.Final_DATA_ENTRY.TABLE_FINAL_DATA, null,
                "status=? AND " + DbConstant.Final_DATA_ENTRY.INSPECTION_DATE + "=? AND " + DbConstant.Final_DATA_ENTRY.PONumber + "=?", new String[]{"pending", date, ponumber}, null, null,
                null, null);
    }
    public void updateFianlAfterSync(String date,String ponumber){
        SQLiteDatabase database= this.getWritableDatabase();
        ContentValues mValues = new ContentValues();
        mValues.put(DbConstant.Final_DATA_ENTRY.STATUS, "fetched");
        database.update(DbConstant.Final_DATA_ENTRY.TABLE_FINAL_DATA, mValues,
                "status=? AND " + DbConstant.Final_DATA_ENTRY.INSPECTION_DATE + "=? AND " + DbConstant.Final_DATA_ENTRY.PONumber + "=?", new String[]{"done", date, ponumber});

    }
    public void updateFinalIRIrnQty(Double balQty, String po_number, String ics_reg_num, String description,
                                    String reportNo, String clientName, String vendorName, String date_of_insp,
                                    String emp_name, String user_name, String station,
                                    Double relsQty, Double rejectedQty, Double inspQty) {
        SQLiteDatabase mDatabase = this.getWritableDatabase();


        ContentValues mValues = new ContentValues();
        mValues.put(DbConstant.Final_DATA_ENTRY.BALANCE_QTY, balQty);
        mValues.put(DbConstant.Final_DATA_ENTRY.REPORT_NO, reportNo);
        mValues.put(DbConstant.Final_DATA_ENTRY.ICSClientName, clientName);
        mValues.put(DbConstant.Final_DATA_ENTRY.VendorName, vendorName);
        mValues.put(DbConstant.Final_DATA_ENTRY.INSPECTION_DATE, date_of_insp);
        mValues.put(DbConstant.Final_DATA_ENTRY.ClientName, po_number);
        mValues.put(DbConstant.Final_DATA_ENTRY.EMP_CODE, user_name);
        mValues.put(DbConstant.Final_DATA_ENTRY.USERNAME, emp_name);
        mValues.put(DbConstant.Final_DATA_ENTRY.STATUS, station);
        mValues.put(DbConstant.Final_DATA_ENTRY.PO_QTY, balQty + relsQty);
        mValues.put(DbConstant.Final_DATA_ENTRY.REL_QTY, relsQty);
        mValues.put(DbConstant.Final_DATA_ENTRY.REJ_QTY, rejectedQty);
        mValues.put(DbConstant.Final_DATA_ENTRY.INSP_QTY, inspQty);
        mValues.put(DbConstant.Final_DATA_ENTRY.STATUS, "pending");
        mValues.put(DbConstant.Final_DATA_ENTRY.INSERTION_DATE, String.valueOf(System.currentTimeMillis()));

        mDatabase.updateWithOnConflict(DbConstant.Final_DATA_ENTRY.TABLE_FINAL_DATA, mValues,
                "ics_reg_number = ? and ponumber=? and qty_description=? and "+DbConstant.Final_DATA_ENTRY.STATUS+"=?",
                new String[]{ics_reg_num, po_number, description,"fetched"}, SQLiteDatabase.CONFLICT_REPLACE);

    }
    public void changefinal()
    {
        SQLiteDatabase mDatabase = this.getWritableDatabase();

        try
        {
//            String query= "UPDATE "+DbConstant.Final_DATA_ENTRY.TABLE_FINAL_DATA+" SET "+DbConstant.Final_DATA_ENTRY.STATUS +"='changed' WHERE "+DbConstant.Final_DATA_ENTRY.STATUS +"='fetched'";
          //  mDatabase.rawQuery(query,null);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbConstant.Final_DATA_ENTRY.STATUS,"changed");
           mDatabase.update(DbConstant.Final_DATA_ENTRY.TABLE_FINAL_DATA,contentValues,DbConstant.Final_DATA_ENTRY.STATUS+"=?",new String[]{"fetched"});
            Log.i(TAG, "check_IR_DATA: FOUND COUNT " );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cursor check_IR_DATA(String date, String ponumber) {
        SQLiteDatabase mDatabase = this.getReadableDatabase();
        Cursor mCursor = null;
        try {
          /*  mCursor = mDatabase.query(DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN, null,
                    "server_status=? AND " + DbConstant.IrIrn_Data_Entry.DATE_OF_INSP + "=? AND " + DbConstant.IrIrn_Data_Entry.PO_NUM + "=?", new String[]{"pending", date, ponumber}, null, null,
                    DbConstant.IrIrn_Data_Entry.DESC_NUM + " ASC", null);
*/
            String Maxquery="select max(desc_num) from IR_IRN WHERE server_status='pending' AND date_of_insp='"+date+"' and po_num='"+ponumber+"' ";
            String query = "select * from IR_IRN WHERE server_status='pending' AND " +
                    "date_of_insp='" + date + "' and po_num='" + ponumber + "' order by _id desc limit (select max(desc_num) from IR_IRN WHERE server_status='pending' AND date_of_insp='"+date+"' and po_num='"+ponumber+"')";
            mCursor = mDatabase.rawQuery(query, null);

            Log.e(TAG, "check_IR_DATA: " + query);

            Log.e(TAG, "check_IR_DATA: FOUND COUNT " + mCursor.getCount());



        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCursor;
    }

    public long dropInspectionTable() {
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        return mDatabase.delete(DbConstant.Inspection_Entry.TABLE_INSPECTION, null, null);
    }

    public void deleteReportAndFinal() {
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        mDatabase.delete(DbConstant.REPORT_SP_ENTRY.TABLE_REPORT_SP, null, null);
        mDatabase.delete(DbConstant.Final_DATA_ENTRY.TABLE_FINAL_DATA, null, null);
        mDatabase.delete(DbConstant.location_entry.TABLE_LOCATION,null,null);
        mDatabase.delete(DbConstant.Description_Entry.TABLE_DESCRIPTION,null,null);
        mDatabase.delete(DbConstant.SiteIncharge_Entry.TABLE_SITEINCHARGE,null,null);
       /* mDatabase.rawQuery("DROP TABLE "+DbConstant.REPORT_SP_ENTRY.TABLE_REPORT_SP, null, null);
        mDatabase.rawQuery("DROP TABLE "+DbConstant.Final_DATA_ENTRY.TABLE_FINAL_DATA, null, null);
        mDatabase.rawQuery("DROP TABLE "+DbConstant.location_entry.TABLE_LOCATION,null,null);
        mDatabase.rawQuery("DROP TABLE "+DbConstant.Description_Entry.TABLE_DESCRIPTION,null,null);
        mDatabase.rawQuery("DROP TABLE "+DbConstant.SiteIncharge_Entry.TABLE_SITEINCHARGE,null,null);
        mDatabase.rawQuery("DROP TABLE "+DbConstant.QuantityDescription_Entry.TABLE_QUANTITY_DESCRIPTION,null,null);
        mDatabase.rawQuery("DROP TABLE "+DbConstant.TEMP_1ST_DATA_ENTRY.TABLE_TEMP_1ST ,null,null);
        mDatabase.rawQuery("DROP TABLE "+DbConstant.Inspection_Entry.TABLE_INSPECTION ,null,null);
        mDatabase.rawQuery("DROP TABLE "+DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN ,null,null);*/
    }

 /*   public void doneInspectionID(String date,String ponumber) {
        Log.i(TAG, "doneInspectionID: Done  id: ");
        SQLiteDatabase mDatabase = this.getReadableDatabase();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(DbConstant.IrIrn_Data_Entry.SERVER_STATUS, "done");

        mDatabase.update(DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN, mContentValues, "date_of_insp=? and po_num=?",
                new String[]{date,ponumber});
    }*/

    // added
    public void doneInspectionID(String id) {
        Log.i(TAG, "doneInspectionID: Done  id: ");
        SQLiteDatabase mDatabase = this.getReadableDatabase();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(DbConstant.IrIrn_Data_Entry.SERVER_STATUS, "done");

        mDatabase.update(DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN, mContentValues, "_id=?",
                new String[]{id});
    }



    public void doneFinalInspectionID(String id) {

        SQLiteDatabase mDatabase = this.getReadableDatabase();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(DbConstant.Final_DATA_ENTRY.STATUS, "done");

        mDatabase.update(DbConstant.Final_DATA_ENTRY.TABLE_FINAL_DATA, mContentValues, "inspection_id=?",
                new String[]{id});
    }

    public void updateReportSPDone(String reg_no, String insp_date, String po_num, String vendor_name) {
        SQLiteDatabase mDatabase = this.getReadableDatabase();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(DbConstant.REPORT_SP_ENTRY.Server_Status, "done");

        mDatabase.update(DbConstant.REPORT_SP_ENTRY.TABLE_REPORT_SP, mContentValues,
                "Emp_Dt=? AND ClientRegNum=? AND PONumber=? AND VendorName=?",
                new String[]{insp_date, reg_no, po_num, vendor_name});
    }

    public void updateHolidaySp(String date) {
        SQLiteDatabase mDatabase = this.getReadableDatabase();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(DbConstant.REPORT_SP_ENTRY.Server_Status, "done");

        mDatabase.update(DbConstant.REPORT_SP_ENTRY.TABLE_REPORT_SP, mContentValues,
                "Emp_Dt=?",
                new String[]{date});
    }

    public String getMaxDescNum(String _id, String cust_ID) {
        SQLiteDatabase mDatabase = this.getReadableDatabase();
        Cursor mCursor = mDatabase.rawQuery("select max(cast(desc_num as integer)) as abc from IR_IRN " +
                "where inspection_id='" + _id + "' AND cust_id='" + cust_ID + "'", null);

        String id = null;
        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                id = mCursor.getString(mCursor.getColumnIndex("abc"));
                Log.i(TAG, "getMaxDescNum: MAX: " + id);
            }
        } else id = "0";
        mCursor.close();
        return id;
    }

    // ************************ Fetch after successful Sync ******************* \\

    //  after successful server sync fetch new TABLE_REPORT_SP data
    public void getNewDataAfterInsert(List<ReportDetailsModel> mList) {
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        try {
            for (int i = 0; i < mList.size(); i++) {

                ContentValues mValues = new ContentValues();
                mValues.put(DbConstant.REPORT_SP_ENTRY.Ol_Name, mList.get(i).getOlName());
                mValues.put(DbConstant.REPORT_SP_ENTRY.ClientRegnum, mList.get(i).getClientRegnum());
                mValues.put(DbConstant.REPORT_SP_ENTRY.VendorName, mList.get(i).getVendorName());
                mValues.put(DbConstant.REPORT_SP_ENTRY.PONumber, mList.get(i).getPONumber());
                mValues.put(DbConstant.REPORT_SP_ENTRY.Emp_Dt, mList.get(i).getEmpDt());
                mValues.put(DbConstant.REPORT_SP_ENTRY.ReportNo, mList.get(i).getReportNo());
                mValues.put(DbConstant.REPORT_SP_ENTRY.Emp_Code, mList.get(i).getEmpCode());
                mValues.put(DbConstant.REPORT_SP_ENTRY.POCopy, mList.get(i).getPOCopy());
                mValues.put(DbConstant.REPORT_SP_ENTRY.QAPCopy, mList.get(i).getQAPCopy());
                mValues.put(DbConstant.REPORT_SP_ENTRY.StandardCopy, mList.get(i).getStandardCopy());
                mValues.put(DbConstant.REPORT_SP_ENTRY.EmpName, mList.get(i).getEmpName());
                mValues.put(DbConstant.REPORT_SP_ENTRY.Station, mList.get(i).getStation());

                Cursor mCursor = mDatabase.rawQuery("SELECT count( * ) as count from " + DbConstant.REPORT_SP_ENTRY.TABLE_REPORT_SP
                                + " where " + DbConstant.REPORT_SP_ENTRY.Ol_Name + "=?"
                                + " AND " + DbConstant.REPORT_SP_ENTRY.ClientRegnum + "=?"
                                + " AND " + DbConstant.REPORT_SP_ENTRY.VendorName + "=?"
                                + " AND " + DbConstant.REPORT_SP_ENTRY.PONumber + "=?"
                                + " AND " + DbConstant.REPORT_SP_ENTRY.Emp_Dt + "=?"
                                + " AND " + DbConstant.REPORT_SP_ENTRY.ReportNo + "=?"
                                + " AND " + DbConstant.REPORT_SP_ENTRY.Emp_Code + "=?"
                                + " AND " + DbConstant.REPORT_SP_ENTRY.POCopy + "=?"
                                + " AND " + DbConstant.REPORT_SP_ENTRY.QAPCopy + "=?"
                                + " AND " + DbConstant.REPORT_SP_ENTRY.StandardCopy + "=?"
                                + " AND " + DbConstant.REPORT_SP_ENTRY.EmpName + "=?"
                                + " AND " + DbConstant.REPORT_SP_ENTRY.Station + "=?"
                        , new String[]{mList.get(i).getOlName(),
                                mList.get(i).getClientRegnum(),
                                mList.get(i).getVendorName(),
                                mList.get(i).getPONumber(),
                                mList.get(i).getEmpDt(),
                                mList.get(i).getReportNo(),
                                mList.get(i).getEmpCode(),
                                mList.get(i).getPOCopy(),
                                mList.get(i).getQAPCopy(),
                                mList.get(i).getStandardCopy(),
                                mList.get(i).getEmpName(),
                                mList.get(i).getStation()
                        });

                if (mCursor != null && mCursor.getCount() > 0) {
                    mCursor.moveToFirst();
                    if (mCursor.getInt(0) == 0)
                        mDatabase.insert(DbConstant.REPORT_SP_ENTRY.TABLE_REPORT_SP, null, mValues);
                    else Log.i(TAG, "report_sp  Rows Exist already");
                    mCursor.close();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            mDatabase.close();
        }


    }

    //  after successful server sync fetch new DESCRIPTION DATA  data
    public void getNewDescriptionDataAfterInsert(ArrayList<GetFinalDescModel> mList) {
        SQLiteDatabase mDatabase = this.getWritableDatabase();

        try {
            for (GetFinalDescModel model : mList) {

                ContentValues mValues = new ContentValues();
                mValues.put(DbConstant.Final_DATA_ENTRY.POidentitynum, model.getPOidentitynum());
                mValues.put(DbConstant.Final_DATA_ENTRY.ICSClientName, model.getIcsClientName());
                mValues.put(DbConstant.Final_DATA_ENTRY.PONumber, model.getPoNumber());
                mValues.put(DbConstant.Final_DATA_ENTRY.VendorName, model.getVendorName());
                mValues.put(DbConstant.Final_DATA_ENTRY.ClientName, model.getClientName());
                mValues.put(DbConstant.Final_DATA_ENTRY.QAP, model.getQap());
                mValues.put(DbConstant.Final_DATA_ENTRY.ICS_REG_NUMBER, model.getIcsRegNumber());
                mValues.put(DbConstant.Final_DATA_ENTRY.QTY_DESCRIPTION, model.getDescription());
                mValues.put(DbConstant.Final_DATA_ENTRY.PO_QTY, model.getPoQty() != null ? model.getPoQty() : "0");
                mValues.put(DbConstant.Final_DATA_ENTRY.REL_QTY, (model.getRelsQty() != null) ? model.getRelsQty() : 0);
                mValues.put(DbConstant.Final_DATA_ENTRY.REJ_QTY, (model.getRejectedQty() != null) ? model.getRejectedQty() : 0);
                mValues.put(DbConstant.Final_DATA_ENTRY.BALANCE_QTY, (model.getBalQty() != null) ? model.getBalQty() : 0);
                mValues.put(DbConstant.Final_DATA_ENTRY.INSP_QTY, model.getInspectedQty() != null ? model.getInspectedQty() : 0);
                mValues.put(DbConstant.Final_DATA_ENTRY.STATUS, "fetched");
                mValues.put(DbConstant.Final_DATA_ENTRY.INSERTION_DATE, String.valueOf(System.currentTimeMillis()));


                Cursor mCursor = mDatabase.rawQuery("SELECT count( * ) as count from " +
                                DbConstant.Final_DATA_ENTRY.TABLE_FINAL_DATA + " where "
                                + DbConstant.Final_DATA_ENTRY.POidentitynum + "=?"
                                + " AND " + DbConstant.Final_DATA_ENTRY.ICSClientName + "=?"
                                + " AND " + DbConstant.Final_DATA_ENTRY.PONumber + "=?"
                                + " AND " + DbConstant.Final_DATA_ENTRY.VendorName + "=?"
                                + " AND " + DbConstant.Final_DATA_ENTRY.ClientName + "=?"
                                + " AND " + DbConstant.Final_DATA_ENTRY.QAP + "=?"
                                + " AND " + DbConstant.Final_DATA_ENTRY.ICS_REG_NUMBER + "=?"
                                + " AND " + DbConstant.Final_DATA_ENTRY.QTY_DESCRIPTION + "=?"
                                + " AND " + DbConstant.Final_DATA_ENTRY.PO_QTY + "=?"
                                + " AND " + DbConstant.Final_DATA_ENTRY.REL_QTY + "=?"
                                + " AND " + DbConstant.Final_DATA_ENTRY.REJ_QTY + "=?"
                                + " AND " + DbConstant.Final_DATA_ENTRY.BALANCE_QTY + "=?"
                                + " AND " + DbConstant.Final_DATA_ENTRY.INSP_QTY + "=?"
                        , new String[]{
                                model.getPOidentitynum(),
                                model.getIcsClientName(),
                                model.getPoNumber(),
                                model.getVendorName(),
                                model.getClientName(),
                                model.getQap(),
                                model.getIcsRegNumber(),
                                model.getDescription(),
                                String.valueOf(model.getPoQty() != null ? model.getPoQty() : 0),
                                String.valueOf(model.getRelsQty() != null ? model.getRelsQty() : 0),
                                String.valueOf(model.getRejectedQty() != null ? model.getRejectedQty() : 0),
                                String.valueOf(model.getBalQty() != null ? model.getBalQty() : 0),
                                String.valueOf(model.getInspectedQty() != null ? model.getInspectedQty() : 0)
                        });
                if (mCursor != null && mCursor.getCount() > 0) {
                    mCursor.moveToFirst();
                    if (mCursor.getInt(0) == 0) {
                        mDatabase.insert(DbConstant.Final_DATA_ENTRY.TABLE_FINAL_DATA, null, mValues); //intCode is primary key
                    }else {
                        Log.i(TAG, "getNewDescriptionDataAfterInsert  Rows Exist already");
                    }
                    mCursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            mDatabase.close();
        }

    }


    // ############################ Temp Tables for Fetching and saving Data  #################### \\

    public Cursor loadFirstPageData(String custName, String vendorName, String poNumbder,
                                    String inspDate, String consulatnt, String empCode) {
        SQLiteDatabase mDatabase = this.getWritableDatabase();

        String selection = DbConstant.TEMP_1ST_DATA_ENTRY.CUSTOMER_NAME + "=? AND "
                + DbConstant.TEMP_1ST_DATA_ENTRY.PONumber + "=? AND "
                + DbConstant.TEMP_1ST_DATA_ENTRY.DATE_OF_INSP + "=? AND "
                + DbConstant.TEMP_1ST_DATA_ENTRY.VendorName + "=? AND "
                + DbConstant.TEMP_1ST_DATA_ENTRY.EMP_CODE + "=?";
        String[] selectionArgs = new String[]{custName, poNumbder, inspDate, vendorName, empCode};
//        return mDatabase.query(DbConstant.TEMP_1ST_DATA_ENTRY.TABLE_TEMP_1ST, null, selection,
//                selectionArgs, null, null, null);
        return mDatabase.query(DbConstant.TEMP_1ST_DATA_ENTRY.TABLE_TEMP_1ST, null, selection, selectionArgs, null, null, null);
    }

    public Cursor LoadholidayData(String Date) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String selection = DbConstant.TEMP_1ST_DATA_ENTRY.DATE_OF_INSP + "=?";
        String[] selectionargs = new String[]{Date};
        return sqLiteDatabase.query(DbConstant.TEMP_1ST_DATA_ENTRY.TABLE_TEMP_1ST, null, selection, selectionargs, null, null, null);
    }

    public void updateFirstPage(FirstPageTempModel mdl) {
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        ContentValues mValues = new ContentValues();

        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.INSPECTION_ID, mdl.getUuid());
        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.ITEM_METARIAL, mdl.getItemMaterial());
        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.BATCH_NO, mdl.getBatchNo());
        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.QUANTITY, mdl.getQuantity());
        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.SPEC_DRAWINGS, mdl.getSpecDrw());
        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.CODES_STANDARD, mdl.getCodeStand());
        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.CUSTOMER_NAME, mdl.getCustName());
        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.VendorName, mdl.getVendName());
        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.PONumber, mdl.getPoNumber());
        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.SUB_VENDOR_PO_NUM, mdl.getSubVendrPoNumber());
        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.DATE_OF_INSP, mdl.getDateOfInspe());
        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.CONSULTANT_NAME, mdl.getEt_cnslt_name());
        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.EMP_CODE, mdl.getEmpCode());
        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.LOCATION, mdl.getLocation());
        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.RANGE, mdl.getRange());
        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.DESCRIPTION, mdl.getDescription());
        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.SITEINCHARGE, mdl.getSiteIncharge());
        mValues.put(DbConstant.TEMP_1ST_DATA_ENTRY.No_OF_INSPECTION, mdl.getNoofinspection());

        String selection = //DbConstant.TEMP_1ST_DATA_ENTRY.CUSTOMER_NAME + "=? AND "
                //     + DbConstant.TEMP_1ST_DATA_ENTRY.VendorName + "=? AND "
                //   + DbConstant.TEMP_1ST_DATA_ENTRY.PONumber + "=? AND "
                DbConstant.TEMP_1ST_DATA_ENTRY.DATE_OF_INSP + "=? AND "
//                + DbConstant.TEMP_1ST_DATA_ENTRY.CONSULTANT_NAME + "=? AND "
                        //+ DbConstant.TEMP_1ST_DATA_ENTRY.EMP_CODE + "=? AND "
                        + DbConstant.TEMP_1ST_DATA_ENTRY.INSPECTION_ID + "=? ";
//                + DbConstant.TEMP_1ST_DATA_ENTRY.LOCATION+"=? AND "
//                + DbConstant.TEMP_1ST_DATA_ENTRY.RANGE+"=? AND "
//                + DbConstant.TEMP_1ST_DATA_ENTRY.DESCRIPTION +"=? AND "
//                + DbConstant.TEMP_1ST_DATA_ENTRY.No_OF_INSPECTION+"=?";


        String[] selectionArgs = new String[]{
                //mdl.getCustName(), mdl.getVendName(), mdl.getPoNumber(),
                mdl.getDateOfInspe(),
                //mdl.getEmpCode(),
                mdl.getUuid()
//                ,mdl.getLocation(),mdl.getRange(),mdl.getDescription(),mdl.getNoofinspection()
        };

        int id = mDatabase.updateWithOnConflict(DbConstant.TEMP_1ST_DATA_ENTRY.TABLE_TEMP_1ST, mValues,
                selection, selectionArgs, SQLiteDatabase.CONFLICT_REPLACE);
        if (id == 0) {
            long id2 = mDatabase.insertOrThrow(DbConstant.TEMP_1ST_DATA_ENTRY.TABLE_TEMP_1ST, null, mValues);
            Log.i(TAG, "updateFirstPage: InSERTED ID2: " + id2);
        }
        Log.i(TAG, "updateFirstPage: UPDATED ID: " + id);
    }

    public void deleteSingleROw(int id) {

        SQLiteDatabase mDatabase = this.getWritableDatabase();
        long deletedId = mDatabase.delete(DbConstant.IrIrn_Data_Entry.TABLE_TEMP_2ND_PAGE, "_id=?", new String[]{String.valueOf(id)});
        Log.i(TAG, "deleteSingleROw: Deleted: " + deletedId);
    }

    public int getCountMax(String uuid) {
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        Cursor mCursor = mDatabase.rawQuery("select count(*) as count  from temp_2nd_page where inspection_id=?", new String[]{String.valueOf(uuid)});
        if (mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            return mCursor.getInt(0);
        } else return 0;
    }

    public void deleteTemp2Page(String uuid) {
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        mDatabase.delete(DbConstant.IrIrn_Data_Entry.TABLE_TEMP_2ND_PAGE, "inspection_id=?", new String[]{String.valueOf(uuid)});
    }

    public void deleteAllTempRows(String uuid) {
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        for (String temp_table : temp_tables) {
            mDatabase.delete(temp_table, DbConstant.TEMP_ENTRY.INSPECTION_ID + "=?", new String[]{uuid});
        }
        mDatabase.delete(DbConstant.TEMP_ENTRY.TABLE_K_TEMP,DbConstant.TEMP_ENTRY.INSPECTION_ID + "=?", new String[]{uuid});
    }

    public ArrayList<SingleRowModel> retrieveTempData(String tableName, String uuid) {
        // DataModel dataModel = new DataModel();
        ArrayList<SingleRowModel> mDataList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = DbConstant.TEMP_ENTRY.INSPECTION_ID + "=?";
        Cursor cursor = db.query(tableName, null, selection, new String[]{uuid}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (tableName=="k_temp")
                {
                    mDataList.add(new SingleRowModel(
                              0,
                            cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_ENTRY.DESC_NUM)),
                            cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_ENTRY.DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_ENTRY.QTY)),
                            cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_ENTRY.UNIT)),
                            cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_ENTRY.QTY_FILE))
                    ));



                }
                else
                {
                    mDataList.add(new SingleRowModel(
                            1,
                            cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_ENTRY.DESC_NUM)),
                            cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_ENTRY.OBS)),
                            cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_ENTRY.OBS_FILE)),
                            cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_ENTRY.INSPECTION_ID))
                    ));
                }



            }
            cursor.close();
            return mDataList;
        }
        return mDataList;
    }



}