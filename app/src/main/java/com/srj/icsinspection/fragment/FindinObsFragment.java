package com.srj.icsinspection.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.srj.icsinspection.BuildConfig;
import com.srj.icsinspection.MainActivity;
import com.srj.icsinspection.R;
import com.srj.icsinspection.adapter.FindingObjAdapter;
import com.srj.icsinspection.constants.DbConstant;
import com.srj.icsinspection.dbhelper.DbHelper;
import com.srj.icsinspection.handler.ApiService;
import com.srj.icsinspection.handler.TempDataSaveListener;
import com.srj.icsinspection.model.DescriptionDataModel;
import com.srj.icsinspection.model.InspectionMoodel;
import com.srj.icsinspection.model.PhotoPathModel;
import com.srj.icsinspection.model.SingleRowModel;
import com.srj.icsinspection.utils.Common;
import com.srj.icsinspection.utils.FileManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.util.Log.i;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindinObsFragment extends Fragment implements FindingObjAdapter.AddClickListener,
        View.OnClickListener, TempDataSaveListener {
    @Override
    public void isTempClickListener(Boolean isPressed) {

    }


    private static final String TAG = "FindinObsFragment";
    private InspectionMoodel model;
    private LinearLayoutManager mLinearLayoutManager;
    private FindingObjAdapter mAdapter, mAdapter_a, mAdapter_b, mAdapter_c, mAdapter_d,
            mAdapter_e, mAdapter_f, mAdapter_g, mAdapter_i, mAdapter_h, mAdapter_j, mAdapter_h_new, mAdapter_j_new,mAdapter_k;
    private List<String> mList;
    private Context mContext;
    private Uri imageToUploadUri;
    private final int IMAGE_CAPTURE_REQUEST = 2515;
    private static final int GALLERY = 1;
    private static final int DOCUMENT =7 ;
    private FindingObjAdapter.MyViewHolder viewHolder, viewHolder_a, viewHolder_b, viewHolder_c,
            viewHolder_d, viewHolder_e, viewHolder_f, viewHolder_g, viewHolder_h, viewHolder_i, viewHolder_j, viewHolder_h_new, viewHolder_j_new,viewHolder_k;
    private Button cameraButton;
    private DbHelper mHelper;
    private SQLiteDatabase mDatabase;
    private ApiService mService;
    private AlertDialog mDialog;
    private int cursour_position_count;
    File imageBtnFile;
    private NotificationManagerCompat managerCompat;
    private static final String IMAGE_DIRECTORY ="/inspectionfolder" ;


    private TextView tv_A, tv_a, tv_b, tv_c, tv_d, tv_e, tv_f, tv_g, tv_h, tv_i, tv_j, tv_h_new, tv_j_new,tv_k;
    private EditText et_A, et_a, et_b, et_c, et_d, et_e, et_f, et_g, et_h, et_i, et_j, et_h_new, et_j_new,et_k, et_inspvisit,
            et_annexture,et_extra_hours;
    private Spinner sp_done_hours;
    private RecyclerView rv_list, rv_list_a, rv_list_b, rv_list_c,
            rv_list_d, rv_list_e, rv_list_f, rv_list_g, rv_list_h, rv_list_i, rv_list_j, rv_list_h_new, rv_list_j_new,rv_list_k;
    private Button btn_upload_po;
    private Button btn_upload_standard;
    private Button btn_upload_qap;
    private Button btn_upload_visit_slip;
    private Button btn_upload_tc;
    private Button btn_upload_calib_cert;
    private Button btn_upload_qaqc;
    private Button btn_upload_other_docs;
    private Button btn_temp_save;
    private Button btn_preview;

    private ImageButton ibtn_upload_visit_slip, ibtn_upload_tc, ibtn_upload_calib_cert, ibtn_upload_qaqc,
            ibtn_upload_other_docs, ibtn_upload_po, ibtn_upload_standard, ibtn_upload_qap;

    MultipartBody.Part part_visit_slip, part_tc_slip, part_calib_part_qaqc, part_other,
            part_po, part_stnd, part_qap, part_qaqc;

    private CompositeDisposable mDisposable = new CompositeDisposable();
    HashMap<Integer, ArrayList<PhotoPathModel>> map;
    private SharedPreferences preferences;
    private ArrayList<Object> items;

    private ArrayList<Integer> mList_A = new ArrayList<>();
    private ArrayList<Integer> mList_a = new ArrayList<>();
    private ArrayList<Integer> mList_b = new ArrayList<>();
    private ArrayList<Integer> mList_c = new ArrayList<>();
    private ArrayList<Integer> mList_d = new ArrayList<>();
    private ArrayList<Integer> mList_e = new ArrayList<>();
    private ArrayList<Integer> mList_f = new ArrayList<>();
    private ArrayList<Integer> mList_g = new ArrayList<>();
    private ArrayList<Integer> mList_h = new ArrayList<>();
    private ArrayList<Integer> mList_i = new ArrayList<>();
    private ArrayList<Integer> mList_j = new ArrayList<>();
    private ArrayList<Integer> mList_h_new = new ArrayList<>();
    private ArrayList<Integer> mList_j_new = new ArrayList<>();
    private ArrayList<Integer> mList_k = new ArrayList<>();

    private ArrayList<SingleRowModel> SingleRowModel_mList_A = new ArrayList<>();
    private ArrayList<SingleRowModel> SingleRowModel_mList_a = new ArrayList<>();
    private ArrayList<SingleRowModel> SingleRowModel_mList_b = new ArrayList<>();
    private ArrayList<SingleRowModel> SingleRowModel_mList_c = new ArrayList<>();
    private ArrayList<SingleRowModel> SingleRowModel_mList_d = new ArrayList<>();
    private ArrayList<SingleRowModel> SingleRowModel_mList_e = new ArrayList<>();
    private ArrayList<SingleRowModel> SingleRowModel_mList_f = new ArrayList<>();
    private ArrayList<SingleRowModel> SingleRowModel_mList_g = new ArrayList<>();
    private ArrayList<SingleRowModel> SingleRowModel_mList_h = new ArrayList<>();
    private ArrayList<SingleRowModel> SingleRowModel_mList_i = new ArrayList<>();
    private ArrayList<SingleRowModel> SingleRowModel_mList_j = new ArrayList<>();
    private ArrayList<SingleRowModel> SingleRowModel_mList_h_new = new ArrayList<>();
    private ArrayList<SingleRowModel> SingleRowModel_mList_j_new = new ArrayList<>();
    private ArrayList<SingleRowModel> SingleRowModel_mList_k = new ArrayList<>();

    private ArrayList<String> inspectionList;
    private static final String INSPECTION_ID = UUID.randomUUID().toString();
    private ArrayList<DescriptionDataModel.Data> mDataArrayList;

    private final int PICK_VISIT_SLIP_REQUEST_CODE = 5253;
    private final int PICK_TC_REQUEST_CODE = 5254;
    private final int PICK_CALIB_CERT_REQUEST_CODE = 5255;
    private final int PICK_QAQC_REQUEST_CODE = 5256;
    private final int PICK_OTHER_REQUEST_CODE = 5257;
    private final int PICK_PO_REQUEST_CODE = 5258;
    private final int PICK_STANDARD_REQUEST_CODE = 5259;
    private final int PICK_QAP_REQUEST_CODE = 5250;

    private final int IMAGE_PICK_VISIT_SLIP_REQUEST_CODE = 6253;
    private final int IMAGE_PICK_TC_REQUEST_CODE = 6254;
    private final int IMAGE_PICK_CALIB_CERT_REQUEST_CODE = 6255;
    private final int IMAGE_PICK_QAQC_REQUEST_CODE = 6256;
    private final int IMAGE_PICK_OTHER_REQUEST_CODE = 6257;
    private final int IMAGE_PICK_PO_REQUEST_CODE = 6258;
    private final int IMAGE_PICK_STANDARD_REQUEST_CODE = 6259;
    private final int IMAGE_PICK_QAP_REQUEST_CODE = 6250;

    List<SingleRowModel> datamodel;

    File file = null;
    private TempDataSaveListener mListener;

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_findin_obs, container, false);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.containsKey(getString(R.string.desc_data))) {
                mDataArrayList = (ArrayList<DescriptionDataModel.Data>) bundle.getSerializable(getString(R.string.desc_data));

                if (mDataArrayList != null && mDataArrayList.size() > 0) {
                    for (int i = 0; i < mDataArrayList.size(); i++) {
                        i(TAG, "onCreateView: List Data; \n" + mDataArrayList.get(i).getRelsQty());
                    }
                }
                this.getArguments().remove(getString(R.string.desc_data));

            }
            model = bundle.getParcelable(getString(R.string.desc_model));
            if (model != null) this.getArguments().remove(getString(R.string.desc_model));
            //Log.i(TAG, "onCreateView: reg num: " + model.getIcs_reg_num());
        }

        init(mView);

        return mView;
    }

    private void init(View mView) {
        mContext = mView.getContext();
        mHelper = new DbHelper(mContext);
        mService = Common.getAPI();
        mDialog = new SpotsDialog(mView.getContext(), "Making IR/IRN, please wait...");
        managerCompat = NotificationManagerCompat.from(mContext);
        preferences = mContext.getSharedPreferences(getString(R.string.user), MODE_PRIVATE);
        // TV
        tv_A = mView.findViewById(R.id.tv_A);
        tv_a = mView.findViewById(R.id.tv_a);
        tv_a = mView.findViewById(R.id.tv_a);
        tv_b = mView.findViewById(R.id.tv_b);
        tv_c = mView.findViewById(R.id.tv_c);
        tv_d = mView.findViewById(R.id.tv_d);
        tv_e = mView.findViewById(R.id.tv_e);
        tv_f = mView.findViewById(R.id.tv_f);
        tv_g = mView.findViewById(R.id.tv_g);
        tv_h = mView.findViewById(R.id.tv_h);
        tv_i = mView.findViewById(R.id.tv_i);
        tv_j = mView.findViewById(R.id.tv_j);
        tv_h_new = mView.findViewById(R.id.tv_h_new);
        tv_j_new = mView.findViewById(R.id.tv_j_new);
        tv_k = mView.findViewById(R.id.tv_k);

        /// RV
        rv_list = mView.findViewById(R.id.rv_list);
        rv_list_a = mView.findViewById(R.id.rv_list_a);
        rv_list_b = mView.findViewById(R.id.rv_list_b);
        rv_list_c = mView.findViewById(R.id.rv_list_c);
        rv_list_d = mView.findViewById(R.id.rv_list_d);
        rv_list_e = mView.findViewById(R.id.rv_list_e);
        rv_list_f = mView.findViewById(R.id.rv_list_f);
        rv_list_g = mView.findViewById(R.id.rv_list_g);
        rv_list_h = mView.findViewById(R.id.rv_list_h);
        rv_list_i = mView.findViewById(R.id.rv_list_i);
        rv_list_j = mView.findViewById(R.id.rv_list_j);
        rv_list_h_new = mView.findViewById(R.id.rv_list_h_new);
        rv_list_j_new = mView.findViewById(R.id.rv_list_j_new);
        rv_list_k = mView.findViewById(R.id.rv_list_k);

        //ET
        et_A = mView.findViewById(R.id.et_A);
        et_a = mView.findViewById(R.id.et_a);
        et_b = mView.findViewById(R.id.et_b);
        et_c = mView.findViewById(R.id.et_c);
        et_d = mView.findViewById(R.id.et_d);
        et_e = mView.findViewById(R.id.et_e);
        et_f = mView.findViewById(R.id.et_f);
        et_g = mView.findViewById(R.id.et_g);
        et_h = mView.findViewById(R.id.et_h);
        et_i = mView.findViewById(R.id.et_i);
        et_j = mView.findViewById(R.id.et_j);
        et_h_new = mView.findViewById(R.id.et_h_new);
        et_j_new = mView.findViewById(R.id.et_j_new);
        et_k = mView.findViewById(R.id.et_k);
        et_annexture = mView.findViewById(R.id.et_annexture);
        et_inspvisit = mView.findViewById(R.id.et_inspvisit);
        et_extra_hours=mView.findViewById(R.id.et_extra_hrs);

        //Spinner
        sp_done_hours=mView.findViewById(R.id.sp_done_hours);

        //btn
        btn_upload_po = mView.findViewById(R.id.btn_upload_po);
        btn_upload_standard = mView.findViewById(R.id.btn_upload_standard);
        btn_upload_qap = mView.findViewById(R.id.btn_upload_qap);
        btn_upload_visit_slip = mView.findViewById(R.id.btn_upload_visit_slip);
        btn_upload_tc = mView.findViewById(R.id.btn_upload_tc);
        btn_upload_calib_cert = mView.findViewById(R.id.btn_upload_calib_cert);
        btn_upload_qaqc = mView.findViewById(R.id.btn_upload_qaqc);
        btn_upload_other_docs = mView.findViewById(R.id.btn_upload_other_docs);
        btn_preview = mView.findViewById(R.id.btn_preview);
        btn_temp_save = mView.findViewById(R.id.btn_temp_save);
        btn_preview.setVisibility(View.INVISIBLE);


        ibtn_upload_visit_slip = mView.findViewById(R.id.ibtn_upload_visit_slip);
        ibtn_upload_tc = mView.findViewById(R.id.ibtn_upload_tc);
        ibtn_upload_calib_cert = mView.findViewById(R.id.ibtn_upload_calib_cert);
        ibtn_upload_qaqc = mView.findViewById(R.id.ibtn_upload_qaqc);
        ibtn_upload_other_docs = mView.findViewById(R.id.ibtn_upload_other_docs);
        ibtn_upload_po = mView.findViewById(R.id.ibtn_upload_po);
        ibtn_upload_standard = mView.findViewById(R.id.ibtn_upload_standard);
        ibtn_upload_qap = mView.findViewById(R.id.ibtn_upload_qap);

        btn_upload_po.setEnabled(false);
        btn_upload_standard.setEnabled(false);
        btn_upload_qap.setEnabled(false);
        btn_upload_visit_slip.setEnabled(true);
        btn_upload_tc.setEnabled(true);
        btn_upload_calib_cert.setEnabled(true);
        btn_upload_qaqc.setEnabled(true);
        btn_upload_other_docs.setEnabled(true);

        // btn_upload_po.setOnClickListener(this);
        // btn_upload_standard.setOnClickListener(this);
        // btn_upload_qap.setOnClickListener(this);
        btn_upload_visit_slip.setOnClickListener(this);
        btn_upload_tc.setOnClickListener(this);
        btn_upload_calib_cert.setOnClickListener(this);
        btn_upload_qaqc.setOnClickListener(this);
        btn_upload_other_docs.setOnClickListener(this);


        ibtn_upload_visit_slip.setOnClickListener(this);
        ibtn_upload_tc.setOnClickListener(this);
        ibtn_upload_calib_cert.setOnClickListener(this);
        ibtn_upload_qaqc.setOnClickListener(this);
        ibtn_upload_other_docs.setOnClickListener(this);


        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(mLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager_a = new LinearLayoutManager(mContext.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list_a.setLayoutManager(mLayoutManager_a);
        rv_list_a.setItemAnimator(new DefaultItemAnimator());


        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager_b = new LinearLayoutManager(mContext.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list_b.setLayoutManager(mLayoutManager_b);
        rv_list_b.setItemAnimator(new DefaultItemAnimator());

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager_c = new LinearLayoutManager(mContext.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list_c.setLayoutManager(mLayoutManager_c);
        rv_list_c.setItemAnimator(new DefaultItemAnimator());

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager_d = new LinearLayoutManager(mContext.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list_d.setLayoutManager(mLayoutManager_d);
        rv_list_d.setItemAnimator(new DefaultItemAnimator());

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager_e = new LinearLayoutManager(mContext.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list_e.setLayoutManager(mLayoutManager_e);
        rv_list_e.setItemAnimator(new DefaultItemAnimator());

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager_f = new LinearLayoutManager(mContext.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list_f.setLayoutManager(mLayoutManager_f);
        rv_list_f.setItemAnimator(new DefaultItemAnimator());

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager_g = new LinearLayoutManager(mContext.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list_g.setLayoutManager(mLayoutManager_g);
        rv_list_g.setItemAnimator(new DefaultItemAnimator());

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager_h = new LinearLayoutManager(mContext.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list_h.setLayoutManager(mLayoutManager_h);
        rv_list_h.setItemAnimator(new DefaultItemAnimator());

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager_i = new LinearLayoutManager(mContext.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list_i.setLayoutManager(mLayoutManager_i);
        rv_list_i.setItemAnimator(new DefaultItemAnimator());

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager_j = new LinearLayoutManager(mContext.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list_j.setLayoutManager(mLayoutManager_j);
        rv_list_j.setItemAnimator(new DefaultItemAnimator());

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager_h_new = new LinearLayoutManager(mContext.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list_h_new.setLayoutManager(mLayoutManager_h_new);
        rv_list_h_new.setItemAnimator(new DefaultItemAnimator());

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager_j_new = new LinearLayoutManager(mContext.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list_j_new.setLayoutManager(mLayoutManager_j_new);
        rv_list_j_new.setItemAnimator(new DefaultItemAnimator());

         @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager_k = new LinearLayoutManager(mContext.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list_k.setLayoutManager(mLayoutManager_k);
        rv_list_k.setItemAnimator(new DefaultItemAnimator());



        mAdapter = new FindingObjAdapter(0,mList_A, SingleRowModel_mList_A, mContext, this);
        mAdapter_a = new FindingObjAdapter(0,mList_a, SingleRowModel_mList_a, mContext, this);
        mAdapter_b = new FindingObjAdapter(0,mList_b, SingleRowModel_mList_b, mContext, this);
        mAdapter_c = new FindingObjAdapter(0,mList_c, SingleRowModel_mList_c, mContext, this);
        mAdapter_d = new FindingObjAdapter(0,mList_d, SingleRowModel_mList_d, mContext, this);
        mAdapter_e = new FindingObjAdapter(0,mList_e, SingleRowModel_mList_e, mContext, this);
        mAdapter_f = new FindingObjAdapter(0,mList_f, SingleRowModel_mList_f, mContext, this);
        mAdapter_g = new FindingObjAdapter(0,mList_g, SingleRowModel_mList_g, mContext, this);
        mAdapter_h = new FindingObjAdapter(0,mList_h, SingleRowModel_mList_h, mContext, this);
        mAdapter_i = new FindingObjAdapter(0,mList_i, SingleRowModel_mList_i, mContext, this);
        mAdapter_j = new FindingObjAdapter(0,mList_j, SingleRowModel_mList_j, mContext, this);
        mAdapter_h_new = new FindingObjAdapter(0,mList_h_new, SingleRowModel_mList_h_new, mContext, this);
        mAdapter_j_new = new FindingObjAdapter(0,mList_j_new, SingleRowModel_mList_j_new, mContext, this);
        mAdapter_k= new FindingObjAdapter(1,mList_k, SingleRowModel_mList_k, mContext, this);

        rv_list.setHasFixedSize(true);
        rv_list_a.setHasFixedSize(true);
        rv_list_b.setHasFixedSize(true);
        rv_list_c.setHasFixedSize(true);
        rv_list_d.setHasFixedSize(true);
        rv_list_e.setHasFixedSize(true);
        rv_list_f.setHasFixedSize(true);
        rv_list_g.setHasFixedSize(true);
        rv_list_h.setHasFixedSize(true);
        rv_list_i.setHasFixedSize(true);
        rv_list_j.setHasFixedSize(true);
        rv_list_h_new.setHasFixedSize(true);
        rv_list_j_new.setHasFixedSize(true);
        rv_list_k.setHasFixedSize(true);

        /*mAdapter.notifyDataSetChanged();
        mAdapter_a.notifyDataSetChanged();
        mAdapter_b.notifyDataSetChanged();
        mAdapter_c.notifyDataSetChanged();
        mAdapter_d.notifyDataSetChanged();
        mAdapter_e.notifyDataSetChanged();
        mAdapter_f.notifyDataSetChanged();
        mAdapter_g.notifyDataSetChanged();
        mAdapter_h.notifyDataSetChanged();
        mAdapter_i.notifyDataSetChanged();
        mAdapter_j.notifyDataSetChanged();*/

        rv_list.setAdapter(mAdapter);
        rv_list_a.setAdapter(mAdapter_a);
        rv_list_b.setAdapter(mAdapter_b);
        rv_list_c.setAdapter(mAdapter_c);
        rv_list_d.setAdapter(mAdapter_d);
        rv_list_e.setAdapter(mAdapter_e);
        rv_list_f.setAdapter(mAdapter_f);
        rv_list_g.setAdapter(mAdapter_g);
        rv_list_h.setAdapter(mAdapter_h);
        rv_list_i.setAdapter(mAdapter_i);
        rv_list_j.setAdapter(mAdapter_j);
        rv_list_h_new.setAdapter(mAdapter_h_new);
        rv_list_j_new.setAdapter(mAdapter_j_new);
        rv_list_k.setAdapter(mAdapter_k);




        Bundle bundle= this.getArguments();
        if(String.valueOf(bundle.getInt("holiday")).equals("1"))
        {
            List<String> list5 = new ArrayList<String>();
            list5.add("H");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, list5);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_done_hours.setAdapter(dataAdapter);

           bundle.clear();
        }

        Log.e("msg ", String.valueOf(bundle.getInt("holiday")));
        refreshPage();
        mAdapter.notifyDataSetChanged();
        et_A.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList_A.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // mList_A.clear();
                if (s.length() > 0 && s.length() <= 15) {
                    SingleRowModel_mList_A.clear();

                    SingleRowModel_mList_A.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_A_TEMP, model.getUuid()));
                    if (s.length() > 0 && s.length() <= 15) {

                        i(TAG, "afterTextChanged: Size: " + SingleRowModel_mList_A.size());
                        int num = Integer.parseInt(s.toString());

                        for (int i = 0; i < num; i++) {

                            mList_A.add(i + 1);
                            if (i < SingleRowModel_mList_A.size()) {
                                SingleRowModel_mList_A.get(i).getObs();
                                i(TAG, "afterTextChanged: Data fetched: " + SingleRowModel_mList_A.get(i).getObs());

                            }
                            // mList_A.add(SingleRowModel_mList_A.size() + i + 1);
                        }
                        mAdapter.notifyDataSetChanged();


                        //prepareMasterList(num, mList_A);

                    } else {
                        int num = Integer.parseInt(s.toString());
                        prepareMasterList(num, mList_A);
                        mAdapter.notifyDataSetChanged();
                    }
                } else if (TextUtils.isEmpty(s.toString()) || s.toString().equals("")) {

                    mAdapter.notifyDataSetChanged();
                }


            }
        });

        et_a.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList_a.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.length() <= 15) {
                    SingleRowModel_mList_a.clear();

                    SingleRowModel_mList_a.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_AA_TEMP, model.getUuid()));
                    if (s.length() > 0 && s.length() <= 15) {

                        i(TAG, "afterTextChanged: Size: " + SingleRowModel_mList_a.size());
                        int num = Integer.parseInt(s.toString());

                        for (int i = 0; i < num; i++) {

                            mList_a.add(i + 1);
                            if (i < SingleRowModel_mList_a.size()) {
                                SingleRowModel_mList_a.get(i).getObs();
                                i(TAG, "afterTextChanged: Data fetched: " + SingleRowModel_mList_a.get(i).getObs());

                            }
                            // mList_A.add(SingleRowModel_mList_A.size() + i + 1);
                        }
                        mAdapter_a.notifyDataSetChanged();


                        //prepareMasterList(num, mList_A);

                    } else {
                        int num = Integer.parseInt(s.toString());
                        prepareMasterList(num, mList_a);
                        mAdapter_a.notifyDataSetChanged();
                    }
                } else if (TextUtils.isEmpty(s.toString()) || s.toString().equals("")) {
                    mList_a.clear();
                    mAdapter_a.notifyDataSetChanged();
                }

            }
        });

        et_b.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList_b.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.length() <= 15) {
                    SingleRowModel_mList_b.clear();

                    SingleRowModel_mList_b.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_B_TEMP, model.getUuid()));
                    if (s.length() > 0 && s.length() <= 15) {

                        i(TAG, "afterTextChanged: Size: " + SingleRowModel_mList_b.size());
                        int num = Integer.parseInt(s.toString());

                        for (int i = 0; i < num; i++) {

                            mList_b.add(i + 1);
                            if (i < SingleRowModel_mList_b.size()) {
                                SingleRowModel_mList_b.get(i).getObs();
                                i(TAG, "afterTextChanged: Data fetched: " + SingleRowModel_mList_b.get(i).getObs());

                            }
                            // mList_A.add(SingleRowModel_mList_A.size() + i + 1);
                        }
                        mAdapter_b.notifyDataSetChanged();


                        //prepareMasterList(num, mList_A);

                    } else {
                        int num = Integer.parseInt(s.toString());
                        prepareMasterList(num, mList_b);
                        mAdapter_b.notifyDataSetChanged();
                    }
                } else if (TextUtils.isEmpty(s.toString()) || s.toString().equals("")) {
                    mList_b.clear();
                    mAdapter_b.notifyDataSetChanged();
                }
            }
        });

        et_c.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList_c.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.length() <= 15) {
                    SingleRowModel_mList_c.clear();

                    SingleRowModel_mList_c.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_C_TEMP, model.getUuid()));
                    if (s.length() > 0 && s.length() <= 15) {

                        i(TAG, "afterTextChanged: Size: " + SingleRowModel_mList_c.size());
                        int num = Integer.parseInt(s.toString());

                        for (int i = 0; i < num; i++) {

                            mList_c.add(i + 1);
                            if (i < SingleRowModel_mList_c.size()) {
                                SingleRowModel_mList_c.get(i).getObs();
                                i(TAG, "afterTextChanged: Data fetched: " + SingleRowModel_mList_c.get(i).getObs());

                            }
                            // mList_A.add(SingleRowModel_mList_A.size() + i + 1);
                        }
                        mAdapter_c.notifyDataSetChanged();


                        //prepareMasterList(num, mList_A);

                    } else {
                        int num = Integer.parseInt(s.toString());
                        prepareMasterList(num, mList_c);
                        mAdapter_c.notifyDataSetChanged();
                    }
                } else if (TextUtils.isEmpty(s.toString()) || s.toString().equals("")) {
                    mList_c.clear();
                    mAdapter_c.notifyDataSetChanged();
                }
            }
        });

        et_d.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList_d.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.length() <= 15) {
                    SingleRowModel_mList_d.clear();

                    SingleRowModel_mList_d.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_D_TEMP, model.getUuid()));
                    if (s.length() > 0 && s.length() <= 15) {

                        i(TAG, "afterTextChanged: Size: " + SingleRowModel_mList_d.size());
                        int num = Integer.parseInt(s.toString());

                        for (int i = 0; i < num; i++) {

                            mList_d.add(i + 1);
                            if (i < SingleRowModel_mList_d.size()) {
                                SingleRowModel_mList_d.get(i).getObs();
                                i(TAG, "afterTextChanged: Data fetched: " + SingleRowModel_mList_d.get(i).getObs());

                            }
                            // mList_A.add(SingleRowModel_mList_A.size() + i + 1);
                        }
                        mAdapter_d.notifyDataSetChanged();


                        //prepareMasterList(num, mList_A);

                    } else {
                        int num = Integer.parseInt(s.toString());
                        prepareMasterList(num, mList_d);
                        mAdapter_d.notifyDataSetChanged();
                    }
                } else if (TextUtils.isEmpty(s.toString()) || s.toString().equals("")) {
                    mList_d.clear();
                    mAdapter_d.notifyDataSetChanged();
                }
            }
        });

        et_e.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList_e.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.length() <= 15) {
                    SingleRowModel_mList_e.clear();

                    SingleRowModel_mList_e.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_E_TEMP, model.getUuid()));
                    if (s.length() > 0 && s.length() <= 15) {

                        i(TAG, "afterTextChanged: Size: " + SingleRowModel_mList_e.size());
                        int num = Integer.parseInt(s.toString());

                        for (int i = 0; i < num; i++) {

                            mList_e.add(i + 1);
                            if (i < SingleRowModel_mList_e.size()) {
                                SingleRowModel_mList_e.get(i).getObs();
                                i(TAG, "afterTextChanged: Data fetched: " + SingleRowModel_mList_e.get(i).getObs());

                            }
                            // mList_A.add(SingleRowModel_mList_A.size() + i + 1);
                        }
                        mAdapter_e.notifyDataSetChanged();


                        //prepareMasterList(num, mList_A);

                    } else {
                        int num = Integer.parseInt(s.toString());
                        prepareMasterList(num, mList_e);
                        mAdapter_e.notifyDataSetChanged();
                    }
                } else if (TextUtils.isEmpty(s.toString()) || s.toString().equals("")) {
                    mList_e.clear();
                    mAdapter_e.notifyDataSetChanged();
                }
            }
        });

        et_f.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList_f.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.length() <= 15) {
                    SingleRowModel_mList_f.clear();

                    SingleRowModel_mList_f.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_F_TEMP, model.getUuid()));
                    if (s.length() > 0 && s.length() <= 15) {

                        i(TAG, "afterTextChanged: Size: " + SingleRowModel_mList_f.size());
                        int num = Integer.parseInt(s.toString());

                        for (int i = 0; i < num; i++) {

                            mList_f.add(i + 1);
                            if (i < SingleRowModel_mList_f.size()) {
                                SingleRowModel_mList_f.get(i).getObs();
                                i(TAG, "afterTextChanged: Data fetched: " + SingleRowModel_mList_f.get(i).getObs());

                            }
                            // mList_A.add(SingleRowModel_mList_A.size() + i + 1);
                        }
                        mAdapter_f.notifyDataSetChanged();


                        //prepareMasterList(num, mList_A);

                    } else {
                        int num = Integer.parseInt(s.toString());
                        prepareMasterList(num, mList_f);
                        mAdapter_f.notifyDataSetChanged();
                    }
                } else if (TextUtils.isEmpty(s.toString()) || s.toString().equals("")) {
                    mList_f.clear();
                    mAdapter_f.notifyDataSetChanged();
                }
            }
        });


        et_g.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList_g.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.length() <= 15) {
                    SingleRowModel_mList_g.clear();

                    SingleRowModel_mList_g.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_G_TEMP, model.getUuid()));
                    if (s.length() > 0 && s.length() <= 15) {

                        i(TAG, "afterTextChanged: Size: " + SingleRowModel_mList_g.size());
                        int num = Integer.parseInt(s.toString());

                        for (int i = 0; i < num; i++) {

                            mList_g.add(i + 1);
                            if (i < SingleRowModel_mList_g.size()) {
                                SingleRowModel_mList_g.get(i).getObs();
                                i(TAG, "afterTextChanged: Data fetched: " + SingleRowModel_mList_g.get(i).getObs());

                            }
                            // mList_A.add(SingleRowModel_mList_A.size() + i + 1);
                        }
                        mAdapter_g.notifyDataSetChanged();


                        //prepareMasterList(num, mList_A);

                    } else {
                        int num = Integer.parseInt(s.toString());
                        prepareMasterList(num, mList_g);
                        mAdapter_g.notifyDataSetChanged();
                    }
                } else if (TextUtils.isEmpty(s.toString()) || s.toString().equals("")) {
                    mList_g.clear();
                    mAdapter_g.notifyDataSetChanged();
                }
            }
        });


        et_h.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList_h.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.length() <= 15) {
                    SingleRowModel_mList_h.clear();

                    SingleRowModel_mList_h.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_H_TEMP, model.getUuid()));
                    if (s.length() > 0 && s.length() <= 15) {

                        i(TAG, "afterTextChanged: Size: " + SingleRowModel_mList_h.size());
                        int num = Integer.parseInt(s.toString());

                        for (int i = 0; i < num; i++) {

                            mList_h.add(i + 1);
                            if (i < SingleRowModel_mList_h.size()) {
                                SingleRowModel_mList_h.get(i).getObs();
                                i(TAG, "afterTextChanged: Data fetched: " + SingleRowModel_mList_h.get(i).getObs());

                            }
                            // mList_A.add(SingleRowModel_mList_A.size() + i + 1);
                        }
                        mAdapter_h.notifyDataSetChanged();


                        //prepareMasterList(num, mList_A);

                    } else {
                        int num = Integer.parseInt(s.toString());
                        prepareMasterList(1, mList_h);
                        mAdapter_h.notifyDataSetChanged();
                    }
                } else if (TextUtils.isEmpty(s.toString()) || s.toString().equals("")) {
                    mList_h.clear();
                    mAdapter_h.notifyDataSetChanged();
                }
            }
        });


        et_i.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList_i.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.length() <= 15) {
                    SingleRowModel_mList_i.clear();

                    SingleRowModel_mList_i.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_I_TEMP, model.getUuid()));
                    if (s.length() > 0 && s.length() <= 15) {

                        i(TAG, "afterTextChanged: Size: " + SingleRowModel_mList_i.size());
                        int num = Integer.parseInt(s.toString());

                        for (int i = 0; i < num; i++) {

                            mList_i.add(i + 1);
                            if (i < SingleRowModel_mList_i.size()) {
                                SingleRowModel_mList_i.get(i).getObs();
                                i(TAG, "afterTextChanged: Data fetched: " + SingleRowModel_mList_i.get(i).getObs());

                            }
                            // mList_A.add(SingleRowModel_mList_A.size() + i + 1);
                        }
                        mAdapter_i.notifyDataSetChanged();


                        //prepareMasterList(num, mList_A);

                    } else {
                        int num = Integer.parseInt(s.toString());
                        prepareMasterList(1, mList_i);
                        mAdapter_i.notifyDataSetChanged();
                    }
                } else if (TextUtils.isEmpty(s.toString()) || s.toString().equals("")) {
                    mList_i.clear();
                    mAdapter_i.notifyDataSetChanged();
                }
            }
        });

        et_j.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList_j.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.length() <= 15) {
                    SingleRowModel_mList_j.clear();

                    SingleRowModel_mList_j.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_J_TEMP, model.getUuid()));
                    if (s.length() > 0 && s.length() <= 15) {

                        i(TAG, "afterTextChanged: Size: " + SingleRowModel_mList_j.size());
                        int num = Integer.parseInt(s.toString());

                        for (int i = 0; i < num; i++) {

                            mList_j.add(i + 1);
                            if (i < SingleRowModel_mList_j.size()) {
                                SingleRowModel_mList_j.get(i).getObs();
                                i(TAG, "afterTextChanged: Data fetched: " + SingleRowModel_mList_j.get(i).getObs());

                            }
                            // mList_A.add(SingleRowModel_mList_A.size() + i + 1);
                        }
                        mAdapter_j.notifyDataSetChanged();


                        //prepareMasterList(num, mList_A);

                    } else {
                        int num = Integer.parseInt(s.toString());
                        prepareMasterList(1, mList_j);
                        mAdapter_j.notifyDataSetChanged();
                    }
                } else if (TextUtils.isEmpty(s.toString()) || s.toString().equals("")) {
                    mList_j.clear();
                    mAdapter_j.notifyDataSetChanged();
                }
            }
        });
        et_h_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList_h_new.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.length() <= 15) {
                    SingleRowModel_mList_h_new.clear();

                    SingleRowModel_mList_h_new.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_H_TEMP_NEW, model.getUuid()));
                    if (s.length() > 0 && s.length() <= 15) {

                        i(TAG, "afterTextChanged: Size: " + SingleRowModel_mList_h_new.size());
                        int num = Integer.parseInt(s.toString());

                        for (int i = 0; i < num; i++) {

                            mList_h_new.add(i + 1);
                            if (i < SingleRowModel_mList_h_new.size()) {
                                SingleRowModel_mList_h_new.get(i).getObs();
                                i(TAG, "afterTextChanged: Data fetched: " + SingleRowModel_mList_h_new.get(i).getObs());

                            }
                            // mList_A.add(SingleRowModel_mList_A.size() + i + 1);
                        }
                        mAdapter_h_new.notifyDataSetChanged();


                        //prepareMasterList(num, mList_A);

                    } else {
                        int num = Integer.parseInt(s.toString());
                        prepareMasterList(1, mList_h_new);
                        mAdapter_h_new.notifyDataSetChanged();
                    }
                } else if (TextUtils.isEmpty(s.toString()) || s.toString().equals("")) {
                    mList_h_new.clear();
                    mAdapter_h_new.notifyDataSetChanged();
                }
            }
        });
        et_j_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList_j_new.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.length() <= 15) {
                    SingleRowModel_mList_j_new.clear();

                    SingleRowModel_mList_j_new.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_J_TEMP_NEW, model.getUuid()));
                    if (s.length() > 0 && s.length() <= 15) {

                        i(TAG, "afterTextChanged: Size: " + SingleRowModel_mList_j_new.size());
                        int num = Integer.parseInt(s.toString());

                        for (int i = 0; i < num; i++) {

                            mList_j_new.add(i + 1);
                            if (i < SingleRowModel_mList_j_new.size()) {
                                SingleRowModel_mList_j_new.get(i).getObs();
                                i(TAG, "afterTextChanged: Data fetched: " + SingleRowModel_mList_j_new.get(i).getObs());

                            }
                            // mList_A.add(SingleRowModel_mList_A.size() + i + 1);
                        }
                        mAdapter_j_new.notifyDataSetChanged();


                        //prepareMasterList(num, mList_A);

                    } else {
                        int num = Integer.parseInt(s.toString());
                        prepareMasterList(1, mList_j_new);
                        mAdapter_j_new.notifyDataSetChanged();
                    }
                } else if (TextUtils.isEmpty(s.toString()) || s.toString().equals("")) {
                    mList_j_new.clear();
                    mAdapter_j_new.notifyDataSetChanged();
                }
            }
        });
        et_k.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList_k.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.length() <= 15) {
                    SingleRowModel_mList_k.clear();

                    SingleRowModel_mList_k.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_K_TEMP, model.getUuid()));
                    if (s.length() > 0 && s.length() <= 15) {

                        i(TAG, "afterTextChanged: Size: " + SingleRowModel_mList_k.size());
                        int num = Integer.parseInt(s.toString());

                        for (int i = 0; i < num; i++) {

                            mList_k.add(i + 1);
                            if (i < SingleRowModel_mList_k.size()) {
                                SingleRowModel_mList_k.get(i).getQuantity();
                                i(TAG, "afterTextChanged: Data fetched: " + SingleRowModel_mList_k.get(i).getQuantity());

                            }
                            // mList_A.add(SingleRowModel_mList_A.size() + i + 1);
                        }
                        mAdapter_k.notifyDataSetChanged();


                        //prepareMasterList(num, mList_A);

                    } else {
                        int num = Integer.parseInt(s.toString());
                        prepareMasterList(1, mList_k);
                        mAdapter_k.notifyDataSetChanged();
                    }
                } else if (TextUtils.isEmpty(s.toString()) || s.toString().equals("")) {
                    mList_k.clear();
                    mAdapter_k.notifyDataSetChanged();
                }
            }
        });

        btn_temp_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sp_done_hours.getSelectedItem().toString().equals("H"))
                {
                    tempSaveInspectionforHoliday();
                }
                else
                {
                    tempSaveInspection(); // Temp  save inspection Report
                }




            }

        });




    }
