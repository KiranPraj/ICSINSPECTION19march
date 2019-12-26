package com.srj.icsinspection.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crashlytics.android.Crashlytics;
import com.srj.icsinspection.MainActivity;
import com.srj.icsinspection.R;
import com.srj.icsinspection.adapter.DoneSyncAdapter;
import com.srj.icsinspection.adapter.SyncAdapter;
import com.srj.icsinspection.constants.DbConstant;
import com.srj.icsinspection.dbhelper.DbHelper;
import com.srj.icsinspection.handler.ApiService;
import com.srj.icsinspection.handler.SyncHandler;
import com.srj.icsinspection.model.DoneSyncModel;
import com.srj.icsinspection.model.GetFinalDescModel;
import com.srj.icsinspection.model.InsertionModel;
import com.srj.icsinspection.model.ReportDetailsModel;
import com.srj.icsinspection.model.SyncModel;
import com.srj.icsinspection.utils.Common;
import com.srj.icsinspection.utils.DelayedProgressDialog;
import com.srj.icsinspection.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.fabric.sdk.android.Fabric;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncFragment extends Fragment implements SyncAdapter.AdapterCallback {
    private static final String TAG = "SyncFragment";


    private AlertDialog mDialog;
    private DbHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Context mContext;
    private ApiService mService;
    private TextView nodata;
    private LinearLayout syndata;
    private ImageView sync;
    private RecyclerView recyclerView;
    private SyncAdapter mAdapter;
    SyncHandler mSyncHandler;
    // private List<SyncModel> synclist = new ArrayList<>();
    private List<SyncModel> synclist = new ArrayList<>();
    private SharedPreferences mPreference;
    final int[] a = {0};
    final int[] b = {0};
    String cursorCount1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Fabric fabric = new Fabric.Builder(getActivity())
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        View mView = inflater.inflate(R.layout.fragment_sync, container, false);
        inti(mView);
        return mView;

    }

    private void inti(final View mView) {
        mContext = mView.getContext();
        mService = Common.getAPI();
        mHelper = new DbHelper(mContext);
        mDatabase = mHelper.getWritableDatabase();
        // btn_start_sync = mView.findViewById(R.id.btn_start_sync);
        nodata = mView.findViewById(R.id.nodata);
        mPreference = getActivity().getSharedPreferences(getString(R.string.user), Context.MODE_PRIVATE);
        sync = mView.findViewById(R.id.sync);
        syndata = mView.findViewById(R.id.syncdata);

        mDialog = new SpotsDialog(mView.getContext(), "Syncing, please wait...");
        //datahandler();


        recyclerView = (RecyclerView) mView.findViewById(R.id.rv_syncdata);
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new SyncAdapter(synclist, this);

        getLocalData();


    }

    private void getLocalData() {

        // getIRIRNDATA();
        nodata.setVisibility(View.GONE);
        final Cursor mCursor = new DbHelper(getActivity()).getPendingIRIRN();

        if (mCursor.getCount() > 0) {

            while (mCursor.moveToNext()) {
                String date = mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.DATE_OF_INSP));
                String ponumber = mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.PO_NUM));
                String date_of_fill = mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.DATE_OF_FILLED_DATA));

                SyncModel syncModel = new SyncModel(date, ponumber, date_of_fill);
                synclist.add(syncModel);
            }
