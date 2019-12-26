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
import com.srj.icsinspection.model.SyncModel;

import java.text.ParseException;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SyncAdapter extends RecyclerView.Adapter<SyncAdapter.ViewHolder> {
    AdapterCallback adapterCallback;

    public interface AdapterCallback {
        int callbackreturn(String date, String ponumber);
    }

    private List<SyncModel> Synclistcontext;

    @Override
    public SyncAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_syncdata, viewGroup, false);
        return new ViewHolder(itemView);
    }

    public SyncAdapter(List<SyncModel> Synclist, AdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
        this.Synclistcontext = Synclist;
    }

    @Override
    public void onBindViewHolder(@NonNull SyncAdapter.ViewHolder viewHolder, int i) {

        final SyncModel syncModel = Synclistcontext.get(i);

        viewHolder.tv_date.setText(syncModel.getDate());
        viewHolder.tv_ponumber.setText(syncModel.getPonumber());
        viewHolder.tv_dateoffil.setText("date of inspection filled :-"+syncModel.getDate_of_fill());

        viewHolder.sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    adapterCallback.callbackreturn(viewHolder.tv_date.getText().toString(), viewHolder.tv_ponumber.getText().toString());
                } else {
                    Toasty.error(v.getContext(), "please Sync the previous dates First...", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout syncdata;
        public TextView tv_date, tv_ponumber,tv_dateoffil;
        private Button sync;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            syncdata = (LinearLayout) itemView.findViewById(R.id.syncdata);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_ponumber = itemView.findViewById(R.id.tv_ponumber);
            tv_dateoffil = itemView.findViewById(R.id.tv_dateoffil);
            sync = itemView.findViewById(R.id.sync);

        }
    }

    @Override
    public int getItemCount() {
        return Synclistcontext.size();
    }
}
