package com.srj.icsinspection.fragment;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.srj.icsinspection.R;
import com.srj.icsinspection.dbhelper.DbHelper;

public class ViewReportFragment extends Fragment {// implements AdapterView.OnItemClickListener


    private ListView mListView;
    private Context mContext;
    private FragmentActivity mActivity;
    private SQLiteDatabase mDatabase;
    private DbHelper mHelper;
    private static final String TAG = "ViewReportFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.fragment_view_report, container, false);
        init(mview);
        return mview;
    }

    private void init(View mview) {
        mContext = mview.getContext();
        mActivity = (FragmentActivity) getActivity();
    }

}
