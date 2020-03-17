package com.srj.icsinspection.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.srj.icsinspection.R;
import com.srj.icsinspection.adapter.DoneReSyncAdapter;
import com.srj.icsinspection.adapter.DoneSyncAdapter;
import com.srj.icsinspection.constants.DbConstant;
import com.srj.icsinspection.dbhelper.DbHelper;
import com.srj.icsinspection.model.DoneSyncModel;
import com.srj.icsinspection.utils.Common;
import com.srj.icsinspection.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class ResSyncFragment extends Fragment implements DoneReSyncAdapter.ReSyncCallback
{
    private DbHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Context mContext;
    private DoneReSyncAdapter mDoneAdapter;
    private RecyclerView donerecyclerview;
    private List<DoneSyncModel> donesynclist = new ArrayList<>();
    private TextView donenodata;
    private ImageView sync;
    private AlertDialog mDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View mView = inflater.inflate(R.layout.resync_layout, container, false);
        init(mView);
        return mView;
    }

    private void init(View mView) {
      mDialog= new SpotsDialog(mView.getContext(), "Syncing, please wait...");
        mHelper = new DbHelper(getActivity());
        mDatabase = mHelper.getWritableDatabase();
        sync = mView.findViewById(R.id.sync);
        donenodata=mView.findViewById(R.id.donenodata);
        donerecyclerview = (RecyclerView) mView.findViewById(R.id.rv_donesyncdata);
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        donerecyclerview.setLayoutManager(mLayoutManager1);
        donerecyclerview.setItemAnimator(new DefaultItemAnimator());
        mDoneAdapter = new DoneReSyncAdapter(donesynclist,this);
        getlocaldata();
    }

    private void getlocaldata() {
        donenodata.setVisibility(View.GONE);
        final Cursor cursor = new DbHelper(getActivity()).getDoneIRIRN();

        if (cursor.getCount() > 0) {



            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.DATE_OF_INSP));
                String ponumber = cursor.getString(cursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.PO_NUM));
                String date_of_filled_data=cursor.getString(cursor.getColumnIndex(DbConstant.IrIrn_Data_Entry.DATE_OF_FILLED_DATA));
                DoneSyncModel donesyncModel = new DoneSyncModel(date, ponumber,date_of_filled_data);
                donesynclist.add(donesyncModel);
            }
//                mAdapter.notifyDataSetChanged();
            //recyclerView.setAdapter(mAdapter);

        }
        if(donesynclist.size()>0)
        {
            mDialog.cancel();
            Common.disableDialog(mDialog);
            donerecyclerview.setAdapter(mDoneAdapter);
            mDoneAdapter.notifyDataSetChanged();
        }
        else {
            mDialog.cancel();
            Common.disableDialog(mDialog);
            donenodata.setVisibility(View.VISIBLE);
            donenodata.setText("No Data Send to Server");
        }
     //   mDoneAdapter.notifyDataSetChanged();
    }

    @Override
    public void callbackreturn(String date, String ponumber) {
        Resynchaandler(date,ponumber);

    }
//added by kiran 1jan20
    private void Resynchaandler(String date, String ponumber)
    {
        if (Utils.INSTANCE.isNetworkAvailable(requireActivity())) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.REsync_before_title)
                    .setMessage(R.string.resync_before_message)
                    .setPositiveButton("Change Sync Status", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            try {
                                updatesyncstatus(date, ponumber,i);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            //   dialogInterface.dismiss();
                        }
                    }).setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
        else {
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            Toasty.error(getActivity(), "No Internet to Resync your data", Toast.LENGTH_LONG).show();
        }
    }

    private void updatesyncstatus(String date, String ponumber,int pos) throws ParseException
    {

       int i=new DbHelper(getActivity()).updateReSyncstaus(date,ponumber);
        Common.showDialog(mDialog);
        mDialog.setTitle("Sending data ....");
       if (i>0)
       {

           donesynclist.remove(donesynclist.size()-1);
           mDoneAdapter.notifyDataSetChanged();
           donesynclist.clear();
           getlocaldata();

       }


       Toasty.info(getContext(),date+ "date data is Available in Sync, Please Sync it again",Toast.LENGTH_LONG).show();


    }
}