///added 18feb2020 by kiran
    private void tempSaveInspectionforHoliday() {
        // added 21 feb
        /*if (mAdapter.getItemCount() > 0 ||
                mAdapter_a.getItemCount() > 0 ||
                mAdapter_b.getItemCount() > 0 ||
                mAdapter_c.getItemCount() > 0 ||
                mAdapter_d.getItemCount() > 0 ||
                mAdapter_e.getItemCount() > 0 ||
                mAdapter_f.getItemCount() > 0 ||
                mAdapter_g.getItemCount() > 0 ||
                mAdapter_h.getItemCount() > 0 ||
                mAdapter_i.getItemCount() > 0 ||
                mAdapter_j.getItemCount() > 0 ||
                mAdapter_h_new.getItemCount() > 0 ||
                mAdapter_j_new.getItemCount() > 0 ||
                mAdapter_k.getItemCount() > 0){*/



            //  Log.i(TAG, "tempSaveInspection: greater than 0");
            final ArrayList<Integer> maxList = checkMaxNum();

            int max = maxList.get(0);
            for (Integer maxNumber : maxList) {
                if (maxNumber > max)
                    max = maxNumber;
            }
            //  Log.i(TAG, "onClick: Maximum number: " + max);
            mDatabase = mHelper.getWritableDatabase();
            int lastCount = new DbHelper(mContext).getCountMax(model.getUuid());
            if (lastCount == 0) {
                i(TAG, "tempSaveInspection: new lows starting");
                //  insertAllNullVewsFirst(max);
            }

            if (max > lastCount && lastCount != 0) {
                i(TAG, "tempSaveInspection: creating max: " + max + " last " + lastCount + " extra rows: " + (max - lastCount) + "");
                //delete  row and insert again
                // new DbHelper(mContext).deleteTemp2Page(model.getUuid());

                //  insertAllNullVewsFirst(max - lastCount);
            }

            new DbHelper(mContext).deleteAllTempRows(model.getUuid());
            try {
                Log.e(TAG, "Adaptor get count: "+mAdapter_k.getItemCount() );


                      //  updateRowsk();
                        updateRowsA();
                        updateRowsk();
                        updateRowsa();
                        updateRowsb();
                        updateRowsc();
                        updateRowsd();
                        updateRowse();
                        updateRowsf();
                        updateRowsg();
                        updateRowsh();
                        updateRowsi();
                        updateRowsj();
                        updateRowshnew();
                        updateRowsjnew();

                        Toasty.warning(mContext, "Draft Saved Successfully ", Toast.LENGTH_LONG).show();

                        btn_preview.setVisibility(View.VISIBLE);
                        btn_temp_save.setVisibility(View.INVISIBLE);
                        btn_preview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                handlePreviewButton(); // insertion of inspection Report
                            }

                        });



            } catch (Exception e) {
                e.printStackTrace();
            }
       /* } else {
            Toasty.warning(mContext, "Insert atleast one row to Save Temporary", Toast.LENGTH_LONG).show();
            btn_preview.setVisibility(View.INVISIBLE);
        }*/

    }

    private void tempSaveInspection() {



        if (mAdapter.getItemCount() > 0 ||
                mAdapter_a.getItemCount() > 0 ||
                mAdapter_b.getItemCount() > 0 ||
                mAdapter_c.getItemCount() > 0 ||
                mAdapter_d.getItemCount() > 0 ||
                mAdapter_e.getItemCount() > 0 ||
                mAdapter_f.getItemCount() > 0 ||
                mAdapter_g.getItemCount() > 0 ||
                mAdapter_h.getItemCount() > 0 ||
                mAdapter_i.getItemCount() > 0 ||
                mAdapter_j.getItemCount() > 0 ||
                mAdapter_h_new.getItemCount() > 0 ||
                mAdapter_j_new.getItemCount() > 0 ||
                mAdapter_k.getItemCount() > 0){
            //  Log.i(TAG, "tempSaveInspection: greater than 0");
            final ArrayList<Integer> maxList = checkMaxNum();

            int max = maxList.get(0);
            for (Integer maxNumber : maxList) {
                if (maxNumber > max)
                    max = maxNumber;
            }
            //  Log.i(TAG, "onClick: Maximum number: " + max);
            mDatabase = mHelper.getWritableDatabase();
            int lastCount = new DbHelper(mContext).getCountMax(model.getUuid());
            if (lastCount == 0) {
                i(TAG, "tempSaveInspection: new lows starting");
                //  insertAllNullVewsFirst(max);
            }

            if (max > lastCount && lastCount != 0) {
                i(TAG, "tempSaveInspection: creating max: " + max + " last " + lastCount + " extra rows: " + (max - lastCount) + "");
                //delete  row and insert again
                // new DbHelper(mContext).deleteTemp2Page(model.getUuid());

                //  insertAllNullVewsFirst(max - lastCount);
            }

            new DbHelper(mContext).deleteAllTempRows(model.getUuid());
            try {
                Log.e(TAG, "Adaptor get count: "+mAdapter_k.getItemCount() );
                if ( mAdapter_k.getItemCount()<1)
                {
                    Toasty.error(mContext,"fill quantity first...",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!updateRowsk()) {
                        Toasty.error(mContext, "Please Select Desc , unit of measure and enter quantity", Toast.LENGTH_SHORT).show();
                    } else {
                        updateRowsA();
                        updateRowsa();
                        updateRowsb();
                        updateRowsc();
                        updateRowsd();
                        updateRowse();
                        updateRowsf();
                        updateRowsg();
                        updateRowsh();
                        updateRowsi();
                        updateRowsj();
                        updateRowshnew();
                        updateRowsjnew();

                        Toasty.warning(mContext, "Draft Saved Successfully ", Toast.LENGTH_LONG).show();

                        btn_preview.setVisibility(View.VISIBLE);
                        btn_temp_save.setVisibility(View.INVISIBLE);
                        btn_preview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                handlePreviewButton(); // insertion of inspection Report
                            }

                        });
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

//

        } else {
            Toasty.warning(mContext, "Insert Some rows to Save Temporary", Toast.LENGTH_LONG).show();
            btn_preview.setVisibility(View.INVISIBLE);
        }

        //refreshPage();

    }



    private void updateRowsA() {
     if(sp_done_hours.getSelectedItem().toString().equals("H"))
     {
         saveContentConstantValues(1, DbConstant.TEMP_ENTRY.TABLE_A_TEMP,
                 "nil", "NA");
     }


        for (int j = 1; j <= mAdapter.getItemCount(); j++) {
            viewHolder = (FindingObjAdapter.MyViewHolder) rv_list.findViewHolderForAdapterPosition(j - 1);
            if ((viewHolder != null ? viewHolder.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder.btn_single_add.getTag());

                try {
                    String btnTag = viewHolder.btn_single_add.getTag() == null ? "NA" : viewHolder.btn_single_add.getTag().toString();

                    if (model != null) {
                        saveContentConstantValues(j, DbConstant.TEMP_ENTRY.TABLE_A_TEMP,
                                viewHolder.et_single_obs.getText().toString(), btnTag);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return;
        }
    }

    private void updateRowsa() {

        for (int j = 1; j <= mAdapter_a.getItemCount(); j++) {
            viewHolder_a = (FindingObjAdapter.MyViewHolder) rv_list_a.findViewHolderForAdapterPosition(j - 1);
            if ((viewHolder_a != null ? viewHolder_a.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_a.btn_single_add.getTag());

                try {
                    String btnTag = viewHolder_a.btn_single_add.getTag() == null ? "NA" : viewHolder_a.btn_single_add.getTag().toString();

                    if (model != null) {

                        saveContentConstantValues(j, DbConstant.TEMP_ENTRY.TABLE_AA_TEMP,
                                viewHolder_a.et_single_obs.getText().toString(), btnTag);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return;
        }
    }

    private void saveContentConstantValues(int j, String tableName, String obs, String obsFile) {
        ContentValues mValues = new ContentValues();
        mValues.put(DbConstant.TEMP_ENTRY.INSPECTION_ID, model.getUuid());
        mValues.put(DbConstant.TEMP_ENTRY.OBS_FILE, obsFile);
        mValues.put(DbConstant.TEMP_ENTRY.OBS, obs);
        mValues.put(DbConstant.IrIrn_Data_Entry.DESC_NUM, String.valueOf(j));

        String selection = "(" + DbConstant.TEMP_ENTRY.OBS + " is null OR "
                + DbConstant.TEMP_ENTRY.OBS + "=? ) AND ( "
                + DbConstant.TEMP_ENTRY.OBS_FILE + " is null OR "
                + DbConstant.TEMP_ENTRY.OBS_FILE + " =? ) AND "
                + DbConstant.TEMP_ENTRY.INSPECTION_ID + "=? AND "
                + DbConstant.TEMP_ENTRY.DESC_NUM + "=? ";

        String[] selectionArgs = new String[]{obs, obsFile, model.getUuid(), String.valueOf(j)};


        int id = mDatabase.update(tableName, mValues, selection, selectionArgs);
        i(TAG, "updateFirstPage: UPDATED TABLE : " + tableName + " ID: " + id);
        if (id == 0) {
            long id2 = mDatabase.insertOrThrow(tableName, null, mValues);
            i(TAG, "updateFirstPage: INSERTED TABLE : " + tableName + " ID: " + id2);
        }
    }
    private void saveContentConstantValuesQTY(int j, String tableName, String desc, String qty, String unit, String file) {
        ContentValues mValues = new ContentValues();
        mValues.put(DbConstant.TEMP_ENTRY.INSPECTION_ID, model.getUuid());
        mValues.put(DbConstant.TEMP_ENTRY.QTY_FILE, file);
        mValues.put(DbConstant.TEMP_ENTRY.QTY, qty);
        mValues.put(DbConstant.TEMP_ENTRY.UNIT, unit);
        mValues.put(DbConstant.TEMP_ENTRY.DESCRIPTION, desc);
        mValues.put(DbConstant.IrIrn_Data_Entry.DESC_NUM, String.valueOf(j));

        String selection = "(" + DbConstant.TEMP_ENTRY.QTY + " is null OR "
                + DbConstant.TEMP_ENTRY.QTY + "=? ) AND ( "
                + DbConstant.TEMP_ENTRY.QTY_FILE + " is null OR "
                + DbConstant.TEMP_ENTRY.QTY_FILE + " =? ) AND ("
                + DbConstant.TEMP_ENTRY.UNIT + " is null OR "
                + DbConstant.TEMP_ENTRY.UNIT + " =? ) AND ("
                + DbConstant.TEMP_ENTRY.DESCRIPTION + " is null OR "
                + DbConstant.TEMP_ENTRY.DESCRIPTION + " =? ) AND "
                + DbConstant.TEMP_ENTRY.INSPECTION_ID + "=? AND "
                + DbConstant.TEMP_ENTRY.DESC_NUM + "=? ";

        String[] selectionArgs = new String[]{ qty,file,unit,desc, model.getUuid(), String.valueOf(j)};


        int id = mDatabase.update(tableName, mValues, selection, selectionArgs);
        i(TAG, "updateFirstPage: UPDATED TABLE : " + tableName + " ID: " + id);
        if (id == 0) {
            long id2 = mDatabase.insertOrThrow(tableName, null, mValues);
            i(TAG, "updateFirstPage: INSERTED TABLE : " + tableName + " ID: " + id2);
        }
    }
    private void updateRowsb() {

        for (int j = 1; j <= mAdapter_b.getItemCount(); j++) {
            viewHolder_b = (FindingObjAdapter.MyViewHolder) rv_list_b.findViewHolderForAdapterPosition(j - 1);
            if ((viewHolder_b != null ? viewHolder_b.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_b.btn_single_add.getTag());

                try {
                    String btnTag = viewHolder_b.btn_single_add.getTag() == null ? "NA" : viewHolder_b.btn_single_add.getTag().toString();

                    if (model != null) {

                        saveContentConstantValues(j, DbConstant.TEMP_ENTRY.TABLE_B_TEMP,
                                viewHolder_b.et_single_obs.getText().toString(), btnTag);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return;
        }
    }

    private void updateRowsc() {

        for (int j = 1; j <= mAdapter_c.getItemCount(); j++) {
            viewHolder_c = (FindingObjAdapter.MyViewHolder) rv_list_c.findViewHolderForAdapterPosition(j - 1);
            if ((viewHolder_c != null ? viewHolder_c.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_c.btn_single_add.getTag());

                try {
                    String btnTag = viewHolder_c.btn_single_add.getTag() == null ? "NA" : viewHolder_c.btn_single_add.getTag().toString();

                    if (model != null) {
                        saveContentConstantValues(j, DbConstant.TEMP_ENTRY.TABLE_C_TEMP,
                                viewHolder_c.et_single_obs.getText().toString(), btnTag);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return;
        }
    }

    private void updateRowsd() {

        for (int j = 1; j <= mAdapter_d.getItemCount(); j++) {
            viewHolder_d = (FindingObjAdapter.MyViewHolder) rv_list.findViewHolderForAdapterPosition(j - 1);
            if ((viewHolder_d != null ? viewHolder_d.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_d.btn_single_add.getTag());

                try {
                    String btnTag = viewHolder_d.btn_single_add.getTag() == null ? "NA" : viewHolder_d.btn_single_add.getTag().toString();

                    if (model != null) {
                        saveContentConstantValues(j, DbConstant.TEMP_ENTRY.TABLE_D_TEMP,
                                viewHolder_d.et_single_obs.getText().toString(), btnTag);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return;
        }
    }

    private void updateRowse() {

        for (int j = 1; j <= mAdapter_e.getItemCount(); j++) {
            viewHolder_e = (FindingObjAdapter.MyViewHolder) rv_list_e.findViewHolderForAdapterPosition(j - 1);
            if ((viewHolder_e != null ? viewHolder_e.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_e.btn_single_add.getTag());

                try {
                    String btnTag = viewHolder_e.btn_single_add.getTag() == null ? "NA" : viewHolder_e.btn_single_add.getTag().toString();

                    if (model != null) {
                        saveContentConstantValues(j, DbConstant.TEMP_ENTRY.TABLE_E_TEMP,
                                viewHolder_e.et_single_obs.getText().toString(), btnTag);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return;
        }
    }

    private void updateRowsf() {

        for (int j = 1; j <= mAdapter_f.getItemCount(); j++) {
            viewHolder_f = (FindingObjAdapter.MyViewHolder) rv_list_f.findViewHolderForAdapterPosition(j - 1);
            if ((viewHolder_f != null ? viewHolder_f.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_f.btn_single_add.getTag());

                try {
                    String btnTag = viewHolder_f.btn_single_add.getTag() == null ? "NA" : viewHolder_f.btn_single_add.getTag().toString();

                    if (model != null) {
                        saveContentConstantValues(j, DbConstant.TEMP_ENTRY.TABLE_F_TEMP,
                                viewHolder_f.et_single_obs.getText().toString(), btnTag);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return;
        }
    }

    private void updateRowsg() {

        for (int j = 1; j <= mAdapter_g.getItemCount(); j++) {
            viewHolder_g = (FindingObjAdapter.MyViewHolder) rv_list_g.findViewHolderForAdapterPosition(j - 1);
            if ((viewHolder_g != null ? viewHolder_g.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_g.btn_single_add.getTag());

                try {
                    String btnTag = viewHolder_g.btn_single_add.getTag() == null ? "NA" : viewHolder_g.btn_single_add.getTag().toString();

                    if (model != null) {
                        saveContentConstantValues(j, DbConstant.TEMP_ENTRY.TABLE_G_TEMP,
                                viewHolder_g.et_single_obs.getText().toString(), btnTag);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return;
        }
    }

    private void updateRowsh() {

        for (int j = 1; j <= mAdapter_h.getItemCount(); j++) {
            viewHolder_h = (FindingObjAdapter.MyViewHolder) rv_list_h.findViewHolderForAdapterPosition(j - 1);
            if ((viewHolder_h != null ? viewHolder_h.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_h.btn_single_add.getTag());

                try {
                    String btnTag = viewHolder_h.btn_single_add.getTag() == null ? "NA" : viewHolder_h.btn_single_add.getTag().toString();

                    if (model != null) {
                        saveContentConstantValues(j, DbConstant.TEMP_ENTRY.TABLE_H_TEMP,
                                viewHolder_h.et_single_obs.getText().toString(), btnTag);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return;
        }
    }

    private void updateRowsi() {

        for (int j = 1; j <= mAdapter_i.getItemCount(); j++) {
            viewHolder_i = (FindingObjAdapter.MyViewHolder) rv_list_i.findViewHolderForAdapterPosition(j - 1);
            if ((viewHolder_i != null ? viewHolder_i.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_i.btn_single_add.getTag());

                try {
                    String btnTag = viewHolder_i.btn_single_add.getTag() == null ? "NA" : viewHolder_i.btn_single_add.getTag().toString();

                    if (model != null) {
                        saveContentConstantValues(j, DbConstant.TEMP_ENTRY.TABLE_I_TEMP,
                                viewHolder_i.et_single_obs.getText().toString(), btnTag);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return;
        }
    }

    private void updateRowsj() {

        for (int j = 1; j <= mAdapter_j.getItemCount(); j++) {
            viewHolder_j = (FindingObjAdapter.MyViewHolder) rv_list_j.findViewHolderForAdapterPosition(j - 1);
            if ((viewHolder_j != null ? viewHolder_j.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_j.btn_single_add.getTag());

                try {
                    String btnTag = viewHolder_j.btn_single_add.getTag() == null ? "NA" : viewHolder_j.btn_single_add.getTag().toString();

                    if (model != null) {
                        saveContentConstantValues(j, DbConstant.TEMP_ENTRY.TABLE_J_TEMP,
                                viewHolder_j.et_single_obs.getText().toString(), btnTag);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return;
        }
    }
    private void updateRowshnew() {

        for (int j = 1; j <= mAdapter_h_new.getItemCount(); j++) {
            viewHolder_h_new = (FindingObjAdapter.MyViewHolder) rv_list_h_new.findViewHolderForAdapterPosition(j - 1);
            if ((viewHolder_h_new != null ? viewHolder_h_new.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_h_new.btn_single_add.getTag());

                try {
                    String btnTag = viewHolder_h_new.btn_single_add.getTag() == null ? "NA" : viewHolder_h_new.btn_single_add.getTag().toString();

                    if (model != null) {
                        saveContentConstantValues(j, DbConstant.TEMP_ENTRY.TABLE_H_TEMP_NEW,
                                viewHolder_h_new.et_single_obs.getText().toString(), btnTag);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return;
        }
    }
    private void updateRowsjnew() {

        for (int j = 1; j <= mAdapter_j_new.getItemCount(); j++) {
            viewHolder_j_new = (FindingObjAdapter.MyViewHolder) rv_list_j_new.findViewHolderForAdapterPosition(j - 1);
            if ((viewHolder_j_new != null ? viewHolder_j_new.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_j_new.btn_single_add.getTag());

                try {
                    String btnTag = viewHolder_j_new.btn_single_add.getTag() == null ? "NA" : viewHolder_j_new.btn_single_add.getTag().toString();

                    if (model != null) {
                        saveContentConstantValues(j, DbConstant.TEMP_ENTRY.TABLE_J_TEMP_NEW,
                                viewHolder_j_new.et_single_obs.getText().toString(), btnTag);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return;
        }
    }
    private boolean updateRowsk() {


        boolean allselected=true;

        for (int j = 1; j <= mAdapter_k.getItemCount(); j++) {
            viewHolder_k = (FindingObjAdapter.MyViewHolder) rv_list_k.findViewHolderForAdapterPosition(j - 1);
            String desc = viewHolder_k.spinner_qty_desc.getSelectedItem().toString();
            String unit = viewHolder_k.spinner_qty_unit.getText().toString();
            String qtty = viewHolder_k.et_qty_qty.getText().toString();
            if (desc.equals(""))
            {
                Toasty.error(mContext,"Kindly click Refresh plan from menu",Toast.LENGTH_LONG).show();
                allselected=false;
                break;
            }
            if((desc.equals("Select"))||(unit.equals("Select"))||(qtty.equals("")))
            {
                allselected=false;
                break;
            }
        }
        if(allselected) {
            for (int j = 1; j <= mAdapter_k.getItemCount(); j++) {
                viewHolder_k = (FindingObjAdapter.MyViewHolder) rv_list_k.findViewHolderForAdapterPosition(j - 1);

                i(TAG, "TAG: " + viewHolder_k.btn_single_add.getTag());

                try {
                    String btnTag = viewHolder_k.btn_single_add.getTag() == null ? "NA" : viewHolder_k.btn_single_add.getTag().toString();
                    String desc = viewHolder_k.spinner_qty_desc.getSelectedItem().toString();
                    String unit = viewHolder_k.spinner_qty_unit.getText().toString();

                    if (model != null) {
                        saveContentConstantValuesQTY(j, DbConstant.TEMP_ENTRY.TABLE_K_TEMP, desc,
                                viewHolder_k.et_qty_qty.getText().toString(), unit, btnTag);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return allselected;

    }

    private void refreshPage() {

        if (model != null) {
            SingleRowModel_mList_A.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_A_TEMP, model.getUuid()));
            SingleRowModel_mList_a.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_AA_TEMP, model.getUuid()));
            SingleRowModel_mList_b.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_B_TEMP, model.getUuid()));
            SingleRowModel_mList_c.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_C_TEMP, model.getUuid()));
            SingleRowModel_mList_d.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_D_TEMP, model.getUuid()));
            SingleRowModel_mList_e.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_E_TEMP, model.getUuid()));
            SingleRowModel_mList_f.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_F_TEMP, model.getUuid()));
            SingleRowModel_mList_g.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_G_TEMP, model.getUuid()));
            SingleRowModel_mList_h.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_H_TEMP, model.getUuid()));
            SingleRowModel_mList_i.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_I_TEMP, model.getUuid()));
            SingleRowModel_mList_j.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_J_TEMP, model.getUuid()));
            SingleRowModel_mList_h_new.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_H_TEMP_NEW, model.getUuid()));
            SingleRowModel_mList_j_new.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_J_TEMP_NEW, model.getUuid()));
            SingleRowModel_mList_k.addAll(new DbHelper(mContext).retrieveTempData(DbConstant.TEMP_ENTRY.TABLE_K_TEMP, model.getUuid()));

            for (int i = 0; i < SingleRowModel_mList_A.size(); i++) mList_A.add(i);
            for (int i = 0; i < SingleRowModel_mList_a.size(); i++) mList_a.add(i);
            for (int i = 0; i < SingleRowModel_mList_b.size(); i++) mList_b.add(i);
            for (int i = 0; i < SingleRowModel_mList_c.size(); i++) mList_c.add(i);
            for (int i = 0; i < SingleRowModel_mList_d.size(); i++) mList_d.add(i);
            for (int i = 0; i < SingleRowModel_mList_e.size(); i++) mList_e.add(i);
            for (int i = 0; i < SingleRowModel_mList_f.size(); i++) mList_f.add(i);
            for (int i = 0; i < SingleRowModel_mList_g.size(); i++) mList_g.add(i);
            for (int i = 0; i < SingleRowModel_mList_h.size(); i++) mList_h.add(i);
            for (int i = 0; i < SingleRowModel_mList_i.size(); i++) mList_i.add(i);
            for (int i = 0; i < SingleRowModel_mList_j.size(); i++) mList_j.add(i);
            for (int i = 0; i < SingleRowModel_mList_h_new.size(); i++) mList_h_new.add(i);
            for (int i = 0; i < SingleRowModel_mList_j_new.size(); i++) mList_j_new.add(i);
            for (int i = 0; i < SingleRowModel_mList_k.size(); i++) mList_k.add(i);
            mAdapter.notifyDataSetChanged();
            mAdapter_a.notifyDataSetChanged();
            mAdapter_b.notifyDataSetChanged();
            mAdapter_c.notifyDataSetChanged();
            mAdapter_d.notifyDataSetChanged();
            mAdapter_e.notifyDataSetChanged();
            mAdapter_f.notifyDataSetChanged();
            mAdapter_g.notifyDataSetChanged();
            mAdapter_h.notifyDataSetChanged();
            mAdapter_i.notifyDataSetChanged();
            mAdapter_j.notifyDataSetChanged();
            mAdapter_h_new.notifyDataSetChanged();
            mAdapter_j_new.notifyDataSetChanged();
            mAdapter_k.notifyDataSetChanged();
            if (SingleRowModel_mList_A.size() > 0)
                et_A.setText(String.valueOf(SingleRowModel_mList_A.size()));
            if (SingleRowModel_mList_a.size() > 0)
                et_a.setText(String.valueOf(SingleRowModel_mList_a.size()));
            if (SingleRowModel_mList_b.size() > 0)
                et_b.setText(String.valueOf(SingleRowModel_mList_b.size()));
            if (SingleRowModel_mList_c.size() > 0)
                et_c.setText(String.valueOf(SingleRowModel_mList_c.size()));
            if (SingleRowModel_mList_d.size() > 0)
                et_d.setText(String.valueOf(SingleRowModel_mList_d.size()));
            if (SingleRowModel_mList_e.size() > 0)
                et_e.setText(String.valueOf(SingleRowModel_mList_e.size()));
            if (SingleRowModel_mList_f.size() > 0)
                et_f.setText(String.valueOf(SingleRowModel_mList_f.size()));
            if (SingleRowModel_mList_g.size() > 0)
                et_g.setText(String.valueOf(SingleRowModel_mList_g.size()));
            if (SingleRowModel_mList_h.size() > 0)
                et_h.setText(String.valueOf(SingleRowModel_mList_h.size()));
            if (SingleRowModel_mList_i.size() > 0)
                et_i.setText(String.valueOf(SingleRowModel_mList_i.size()));
            if (SingleRowModel_mList_j.size() > 0)
                et_j.setText(String.valueOf(SingleRowModel_mList_j.size()));
            if (SingleRowModel_mList_h_new.size() > 0)
                et_h_new.setText(String.valueOf(SingleRowModel_mList_h_new.size()));
            if (SingleRowModel_mList_j_new.size() > 0)
                et_j_new.setText(String.valueOf(SingleRowModel_mList_j_new.size()));
            if (SingleRowModel_mList_k.size() > 0)
                et_k.setText(String.valueOf(SingleRowModel_mList_k.size()));
        }

    }




    // handle preview Button click listener
    private void handlePreviewButton() {
        if(sp_done_hours.getSelectedItem().toString().equals("H")) {
           // tempSaveInspectionforHoliday();  // addedd 18feb 2020 kiran
        }
        else
        {
            tempSaveInspection();
        }
        if(sp_done_hours.getSelectedItem().toString().equals("Select"))
        {
            Toasty.error(mContext, "Please fill working hours", Toast.LENGTH_LONG).show();
            return;
        }


                    inspectionList = new ArrayList<>();

                    inspectionList.add("ICS Reg. No.: " + model.getIcs_reg_num());
                    inspectionList.add("Customer Name: " + model.getCust_name());
                    inspectionList.add("Consultrainer Name: " + model.getConslt_name());
                    inspectionList.add("PO. No.: " + model.getPo_number());
                    inspectionList.add("MF./SUPPLIER Name: " + model.getProject_vend());
                    inspectionList.add("Date of Inspection.: " + model.getDate_of_insp());
                    inspectionList.add("Item Material: " + model.getItem());
                    inspectionList.add("Quantaty: " + model.getQuantity());
                    inspectionList.add("Inspection Type: " + model.getInsp_type());
                    inspectionList.add("Code Standard: " + model.getCodes_standard());
                    inspectionList.add("Spec. Drawning: " + model.getSpec_drawings());
                    inspectionList.add("Batch No.: " + model.getBatch_no());
                    inspectionList.add("Location.: "+model.getLocation());
                    inspectionList.add("Description.: "+model.getDescription());
                    inspectionList.add("Site Incharge.: "+model.getSiteIncharge());
                    inspectionList.add("Range.: "+model.getRange());
                    inspectionList.add("Project Type.: "+model.getProject_type());
                    inspectionList.add("Done Hours.: "+sp_done_hours.getSelectedItem().toString());
                    inspectionList.add("Extra Hours.:"+et_extra_hours.getText().toString());
                    if(model.getProject_type().toString().equals("Quantity"))
                    {
                        inspectionList.add("No of Jobs.: "+model.getNo_of_jobs());
                    }

                    if (model.getInsp_type() != null && model.getInsp_type().equalsIgnoreCase("Final")) {
                        if (mDataArrayList != null && mDataArrayList.size() > 0) {
                            for (int i = 0; i < mDataArrayList.size(); i++) {
                                inspectionList.add("Description: " + (i + 1) + " : " +
                                        mDataArrayList.get(i).getDescription() + "\n Balance Qty: "
                                        + mDataArrayList.get(i).getBalQty());
                            }
                        }
                    }

                    previewing_observation(viewHolder, mAdapter, rv_list, tv_A);
                    previewing_observation(viewHolder_a, mAdapter_a, rv_list_a, tv_a);
                    previewing_observation(viewHolder_b, mAdapter_b, rv_list_b, tv_b);
                    previewing_observation(viewHolder_c, mAdapter_c, rv_list_c, tv_c);
                    previewing_observation(viewHolder_d, mAdapter_d, rv_list_d, tv_d);
                    previewing_observation(viewHolder_e, mAdapter_e, rv_list_e, tv_e);
                    previewing_observation(viewHolder_f, mAdapter_f, rv_list_f, tv_f);
                    previewing_observation(viewHolder_g, mAdapter_g, rv_list_g, tv_g);
                    previewing_observation(viewHolder_h, mAdapter_h, rv_list_h, tv_h);
                    previewing_observation(viewHolder_i, mAdapter_i, rv_list_i, tv_i);
                    previewing_observation(viewHolder_j, mAdapter_j, rv_list_j, tv_j);
                    previewing_observation(viewHolder_h_new, mAdapter_h_new, rv_list_h_new, tv_h_new);
                    previewing_observation(viewHolder_j_new, mAdapter_j_new, rv_list_j_new, tv_j_new);
                    previewing_observationQty(viewHolder_k, mAdapter_k, rv_list_k, tv_k);

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    alertDialog.setTitle("FINAL SUBMIT \n check entries before submitting data");
                    View convertView = inflater.inflate(R.layout.layout_preview_list, null);
                    alertDialog.setView(convertView);
                    alertDialog.setPositiveButton(getString(R.string.submit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            handle_submit_btn(model.getDate_of_insp());
                        }
                    });

                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });


                    ListView lv = convertView.findViewById(R.id.lv_preview);
                    lv.setClickable(false);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, inspectionList);
                    lv.setAdapter(adapter);
                    alertDialog.show();

//                }
//                else {
//                    Toasty.warning(mContext, "please fill all Required Fields", Toast.LENGTH_SHORT).show();
//                }
            }
//        }
//}




    private ArrayList<Integer> checkMaxNum() {
        final ArrayList<Integer> maxList = new ArrayList<>();
        maxList.add(mAdapter.getItemCount());
        maxList.add(mAdapter_a.getItemCount());
        maxList.add(mAdapter_b.getItemCount());
        maxList.add(mAdapter_c.getItemCount());
        maxList.add(mAdapter_d.getItemCount());
        maxList.add(mAdapter_e.getItemCount());
        maxList.add(mAdapter_f.getItemCount());
        maxList.add(mAdapter_g.getItemCount());
        maxList.add(mAdapter_h.getItemCount());
        maxList.add(mAdapter_i.getItemCount());
        maxList.add(mAdapter_j.getItemCount());
        maxList.add(mAdapter_h_new.getItemCount());
        maxList.add(mAdapter_j_new.getItemCount());
        maxList.add(mAdapter_k.getItemCount());
        return maxList;
    }

    private void handle_submit_btn(String date) {

        i(TAG, "handle_submit_btn: Name : " + preferences.getString("emp_name", "-") +
                " Code: " + preferences.getString("user_name", "-") +
                " Station: " + preferences.getString("station", "-"));

        final ArrayList<Integer> maxList = checkMaxNum();
        mDatabase = mHelper.getWritableDatabase();


        if (model != null) {
            if (checkUploadedSlipsRecords())
                new AlertDialog.Builder(mContext)
                        .setMessage("Are you sure wanted to submit data?"+"\n\n"+" If you leave any fields blank.It will be treated as intentionally blank")
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(sp_done_hours.getSelectedItem().toString().equals("H"))
                                {
                                    new AlertDialog.Builder(mContext)
                                            .setMessage("Are you sure to be considered as an holiday.?"+"\n\n"+"Day is consider as an holiday and today's planning will be cancelled")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                   // holidayirirn();
                                                    holidayirirnNEW();
                                                    new DbHelper(mContext).updateHolidaySp(date);
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setNegativeButton("No, Let me check again", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();

                                }
                                else
                                {
                                    holidayirirn();
                                }


                              /*  new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getActivity().finish();
                                        startActivity(new Intent(getActivity(), MainActivity.class));
                                    }
                                }, 3000);*/
                            }

                            // added by kiran 22 feb 20202
                            private void holidayirirnNEW()
                            {

                                ContentValues mValues = new ContentValues();
                                mValues.put(DbConstant.Inspection_Entry.INSPECTION_ID, INSPECTION_ID);
                                mValues.put(DbConstant.Inspection_Entry.CUSTOMER_NAEM, model.getCust_name());
                                mValues.put(DbConstant.Inspection_Entry.CONSULTANT_NAEM, model.getConslt_name());
                                mValues.put(DbConstant.Inspection_Entry.PROJECT_VEND, model.getProject_vend());
                                mValues.put(DbConstant.Inspection_Entry.ITEM, model.getItem());
                                mValues.put(DbConstant.Inspection_Entry.BATCH_NO, model.getBatch_no());
                                mValues.put(DbConstant.Inspection_Entry.QUANTITY, model.getQuantity());
                                mValues.put(DbConstant.Inspection_Entry.SPEC_DRAWINGS, model.getSpec_drawings());
                                mValues.put(DbConstant.Inspection_Entry.CODES_STANDARD, model.getCodes_standard());
                                mValues.put(DbConstant.Inspection_Entry.DATE_OF_INSP, model.getDate_of_insp());
                                mValues.put(DbConstant.Inspection_Entry.INSP_TYPE, model.getInsp_type());
                                mValues.put(DbConstant.Inspection_Entry.EMP_CODE, preferences.getString("user_name", null));
                                mValues.put(DbConstant.Inspection_Entry.ICS_REG_NUM, preferences.getString(getString(R.string.ics_reg_num), null));
                                //new
                                mValues.put(DbConstant.Inspection_Entry.SUB_VENDOR_PO_NUMBER, model.getSub_ven_po_number());

                                mValues.put(DbConstant.Inspection_Entry.EMP_STATION, preferences.getString(getString(R.string.station), null));
                                mValues.put(DbConstant.Inspection_Entry.COUNTDOUN, 1); // changgs
                                mValues.put(DbConstant.Inspection_Entry.LOCATION,model.getLocation());
                                mValues.put(DbConstant.Inspection_Entry.DESCRIPTION,model.getDescription());
                                mValues.put(DbConstant.Inspection_Entry.SITEINCHARGE,model.getSiteIncharge());
                                mValues.put(DbConstant.Inspection_Entry.RANGE,model.getRange());
                                mValues.put(DbConstant.Inspection_Entry.PROJECT_TYPE,model.getProject_type());
                                mValues.put(DbConstant.Inspection_Entry.DONE_HOURS,sp_done_hours.getSelectedItem().toString());
                                if(et_extra_hours.getText().toString().equals(""))
                                {
                                    mValues.put(DbConstant.Inspection_Entry.EXTRA_HOURS,"0");
                                }
                                else
                                {
                                    mValues.put(DbConstant.Inspection_Entry.EXTRA_HOURS,et_extra_hours.getText().toString());
                                }
                                if(model.getProject_type().equals("Quantity"))
                                {
                                    mValues.put(DbConstant.Inspection_Entry.NO_OF_JOBS,model.getNo_of_jobs());
                                }
                                else
                                {
                                    mValues.put(DbConstant.Inspection_Entry.NO_OF_JOBS,"");
                                }
                                long id = mDatabase.insert(DbConstant.Inspection_Entry.TABLE_INSPECTION,null,mValues);

                                if (id <= 0) {
                                    Toasty.error(mContext, "Data Insertion Failed", Toast.LENGTH_SHORT, true).show();
                                }
                                insert_A();
                                insert_a();
                                insert_b();
                                insert_c();
                                insert_d();
                                insert_e();
                                insert_f();
                                insert_g();
                                insert_h();
                                insert_i();
                                insert_j();
                                insert_h_new();
                                insert_j_new();
                                insert_k();

                                if (model.getInsp_type().equalsIgnoreCase("final")) {
                                    if (sendBalQty())
                                        insertIRIRN();
                                    Toasty.info(mContext, "Data Submitted successfully", Toast.LENGTH_SHORT).show();

                                    try {
                                        getActivity().finish();
                                        mContext.startActivity(new Intent(getActivity(), MainActivity.class));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (model.getInsp_type().equalsIgnoreCase("Stage")) {
                                    insertIRIRN();
                                    Toasty.info(mContext, "Data Submitted successfully", Toast.LENGTH_LONG).show();
                                    try {
                                        getActivity().finish();
                                        mContext.startActivity(new Intent(getActivity(), MainActivity.class));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            }



                            private void holidayirirn() {
                                int max = maxList.get(0);
                                for (Integer maxNumber : maxList) {
                                    if (maxNumber > max)
                                        max = maxNumber;
                                }
                                i(TAG, "onClick: Maximum number: " + max);

                                for (int i = 1; i <= max; i++)
                                    try {
                                        if (model != null) {
                                            ContentValues mValues = new ContentValues();
                                            mValues.put(DbConstant.Inspection_Entry.INSPECTION_ID, INSPECTION_ID);
                                            mValues.put(DbConstant.Inspection_Entry.CUSTOMER_NAEM, model.getCust_name());
                                            mValues.put(DbConstant.Inspection_Entry.CONSULTANT_NAEM, model.getConslt_name());
                                            mValues.put(DbConstant.Inspection_Entry.PROJECT_VEND, model.getProject_vend());
                                            mValues.put(DbConstant.Inspection_Entry.ITEM, model.getItem());
                                            mValues.put(DbConstant.Inspection_Entry.BATCH_NO, model.getBatch_no());
                                            mValues.put(DbConstant.Inspection_Entry.QUANTITY, model.getQuantity());
                                            mValues.put(DbConstant.Inspection_Entry.SPEC_DRAWINGS, model.getSpec_drawings());
                                            mValues.put(DbConstant.Inspection_Entry.CODES_STANDARD, model.getCodes_standard());
                                            mValues.put(DbConstant.Inspection_Entry.DATE_OF_INSP, model.getDate_of_insp());
                                            mValues.put(DbConstant.Inspection_Entry.INSP_TYPE, model.getInsp_type());
                                            mValues.put(DbConstant.Inspection_Entry.EMP_CODE, preferences.getString("user_name", null));
                                            mValues.put(DbConstant.Inspection_Entry.ICS_REG_NUM, preferences.getString(getString(R.string.ics_reg_num), null));
                                            //new
                                            mValues.put(DbConstant.Inspection_Entry.SUB_VENDOR_PO_NUMBER, model.getSub_ven_po_number());

                                            mValues.put(DbConstant.Inspection_Entry.EMP_STATION, preferences.getString(getString(R.string.station), null));
                                            mValues.put(DbConstant.Inspection_Entry.COUNTDOUN, String.valueOf(i));
                                            mValues.put(DbConstant.Inspection_Entry.LOCATION,model.getLocation());
                                            mValues.put(DbConstant.Inspection_Entry.DESCRIPTION,model.getDescription());
                                            mValues.put(DbConstant.Inspection_Entry.SITEINCHARGE,model.getSiteIncharge());
                                            mValues.put(DbConstant.Inspection_Entry.RANGE,model.getRange());
                                            mValues.put(DbConstant.Inspection_Entry.PROJECT_TYPE,model.getProject_type());
                                            mValues.put(DbConstant.Inspection_Entry.DONE_HOURS,sp_done_hours.getSelectedItem().toString());
                                            if(et_extra_hours.getText().toString().equals(""))
                                            {
                                                mValues.put(DbConstant.Inspection_Entry.EXTRA_HOURS,"0");
                                            }
                                            else
                                            {
                                                mValues.put(DbConstant.Inspection_Entry.EXTRA_HOURS,et_extra_hours.getText().toString());
                                            }
                                            if(model.getProject_type().equals("Quantity"))
                                            {
                                                mValues.put(DbConstant.Inspection_Entry.NO_OF_JOBS,model.getNo_of_jobs());
                                            }
                                            else
                                            {
                                                mValues.put(DbConstant.Inspection_Entry.NO_OF_JOBS,"");
                                            }
                                            long id = mDatabase.insert(DbConstant.Inspection_Entry.TABLE_INSPECTION,null,mValues);

                                            if (id <= 0) {
                                                Toasty.error(mContext, "Data Insertion Failed", Toast.LENGTH_SHORT, true).show();
                                            }
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                insert_A();
                                insert_a();
                                insert_b();
                                insert_c();
                                insert_d();
                                insert_e();
                                insert_f();
                                insert_g();
                                insert_h();
                                insert_i();
                                insert_j();
                                insert_h_new();
                                insert_j_new();
                                insert_k();

                                if (model.getInsp_type().equalsIgnoreCase("final")) {
                                    if (sendBalQty())
                                        insertIRIRN();
                                        Toasty.info(mContext, "Data Submitted successfully", Toast.LENGTH_SHORT).show();

                                    try {
                                        getActivity().finish();
                                        mContext.startActivity(new Intent(getActivity(), MainActivity.class));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (model.getInsp_type().equalsIgnoreCase("Stage")) {
                                    insertIRIRN();
                                    Toasty.info(mContext, "Data Submitted successfully", Toast.LENGTH_LONG).show();
                                    try {
                                        getActivity().finish();
                                        mContext.startActivity(new Intent(getActivity(), MainActivity.class));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("No, Let me check again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();


        } else {
            i(TAG, "handle_submit_btn: Null model");
        }
    }



    // Sending calculated balance qty
    private boolean sendBalQty() {
        final ArrayList<String> checkQtyList = new ArrayList<>();
        Common.disableDialog(mDialog);
        if (mDataArrayList != null && mDataArrayList.size() > 0) {
            for (int i = 0; i < mDataArrayList.size(); i++) {
                i(TAG, "sendBalQty: Balance:" + mDataArrayList.get(i).getBalQty()
                        + "  PoNum: " + model.getPo_number() + " reg_num: " + model.getIcs_reg_num()
                        + " Description: " + mDataArrayList.get(i).getDescription());

                new DbHelper(mContext).updateFinalIRIrnQty(mDataArrayList.get(i).getBalQty(),
                        model.getPo_number(),
                        model.getIcs_reg_num(),
                        mDataArrayList.get(i).getDescription(),
                        preferences.getString(getString(R.string.report_no), "-"),
                        Common.PO_NO,
                        Common.VENDOR_NAME,
                        model.getDate_of_insp(),
                        preferences.getString("emp_name", "-"),
                        preferences.getString("user_name", "-"),
                        preferences.getString("station", "-"),
                        mDataArrayList.get(i).getRelsQty(),
                        mDataArrayList.get(i).getRejectedQty(),
                        mDataArrayList.get(i).getInspQty()
                );
            }
        } else {
            i(TAG, "sendBalQty: Null List found");
            return false;
        }
        return true;


    }

    private void insert_j() {

        preferences = mContext.getSharedPreferences(getString(R.string.user), MODE_PRIVATE);
        for (int i = 1; i <= mAdapter_j.getItemCount(); i++) {
            viewHolder_j = (FindingObjAdapter.MyViewHolder) rv_list_j.findViewHolderForAdapterPosition(i - 1);
            if ((viewHolder_j != null ? viewHolder_j.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_j.btn_single_add.getTag());
                try {
                    if (model != null) {
                        /*ContentValues mValues = new ContentValues();
                        mValues.put(DbConstant.Inspection_Entry.ANNEXURE_A, tv_j.getText().toString());
                        mValues.put(DbConstant.Inspection_Entry.Identifation, viewHolder_j.et_single_obs.getText().toString());
                        mValues.put(DbConstant.Inspection_Entry.Identifation_file, btnTag);

                        long id = mDatabase.updateWithOnConflict(DbConstant.Inspection_Entry.TABLE_INSPECTION, mValues,
                                DbConstant.Inspection_Entry.INSPECTION_ID + "=? AND count_down=?",
                                new String[]{INSPECTION_ID, String.valueOf(i)}, SQLiteDatabase.CONFLICT_REPLACE);*/

                        String btnTag = viewHolder_j.btn_single_add.getTag() == null ? "NA" : viewHolder_j.btn_single_add.getTag().toString();
                        long id = new DbHelper(mContext).insertImagePath(
                                tv_j.getText().toString(),
                                DbConstant.Inspection_Entry.Identifation,
                                viewHolder_j.et_single_obs.getText().toString(),
                                DbConstant.Inspection_Entry.Identifation_file,
                                btnTag, INSPECTION_ID, i
                        );

                        if (id <= 0) {
                            Toasty.error(mContext, "Data Insertion Failed", Toast.LENGTH_SHORT, true).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                return;
        }
    }
    private void insert_h_new() {

        preferences = mContext.getSharedPreferences(getString(R.string.user), MODE_PRIVATE);
        for (int i = 1; i <= mAdapter_h_new.getItemCount(); i++) {
            viewHolder_h_new = (FindingObjAdapter.MyViewHolder) rv_list_h_new.findViewHolderForAdapterPosition(i - 1);
            if ((viewHolder_h_new != null ? viewHolder_h_new.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_h_new.btn_single_add.getTag());
                try {
                    if (model != null) {
                        /*ContentValues mValues = new ContentValues();
                        mValues.put(DbConstant.Inspection_Entry.ANNEXURE_A, tv_j.getText().toString());
                        mValues.put(DbConstant.Inspection_Entry.Identifation, viewHolder_j.et_single_obs.getText().toString());
                        mValues.put(DbConstant.Inspection_Entry.Identifation_file, btnTag);

                        long id = mDatabase.updateWithOnConflict(DbConstant.Inspection_Entry.TABLE_INSPECTION, mValues,
                                DbConstant.Inspection_Entry.INSPECTION_ID + "=? AND count_down=?",
                                new String[]{INSPECTION_ID, String.valueOf(i)}, SQLiteDatabase.CONFLICT_REPLACE);*/

                        String btnTag = viewHolder_h_new.btn_single_add.getTag() == null ? "NA" : viewHolder_h_new.btn_single_add.getTag().toString();
                        long id = new DbHelper(mContext).insertImagePath(
                                tv_h_new.getText().toString(),
                                DbConstant.Inspection_Entry.InspectionResult,
                                viewHolder_h_new.et_single_obs.getText().toString(),
                                DbConstant.Inspection_Entry.InspectionResultFile,
                                btnTag, INSPECTION_ID, i
                        );

                        if (id <= 0) {
                            Toasty.error(mContext, "Data Insertion Failed", Toast.LENGTH_SHORT, true).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                return;
        }
    }
    private void insert_j_new() {

        preferences = mContext.getSharedPreferences(getString(R.string.user), MODE_PRIVATE);
        for (int i = 1; i <= mAdapter_j_new.getItemCount(); i++) {
            viewHolder_j_new = (FindingObjAdapter.MyViewHolder) rv_list_j_new.findViewHolderForAdapterPosition(i - 1);
            if ((viewHolder_j_new != null ? viewHolder_j_new.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_j_new.btn_single_add.getTag());
                try {
                    if (model != null) {
                        /*ContentValues mValues = new ContentValues();
                        mValues.put(DbConstant.Inspection_Entry.ANNEXURE_A, tv_j.getText().toString());
                        mValues.put(DbConstant.Inspection_Entry.Identifation, viewHolder_j.et_single_obs.getText().toString());
                        mValues.put(DbConstant.Inspection_Entry.Identifation_file, btnTag);

                        long id = mDatabase.updateWithOnConflict(DbConstant.Inspection_Entry.TABLE_INSPECTION, mValues,
                                DbConstant.Inspection_Entry.INSPECTION_ID + "=? AND count_down=?",
                                new String[]{INSPECTION_ID, String.valueOf(i)}, SQLiteDatabase.CONFLICT_REPLACE);*/

                        String btnTag = viewHolder_j_new.btn_single_add.getTag() == null ? "NA" : viewHolder_j_new.btn_single_add.getTag().toString();
                        long id = new DbHelper(mContext).insertImagePath(
                                tv_j_new.getText().toString(),
                                DbConstant.Inspection_Entry.Photograph,
                                viewHolder_j_new.et_single_obs.getText().toString(),
                                DbConstant.Inspection_Entry.Photograph_file,
                                btnTag, INSPECTION_ID, i
                        );

                        if (id <= 0) {
                            Toasty.error(mContext, "Data Insertion Failed", Toast.LENGTH_SHORT, true).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                return;
        }
    }
    private void insert_k() {

        preferences = mContext.getSharedPreferences(getString(R.string.user), MODE_PRIVATE);
        for (int i = 1; i <= mAdapter_k.getItemCount(); i++) {
            viewHolder_k = (FindingObjAdapter.MyViewHolder) rv_list_k.findViewHolderForAdapterPosition(i - 1);
            if ((viewHolder_k != null ? viewHolder_k.et_qty_qty.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_k.btn_single_add.getTag());
                try {
                    if (model != null) {

                        String btnTag = viewHolder_k.btn_single_add.getTag() == null ? "NA" : viewHolder_k.btn_single_add.getTag().toString();
                        long id = new DbHelper(mContext).insertImagePathQTY(
                                tv_k.getText().toString(),
                                DbConstant.Inspection_Entry.QUANTITY_DESC,
                                viewHolder_k.spinner_qty_desc.getSelectedItem().toString(),
                                DbConstant.Inspection_Entry.QUANTITY_QTY,
                                viewHolder_k.et_qty_qty.getText().toString(),
                                DbConstant.Inspection_Entry.QUANTITY_UNIT,
                                viewHolder_k.spinner_qty_unit.getText().toString(),
                                DbConstant.Inspection_Entry.QUANTITY_FILE,
                                btnTag, INSPECTION_ID, i
                        );

                        if (id <= 0) {
                            Toasty.error(mContext, "Data Insertion Failed", Toast.LENGTH_SHORT, true).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                return;
        }
    }

    private void insert_i() {


        preferences = mContext.getSharedPreferences(getString(R.string.user), MODE_PRIVATE);
        for (int i = 1; i <= mAdapter_i.getItemCount(); i++) {
            viewHolder_i = (FindingObjAdapter.MyViewHolder) rv_list_i.findViewHolderForAdapterPosition(i - 1);
            if ((viewHolder_i != null ? viewHolder_i.et_single_obs.getText().length() : 0) > 0) {

                i(TAG, "TAG: " + viewHolder_i.btn_single_add.getTag());
                try {
                    if (model != null) {

                        String btnTag = viewHolder_i.btn_single_add.getTag() == null ? "NA" : viewHolder_i.btn_single_add.getTag().toString();
                        long id = new DbHelper(mContext).insertImagePath(
                                tv_i.getText().toString(),
                                DbConstant.Inspection_Entry.Deviation,
                                viewHolder_i.et_single_obs.getText().toString(),
                                DbConstant.Inspection_Entry.Deviation_file,
                                btnTag, INSPECTION_ID, i
                        );

                        if (id <= 0) {
                            Toasty.error(mContext, "Data Insertion Failed", Toast.LENGTH_SHORT, true).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                return;
           /* if (viewHolder_i.btn_single_add.getText().toString().equalsIgnoreCase("uploaded")) {
             }*/

        }

    }

    private void insert_h() {


        preferences = mContext.getSharedPreferences(getString(R.string.user), MODE_PRIVATE);
        for (int i = 1; i <= mAdapter_h.getItemCount(); i++) {
            viewHolder_h = (FindingObjAdapter.MyViewHolder) rv_list_h.findViewHolderForAdapterPosition(i - 1);
            if ((viewHolder_h != null ? viewHolder_h.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_h.btn_single_add.getTag());
                try {
                    if (model != null) {

                        String btnTag = viewHolder_h.btn_single_add.getTag() == null ? "NA" : viewHolder_h.btn_single_add.getTag().toString();
                        long id = new DbHelper(mContext).insertImagePath(
                                tv_h.getText().toString(),
                                DbConstant.Inspection_Entry.Other,
                                viewHolder_h.et_single_obs.getText().toString(),
                                DbConstant.Inspection_Entry.Other_file,
                                btnTag, INSPECTION_ID, i
                        );
                        if (id <= 0)
                            Toasty.error(mContext, "Data Insertion Failed", Toast.LENGTH_SHORT, true).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                return;
        }
    }

    private void insert_g() {

        preferences = mContext.getSharedPreferences(getString(R.string.user), MODE_PRIVATE);
        for (int i = 1; i <= mAdapter_g.getItemCount(); i++) {
            viewHolder_g = (FindingObjAdapter.MyViewHolder) rv_list_g.findViewHolderForAdapterPosition(i - 1);

            if ((viewHolder_g != null ? viewHolder_g.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_g.btn_single_add.getTag());
                try {

                    mDatabase = mHelper.getWritableDatabase();
                    if (model != null) {
                        /*ContentValues mValues = new ContentValues();
                        mValues.put(DbConstant.Inspection_Entry.ANNEXURE_A, tv_g.getText().toString());
                        mValues.put(DbConstant.Inspection_Entry.TestWitness, viewHolder_g.et_single_obs.getText().toString());
                        String btnTag = viewHolder_g.btn_single_add.getTag() == null ? "NA" : viewHolder_g.btn_single_add.getTag().toString();
                        mValues.put(DbConstant.Inspection_Entry.TestWitness_File, btnTag);
                        long id = mDatabase.updateWithOnConflict(DbConstant.Inspection_Entry.TABLE_INSPECTION, mValues,
                                DbConstant.Inspection_Entry.INSPECTION_ID + "=? AND count_down=?",
                                new String[]{INSPECTION_ID, String.valueOf(i)}, SQLiteDatabase.CONFLICT_REPLACE);*/

                        String btnTag = viewHolder_g.btn_single_add.getTag() == null ? "NA" : viewHolder_g.btn_single_add.getTag().toString();
                        long id = new DbHelper(mContext).insertImagePath(
                                tv_g.getText().toString(),
                                DbConstant.Inspection_Entry.TestWitness,
                                viewHolder_g.et_single_obs.getText().toString(),
                                DbConstant.Inspection_Entry.TestWitness_File,
                                btnTag, INSPECTION_ID, i
                        );

                        if (id <= 0)
                            Toasty.error(mContext, "Data Insertion Failed", Toast.LENGTH_SHORT, true).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                return;

        }

    }

    private void insert_f() {


        preferences = mContext.getSharedPreferences(getString(R.string.user), MODE_PRIVATE);
        for (int i = 1; i <= mAdapter_f.getItemCount(); i++) {
            viewHolder_f = (FindingObjAdapter.MyViewHolder) rv_list_f.findViewHolderForAdapterPosition(i - 1);
            if ((viewHolder_f != null ? viewHolder_f.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_f.btn_single_add.getTag());
                try {

                    mDatabase = mHelper.getWritableDatabase();
                    if (model != null) {
                        /*ContentValues mValues = new ContentValues();
                        mValues.put(DbConstant.Inspection_Entry.ANNEXURE_A, tv_f.getText().toString());
                        mValues.put(DbConstant.Inspection_Entry.ReportsR, viewHolder_f.et_single_obs.getText().toString());
                        mValues.put(DbConstant.Inspection_Entry.ReportsR_file, btnTag);
                        long id = mDatabase.updateWithOnConflict(DbConstant.Inspection_Entry.TABLE_INSPECTION, mValues,
                                DbConstant.Inspection_Entry.INSPECTION_ID + "=? AND count_down=?",
                                new String[]{INSPECTION_ID, String.valueOf(i)}, SQLiteDatabase.CONFLICT_REPLACE);*/

                        String btnTag = viewHolder_f.btn_single_add.getTag() == null ? "NA" : viewHolder_f.btn_single_add.getTag().toString();
                        long id = new DbHelper(mContext).insertImagePath(
                                tv_f.getText().toString(),
                                DbConstant.Inspection_Entry.ReportsR,
                                viewHolder_f.et_single_obs.getText().toString(),
                                DbConstant.Inspection_Entry.ReportsR_file,
                                btnTag, INSPECTION_ID, i
                        );

                        if (id <= 0)
                            Toasty.error(mContext, "Data Insertion Failed", Toast.LENGTH_SHORT, true).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                return;
        }
    }

    private void insert_e() {


        preferences = mContext.getSharedPreferences(getString(R.string.user), MODE_PRIVATE);
        for (int i = 1; i <= mAdapter_e.getItemCount(); i++) {
            viewHolder_e = (FindingObjAdapter.MyViewHolder) rv_list_e.findViewHolderForAdapterPosition(i - 1);

            if ((viewHolder_e != null ? viewHolder_e.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_e.btn_single_add.getTag());
                try {
                    if (model != null) {

                        String btnTag = viewHolder_e.btn_single_add.getTag() == null ? "NA" : viewHolder_e.btn_single_add.getTag().toString();
                        long id = new DbHelper(mContext).insertImagePath(
                                tv_e.getText().toString(),
                                DbConstant.Inspection_Entry.Calibration,
                                viewHolder_e.et_single_obs.getText().toString(),
                                DbConstant.Inspection_Entry.Calibration_file,
                                btnTag, INSPECTION_ID, i
                        );

                        if (id <= 0)
                            Toasty.error(mContext, "Data Insertion Failed", Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                return;
        }

    }

    private void insert_d() {

        preferences = mContext.getSharedPreferences(getString(R.string.user), MODE_PRIVATE);
        for (int i = 1; i <= mAdapter_d.getItemCount(); i++) {
            viewHolder_d = (FindingObjAdapter.MyViewHolder) rv_list_d.findViewHolderForAdapterPosition(i - 1);

            if ((viewHolder_d != null ? viewHolder_d.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_d.btn_single_add.getTag());
                try {

                    if (model != null) {
                        /*ContentValues mValues = new ContentValues();
                        mValues.put(DbConstant.Inspection_Entry.ANNEXURE_A, tv_d.getText().toString());
                        mValues.put(DbConstant.Inspection_Entry.TEST_B, viewHolder_d.et_single_obs.getText().toString());
                        mValues.put(DbConstant.Inspection_Entry.TEST_B_FILE, btnTag);
                        long id = mDatabase.updateWithOnConflict(DbConstant.Inspection_Entry.TABLE_INSPECTION, mValues,
                                DbConstant.Inspection_Entry.INSPECTION_ID + "=? AND count_down=?",
                                new String[]{INSPECTION_ID, String.valueOf(i)}, SQLiteDatabase.CONFLICT_REPLACE);*/

                        String btnTag = viewHolder_d.btn_single_add.getTag() == null ? "NA" :
                                viewHolder_d.btn_single_add.getTag().toString();
                        long id = new DbHelper(mContext).insertImagePath(
                                tv_d.getText().toString(),
                                DbConstant.Inspection_Entry.TEST_B,
                                viewHolder_d.et_single_obs.getText().toString(),
                                DbConstant.Inspection_Entry.TEST_B_FILE,
                                btnTag, INSPECTION_ID, i
                        );


                        if (id <= 0)
                            Toasty.error(mContext, "Data Insertion Failed", Toast.LENGTH_SHORT, true).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return;


        }
    }

    private void insert_c() {

        preferences = mContext.getSharedPreferences(getString(R.string.user), MODE_PRIVATE);
        for (int i = 1; i <= mAdapter_c.getItemCount(); i++) {
            viewHolder_c = (FindingObjAdapter.MyViewHolder) rv_list_c.findViewHolderForAdapterPosition(i - 1);

            if ((viewHolder_c != null ? viewHolder_c.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_c.btn_single_add.getTag());
                try {
                    if (model != null) {
                        /*ContentValues mValues = new ContentValues();
                        mValues.put(DbConstant.Inspection_Entry.ANNEXURE_A, tv_c.getText().toString());
                        mValues.put(DbConstant.Inspection_Entry.TEST_A, viewHolder_c.et_single_obs.getText().toString());
                        mValues.put(DbConstant.Inspection_Entry.TEST_A_FILE, btnTag);
                        long id = mDatabase.updateWithOnConflict(DbConstant.Inspection_Entry.TABLE_INSPECTION, mValues,
                                DbConstant.Inspection_Entry.INSPECTION_ID + "=? AND count_down=?",
                                new String[]{INSPECTION_ID, String.valueOf(i)}, SQLiteDatabase.CONFLICT_REPLACE);*/

                        String btnTag = viewHolder_c.btn_single_add.getTag() == null ? "NA" : viewHolder_c.btn_single_add.getTag().toString();
                        long id = new DbHelper(mContext).insertImagePath(
                                tv_c.getText().toString(),
                                DbConstant.Inspection_Entry.TEST_A,
                                viewHolder_c.et_single_obs.getText().toString(),
                                DbConstant.Inspection_Entry.TEST_A_FILE,
                                btnTag, INSPECTION_ID, i
                        );

                        if (id <= 0)
                            Toasty.error(mContext, "Data Insertion Failed", Toast.LENGTH_SHORT, true).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else return;
        }
    }

    private void insert_b() {
        preferences = mContext.getSharedPreferences(getString(R.string.user), MODE_PRIVATE);
        for (int i = 1; i <= mAdapter_b.getItemCount(); i++) {
            viewHolder_b = (FindingObjAdapter.MyViewHolder) rv_list_b.findViewHolderForAdapterPosition(i - 1);

            if ((viewHolder_b != null ? viewHolder_b.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_b.btn_single_add.getTag());

                try {


                    mDatabase = mHelper.getWritableDatabase();
                    if (model != null) {
                        /*ContentValues mValues = new ContentValues();
                        mValues.put(DbConstant.Inspection_Entry.ANNEXURE_A, tv_b.getText().toString());
                        mValues.put(DbConstant.Inspection_Entry.Dimensional, viewHolder_b.et_single_obs.getText().toString());
                        mValues.put(DbConstant.Inspection_Entry.Dimensional_file, btnTag);
                        long id = mDatabase.updateWithOnConflict(DbConstant.Inspection_Entry.TABLE_INSPECTION, mValues,
                                DbConstant.Inspection_Entry.INSPECTION_ID + "=? AND count_down=?",
                                new String[]{INSPECTION_ID, String.valueOf(i)}, SQLiteDatabase.CONFLICT_REPLACE);*/

                        String btnTag = viewHolder_b.btn_single_add.getTag() == null ? "NA" : viewHolder_b.btn_single_add.getTag().toString();
                        long id = new DbHelper(mContext).insertImagePath(
                                tv_b.getText().toString(),
                                DbConstant.Inspection_Entry.Dimensional,
                                viewHolder_b.et_single_obs.getText().toString(),
                                DbConstant.Inspection_Entry.Dimensional_file,
                                btnTag, INSPECTION_ID, i
                        );


                        if (id <= 0)
                            Toasty.error(mContext, "Data Insertion Failed", Toast.LENGTH_SHORT, true).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                return;
        }
    }

    private void insert_a() {

        preferences = mContext.getSharedPreferences(getString(R.string.user), MODE_PRIVATE);
        for (int i = 1; i <= mAdapter_a.getItemCount(); i++) {
            viewHolder_a = (FindingObjAdapter.MyViewHolder) rv_list_a.findViewHolderForAdapterPosition(i - 1);

            if ((viewHolder_a != null ? viewHolder_a.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder_a.btn_single_add.getTag());

                try {

                    mDatabase = mHelper.getWritableDatabase();
                    if (model != null) {
                        /*ContentValues mValues = new ContentValues();
                        mValues.put(DbConstant.Inspection_Entry.ANNEXURE_A, tv_a.getText().toString());
                        mValues.put(DbConstant.Inspection_Entry.VISUALB, viewHolder_a.et_single_obs.getText().toString());
                        mValues.put(DbConstant.Inspection_Entry.VISUALB_FILE, btnTag);
                        long id = mDatabase.updateWithOnConflict(DbConstant.Inspection_Entry.TABLE_INSPECTION, mValues,
                                DbConstant.Inspection_Entry.INSPECTION_ID + "=? AND count_down=?",
                                new String[]{INSPECTION_ID, String.valueOf(i)}, SQLiteDatabase.CONFLICT_REPLACE);*/

                        String btnTag = viewHolder_a.btn_single_add.getTag() == null ? "NA" : viewHolder_a.btn_single_add.getTag().toString();
                        long id = new DbHelper(mContext).insertImagePath(
                                tv_a.getText().toString(),
                                DbConstant.Inspection_Entry.VISUALB,
                                viewHolder_a.et_single_obs.getText().toString(),
                                DbConstant.Inspection_Entry.VISUALB_FILE,
                                btnTag, INSPECTION_ID, i
                        );

                        if (id <= 0)
                            Toasty.error(mContext, "Data Insertion Failed", Toast.LENGTH_SHORT, true).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                return;

        }
    }

    private void insert_A() {

     // added 22 feb2020
       if(sp_done_hours.getSelectedItem().toString().equals("H"))
       {
           long id1 = new DbHelper(mContext).insertImagePath(
                   tv_A.getText().toString(),
                   DbConstant.Inspection_Entry.VISUALA,
                   "nil",
                   DbConstant.Inspection_Entry.VISUALA_FILE,
                   "NA", INSPECTION_ID, 1
           );
       }

// till here

        for (int i = 1; i <= mAdapter.getItemCount(); i++) {
            viewHolder = (FindingObjAdapter.MyViewHolder) rv_list.findViewHolderForAdapterPosition(i - 1);
            if ((viewHolder != null ? viewHolder.et_single_obs.getText().length() : 0) > 0) {
                i(TAG, "TAG: " + viewHolder.btn_single_add.getTag());

                try {

                    String btnTag = viewHolder.btn_single_add.getTag() == null ? "NA" : viewHolder.btn_single_add.getTag().toString();
                    long id = new DbHelper(mContext).insertImagePath(
                            tv_A.getText().toString(),
                            DbConstant.Inspection_Entry.VISUALA,
                            viewHolder.et_single_obs.getText().toString(),
                            DbConstant.Inspection_Entry.VISUALA_FILE,
                            btnTag, INSPECTION_ID, i
                    );
                    if (id <= 0)
                        Toasty.error(mContext, "Data Insertion Failed", Toast.LENGTH_SHORT, true).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return;
        }
    }

    // preview observation
    private void previewing_observation(FindingObjAdapter.MyViewHolder viewHolder, FindingObjAdapter mAdapter, RecyclerView rv, TextView textView) {


        for (int i = 1; i <= mAdapter.getItemCount(); i++) {
            viewHolder = (FindingObjAdapter.MyViewHolder) rv.findViewHolderForAdapterPosition(i - 1);
            if (viewHolder != null) {
                String mStringBuilder;
                if (viewHolder.et_single_obs.getText().length() > 0) {
                    try {
                         mStringBuilder= textView.getText().toString() +
                                "\n" +
                                "Description: " +
                                viewHolder.et_single_obs.getText().toString() +
                                "\n" +
                                "Photos: " + (viewHolder.btn_single_add.getTag() == null ? "Not Uploaded" : "Uploaded");

                        inspectionList.add(mStringBuilder);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    return;
            }
        }
    }
    private void previewing_observationQty(FindingObjAdapter.MyViewHolder viewHolder, FindingObjAdapter mAdapter, RecyclerView rv, TextView textView) {


        for (int i = 1; i <= mAdapter.getItemCount(); i++) {
            viewHolder = (FindingObjAdapter.MyViewHolder) rv.findViewHolderForAdapterPosition(i - 1);
            if (viewHolder != null) {
                String mStringBuilder;
                if (viewHolder.et_qty_qty.getText().length() > 0) {
                    try {
                        mStringBuilder= textView.getText().toString() +
                                "\n" +
                                "Description: " +
                                viewHolder.spinner_qty_desc.getSelectedItem().toString() +
                                "\n" +
                                "Quantity: " +
                                viewHolder.et_qty_qty.getText().toString() +
                                viewHolder.spinner_qty_unit.getText().toString() +
                                "\n" +
                                "Photos: " + (viewHolder.btn_single_add.getTag() == null ? "Not Uploaded" : "Uploaded");

                        inspectionList.add(mStringBuilder);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    return;
            }
        }
    }
    private void prepareMasterList(int num, ArrayList<Integer> listofrows) {
        listofrows.clear();
        for (int i = 0; i < num; i++) {
            listofrows.add(i + 1);
        }
    }

    public boolean checkETNotNull(EditText et) {
        return !et.getText().toString().equalsIgnoreCase("");
    }

    @Override
    public void clickListener(int position, View mvView) {

        i(TAG, "Clicked at position: " + position);
        cameraButton = (Button) mvView;
        //cameraButton.setTag(position);
   // added 18feb2020 by kiran

        final CharSequence[] options = {"Take Photo From Camera", "Choose From Gallery","Cancel"};
       AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo From Camera")) {
                    dialog.dismiss();
                    startCamera(IMAGE_CAPTURE_REQUEST);
                } else if (options[item].equals("Choose From Gallery")) {
                    dialog.dismiss();
                    galleryimage(GALLERY);
                   // Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                  //  startActivityForResult(galleryIntent, GALLERY);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


    }
private void galleryimage(int requestCode)
{
    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
     startActivityForResult(galleryIntent, requestCode);
}

    private void startCamera(int requestCode) {

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ICS INSPECTION/"; // /inspectionfolder /ICS INSPECTION/
        File newdir = new File(dir);
        newdir.mkdirs();
        String file = dir + "Inspection_" + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString() + ".jpg";


        imageBtnFile = new File(file);
        try {
            imageBtnFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //  imageToUploadUri = Uri.fromFile(imageBtnFile);

        imageToUploadUri = FileProvider.getUriForFile(mContext,
                BuildConfig.APPLICATION_ID + ".provider", imageBtnFile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUploadUri);
        if ( Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP ) {
            cameraIntent.setClipData( ClipData.newRawUri( "", imageToUploadUri ) );
            cameraIntent.addFlags( Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION );
        }


        startActivityForResult(cameraIntent, requestCode);
    }




    // Insert d data
    public boolean CheckANdSave_d() {

        int count_empty_text = 0;
        if (rv_list_d != null) {

            for (int i = 0; i < mAdapter_d.getItemCount(); i++) {
                viewHolder_d = (FindingObjAdapter.MyViewHolder) rv_list_d.findViewHolderForAdapterPosition(i);
                try {
                    if (viewHolder_d.et_single_obs.getText().toString().length() <= 0) {

                        count_empty_text++;

                        Toasty.error(mContext, getString(R.string.fill_all_details), Toast.LENGTH_SHORT, true).show();
                        i(TAG, "returning: " + count_empty_text);
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (count_empty_text == 0) {

                preferences = mContext.getSharedPreferences(getString(R.string.user), MODE_PRIVATE);
                for (int i = 0; i < mAdapter_d.getItemCount(); i++) {
                    viewHolder_d = (FindingObjAdapter.MyViewHolder) rv_list_d.findViewHolderForAdapterPosition(i);
                    if (viewHolder_d.et_single_obs.getText().toString().length() > 0) {

                        i(TAG, "TAG: " + viewHolder_d.btn_single_add.getTag());
                        return true;
                    }

                }

            }
        }
        return false;
    }


    // Insert e data
    public boolean CheckANdSave_e() {

        int count_empty_text = 0;

        if (rv_list_e != null) {

            for (int i = 0; i < mAdapter_e.getItemCount(); i++) {
                viewHolder_e = (FindingObjAdapter.MyViewHolder) rv_list_e.findViewHolderForAdapterPosition(i);
                try {
                    if (viewHolder_e.et_single_obs.getText().toString().length() <= 0) {

                        count_empty_text++;

                        Toasty.error(mContext, getString(R.string.fill_all_details), Toast.LENGTH_SHORT, true).show();
                        i(TAG, "returning: " + count_empty_text);
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (count_empty_text == 0) {

                preferences = mContext.getSharedPreferences(getString(R.string.user), MODE_PRIVATE);
                for (int i = 0; i < mAdapter_e.getItemCount(); i++) {
                    viewHolder_e = (FindingObjAdapter.MyViewHolder) rv_list_e.findViewHolderForAdapterPosition(i);
                    if (viewHolder_e.et_single_obs.getText().toString().length() > 0) {

                        i(TAG, "TAG: " + viewHolder_e.btn_single_add.getTag());
                        return true;
                    }

                }

            }
        }
        return false;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case IMAGE_CAPTURE_REQUEST:
                imageButtonClickHandler(resultCode, cameraButton);
                break;
                //added 18feb2020 by kiran
            case GALLERY:
            {

                if (data != null)
                {
                    Uri contentURI = data.getData();
                    try
                    {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), contentURI);
                        String path = saveImage(bitmap);
                        i(TAG, "$path");
                        cameraButton.setTag(path);
                        try {
                            cameraButton.setText("Uploaded");
                        } catch (Exception e) {
                        e.printStackTrace();
                    }

                        cameraButton.setBackgroundColor(
                            ContextCompat.getColor(
                                    mContext,
                                    android.R.color.holo_green_light
                            )
                    );
                        //   Toast.makeText(this@MainActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
                        //  imageview!!.setImageBitmap(bitmap)

                    }
                    catch (IOException e) {
                    e.printStackTrace();
                    //Toast.makeText(this@MainActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }

                }
                break;
            }

            case PICK_VISIT_SLIP_REQUEST_CODE:

               documentResultHandler(resultCode, data, btn_upload_visit_slip);
                break;

            case PICK_TC_REQUEST_CODE:
                documentResultHandler(resultCode, data, btn_upload_tc);
                break;

            case PICK_OTHER_REQUEST_CODE:
                documentResultHandler(resultCode, data, btn_upload_other_docs);
                break;

            case PICK_STANDARD_REQUEST_CODE:
                documentResultHandler(resultCode, data, btn_upload_standard);
                break;

            case PICK_QAP_REQUEST_CODE:
                documentResultHandler(resultCode, data, btn_upload_qap);
                break;

            case PICK_QAQC_REQUEST_CODE:
                documentResultHandler(resultCode, data, btn_upload_qaqc);
                break;

            case PICK_PO_REQUEST_CODE:
                documentResultHandler(resultCode, data, btn_upload_po);
                break;

            case PICK_CALIB_CERT_REQUEST_CODE:
                documentResultHandler(resultCode, data, btn_upload_calib_cert);
                break;

            //--------------- Imageview Button -------------
            case IMAGE_PICK_VISIT_SLIP_REQUEST_CODE:
                imageButtonClickHandlerListener(resultCode, ibtn_upload_visit_slip);
                break;

            case IMAGE_PICK_TC_REQUEST_CODE:
                imageButtonClickHandlerListener(resultCode, ibtn_upload_tc);

                break;

            case IMAGE_PICK_OTHER_REQUEST_CODE:
                imageButtonClickHandlerListener(resultCode, ibtn_upload_other_docs);
                break;

            case IMAGE_PICK_STANDARD_REQUEST_CODE:
                imageButtonClickHandlerListener(resultCode, ibtn_upload_standard);
                break;

            case IMAGE_PICK_QAP_REQUEST_CODE:
                imageButtonClickHandlerListener(resultCode, ibtn_upload_qap);
                break;

            case IMAGE_PICK_QAQC_REQUEST_CODE:
                imageButtonClickHandlerListener(resultCode, ibtn_upload_qaqc);
                break;

            case IMAGE_PICK_PO_REQUEST_CODE:
                imageButtonClickHandlerListener(resultCode, ibtn_upload_po);
                break;

            case IMAGE_PICK_CALIB_CERT_REQUEST_CODE:
                imageButtonClickHandlerListener(resultCode, ibtn_upload_calib_cert);
                break;
        }
    }
//added18feb2020
    private String saveImage(Bitmap bitmap)
    {
        ByteArrayOutputStream bytes =new  ByteArrayOutputStream ();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
       File wallpaperDirectory = new File(
                (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString());
        if (!wallpaperDirectory.exists())
        {

            wallpaperDirectory.mkdirs();
        }

        try
        {
            Log.d("heel",wallpaperDirectory.toString());
            File f = new File(wallpaperDirectory, ((Calendar.getInstance()
                    .getTimeInMillis())+ ".jpg"));
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(mContext,
                    new String[]{(f.getPath())},
                    new String[]{("image/jpeg")}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        }
        catch ( IOException e) {
        e.printStackTrace();
    }

        return "";
    }

    /**
     * @param resultCode     OnActivityResult resultCode
     * @param imageiewButton Button to set tag,backgroubnd text
     */
    private void imageButtonClickHandler(int resultCode, final Button imageiewButton) {

        if (resultCode == RESULT_OK) {

            i(TAG, "onActivityResult: " + imageToUploadUri.toString());

            try {
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(final Void... params) {

                        String path = compressImage(imageBtnFile.toString());

                        i(TAG, "doInBackground: path: " + path);

                        return path;
                    }

                    @Override
                    protected void onPostExecute(String path) {

                        imageiewButton.setTag(path);
                        try {
                            imageiewButton.setText("Uploaded");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        imageiewButton.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.holo_green_light));

                    }
                }.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d("Employee Pic", "Picture is not saved");

        }
    }

    /**
     * @param resultCode     OnActivityResult resultCode
     * @param imageiewButton Button to set tag,backgroubnd text
     */
    private void imageButtonClickHandlerListener(int resultCode,
                                                 final ImageButton imageiewButton) {

        if (resultCode == RESULT_OK) {

            i(TAG, "onActivityResult: IV Button" + imageToUploadUri.toString());
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(final Void... params) {
                    String path = null;
                    try {
                        path = compressImage(imageBtnFile.toString());

                        i(TAG, "doInBackground: path: " + path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return path;
                }

                @Override
                protected void onPostExecute(String path) {
                    imageiewButton.setTag(path);
                    imageiewButton.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.holo_green_light));
                }
            }.execute();


        } else {
            Log.d("Employee Pic", "Picture is not saved");

        }
    }

    /**
     * Handle File Doc image button clicked Result
     *
     * @param resultCode : onActivityResult resultcode
     * @param data       : onActivityResult intent data
     * @param fileButton : Button where to setting tag, changing background, text
     */
    private void documentResultHandler(int resultCode, Intent data, Button fileButton) {
        if (resultCode == Activity.RESULT_OK && data != null) {

            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                String picturePath = FileManager.getPath(mContext, selectedImage);
                Log.i(TAG, "documentResultHandler: " + picturePath);
                try {
                    File file = new File(picturePath);
                    if (file.exists()) {
                        if (file.length() / 1024 >= 4096) { //15 mb
                            Toast.makeText(mContext, "File Size limit is upto 4MB", Toast.LENGTH_LONG)
                                    .show();
                            return;
                        }
                    }
                }
                catch ( java.lang.Exception e) {
                    e.printStackTrace();
                }
                if (picturePath==null) {
                    //picturePath=FileManager.getFilePath(mContext,selectedImage);
                    showDuplicatePhotoDialog("Upload your pdf from file manager directory");
                } else {
                    System.out.println("picturePath +" + picturePath);  //path of sdcard
                    fileButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.maroonColor));
                    fileButton.setTag(picturePath);
                    fileButton.setText(R.string.uploaded);
                }


            } else Log.i(TAG, "documentResultHandler: No File Found");
        } else
            Toasty.error(mContext, getString(R.string.error_file_executong), Toast.LENGTH_LONG).show();



    }

    // compress images
    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 1500.0f;
        float maxWidth = 2000.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "ICS INSPECTION/");
        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + "ICS_INSPECTION_" + UUID.randomUUID().toString() + "_IMG_" + System.currentTimeMillis() + ".jpg");
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        i(TAG, "getRealPathFromURI: " + contentUri.toString());


        Cursor cursor = mContext.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            //cursor.close();
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                     int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    // Send IRIRN DeclickListenertails
    @SuppressLint("StaticFieldLeak")
    private void insertIRIRN() {


        //= new DbHelper(mContext).getcfrid();

        final Cursor mCursor = mDatabase.rawQuery("select * from inspection order by count_down asc", null);
        String A_v, a_v, b, c, d, e, f, g, h, i, j,h_new,j_new;

        File mFile;
        RequestBody mRequestBody;
        MultipartBody.Part body1, body2, body3, body4, body5, body6, body7, body8, body9,body10,body11;
        final ArrayList<String> resultList = new ArrayList<>();
        String cust_ID = UUID.randomUUID().toString();
        if (mCursor != null && mCursor.getCount() > 0) {
            String tempOther = null, tempDeviation = null, tempIdentif = null;

            // Notification Prompt
            final NotificationCompat.Builder mNotification = new NotificationCompat.Builder(mContext, "Channel_notification_id");
            mNotification.setSmallIcon(R.mipmap.ic_launcher)
                    .setStyle(new NotificationCompat.InboxStyle()
                            .setBigContentTitle("Submitted Inspection Entry result")
                            .setSummaryText("Please Don't Close the application till last " +
                                    "result get's successfully"))
                    .setGroup("All ICS IR_IRN Inspection Report")
                    .setOngoing(true)
                    .setOnlyAlertOnce(false)
                    .setAutoCancel(true)
                    .setProgress(mCursor.getCount(), mCursor.getPosition(), false)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                    .setGroupSummary(true);

            managerCompat.notify(2, mNotification.build());
            Log.v(TAG, DatabaseUtils.dumpCursorToString(mCursor));

            while (mCursor.moveToNext()) {

                cursour_position_count = (mCursor.getPosition() + 1);

                mDatabase = mHelper.getWritableDatabase();
                 String str_cfrid=mHelper.getcfrid(model.getDescription());
                 if(str_cfrid.equals("Select"))
                 {
                     str_cfrid="NA";
                 }
                final String str_proj_vend = mCursor.getString(mCursor.getColumnIndex("project_vend"));
                final String str_po_nm = model.getPo_number();
                final String str_sub_ven_po_num = model.getSub_ven_po_number();
                final String str_client_nm = mCursor.getString(mCursor.getColumnIndex("customer_name"));
                final String str_ics_reg_nm = model.getIcs_reg_num();
                final String str_emp_cd = mCursor.getString(mCursor.getColumnIndex("emp_code"));
                final String str_station = mCursor.getString(mCursor.getColumnIndex("emp_station"));
                final String str_insp_date = mCursor.getString(mCursor.getColumnIndex("date_of_insp"));
                final String str_item_material = mCursor.getString(mCursor.getColumnIndex("item"));
                final String str_quantity = mCursor.getString(mCursor.getColumnIndex("quantity"));
                final String str_spec_drw = mCursor.getString(mCursor.getColumnIndex("spec_drawings"));
                final String str_code_stnd = mCursor.getString(mCursor.getColumnIndex("codes_standard"));
                final String str_empName = preferences.getString("emp_name", "-");
                final String str_batch_no = mCursor.getString(mCursor.getColumnIndex("batch_no"));
                final String str_descNum = mCursor.getString(mCursor.getColumnIndex("count_down"));
                final String str_upload_po = Common.POCOPY;
                final String str_upload_qap = Common.QAPCOPY;
                final String str_upload_stand = Common.STANDARDCOPY;
                final String str_cunsltant_name = Common.CONSULTANT_NAME;
                final String str_insp_type = mCursor.getString(mCursor.getColumnIndex("insp_type"));
                final String str_location=mCursor.getString(mCursor.getColumnIndex(DbConstant.Inspection_Entry.LOCATION));
                final String str_description=mCursor.getString(mCursor.getColumnIndex(DbConstant.Inspection_Entry.DESCRIPTION));
                String str_Siteincharge = mCursor.getString(mCursor.getColumnIndex(DbConstant.Inspection_Entry.SITEINCHARGE));
                if(str_Siteincharge.equals("Select"))
                {
                    str_Siteincharge="NA";
                }
                String str_range=mCursor.getString(mCursor.getColumnIndex(DbConstant.Inspection_Entry.RANGE));
                if(str_range.equals("WITH IN MUNICIPLE LIMIT"))
                {
                    str_range="Within";
                }
                else
                {
                    str_range="Outside";
                }
                final String str_project_type=mCursor.getString(mCursor.getColumnIndex(DbConstant.Inspection_Entry.PROJECT_TYPE));
                String str_no_of_jobs="1";
                if(str_project_type.equals("Quantity"))
                {
                    str_no_of_jobs=mCursor.getString(mCursor.getColumnIndex(DbConstant.Inspection_Entry.NO_OF_JOBS));
                    if(str_no_of_jobs.equals(""))
                    {
                        str_no_of_jobs="1";
                    }
                }
                final String str_done_hours=mCursor.getString(mCursor.getColumnIndex(DbConstant.Inspection_Entry.DONE_HOURS));
                final String str_extra_hours=mCursor.getString(mCursor.getColumnIndex(DbConstant.Inspection_Entry.EXTRA_HOURS));

                if (mCursor.getString(mCursor.getColumnIndex("visualA")) != null) {
                    A_v = mCursor.getString(mCursor.getColumnIndex("visualA"));
                    i(TAG, "check_IR_DATA: visualA" + A_v);
                    if (mCursor.getString(mCursor.getColumnIndex("visualA_file")) != null &&
                            !mCursor.getString(mCursor.getColumnIndex("visualA_file")).equalsIgnoreCase("NA")) {
                        mFile = new File(mCursor.getString(mCursor.getColumnIndex("visualA_file")));
                        mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                        body1 = MultipartBody.Part.createFormData("v_a_file", mFile.getName(), mRequestBody);
                    } else {
                        mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                        body1 = MultipartBody.Part.createFormData("v_a_file", "", mRequestBody);
                    }

                } else {
                    A_v = "";
                    mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                    body1 = MultipartBody.Part.createFormData("v_a_file", "", mRequestBody);
                }

                if (mCursor.getString(mCursor.getColumnIndex("visualB")) != null) {

                    a_v = mCursor.getString(mCursor.getColumnIndex("visualB"));
                    i(TAG, "check_IR_DATA: visualA" + a_v);
                    if (mCursor.getString(mCursor.getColumnIndex("visualB_file")) != null &&
                            !mCursor.getString(mCursor.getColumnIndex("visualB_file")).equalsIgnoreCase("NA")) {
                        mFile = new File(mCursor.getString(mCursor.getColumnIndex("visualB_file")));
                        mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                        body2 = MultipartBody.Part.createFormData("v_b_file", mFile.getName(), mRequestBody);
                    } else {
                        mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                        body2 = MultipartBody.Part.createFormData("v_b_file", "", mRequestBody);
                    }
                } else {
                    a_v = "";
                    mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                    body2 = MultipartBody.Part.createFormData("v_b_file", "", mRequestBody);
                }

                if (mCursor.getString(mCursor.getColumnIndex("Dimensional")) != null) {

                    b = mCursor.getString(mCursor.getColumnIndex("Dimensional"));
                    if (!mCursor.getString(mCursor.getColumnIndex("Dimensional_file")).equalsIgnoreCase("NA") &&
                            mCursor.getString(mCursor.getColumnIndex("Dimensional_file")) != null) {
                        mFile = new File(mCursor.getString(mCursor.getColumnIndex("Dimensional_file")));
                        mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                        body3 = MultipartBody.Part.createFormData("dim_file", mFile.getName(), mRequestBody);
                    } else {
                        mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                        body3 = MultipartBody.Part.createFormData("dim_file", "", mRequestBody);
                    }
                } else {
                    b = "";
                    mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                    body3 = MultipartBody.Part.createFormData("dim_file", "", mRequestBody);
                }

                if (mCursor.getString(mCursor.getColumnIndex("test_a")) != null) {

                    c = mCursor.getString(mCursor.getColumnIndex("test_a"));

                   /* mFile = new File(mCursor.getString(mCursor.getColumnIndex("photo_1")));
                    mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                    body = MultipartBody.Part.createFormData("file", mFile.getName(), mRequestBody);*/
                } else {
                    c = "";  //body = null;
                }

                if (mCursor.getString(mCursor.getColumnIndex("test_b")) != null) {

                    d = mCursor.getString(mCursor.getColumnIndex("test_b"));

                  /*  mFile = new File(mCursor.getString(mCursor.getColumnIndex("photo_1")));
                    mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                    body = MultipartBody.Part.createFormData("file", mFile.getName(), mRequestBody);*/
                } else {
                    d = "";
                    // body = null;
                }


                if (mCursor.getString(mCursor.getColumnIndex("Calibration")) != null) {

                    e = mCursor.getString(mCursor.getColumnIndex("Calibration"));
                    if (!mCursor.getString(mCursor.getColumnIndex("Calibration_file")).equalsIgnoreCase("NA") &&
                            mCursor.getString(mCursor.getColumnIndex("Calibration_file")) != null) {
                        mFile = new File(mCursor.getString(mCursor.getColumnIndex("Calibration_file")));
                        mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                        body4 = MultipartBody.Part.createFormData("calib_file", mFile.getName(), mRequestBody);
                    } else {
                        mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                        body4 = MultipartBody.Part.createFormData("calib_file", "", mRequestBody);
                    }
                } else {
                    e = "";
                    mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                    body4 = MultipartBody.Part.createFormData("calib_file", "", mRequestBody);
                }

                if (mCursor.getString(mCursor.getColumnIndex("ReportsR")) != null) {

                    f = mCursor.getString(mCursor.getColumnIndex("ReportsR"));
                    if (!mCursor.getString(mCursor.getColumnIndex("ReportsR_file")).equalsIgnoreCase("NA") &&
                            mCursor.getString(mCursor.getColumnIndex("ReportsR_file")) != null) {
                        mFile = new File(mCursor.getString(mCursor.getColumnIndex("ReportsR_file")));
                        mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                        body5 = MultipartBody.Part.createFormData("report_r_file", mFile.getName(), mRequestBody);
                    } else {
                        mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                        body5 = MultipartBody.Part.createFormData("report_r_file", "", mRequestBody);
                    }
                } else {
                    f = "";
                    mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                    body5 = MultipartBody.Part.createFormData("report_r_file", "", mRequestBody);
                }


                if (mCursor.getString(mCursor.getColumnIndex("TestWitness")) != null) {

                    g = mCursor.getString(mCursor.getColumnIndex("TestWitness"));
                    if (!mCursor.getString(mCursor.getColumnIndex("TestWitness_File")).equalsIgnoreCase("NA") &&
                            mCursor.getString(mCursor.getColumnIndex("TestWitness_File")) != null) {
                        mFile = new File(mCursor.getString(mCursor.getColumnIndex("TestWitness_File")));
                        mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                        body6 = MultipartBody.Part.createFormData("test_w_file", mFile.getName(), mRequestBody);
                    } else {
                        mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                        body6 = MultipartBody.Part.createFormData("test_w_file", "", mRequestBody);
                    }
                } else {

                    g = "";
                    mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                    body6 = MultipartBody.Part.createFormData("test_w_file", "", mRequestBody);
                }

                if (mCursor.getString(mCursor.getColumnIndex("Other")) != null) {

                    h = mCursor.getString(mCursor.getColumnIndex("Other"));
                    if (!mCursor.getString(mCursor.getColumnIndex("Other_file")).equalsIgnoreCase("NA") &&
                            mCursor.getString(mCursor.getColumnIndex("Other_file")) != null) {
                        mFile = new File(mCursor.getString(mCursor.getColumnIndex("Other_file")));
                        mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                        body9 = MultipartBody.Part.createFormData("Other_file", mFile.getName(), mRequestBody);
                    } else {
                        mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                        body9 = MultipartBody.Part.createFormData("Other_file", "", mRequestBody);
                    }
                } else {

                    h = "";
                    mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                    body9 = MultipartBody.Part.createFormData("Other_file", "", mRequestBody);
                }

                if (mCursor.getString(mCursor.getColumnIndex("Deviation")) != null) {

                    i = mCursor.getString(mCursor.getColumnIndex("Deviation"));
                    if (!mCursor.getString(mCursor.getColumnIndex("Deviation_file")).equalsIgnoreCase("NA") &&
                            mCursor.getString(mCursor.getColumnIndex("Deviation_file")) != null) {
                        mFile = new File(mCursor.getString(mCursor.getColumnIndex("Deviation_file")));
                        mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                        body10 = MultipartBody.Part.createFormData("Deviation_file", mFile.getName(), mRequestBody);
                    } else {
                        mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                        body10 = MultipartBody.Part.createFormData("Deviation_file", "", mRequestBody);
                    }
                } else {

                    i = "";
                    mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                    body10 = MultipartBody.Part.createFormData("Deviation_file", "", mRequestBody);
                }
                if (mCursor.getString(mCursor.getColumnIndex("Identifation")) != null) {

                    j = mCursor.getString(mCursor.getColumnIndex("Identifation"));
                    if (!mCursor.getString(mCursor.getColumnIndex("Identifation_file")).equalsIgnoreCase("NA") &&
                            mCursor.getString(mCursor.getColumnIndex("Identifation_file")) != null) {
                        mFile = new File(mCursor.getString(mCursor.getColumnIndex("Identifation_file")));
                        mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                        body11 = MultipartBody.Part.createFormData("Identifation_file", mFile.getName(), mRequestBody);
                    } else {
                        mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                        body11 = MultipartBody.Part.createFormData("Identifation_file", "", mRequestBody);
                    }
                } else {

                    j = "";
                    mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                    body11 = MultipartBody.Part.createFormData("Identifation_file", "", mRequestBody);
                }
                //new
                if (mCursor.getString(mCursor.getColumnIndex("InspectionResult")) != null) {
                    h_new = mCursor.getString(mCursor.getColumnIndex("InspectionResult"));
                    i(TAG, "check_IR_DATA: InspectionResult" + h_new);
                    if (mCursor.getString(mCursor.getColumnIndex("InspectionResult_File")) != null &&
                            !mCursor.getString(mCursor.getColumnIndex("InspectionResult_File")).equalsIgnoreCase("NA")) {
                        mFile = new File(mCursor.getString(mCursor.getColumnIndex("InspectionResult_File")));
                        mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                        body7 = MultipartBody.Part.createFormData("InspectionResult_File", mFile.getName(), mRequestBody);
                    } else {
                        mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                        body7 = MultipartBody.Part.createFormData("InspectionResult_File", "", mRequestBody);
                    }

                } else {
                    h_new = "";
                    mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                    body7 = MultipartBody.Part.createFormData("InspectionResult_File", "", mRequestBody);
                }

                if (mCursor.getString(mCursor.getColumnIndex("Photograph")) != null) {
                    j_new = mCursor.getString(mCursor.getColumnIndex("Photograph"));
                    i(TAG, "check_IR_DATA: Photograph" + j_new);
                    if (mCursor.getString(mCursor.getColumnIndex("Photograph_File")) != null &&
                            !mCursor.getString(mCursor.getColumnIndex("Photograph_File")).equalsIgnoreCase("NA")) {
                        mFile = new File(mCursor.getString(mCursor.getColumnIndex("Photograph_File")));
                        mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                        body8 = MultipartBody.Part.createFormData("Photograph_File", mFile.getName(), mRequestBody);
                    } else {
                        mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                        body8 = MultipartBody.Part.createFormData("Photograph_File", "", mRequestBody);
                    }

                } else {
                    j_new = "";
                    mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                    body8 = MultipartBody.Part.createFormData("Photograph_File", "", mRequestBody);
                }
// using retrofit only


                final MultipartBody.Part finalBody = body1;
                final MultipartBody.Part finalBody1 = body2;
                final MultipartBody.Part finalBody2 = body3;
                final MultipartBody.Part finalBody3 = body4;
                final MultipartBody.Part finalBody4 = body5;
                final MultipartBody.Part finalBody5 = body6;
                final MultipartBody.Part finalBody6 = body7;
                final MultipartBody.Part finalBody7 = body8;
                final MultipartBody.Part finalBody8 = body9;
                final MultipartBody.Part finalBody9 = body10;
                final MultipartBody.Part finalBody10 = body11;
                final String finalA_v = A_v;
                final String finalA_v1 = a_v;
                final String finalB = b;
                final String finalC = c;
                final String finalD = d;
                final String finalE = e;
                final String finalF = f;
                final String finalG = g;
                final String finalJ = j;
                final String finalI = i;
                final String finalH = h;
                final String finalH_NEW = h_new;
                final String finalJ_NEW = j_new;


                ContentValues mValues = new ContentValues();
                mValues.put(DbConstant.IrIrn_Data_Entry.INSPECTION_ID, INSPECTION_ID);
                mValues.put(DbConstant.IrIrn_Data_Entry.PROJECT_VEND, str_proj_vend);
                mValues.put(DbConstant.IrIrn_Data_Entry.PO_NUM, str_po_nm);
                mValues.put(DbConstant.IrIrn_Data_Entry.SUB_VENDOR_PO_NUM, str_sub_ven_po_num);
                mValues.put(DbConstant.IrIrn_Data_Entry.CONSULTANT_NAME, str_cunsltant_name == null ? "-" : str_cunsltant_name);
                mValues.put(DbConstant.IrIrn_Data_Entry.CUSTOMER_NAME, str_client_nm == null ? "-" : str_client_nm);
                mValues.put(DbConstant.IrIrn_Data_Entry.ICS_REG_NUMBER, str_ics_reg_nm == null ? "-" : str_ics_reg_nm);
                mValues.put(DbConstant.IrIrn_Data_Entry.EMP_CODE, str_emp_cd == null ? "-" : str_emp_cd);
                mValues.put(DbConstant.IrIrn_Data_Entry.EMP_STATION, str_station == null ? "-" : str_station);
                mValues.put(DbConstant.IrIrn_Data_Entry.INSP_TYPE, str_insp_type == null ? "-" : str_insp_type);
                mValues.put(DbConstant.IrIrn_Data_Entry.VISUALA, finalA_v );
                mValues.put(DbConstant.IrIrn_Data_Entry.VISUALA_FILE, mCursor.getString(mCursor.getColumnIndex("visualA_file")));
                mValues.put(DbConstant.IrIrn_Data_Entry.VISUALB, finalA_v1);
                mValues.put(DbConstant.IrIrn_Data_Entry.VISUALB_FILE, mCursor.getString(mCursor.getColumnIndex("visualB_file")));
                mValues.put(DbConstant.IrIrn_Data_Entry.Dimensional, finalB);
                mValues.put(DbConstant.IrIrn_Data_Entry.FeInspection, finalC); // test_a (list of instrument/equipment..)
                mValues.put(DbConstant.IrIrn_Data_Entry.FnInspection, finalD); // test_b  (desc of any jig/fixture...)
                mValues.put(DbConstant.IrIrn_Data_Entry.VISUALB_FILE, mCursor.getString(mCursor.getColumnIndex("visualB_file")));
                mValues.put(DbConstant.IrIrn_Data_Entry.Dimensional_file, mCursor.getString(mCursor.getColumnIndex("Dimensional_file")));
                mValues.put(DbConstant.IrIrn_Data_Entry.Calibration, finalE=="-"?null:finalE);
                mValues.put(DbConstant.IrIrn_Data_Entry.Calibration_file, mCursor.getString(mCursor.getColumnIndex("Calibration_file")));
                mValues.put(DbConstant.IrIrn_Data_Entry.ReportsR, finalF);
                mValues.put(DbConstant.IrIrn_Data_Entry.ReportsR_file, mCursor.getString(mCursor.getColumnIndex("ReportsR_file")));
                mValues.put(DbConstant.IrIrn_Data_Entry.TestWitness, finalG);
                mValues.put(DbConstant.IrIrn_Data_Entry.TestWitness_File, mCursor.getString(mCursor.getColumnIndex("TestWitness_File")));
                //new
                mValues.put(DbConstant.IrIrn_Data_Entry.INSPECTION_RESULTS, finalH_NEW);
                mValues.put(DbConstant.IrIrn_Data_Entry.INSPECTION_RESULTS_File, mCursor.getString(mCursor.getColumnIndex("InspectionResult_File")));
                mValues.put(DbConstant.IrIrn_Data_Entry.PHOTOGRAPH, finalJ_NEW);
                mValues.put(DbConstant.IrIrn_Data_Entry.PHOTOGRAPH_File, mCursor.getString(mCursor.getColumnIndex("Photograph_File")));
                mValues.put(DbConstant.IrIrn_Data_Entry.QUANTITY_DESC, mCursor.getString(mCursor.getColumnIndex("Quantity_Desc")));
                mValues.put(DbConstant.IrIrn_Data_Entry.QUANTITY_QTY, mCursor.getString(mCursor.getColumnIndex("Quantity_Qty")));
                mValues.put(DbConstant.IrIrn_Data_Entry.QUANTITY_UNIT, mCursor.getString(mCursor.getColumnIndex("Quantity_Unit")));
                mValues.put(DbConstant.IrIrn_Data_Entry.QUANTITY_FILE, mCursor.getString(mCursor.getColumnIndex("Quantity_File")));

                mValues.put(DbConstant.IrIrn_Data_Entry.DATE_OF_INSP, str_insp_date);
                mValues.put(DbConstant.IrIrn_Data_Entry.ITEM_METARIAL, str_item_material);
                mValues.put(DbConstant.IrIrn_Data_Entry.QUANTITY, str_quantity);
                mValues.put(DbConstant.IrIrn_Data_Entry.SPEC_DRAWINGS, str_spec_drw);
                mValues.put(DbConstant.IrIrn_Data_Entry.CODES_STANDARD, str_code_stnd);
                mValues.put(DbConstant.IrIrn_Data_Entry.Other, finalH);
                mValues.put(DbConstant.IrIrn_Data_Entry.Other_file, mCursor.getString(mCursor.getColumnIndex("Other_file")));
                mValues.put(DbConstant.IrIrn_Data_Entry.Deviation, finalI);
                mValues.put(DbConstant.IrIrn_Data_Entry.Deviation_file, mCursor.getString(mCursor.getColumnIndex("Deviation_file")));
                mValues.put(DbConstant.IrIrn_Data_Entry.Identification, finalJ);
                mValues.put(DbConstant.IrIrn_Data_Entry.Identifation_file, mCursor.getString(mCursor.getColumnIndex("Identifation_file")));
                mValues.put(DbConstant.IrIrn_Data_Entry.EMP_NAME, str_empName);
                mValues.put(DbConstant.IrIrn_Data_Entry.STATION, str_station);
                mValues.put(DbConstant.IrIrn_Data_Entry.BATCH_NO, str_batch_no);
                mValues.put(DbConstant.IrIrn_Data_Entry.REPORT_NO, preferences.getString(getString(R.string.report_no), "-"));
                mValues.put(DbConstant.IrIrn_Data_Entry.PART_VISIT_SLIP, Common.PART_VISIT_SLIP);
                mValues.put(DbConstant.IrIrn_Data_Entry.PART_TC_SLIP, Common.PART_TC_SLIP);
                mValues.put(DbConstant.IrIrn_Data_Entry.UPLOAD_STAND, str_upload_stand);
                mValues.put(DbConstant.IrIrn_Data_Entry.PART_QAQC, Common.PART_QAQC);
                mValues.put(DbConstant.IrIrn_Data_Entry.UPLOAD_QAP, str_upload_qap);
                mValues.put(DbConstant.IrIrn_Data_Entry.UPLOAD_PO, str_upload_po);
                mValues.put(DbConstant.IrIrn_Data_Entry.PART_CALIB_PART_QAQC, Common.PART_CALIB_PART_QAQC);
                mValues.put(DbConstant.IrIrn_Data_Entry.PART_OTHER, Common.PART_OTHER);
                mValues.put(DbConstant.IrIrn_Data_Entry.DESC_NUM, str_descNum);
                mValues.put(DbConstant.IrIrn_Data_Entry.TAG_TYPE, Common.TAG_TYPE);
                mValues.put(DbConstant.IrIrn_Data_Entry.BALANCE_QTY, Common.TAG_TYPE);
                mValues.put(DbConstant.IrIrn_Data_Entry.INSP_VISIT, et_inspvisit.getText().toString());
                mValues.put(DbConstant.IrIrn_Data_Entry.ANNEX, et_annexture.getText().toString());
                mValues.put(DbConstant.IrIrn_Data_Entry.CUST_ID, cust_ID);
                mValues.put(DbConstant.IrIrn_Data_Entry.DATE_OF_FILLED_DATA, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                mValues.put(DbConstant.IrIrn_Data_Entry.LOCATION,str_location);
                mValues.put(DbConstant.IrIrn_Data_Entry.DESCRIPTION,str_description);
                mValues.put(DbConstant.IrIrn_Data_Entry.SITEINCHARGE,str_Siteincharge);
                mValues.put(DbConstant.IrIrn_Data_Entry.RANGE,str_range);
                mValues.put(DbConstant.IrIrn_Data_Entry.PROJECT_TYPE,str_project_type);
                mValues.put(DbConstant.IrIrn_Data_Entry.NO_OF_JOBS,str_no_of_jobs);
                mValues.put(DbConstant.IrIrn_Data_Entry.DONE_HOURS,str_done_hours);
                mValues.put(DbConstant.IrIrn_Data_Entry.EXTRA_HOURS,str_extra_hours);
                mValues.put(DbConstant.IrIrn_Data_Entry.CFRID,str_cfrid);
                mValues.put(DbConstant.IrIrn_Data_Entry.SERVER_STATUS, "pending");
///////////////////////////////////////////////////////////

                long id = mDatabase.insert(DbConstant.IrIrn_Data_Entry.TABLE_IR_IRN, null, mValues);
                if (id > 0) {
                    new DbHelper(mContext).updateReportSPDone(str_ics_reg_nm, str_insp_date,str_po_nm,str_proj_vend);
                }
                i(TAG, "check_IR_DATA: log of inserting: " + id);



            }
            i(TAG, "check_IR_DATA: Database deleted: " + new DbHelper(mContext).dropInspectionTable());
        }

        if (mCursor != null) mCursor.close();
        if (mDatabase.isOpen()) mDatabase.close();

    }

    private boolean checkUploadedSlipsRecords() {
        String msg = "please choose any appropriate media files";
        File mFile;
        RequestBody mRequestBody;


            if (ibtn_upload_visit_slip.getTag() != null) { //ibtn_upload_visit_slip

                Uri contentUri = Uri.parse(ibtn_upload_visit_slip.getTag().toString());
                i(TAG, "checkUploadedSlipsRecords:ibtn_upload_visit_slip " + contentUri.toString());
                i(TAG, "checkUploadedSlipsRecords:ibtn_upload_visit_slip path " + contentUri.getPath());
                mFile = new File(contentUri.getPath());
                Common.PART_VISIT_SLIP = contentUri.getPath();
                mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                part_visit_slip =
                        MultipartBody.Part.createFormData("v_slip", mFile.getName(), mRequestBody);
            }

            if (btn_upload_visit_slip.getTag() != null) { // btn_upload_visit_slip
                Uri contentUri = Uri.parse(btn_upload_visit_slip.getTag().toString());
                i(TAG, "checkUploadedSlipsRecords:ibtn_upload_visit_slip " + contentUri.toString());
                i(TAG, "checkUploadedSlipsRecords:ibtn_upload_visit_slip path " + contentUri.getPath());
                mFile = new File(contentUri.getPath());
                Common.PART_VISIT_SLIP = contentUri.getPath();
                mRequestBody = RequestBody.create(MediaType.parse("application/*"), mFile);
                part_visit_slip =
                        MultipartBody.Part.createFormData("v_slip", UUID.randomUUID().toString() + "_ICS_Visit_slip_" + mFile.getName(), mRequestBody);
            }

            if (ibtn_upload_tc.getTag() != null) { // ibtn_upload_tc

                Uri contentUri = Uri.parse(ibtn_upload_tc.getTag().toString());
                i(TAG, "checkUploadedSlipsRecords:tc_slip " + contentUri.toString());
                mFile = new File(contentUri.getPath());
                Common.PART_TC_SLIP = contentUri.getPath();
                mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                part_tc_slip =
                        MultipartBody.Part.createFormData("tc_slip", mFile.getName(), mRequestBody);
            }

            if (btn_upload_tc.getTag() != null) { // btn_upload_tc

                Uri contentUri = Uri.parse(btn_upload_tc.getTag().toString());
                i(TAG, "checkUploadedSlipsRecords:tc_slip " + contentUri.toString());
                Common.PART_TC_SLIP = contentUri.getPath();
                mFile = new File(contentUri.getPath());
                mRequestBody = RequestBody.create(MediaType.parse("application/*"), mFile);
                part_tc_slip =
                        MultipartBody.Part.createFormData("tc_slip", UUID.randomUUID().toString() + "_ICS_tc_slip_" + mFile.getName(), mRequestBody);
            }

            if (Common.STANDARDCOPY == null || Common.STANDARDCOPY.equalsIgnoreCase("-") && !Common.STANDARDCOPY.contains(".")) {
                if (ibtn_upload_standard.getTag() != null) {

                    Uri contentUri = Uri.parse(ibtn_upload_standard.getTag().toString());
                    i(TAG, "checkUploadedSlipsRecords:ibtn_upload_visit_slip " + contentUri.toString());
                    Common.UPLOAD_STAND = contentUri.getPath();
                    mFile = new File(contentUri.getPath());
                    mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                    part_stnd =
                            MultipartBody.Part.createFormData("stand", mFile.getName(), mRequestBody);
                }
            }


            if (ibtn_upload_qaqc.getTag() != null) { // ibtn_upload_qaqc

                Uri contentUri = Uri.parse(ibtn_upload_qaqc.getTag().toString());
                i(TAG, "checkUploadedSlipsRecords:ibtn_upload_visit_slip " + contentUri.toString());
                Common.PART_QAQC = contentUri.getPath();
                mFile = new File(contentUri.getPath());
                mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                part_qaqc =
                        MultipartBody.Part.createFormData("qaqc", mFile.getName(), mRequestBody);
            }


            if (btn_upload_qaqc.getTag() != null) { // btn_upload_qaqc

                Uri contentUri = Uri.parse(btn_upload_qaqc.getTag().toString());
                i(TAG, "checkUploadedSlipsRecords:ibtn_upload_visit_slip " + contentUri.toString());
                Common.PART_QAQC = contentUri.getPath();
                mFile = new File(contentUri.getPath());
                mRequestBody = RequestBody.create(MediaType.parse("application/*"), mFile);
                part_qaqc =
                        MultipartBody.Part.createFormData("qaqc", UUID.randomUUID().toString() + "_ICS_qaqc_" + mFile.getName(), mRequestBody);
            }

            if (Common.QAPCOPY == null || Common.QAPCOPY.equalsIgnoreCase("-")) {
                if (ibtn_upload_qap.getTag() != null) {

                    Uri contentUri = Uri.parse(ibtn_upload_qap.getTag().toString());
                    i(TAG, "checkUploadedSlipsRecords:ibtn_upload_visit_slip " + contentUri.toString());
                    mFile = new File(contentUri.getPath());
                    mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                    part_qap =
                            MultipartBody.Part.createFormData("qsp", mFile.getName(), mRequestBody);
                }
            }

            if (Common.POCOPY == null || Common.POCOPY.equalsIgnoreCase("-")) {
                if (ibtn_upload_po.getTag() != null) {

                    Uri contentUri = Uri.parse(ibtn_upload_po.getTag().toString());
                    i(TAG, "checkUploadedSlipsRecords:ibtn_upload_visit_slip " + contentUri.toString());
                    mFile = new File(contentUri.getPath());
                    mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                    part_po =
                            MultipartBody.Part.createFormData("upload_po", mFile.getName(), mRequestBody);
                }
            }


            if (ibtn_upload_calib_cert.getTag() != null) { // ibtn_upload_calib_cert

                Uri contentUri = Uri.parse(ibtn_upload_calib_cert.getTag().toString());
                i(TAG, "checkUploadedSlipsRecords:ibtn_upload_visit_slip " + contentUri.toString());
                Common.PART_CALIB_PART_QAQC = contentUri.getPath();
                mFile = new File(contentUri.getPath());
                mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                part_calib_part_qaqc =
                        MultipartBody.Part.createFormData("upload_calib", mFile.getName(), mRequestBody);
            }

            if (btn_upload_calib_cert.getTag() != null) { // btn_upload_calib_cert

                Uri contentUri = Uri.parse(btn_upload_calib_cert.getTag().toString());
                i(TAG, "checkUploadedSlipsRecords:ibtn_upload_visit_slip " + contentUri.toString());
                Common.PART_CALIB_PART_QAQC = contentUri.getPath();
                mFile = new File(contentUri.getPath());
                mRequestBody = RequestBody.create(MediaType.parse("application/*"), mFile);
                part_calib_part_qaqc =
                        MultipartBody.Part.createFormData("upload_calib", UUID.randomUUID().toString() + "_ICS_calib_" + mFile.getName(), mRequestBody);
            }
            if (ibtn_upload_other_docs.getTag() != null) {

                Uri contentUri = Uri.parse(ibtn_upload_other_docs.getTag().toString());
                i(TAG, "checkUploadedSlipsRecords:ibtn_upload_visit_slip " + contentUri.toString());
                Common.PART_OTHER = contentUri.getPath();
                mFile = new File(contentUri.getPath());
                mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                part_other =
                        MultipartBody.Part.createFormData("other_doc", mFile.getName(), mRequestBody);
            }

            if (btn_upload_other_docs.getTag() != null) { //btn_upload_other_docs

                Uri contentUri = Uri.parse(btn_upload_other_docs.getTag().toString());
                i(TAG, "checkUploadedSlipsRecords:ibtn_upload_visit_slip " + contentUri.toString());
                Common.PART_OTHER = contentUri.getPath();
                mFile = new File(contentUri.getPath());
                mRequestBody = RequestBody.create(MediaType.parse("application/*"), mFile);
                part_other =
                        MultipartBody.Part.createFormData("other_doc", UUID.randomUUID().toString() + "_ICS_other_docs_" + mFile.getName(), mRequestBody);
            }

            return true;

    }

    @Override
    public void onPause() {
        mDisposable.clear();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        String msg = "You cannot use both file, Choose only one";
        switch (v.getId()) {
            case R.id.btn_upload_visit_slip:
                i(TAG, "onClick: Clicked Button");
                if (ibtn_upload_visit_slip.getTag() == null)
                    pickDoc("choose Visit Slip Doccuments", PICK_VISIT_SLIP_REQUEST_CODE);
                else
                    showDuplicatePhotoDialog(msg);
                break;

            case R.id.btn_upload_tc:
                if (ibtn_upload_tc.getTag() == null)
                    pickDoc("Upload TC", PICK_TC_REQUEST_CODE);
                else
                    showDuplicatePhotoDialog(msg);
                break;

            case R.id.btn_upload_calib_cert:

                if (ibtn_upload_calib_cert.getTag() == null)
                    pickDoc("Upload Calibration Certification", PICK_CALIB_CERT_REQUEST_CODE);
                else
                    showDuplicatePhotoDialog(msg);
                break;

            case R.id.btn_upload_qaqc:
                if (ibtn_upload_qaqc.getTag() == null)
                    pickDoc("Upload QA/QC records", PICK_QAQC_REQUEST_CODE);
                else
                    showDuplicatePhotoDialog(msg);
                break;

            case R.id.btn_upload_po:
                if (ibtn_upload_po.getTag() == null)
                    pickDoc("Upload PO", PICK_PO_REQUEST_CODE);
                else
                    showDuplicatePhotoDialog(msg);
                break;

            case R.id.btn_upload_standard:
                if (ibtn_upload_standard.getTag() == null)
                    pickDoc("Upload Standard", PICK_STANDARD_REQUEST_CODE);
                else
                    showDuplicatePhotoDialog(msg);
                break;

            case R.id.btn_upload_qap:
                if (ibtn_upload_qap.getTag() == null)
                    pickDoc("Upload QAP", PICK_QAP_REQUEST_CODE);
                else
                    showDuplicatePhotoDialog(msg);
                break;

            case R.id.btn_upload_other_docs:
                if (ibtn_upload_other_docs.getTag() == null)
                    pickDoc("Upload Other Docs", PICK_OTHER_REQUEST_CODE);
                else
                    showDuplicatePhotoDialog(msg);
                break;

            //--------------------ImageButton Click Handler------------\\

            case R.id.ibtn_upload_visit_slip:
                if (btn_upload_visit_slip.getTag() == null)
                    startCamera(IMAGE_PICK_VISIT_SLIP_REQUEST_CODE);
                else
                    showDuplicatePhotoDialog(msg);
                break;

            case R.id.ibtn_upload_tc:
                if (btn_upload_tc.getTag() == null)
                    startCamera(IMAGE_PICK_TC_REQUEST_CODE);
                else
                    showDuplicatePhotoDialog(msg);
                break;

            case R.id.ibtn_upload_calib_cert:

                if (btn_upload_calib_cert.getTag() == null)
                    startCamera(IMAGE_PICK_CALIB_CERT_REQUEST_CODE);
                else
                    showDuplicatePhotoDialog(msg);
                break;

            case R.id.ibtn_upload_qaqc:
                if (btn_upload_qaqc.getTag() == null)
                    startCamera(IMAGE_PICK_QAQC_REQUEST_CODE);
                else
                    showDuplicatePhotoDialog(msg);
                break;

            case R.id.ibtn_upload_po:
                if (btn_upload_po.getTag() == null)
                    startCamera(IMAGE_PICK_PO_REQUEST_CODE);
                else
                    showDuplicatePhotoDialog(msg);
                break;

            case R.id.ibtn_upload_standard:
                if (btn_upload_standard.getTag() == null)
                    startCamera(IMAGE_PICK_STANDARD_REQUEST_CODE);
                else
                    showDuplicatePhotoDialog(msg);
                break;

            case R.id.ibtn_upload_qap:
                if (btn_upload_qap.getTag() == null)
                    startCamera(IMAGE_PICK_QAP_REQUEST_CODE);
                else
                    showDuplicatePhotoDialog(msg);
                break;

            case R.id.ibtn_upload_other_docs:
                if (btn_upload_other_docs.getTag() == null)
                    startCamera(IMAGE_PICK_OTHER_REQUEST_CODE);
                else
                    showDuplicatePhotoDialog(msg);
                break;

        }
    }

    private void pickDoc(String title, int requestCode) {


        try {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                        Uri uri = Uri.parse(Environment.getRootDirectory().getAbsolutePath());
//                        fileIntent.setData(uri);
           // fileIntent.setType("application/pdf");  //comment 19feb2020
            fileIntent.setType("*/*");  // added 19feb
            String[] extraMimeTypes = {"audio/*", "video/*", "application/*", "application/pdf", "application/msword", "application/vnd.ms-powerpoint", "application/vnd.ms-excel", "application/zip", "audio/x-wav|text/plain"};
            fileIntent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
            startActivityForResult(fileIntent, requestCode);
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDuplicatePhotoDialog(String msg) {
        new AlertDialog.Builder(mContext)
                .setMessage("Error")
                .setMessage(msg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();

    }


}
