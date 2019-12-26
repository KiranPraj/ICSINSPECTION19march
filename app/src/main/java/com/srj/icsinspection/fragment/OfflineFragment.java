package com.srj.icsinspection.fragment;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.srj.icsinspection.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfflineFragment extends Fragment {


    public OfflineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.offline_layout, container, false);
    }

}
