package com.srj.icsinspection.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.srj.icsinspection.R;
import com.srj.icsinspection.constants.DbConstant;
import com.srj.icsinspection.dbhelper.DbHelper;
import com.srj.icsinspection.model.SingleRowModel;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FindingObjAdapter extends RecyclerView.Adapter<FindingObjAdapter.MyViewHolder> {

    private static final String TAG = "FindingObjAdapter";
    private List<Integer> mList;
    ArrayList<SingleRowModel> mSingleRowModelArrayList;
    private Context mContext;
    AddClickListener mClickListener;
    SharedPreferences preferences;
    private static int TYPE_QTY = 1;
    private static int TYPE_NORMAL = 2;
    int type;
    SingleRowModel model;



    public interface AddClickListener {
        void clickListener(int position, View mvView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name_of_find, tv_single_srno,tv_qty_srno;
       public TextView spinner_qty_unit;
       public SearchableSpinner spinner_qty_desc;
        public Button btn_single_add;
        public EditText et_single_obs,et_qty_qty;
        public ImageButton ib_delete_row;

        MyViewHolder(View view) {
            super(view);
            tv_single_srno = view.findViewById(R.id.tv_single_srno);
            tv_qty_srno = view.findViewById(R.id.tv_qty_srno);
            spinner_qty_desc = view.findViewById(R.id.spinner_qty_desc);
            spinner_qty_unit = view.findViewById(R.id.spinner_qty_unit);
            et_qty_qty=view.findViewById(R.id.et_qty_qty);
            preferences = mContext.getSharedPreferences("USER_DB", MODE_PRIVATE);
            et_single_obs = view.findViewById(R.id.et_single_obs);
            btn_single_add = view.findViewById(R.id.btn_single_add);
            ib_delete_row = view.findViewById(R.id.ib_delete_row);

        }

    }

    public FindingObjAdapter(int type,List<Integer> mList, ArrayList<SingleRowModel> mSingleRowModelArrayList,
                             Context mContext, AddClickListener mAddClickListener) {
       this.type=type;
        this.mList = mList;
        this.mSingleRowModelArrayList = mSingleRowModelArrayList;
        this.mContext = mContext;
        this.mClickListener = mAddClickListener;
    }

    @Override
    public int getItemViewType(int position) {

        switch (type) {
            case 0:
                return SingleRowModel.COMON_TYPE;
            case 1:
                return SingleRowModel.QTY_TYPE;

            default:
                return -1;


        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case SingleRowModel.COMON_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_single, parent, false);
                return new MyViewHolder(view);
            case SingleRowModel.QTY_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_qty_layout, parent, false);
                return new MyViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final int data = mList.get(position);
        ArrayList<String> Desc=new ArrayList<>();
        Cursor mCursor= new DbHelper(mContext).getQuantitydescription(preferences.getString("ics_reg_num", null));
        if (mCursor != null && mCursor.getCount() > 0) {
            Desc.add("Select");
            while (mCursor.moveToNext()) {
                if (!Desc.contains(mCursor.getString(1)))
                    Desc.add(mCursor.getString(1));
            }
        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter(mContext,android.R.layout.simple_spinner_item, Desc);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        ArrayAdapter arrayAdapter=new ArrayAdapter(mContext,android.R.layout.simple_spinner_item,units);

        if (type==1)
        {
            holder.tv_qty_srno.setText(String.valueOf(data));
          //  holder.et_qty_qty.setText(model.getQuantity());
            //holder.spinner_qty_unit.setAdapter(arrayAdapter);
            holder.spinner_qty_desc.setAdapter(dataAdapter);
            holder.spinner_qty_desc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i!=0)
                    {
                        mCursor.moveToPosition(i-1);
                        String units = null;
                      //  ArrayList<String> units = new ArrayList<>();
                      //  units.add(mCursor.getString(mCursor.getColumnIndex(DbConstant.QuantityDescription_Entry.UNIT)));
                     //   ArrayAdapter arrayAdapter=new ArrayAdapter(mContext,android.R.layout.simple_spinner_item,units);
                     //   holder.spinner_qty_unit.setAdapter(arrayAdapter);
                     //   arrayAdapter.notifyDataSetChanged();
                        units=mCursor.getString(mCursor.getColumnIndex(DbConstant.QuantityDescription_Entry.UNIT));
                        if (units!=null)
                        {
                            holder.spinner_qty_unit.setText(units);
                        }
                        else
                        {
                            holder.spinner_qty_unit.setText("");
                        }

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
        else
        {
            //holder.tv_qty_srno.setText(String.valueOf(data));
            holder.tv_single_srno.setText(String.valueOf(data));
           //
        }


      //  holder.et_single_obs.setText("");
        holder.setIsRecyclable(false);



        if (mSingleRowModelArrayList != null && mSingleRowModelArrayList.size() > 0) {
            Log.i(TAG, "onBindViewHolder: data > 0 position: " + position);
            if (mSingleRowModelArrayList.size() <= mList.size() && mSingleRowModelArrayList.size() > position) {
                 model = mSingleRowModelArrayList.get(position);
                Log.i(TAG, "onBindViewHolder: data size" + mSingleRowModelArrayList.get(holder.getAdapterPosition()));
                if (model != null) {
//
                    holder.ib_delete_row.setTag(model.getDeletedId());
                    if(type==0)
                    {
                        holder.et_single_obs.setText(model.getObs());
                        holder.tv_single_srno.setTag(holder.getAdapterPosition());
                        holder.tv_single_srno.setText(model.getSrno());
                        if (!model.getUpload().contains("NA")) {
                            holder.btn_single_add.setTag(model.getUpload());
                            holder.btn_single_add.setText(mContext.getString(R.string.uploaded));
                            holder.btn_single_add.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.holo_green_light));

                        }
                    }
                    else
                    {
                        holder.et_qty_qty.setText(model.getQuantity());
                        holder.tv_qty_srno.setTag(holder.getAdapterPosition());
                        holder.tv_qty_srno.setText(model.getSrno());
                        if(model.getDescription()!=null)
                        {
                            holder.spinner_qty_desc.setSelection(dataAdapter.getPosition(model.getDescription()));
                        }
                        if(model.getUnit()!=null)
                        {
                           // holder.spinner_qty_unit.setSelection(arrayAdapter.getPosition(model.getUnit()));
                        }
                        if (!model.getFile().contains("NA")) {
                            holder.btn_single_add.setTag(model.getFile());
                            holder.btn_single_add.setText(mContext.getString(R.string.uploaded));
                            holder.btn_single_add.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.holo_green_light));

                        }
                    }

                }
            } else if (mSingleRowModelArrayList.size() >= mList.size()) {
                SingleRowModel model = mSingleRowModelArrayList.get(position);
                if (model != null) {
                    //holder.et_qty_qty.setText(model.getQuantity());
                    holder.ib_delete_row.setTag(model.getDeletedId());
                    if(type==0)
                    {
                        holder.tv_single_srno.setTag(holder.getAdapterPosition());
                        holder.tv_single_srno.setText(model.getSrno());
                        holder.et_single_obs.setText(model.getObs());
                        if (!model.getUpload().contains("NA")) {
                            holder.btn_single_add.setTag(model.getUpload());
                            holder.btn_single_add.setText(mContext.getString(R.string.uploaded));
                            holder.btn_single_add.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.holo_green_light));
                        }
                    }
                    else
                    {
                        holder.et_qty_qty.setText(model.getQuantity());
                        holder.tv_qty_srno.setTag(holder.getAdapterPosition());
                        holder.tv_qty_srno.setText(model.getSrno());
                        if(model.getDescription()!=null)
                        {
                            holder.spinner_qty_desc.setSelection(dataAdapter.getPosition(model.getDescription()));
                        }
                        if(model.getUnit()!=null)
                        {
                           // holder.spinner_qty_unit.setSelection(arrayAdapter.getPosition(model.getUnit()));
                        }
                        if (!model.getFile().contains("NA")) {
                            holder.btn_single_add.setTag(model.getFile());
                            holder.btn_single_add.setText(mContext.getString(R.string.uploaded));
                            holder.btn_single_add.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.holo_green_light));

                        }
                    }

                }

            } else Log.i(TAG, "onBindViewHolder: Not satisfied condition");

        }

        Log.i(TAG, "Data found: " + data);

        holder.btn_single_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.clickListener(position, view);
            }
        });
        holder.ib_delete_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  removeItem(data);
                mList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), mList.size());


                Log.i(TAG, "onClick: Deleted tag: " + holder.ib_delete_row.getTag());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void removeItem(int data) {
        //new DbHelper(mContext).deleteSingleROw((Integer) id);
        int in = mList.indexOf(data);
        mList.remove(in);
        notifyItemRemoved(in);

    }
}
