package com.srj.icsinspection.utils;

import android.content.Context;
import android.util.Log;

import com.srj.icsinspection.handler.ApiService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Common {

    //public static final String GET_MATERIALS_URL = "http://192.168.0.243:86/api/Getmaterial";
    //private static final String BASE_URL = "http://10.0.3.2/ics_inspection/api/";
    //private static final String BASE_URL = "http://192.168.0.247/ics_inspection/api/";
   //  private static final String BASE_URL = "http://192.168.0.243:88/Home/";  // local url
    //public static final String BASE_URL = "http://icspl.org:5009/Home/";
     public static final String BASE_URL = "http://icspl.org:5008/Home/"; //  live url

   // private static final String BASE_URL = "http://183.87.126.174:5008/Home/";
   // public static final String BASE_URL="http://103.69.91.214:5008/Home/";
    // private static final String BASE_URL = "http://183.87.133.130:82/ics_inspection/api/";
    //  private static final String BASE_URL = "http://icspl.org:82/corcon/api/";
    //private static final String MAP_BASE_URL = "https://maps.googleapis.com/";

    public static String CUSTOMER_NAME = null;
    public static String VENDOR_NAME = null;
    public static String STATION = null;
    public static String USER_NAME = null;
    public static String PO_NO = null;
    public static String ICS_REG_NUMBER = null;
    public static String DATE_OF_INSPECTION = null;
    public static String CONSULTANT_NAME = null;
    public static String MF_SUPPLIER_NAME = null;
    public static String ITEM_MATERIAL = null;
    public static String BATCH_SR_NO = null;
    public static int QUANTITY = 0;
    public static String SPEC_DRAWING = null;
    public static String CODE_STD = null;
    public static String INSPECTION_TYPE = null;
    public static String POCOPY = null;
    public static String STANDARDCOPY = null;
    public static String QAPCOPY = null;
    public static String TAG_TYPE = null;
    public static String PART_VISIT_SLIP = null;
    public static String PART_TC_SLIP = null;
    public static String UPLOAD_STAND = null;
    public static String PART_QAQC = null;
    public static String PART_OTHER = null;
    public static String PART_CALIB_PART_QAQC = null;


    public static String IRIRN_REPORT_ID = null;
    public static String FINAL_IRIRN_REPORT_ID = null;

    public static ApiService getAPI() {
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }


    // Show Alert Dialog
    public static void showDialog(android.app.AlertDialog mDialog) {
        if (!mDialog.isShowing())
            mDialog.show();
    }

    // cancel Alert Dialog
    public static void disableDialog(android.app.AlertDialog mDialog) {
        try {
            if (mDialog.isShowing())
                mDialog.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void writeToFile(String data, Context context) {
        try {

            String FOLDERNAME = "sub";
            String folder = context.getFilesDir().getAbsolutePath() + File.separator + FOLDERNAME;
            File subFolder = new File(folder);

            if (!subFolder.exists()) {
                subFolder.mkdirs();
            }


            FileOutputStream outputStream = new FileOutputStream(new File(subFolder, "inspection"));

            outputStream.write(data.getBytes());
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public static MultipartBody.Part convertMultipart(String coloumnName, String paramName) {
        RequestBody mRequestBody;
        MultipartBody.Part body5;
        if (coloumnName != null)
            if (!coloumnName.equalsIgnoreCase("NA")) {
                File mFile = new File(coloumnName);
                mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                body5 = MultipartBody.Part.createFormData(paramName, mFile.getName(), mRequestBody);
                Log.i("Multipart body", "convertMultipart: " + mFile.getName());
            } else {
                mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                body5 = MultipartBody.Part.createFormData(paramName, "NA", mRequestBody);
            }
        else {
            mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
            body5 = MultipartBody.Part.createFormData(paramName, "NA", mRequestBody);
        }
        return body5;
    }
}
