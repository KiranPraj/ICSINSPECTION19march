package com.srj.icsinspection.adapter;

import android.content.Context;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.srj.icsinspection.R;
import com.srj.icsinspection.model.DescriptionDataModel;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class PoAdapter extends RecyclerView.Adapter<PoAdapter.PoViewHolder> {

    private static final String TAG = "PoAdapter";
    private List<DescriptionDataModel.Data> mList;
    private Context mContext;
    private AddClickListener mClickListener;
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_ITEM = 1;


    public interface AddClickListener {
        void clickListener(int position, View mvView);
    }

    public class PoViewHolder extends RecyclerView.ViewHolder {

        int view_type;
        public TextView et_item_desc, et_item_tot_q, et_item_bal_q, et_item_rejected_q,
                item_head_desc, item_head_tot_q, item_head_bal_q, item_head_maon_q, item_head_add;
        public EditText et_item_insp_q, et_item_released_q;


        PoViewHolder(View view, int viewType) {
            super(view);
            if (viewType == TYPE_ITEM) {

                et_item_desc = view.findViewById(R.id.et_item_desc);
                et_item_tot_q = view.findViewById(R.id.et_item_tot_q);
                et_item_bal_q = view.findViewById(R.id.et_item_bal_q);
                et_item_rejected_q = view.findViewById(R.id.et_item_rejected_q);
                et_item_insp_q = view.findViewById(R.id.et_item_insp_q);
                et_item_released_q = view.findViewById(R.id.et_item_released_q);
                view_type = 1;
            } else if (viewType == TYPE_HEAD) {
                view_type = 0;
            }
        }

    }

    public PoAdapter(List<DescriptionDataModel.Data> mList, Context mContext, AddClickListener mAddClickListener) {
        this.mList = mList;
        this.mContext = mContext;
        this.mClickListener = mAddClickListener;
    }

    @NonNull
    @Override
    public PoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        PoViewHolder mPoViewHolder = null;
        if (viewType == TYPE_ITEM) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_po_description, parent, false);
            mPoViewHolder = new PoViewHolder(itemView, viewType);
            return mPoViewHolder;
        } else if (viewType == TYPE_HEAD) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_desc_header, parent, false);
            mPoViewHolder = new PoViewHolder(itemView, viewType);
            return mPoViewHolder;
        }
        return mPoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PoViewHolder holder, final int position) {

        if (holder.view_type == TYPE_ITEM) {
            final DescriptionDataModel.Data model = mList.get(position - 1);
            holder.et_item_desc.setText(model.getDescription());
            holder.et_item_bal_q.setText(String.valueOf(model.getBalQty()));
            holder.et_item_rejected_q.setText(String.valueOf(model.getRejectedQty()));
            holder.et_item_tot_q.setText(String.valueOf(model.getPoQty()));

            holder.et_item_insp_q.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    try {

                        if (!s.toString().equals("") && !TextUtils.isEmpty(s)) {

                            if ((holder.et_item_released_q.getText() != null) &&
                                    !holder.et_item_released_q.getText().toString().equalsIgnoreCase("")) {
                                Double released = Double.parseDouble(holder.et_item_released_q.getText().toString());
                                Double inspected = Double.parseDouble(s.toString());
                                if (released > inspected) {
                                    Toasty.error(mContext, "Release can't be  Greater than Inspection Qty", Toast.LENGTH_SHORT).show();
                                } else {
                                    String rejResult = String.valueOf((inspected - released));
                                    holder.et_item_rejected_q.setText(rejResult);
                                }
                            }
                        } else {
                            Toasty.warning(mContext, "Release can't be  empty", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().equals("")) {
                        holder.et_item_bal_q.setText(String.valueOf(model.getBalQty()));
                    }
                }
            });

            holder.et_item_released_q.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    try {

                        if (!s.toString().equals("") && !TextUtils.isEmpty(s)) {

                            if ((holder.et_item_insp_q.getText() != null) &&
                                    !holder.et_item_insp_q.getText().toString().equalsIgnoreCase("")) {
                                Double inspected = Double.parseDouble(holder.et_item_insp_q.getText().toString());
                                Double released = Double.parseDouble(s.toString());
                                Double nos = Double.parseDouble(holder.et_item_tot_q.getText().toString());

                                if (released > inspected) {
                                    Toasty.error(mContext, "Release can't be  Greater than Inspection Qty", Toast.LENGTH_SHORT).show();
                                } else {
                                    String rejResult = String.valueOf((inspected - released));
                                    holder.et_item_rejected_q.setText(rejResult);

                                    String balResult = String.valueOf(nos-released);
                                    holder.et_item_bal_q.setText(balResult);
                                }
                            }

                        } else {
                            Toasty.warning(mContext, "Release can't be  empty", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }


                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().equals("")) {
                        holder.et_item_bal_q.setText(String.valueOf(model.getBalQty()));
                    }
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEAD;
        return TYPE_ITEM;
    }
}