//                mAdapter.notifyDataSetChanged();
            //recyclerView.setAdapter(mAdapter);
        }
        if (synclist.size() > 0) {
            recyclerView.setAdapter(mAdapter);
            mDialog.cancel();
            Common.disableDialog(mDialog);
        } else {
            nodata.setVisibility(View.VISIBLE);
            nodata.setText("No Data To Sync");
            mDialog.cancel();
            Common.disableDialog(mDialog);
        }
      //  mProgressDialog.cancel();

        //done irirn
    }

    private void getIRIRNDATA() {

    }

    private void datahandler() {
//        MainActivity mn=new MainActivity();
//        try {
//            mn.sendQTYDATA();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }


    // insert Product_name db
    private void insert_sql_file(int[] sql_name) {
        mDatabase = mHelper.getWritableDatabase();
        try {
            for (int file : sql_name) {
                int insertCount = insertFromFile(mContext, file);
                //   Toast.makeText(mActivity, "Rows loaded from file= " + insertCount, Toast.LENGTH_SHORT).show();
                mDialog.cancel();
            }
        } catch (IOException e) {
            Toasty.error(mContext, e.toString(), Toast.LENGTH_SHORT, true).show();

            e.printStackTrace();
        }
    }

    public int insertFromFile(Context context, int resourceId) throws IOException {
        mDatabase = mHelper.getWritableDatabase();
        // Reseting Counter
        int result = 0;

        // Open the resource
        InputStream insertsStream = context.getResources().openRawResource(resourceId);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

        // Iterate through lines (assuming each insert has its own line and theres no other stuff)
        while (insertReader.ready()) {
            String insertStmt = insertReader.readLine();
            try {
                mDatabase.execSQL(insertStmt);
            } catch (Exception e) {
                e.getMessage();
            }

            result++;
        }
        insertReader.close();

        // returning number of inserted rows
        return result;
    }

    // Handle Sync Button Click
    private void SyncHandler(String date, String ponumber) {
        int size = synclist.size();

        if (Utils.INSTANCE.isNetworkAvailable(requireActivity())) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.sync_before_title)
                    .setMessage(R.string.sync_before_message)
                    .setPositiveButton("Sync Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            try {
                                sendQTYDATA(date, ponumber);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            //   dialogInterface.dismiss();
                        }
                    }).setNegativeButton("Cancel Sync", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
        else {
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            Toasty.error(getActivity(), "No Internet to sync your data", Toast.LENGTH_LONG).show();
        }
    }

    // sending final qty data by pressing sync button
    @SuppressLint("CheckResult")
    public void sendQTYDATA(String date, String ponumber) throws ParseException {
        Cursor cursor = new DbHelper(getActivity()).getAllFinalData(date, ponumber);
        final int[] Queue = {1};
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                Common.showDialog(mDialog);
                mDialog.setTitle("Sending Final data...");
            //   mDialog.setCancelable(false);
//                SimpleDateFormat dateFormatprev = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
//                Date d;
                final ArrayList<String> checkList = new ArrayList<>();
//                d = dateFormatprev.parse(cursor.getString(cursor.getColumnIndex("inspection_date")));
//                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
//                String inspDate = dateFormat.format(d);
                Common.FINAL_IRIRN_REPORT_ID = cursor.getString(cursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.INSPECTION_ID));

                Log.i(TAG, "sendQTYDATA: " + cursor.getString(cursor.getColumnIndex("inspection_id")));
                checkList.add(Common.FINAL_IRIRN_REPORT_ID);
                mService.irn(
                        cursor.getString(cursor.getColumnIndex("balance_qty")),
                        cursor.getString(cursor.getColumnIndex("ponumber")),
                        cursor.getString(cursor.getColumnIndex("ics_reg_number")),
                        cursor.getString(cursor.getColumnIndex("qty_description")),
                        cursor.getString(cursor.getColumnIndex("report_no")),
                        cursor.getString(cursor.getColumnIndex("ponumber")),
                        cursor.getString(cursor.getColumnIndex("vendorname")),
                        cursor.getString(cursor.getColumnIndex("inspection_date")),
                        cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getString(cursor.getColumnIndex("emp_code")),
                        cursor.getString(cursor.getColumnIndex("emp_station")),
                        String.valueOf(cursor.getInt(cursor.getColumnIndex("po_qty"))),
                        String.valueOf(cursor.getInt(cursor.getColumnIndex("rel_qty"))),
                        String.valueOf(cursor.getInt(cursor.getColumnIndex("rej_qty"))),
                        "fdfd", "fgfg", "dfdfd",
                        String.valueOf(cursor.getInt(cursor.getColumnIndex("insp_qty")))
                ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new Observer<List<InsertionModel>>() {
                            @Override
                            public void onSubscribe(Disposable disposable) {

                            }

                            @Override
                            public void onNext(List<InsertionModel> insertionModels) {
                                if (insertionModels != null) {
                                    Log.i(TAG, "onResponse:Database Count " + insertionModels.get(0).getSts());

                                    if (insertionModels.get(0).getSts() > 0) {
                                        if (checkList.size() > 0) {
                                            new DbHelper(getActivity()).doneFinalInspectionID(checkList.get(0));
                                            Log.i(TAG, "onResponse: Qty Sent Successfully");
                                            Log.i(TAG, "onResponse:Checklist: final result " + checkList.get(0));
                                            Queue[0] = Queue[0] + 1;
                                            Log.e("count",""+cursor.getCount()+" :"+cursor.getPosition()+"\n "+Queue[0]);

                                            if (cursor.getPosition() == cursor.getCount()) {
                                                synclist.clear();
                                                mDatabase.delete(DbConstant.Final_DATA_ENTRY.TABLE_FINAL_DATA, null, null);
                                                onlinefinaldata();
                                              //  Common.disableDialog(mDialog);
                                               // Toasty.success(getActivity(),  " Data send Successfully", Toast.LENGTH_LONG).show();

                                                //mHelper.updateFianlAfterSync(date,ponumber);
                                            }
                                            Toasty.success(getActivity(),  " Data send Successfully", Toast.LENGTH_LONG).show();

                                        }
                                    } else {
                                        Toasty.error(getActivity(), "Incorrect Entries has been filled ", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toasty.error(getActivity(), "Network Error Please Try Again ", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onError(Throwable throwable) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

            }
            cursor.close();
            sendIRData(date, ponumber);
        } else {
            sendIRData(date, ponumber);
        }
    }



    // added by kiran
    private void onlinefinaldata() {

        mService.getFinalDescription(mPreference.getString("user_name","-")).enqueue(new Callback<List<GetFinalDescModel>>() {
            @Override
            public void onResponse(Call<List<GetFinalDescModel>> call, Response<List<GetFinalDescModel>> response) {
                if (response.isSuccessful() && response.body()!=null)
                {

                    // List<GetFinalDescModel>mLIst=response.body();
                    List<GetFinalDescModel> mLIst = response.body();
                    if (mLIst.isEmpty())
                    {

                        Toasty.error(mContext,"lISt is empty",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        mHelper.insertFinalDescription((ArrayList<GetFinalDescModel>) mLIst);

                    }
                }
                else
                {
                    Toast.makeText(mContext,"no response",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<GetFinalDescModel>> call, Throwable t) {
                t.printStackTrace();
                Toasty.error(mContext,"Server Not Responding your data, try again after some time",Toast.LENGTH_LONG).show();

            }
        });


    }

// added code here


    @SuppressLint("CheckResult")
    void sendIRData(String date, String ponumber) {
        final int[] Queue = {1};
        final Cursor mCursor = new DbHelper(getActivity()).check_IR_DATA(date, ponumber);
        cursorCount1= String.valueOf(mCursor.getCount());
        if (mCursor.getCount() > 0) {



            while (mCursor.moveToNext()) {

                final ArrayList<String> checkList = new ArrayList<>();

                //Log.i(TAG,"sendIRData: TC DATA:"+ new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.ENGLISH).format("2014-12-09 02:18:38"));
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                String input = mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.DATE_OF_INSP));
                Date dt;

                String strDate = "1996-02-21";
                try {
                    dt = sdf.parse(input);

                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    strDate = sdf2.format(dt);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Common.IRIRN_REPORT_ID = mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry._id));
                Log.i(TAG, "sendIRData: Before Sending INSPECTIO ID: " + Common.IRIRN_REPORT_ID);

                checkList.add(Common.IRIRN_REPORT_ID);

                final String finalStrDate = strDate;
                Common.showDialog(mDialog);
                mDialog.setMessage("sending your data...");
                mDialog.setCancelable(false);
//                mSyncHandler.syncCallback();
                final DelayedProgressDialog mProgressDialog = new DelayedProgressDialog();
                mProgressDialog.setCancelable(false);
//                mProgressDialog.show(getSupportFragmentManager(), "ir");

                Log.i(TAG, "sendIRData: Sending DESC nunber: " +
                        "" + mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.DESC_NUM)) +
                        " MAX: " + new DbHelper(getActivity()).getMaxDescNum(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.INSPECTION_ID)), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.CUST_ID))));
            //  String other=  mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Other));
            //  String deviation=mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Deviation));
             //   String identification=mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Identification));
                  mService.sendirirndata(RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.PROJECT_VEND)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.PROJECT_VEND))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.PO_NUM)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.PO_NUM))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.CONSULTANT_NAME)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.CONSULTANT_NAME))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.CUSTOMER_NAME)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.CUSTOMER_NAME))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.ICS_REG_NUMBER)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.ICS_REG_NUMBER))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.EMP_CODE)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.EMP_CODE))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.EMP_STATION)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.EMP_STATION))),
                         RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.VISUALA)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.VISUALA))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.VISUALB)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.VISUALB))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.FeInspection)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.FeInspection))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.FnInspection)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.FnInspection))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Dimensional)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Dimensional))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Calibration)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Calibration))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.ReportsR)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.ReportsR))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.TestWitness)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.TestWitness))),
                        RequestBody.create(MediaType.parse("text/plain"), "TD"),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.INSP_TYPE)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.INSP_TYPE))),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.VISUALA_FILE)), "V_a_file"),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.VISUALB_FILE)), "V_b_file"),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Dimensional_file)), "Dim_file"),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Calibration_file)), "Calib_file"),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.ReportsR_file)), "Report_r_file"),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.TestWitness_File)), "Test_w_file"),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.DATE_OF_INSP))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.ITEM_METARIAL)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.ITEM_METARIAL))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.QUANTITY)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.QUANTITY))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.SPEC_DRAWINGS)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.SPEC_DRAWINGS))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.CODES_STANDARD)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.CODES_STANDARD))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Other)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Other))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Deviation)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Deviation))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Identification)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Identification))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.EMP_NAME)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.EMP_NAME))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.STATION)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.STATION))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.BATCH_NO)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.BATCH_NO))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.REPORT_NO))),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.PART_VISIT_SLIP)), "V_slip"),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.PART_TC_SLIP)), "Tc_slip"),
                        RequestBody.create(MediaType.parse("text/plain"), "-"),//mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.UPLOAD_STAND))),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.PART_QAQC)), "Qaqc"),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.UPLOAD_QAP)) == null ? "-" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.UPLOAD_QAP))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.UPLOAD_PO)) == null ? "-" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.UPLOAD_PO))),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.PART_CALIB_PART_QAQC)), "Upload_calib"),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.PART_OTHER)), "Other_doc"),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.DESC_NUM))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.TAG_TYPE))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.DATE_OF_FILLED_DATA))),
                        RequestBody.create(MediaType.parse("text/plain"), new DbHelper(getActivity()).getMaxDescNum(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.INSPECTION_ID)),mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.CUST_ID)))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.INSP_VISIT))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.ANNEX))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.LOCATION))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.RANGE))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.DESCRIPTION))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.PROJECT_TYPE))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.NO_OF_JOBS))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.DONE_HOURS))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.EXTRA_HOURS))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.CFRID))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.SITEINCHARGE))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.INSPECTION_RESULTS)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.INSPECTION_RESULTS))),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.INSPECTION_RESULTS_File)), "InspectionResult_File"),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.PHOTOGRAPH)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.PHOTOGRAPH))),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.PHOTOGRAPH_File)), "Photograph_File"),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.QUANTITY_DESC)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.QUANTITY_DESC))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.QUANTITY_QTY)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.QUANTITY_QTY))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.QUANTITY_UNIT)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.QUANTITY_UNIT))),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.QUANTITY_FILE)), "Quantity_File"),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.QUANTITY_DESC)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.QUANTITY_DESC))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.QUANTITY_QTY)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.QUANTITY_QTY))),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.QUANTITY_UNIT)) == null ? "" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.QUANTITY_UNIT))),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.PART_OTHER)), "DescriptionFile"),
                        RequestBody.create(MediaType.parse("text/plain"), mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.SUB_VENDOR_PO_NUM)) == null ? "-" : mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.SUB_VENDOR_PO_NUM))),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Other_file)), "Other_File"),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Deviation_file)), "Deviation_File"),
                        Common.convertMultipart(mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.Identifation_file)), "Identification_File"),
                        RequestBody.create(MediaType.parse("text/plain"), cursorCount1)

                )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new Observer<List<InsertionModel>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(List<InsertionModel> insertionModels) {

                                if (insertionModels != null) {
                                    Log.i(TAG, "onResponse:Database Count " + insertionModels.get(0).getSts());

                                    if (insertionModels.get(0).getSts() > 0) {
                                        if (checkList.size() > 0) {
                                            mDialog.show();
                                            Common.showDialog(mDialog);
                                            new DbHelper(getActivity()).doneInspectionID(checkList.get(0));
                                            Log.i(TAG, "onResponse:Checklist: " + checkList.get(0));
                                            Log.i(TAG, "onResponse:Updated Report ID " + Common.IRIRN_REPORT_ID);
                                            //new DbHelper(getActivity()).doneInspectionID(checkList.get(0));
                                            //mSyncHandler.syncIRAgainCallback();

                                           // Toasty.success(getActivity(), "" + Queue[0] + " Data send Successfully", Toast.LENGTH_LONG).show();
                                            Queue[0] = Queue[0] + 1;
                                            Log.e("count",""+mCursor.getCount()+" :"+mCursor.getPosition()+"\n "+Queue[0]);
                                            if (mCursor.getPosition() == mCursor.getCount()) {
                                                synclist.clear();
                                                getLocalData();

                                                Toasty.success(getActivity(), "Data Sync Successfully " , Toast.LENGTH_LONG).show();


                                            }
                                      /* int p=mCursor.getPosition();
                                       int c=mCursor.getCount();
                                            if (mCursor.getPosition() == mCursor.getCount()) {
                                           mCursor.moveToLast();
                                              //  Common.disableDialog(mDialog);
                                               // new DbHelper(getActivity()).doneInspectionID(date,ponumber);
                                                synclist.clear();
                                                getLocalData();
                                               Toasty.info(getActivity(),"Status "+insertionModels.get(0).getSts(),Toast.LENGTH_SHORT).show();

                                               // Toasty.success(getActivity(), "Data Synced Successfully  of date :"+mCursor.getString(mCursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.DATE_OF_INSP)), Toast.LENGTH_LONG).show();
                                                Common.disableDialog(mDialog);



                                            }*/

                                        }


                                    } else {
                                        mDialog.cancel();
                                        mProgressDialog.cancel();
                                        Toasty.error(getActivity(), "Status "+insertionModels.get(0).getSts(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                                    else {
                                        mDialog.cancel();
                                        mProgressDialog.cancel();
                                        Toasty.error(getActivity(),"network error",Toast.LENGTH_SHORT).show();
                                        //  Toasty.error(getActivity(), "Please Try Again", Toast.LENGTH_LONG).show();
//                                        Toast.makeText(mContext, "Please Try Again", Toast.LENGTH_SHORT).show();
                                    }



                            }

                            @Override
                            public void onError(Throwable e) {
                                //   mSyncHandler.cancelsyncCallback();
                                mProgressDialog.cancel();
                                mDialog.cancel();
                                Log.i(TAG, "onResponse:Updated Report ID " + e.getMessage());
                                Toasty.error(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {

                                Log.i(TAG, "onComplete: Called");

                                // mDialog.cancel();
                                //  mSyncHandler.completedSyncCallback();
                            }
                        });

            }
            //mCursor.close();
            mDialog.setTitle("Getting new planning details");
            mDialog.setCancelable(false);
        } else {
            msg("No Data Sync OR Data has been Synced Successfully");
        }
        //new DbHelper(MainActivity.this).deleteReportAndFinal();
    }

    void msg(String s) {
        Toasty.success(getActivity(), s, Toast.LENGTH_LONG).show();
    }


    @Override
    public int callbackreturn(String date, String ponumber) {
        String Date = date;
        String Ponumber = ponumber;
        SyncHandler(Date, Ponumber);
        return 0;
    }
}
