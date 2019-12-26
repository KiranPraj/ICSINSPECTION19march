package com.srj.icsinspection.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.srj.icsinspection.MainActivity;
import com.srj.icsinspection.R;
import com.srj.icsinspection.fragment.SyncFragment;
import com.srj.icsinspection.model.DoneSyncModel;
import com.srj.icsinspection.model.SyncModel;

import java.text.ParseException;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class DoneSyncAdapter extends RecyclerView.Adapter<DoneSyncAdapter.ViewHolder> {

    private List<DoneSyncModel> DoneSynclistcontext;

    @Override
    public DoneSyncAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_synced_data, viewGroup, false);
        return new ViewHolder(itemView);
    }

    public DoneSyncAdapter(List<DoneSyncModel> DoneSynclist) {
         this.DoneSynclistcontext = DoneSynclist;
    }

    @Override
    public void onBindViewHolder(@NonNull DoneSyncAdapter.ViewHolder viewHolder, int i) {

        final DoneSyncModel donesyncModel = DoneSynclistcontext.get(i);
        viewHolder.tv_date.setText(donesyncModel.getDate());
        viewHolder.tv_ponumber.setText(donesyncModel.getPonumber());
        viewHolder.tv_done_date_of_filled.setText("date of inspection filled :-"+donesyncModel.getDate_of_filled_data());

//        viewHolder.sync.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (i == 0) {
//                    int a = adapterCallback.callbackreturn(viewHolder.tv_date.getText().toString(), viewHolder.tv_ponumber.getText().toString());
//                } else {
//                    Toasty.error(v.getContext(), "please Sync the previous dates First...", Toast.LENGTH_LONG).show();
//                }
//            }
//        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout syncdata;
        public TextView tv_date, tv_ponumber,tv_done_date_of_filled;
        private Button sync;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            syncdata = (LinearLayout) itemView.findViewById(R.id.donesyncdata);
            tv_date = itemView.findViewById(R.id.tv_done_date);
            tv_ponumber = itemView.findViewById(R.id.tv_done_ponumber);
            tv_done_date_of_filled=itemView.findViewById(R.id.tv_done_date_of_filled);

        }
    }

    @Override
    public int getItemCount() {
        return DoneSynclistcontext.size();
    }
}
