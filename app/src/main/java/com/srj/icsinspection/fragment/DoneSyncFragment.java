package com.srj.icsinspection.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.srj.icsinspection.R;
import com.srj.icsinspection.adapter.DoneSyncAdapter;
import com.srj.icsinspection.constants.DbConstant;
import com.srj.icsinspection.dbhelper.DbHelper;
import com.srj.icsinspection.model.DoneSyncModel;

import java.util.ArrayList;
import java.util.List;

public class DoneSyncFragment extends Fragment {
    private DbHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Context mContext;
    private DoneSyncAdapter mDoneAdapter;
    private RecyclerView donerecyclerview;
    private List<DoneSyncModel> donesynclist = new ArrayList<>();
    private TextView donenodata;
    private ImageView sync;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View mView = inflater.inflate(R.layout.done_sync, container, false);
        init(mView);
        return mView;
    }

    private void init(View mView) {
        mHelper = new DbHelper(getActivity());
        mDatabase = mHelper.getWritableDatabase();
        sync = mView.findViewById(R.id.sync);
        donenodata=mView.findViewById(R.id.donenodata);
        donerecyclerview = (RecyclerView) mView.findViewById(R.id.rv_donesyncdata);
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        donerecyclerview.setLayoutManager(mLayoutManager1);
        donerecyclerview.setItemAnimator(new DefaultItemAnimator());
        mDoneAdapter = new DoneSyncAdapter(donesynclist);
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
            donerecyclerview.setAdapter(mDoneAdapter);
        }
        else {
            donenodata.setVisibility(View.VISIBLE);
            donenodata.setText("No Data Send to Server");
        }
        mDoneAdapter.notifyDataSetChanged();
    }
}
