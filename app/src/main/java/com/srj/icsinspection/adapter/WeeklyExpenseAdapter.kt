package com.srj.icsinspection.adapter

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.srj.icsinspection.R

import com.srj.icsinspection.model.WeeklyExpenseModel


import java.util.*
import kotlin.collections.ArrayList
import android.view.MotionEvent
import android.widget.*


class WeeklyExpenseAdapter(internal var context: Context, internal var array: ArrayList<WeeklyExpenseModel>, private val mCallback: ITopicClickListener) :
    RecyclerView.Adapter<WeeklyExpenseAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyExpenseAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.raw_weekly_table_layout, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: WeeklyExpenseAdapter.Holder, position: Int)
    {

     holder.selectdate.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)

            holder.picker = DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    holder.selectdate.text = (monthOfYear + 1).toString() + "-" + dayOfMonth + "-" + year
                }, year, month, day
            )
            holder.picker.datePicker.maxDate = cldr.timeInMillis
            holder.picker.show()
            var calenderdate=holder.selectdate.text.toString()

        }
        holder.particular_expenses.setOnTouchListener(View.OnTouchListener { view, event ->
            if (view.id == R.id.particular_expenses) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })

        holder.service_provider.setOnTouchListener(View.OnTouchListener { view, event ->
            if (view.id == R.id.service_provider ) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })
        holder.browse_file.setOnClickListener {
            mCallback.photoCallback(holder.browse_file)
        }

        holder.delete_row.setOnClickListener {
            mCallback.deleterowcallback(holder.delete_row,holder.selectdate.text.toString(),array[position])
        }
       var date1=array.get(position).getDate()
       var particular_expenses1=array.get(position).getParticular_expense()
       var total_amount1=array.get(position).getTotal_amount()
       var igst1=array.get(position).getIgst()
       var cgst1=array.get(position).getCgst()
       var sgst1=array.get(position).getSgst()
       var gst_no1=array.get(position).getGst_no()
       var service_provider1=array.get(position).getService_provider()
       var inv_amount1=array.get(position).getInv_amount()
       var client_no1=array.get(position).getClient_no()
       var browse_file1=array.get(position).getBrowse_file()
       var paid1=array.get(position).getPaid_by()

        holder.selectdate.setText(date1)
        holder.particular_expenses.setText(particular_expenses1)
        holder.total_amount.setText(total_amount1)
        holder.igst.setText(array.get(position).getIgst())
        holder.cgst.setText(array.get(position).getCgst())
        holder.sgst.setText(array.get(position).getSgst())
        holder.gst_no.setText(array.get(position).getGst_no())
        holder.service_provider.setText(array.get(position).getService_provider())
        holder.inv_amount.setText(array.get(position).getInv_amount())
        holder.client_no.setText(array.get(position).getClient_no())
        holder.browse_file.setText(array.get(position).getBrowse_file())
        var paidby:String=array.get(position).getPaid_by()

        // var paid_by= holder.paid_by.check(holder.paid_by.checkedRadioButtonId).toString()


        if (paidby=="Self")
        {
            holder.radiobtn2.isChecked=true

        }
        else
        {
            holder.radiobtn1.isChecked=true
        }

        holder.add_row.setOnClickListener {

            holder.particular_expenses.text
            mCallback.addrowcallback(holder.add_row,holder.selectdate.text.toString(), holder.particular_expenses.text.toString(),holder.total_amount.text.toString(),holder.igst.text.toString(),holder.cgst.text.toString(),holder.sgst.text.toString(),holder.gst_no.text.toString(),holder.service_provider.text.toString(),
                    holder.inv_amount.text.toString(),holder.client_no.text.toString(),holder.browse_file.text.toString(),holder.paidby_text.toString())
        }



    }




    override fun getItemCount(): Int {
        return array.size
    }




    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var selectdate: TextView
        internal var particular_expenses: EditText
        internal var total_amount: EditText
        internal var igst: EditText
        internal var cgst: EditText
        internal var sgst: EditText
        internal var gst_no: EditText
        internal var service_provider: EditText
       internal var paid_by: RadioGroup
        internal var inv_amount: EditText
        internal var client_no: EditText
        internal var browse_file: TextView
        internal var delete_row: TextView
        internal var add_row: TextView
        internal lateinit var picker: DatePickerDialog
        internal lateinit var radiobtn1: RadioButton
        internal lateinit var radiobtn2: RadioButton
        var paidby_text: String? =null

        init {
            selectdate = itemView!!.findViewById(R.id.edit_date)
            particular_expenses = itemView!!.findViewById(R.id.particular_expenses)
            total_amount = itemView!!.findViewById(R.id.total_amount)
            igst = itemView!!.findViewById(R.id.igst)
            cgst = itemView!!.findViewById(R.id.cgst)
            sgst = itemView!!.findViewById(R.id.sgst)
            gst_no = itemView!!.findViewById(R.id.gst_no)
            service_provider = itemView!!.findViewById(R.id.service_provider)
            inv_amount = itemView!!.findViewById(R.id.inv_amount)
            browse_file = itemView!!.findViewById(R.id.chose_file)
            delete_row = itemView!!.findViewById(R.id.delete)
            add_row = itemView!!.findViewById(R.id.add)
            client_no=itemView.findViewById(R.id.client_no)
            paid_by=itemView.findViewById(R.id.expenses_radio_grp)
            radiobtn1=itemView.findViewById(R.id.r_first_weekly)
            radiobtn2=itemView.findViewById(R.id.r_second_weekly)
            paidby_text="Finance"
            paid_by.setOnCheckedChangeListener { group, checkedId ->
                val radiobtn_id:RadioButton=itemView.findViewById(checkedId)
                paidby_text=radiobtn_id.text.toString()
            }




        }
    }
}

interface ITopicClickListener {
    fun photoCallback(btnPhoto: TextView)
    fun deleterowcallback(text: TextView, selecteddate:String, position: WeeklyExpenseModel)
    fun addrowcallback(text: TextView, date1: String, particularExpenses1: String, totalAmount1: String, igst1: String, cgst1: String, sgst1: String, gstNo1: String, serviceProvider1: String, invAmount1: String, clientNo1: String, browseFile1: String, paid1: String)
}
