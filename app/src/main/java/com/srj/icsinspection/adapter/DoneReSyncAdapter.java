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

import com.srj.icsinspection.R;
import com.srj.icsinspection.model.DoneSyncModel;
import com.srj.icsinspection.model.SyncModel;

import java.util.List;



public class DoneReSyncAdapter extends RecyclerView.Adapter<DoneReSyncAdapter.ViewHolder> {

    private List<DoneSyncModel> DoneSynclistcontext;
    ReSyncCallback adapterCallback;

    public interface ReSyncCallback {
        public void callbackreturn(String date, String ponumber);
    }
    @Override
    public DoneReSyncAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_synced_data, viewGroup, false);
        return new ViewHolder(itemView);
    }

    public DoneReSyncAdapter(List<DoneSyncModel> DoneSynclist, DoneReSyncAdapter.ReSyncCallback adapterCallback) {
        this.DoneSynclistcontext = DoneSynclist;
        this.adapterCallback=adapterCallback;

    }

    @Override
    public void onBindViewHolder(@NonNull DoneReSyncAdapter.ViewHolder viewHolder, int i) {
        viewHolder.sync.setVisibility(View.GONE);
        viewHolder.linearLayout.setVisibility(View.VISIBLE);
        final DoneSyncModel donesyncModel = DoneSynclistcontext.get(i);
        viewHolder.tv_date.setText(donesyncModel.getDate());
         String datee=donesyncModel.getDate();
         String ponumberr=donesyncModel.getPonumber();
        viewHolder.tv_ponumber.setText(donesyncModel.getPonumber());
        viewHolder.tv_done_date_of_filled.setText("date of inspection filled :-"+donesyncModel.getDate_of_filled_data());

        viewHolder.Resyncbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datee=donesyncModel.getDate();
                String ponumberr=donesyncModel.getPonumber();
                 adapterCallback.callbackreturn(datee, ponumberr);

                    //Toasty.error(v.getContext(), "please Sync the previous dates First...", Toast.LENGTH_LONG).show();

            }
        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout syncdata;
        public TextView tv_date, tv_ponumber,tv_done_date_of_filled;
        public Button sync,Resyncbtn;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            syncdata = (LinearLayout) itemView.findViewById(R.id.donesyncdata);
            tv_date = itemView.findViewById(R.id.tv_done_date);
            tv_ponumber = itemView.findViewById(R.id.tv_done_ponumber);
            tv_done_date_of_filled=itemView.findViewById(R.id.tv_done_date_of_filled);
            linearLayout=itemView.findViewById(R.id.linearlayout_resycbtn);
            sync=itemView.findViewById(R.id.sync);
            Resyncbtn=itemView.findViewById(R.id.Resync);


        }
    }

    @Override
    public int getItemCount() {
        return DoneSynclistcontext.size();
    }
}
