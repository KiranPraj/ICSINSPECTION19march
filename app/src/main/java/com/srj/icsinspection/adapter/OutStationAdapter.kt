package com.srj.icsinspection.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.srj.icsinspection.R
import com.srj.icsinspection.model.OutStationExpensesModel


class OutStationAdapter(var type: Int,internal var context: Context, internal var array: ArrayList<OutStationExpensesModel>, private val mCallback1: IListnerCallback) :
    RecyclerView.Adapter<OutStationAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutStationAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.raw_outstation_expenses, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: OutStationAdapter.Holder, position: Int) {
         holder.location_boarding.setText(array.get(position).location_boarding)
         holder.expenses_boarding.setText(array.get(position).expense_boarding)
         holder.igst_boarding.setText(array.get(position).igst_boarding)
         holder.cgst_boarding.setText(array.get(position).cgst_boarding)
         holder.sgst_boarding.setText(array.get(position).sgst_boarding)
         holder.gst_no_boarding.setText(array.get(position).gst_no_boarding)
         holder.service_provider_boarding.setText(array.get(position).service_provider_boarding)
         holder.fromdate.setText(array.get(position).from_date_local)
         holder.todate.setText(array.get(position).to_date_local)
        holder.narration_boarding.setText(array.get(position).narration_boarding)
        var paid: String=array.get(position).paid_by_boarding
        var invoice:String=array.get(position).invoiceable_boarding
        if (invoice=="No") {
            holder.invoice_no.isChecked=true
        }
        else
        {
            holder.invoice_yes.isChecked=true
        }
       if (paid=="Self")
       {
           holder.r_second_paid.isChecked=true
       }
        else
       {
           holder.r_first_paid.isChecked=true
       }

        holder.browse_file_boarding.setText(array.get(position).browse_file_boarding)


            if(type==1)
            {
                holder.dateView.visibility=View.GONE
              //  holder.textforoutstation.text="Boarding and Lodging/Communication(Phone,Fax,Email)/Other Business Expenses"
            }
        else
            {
                holder.locaationView.visibility=View.GONE
              //  holder.textforoutstation.text="Fare(From-To)/Local Travel"
            }
        holder.browse_file.setOnClickListener {
            mCallback1.photoCallback(holder.browse_file)
        }
        holder.remove_textview.setOnClickListener {
            mCallback1.deleterowcallback1(holder.remove_textview,array[position],type, holder.gst_no_boarding.text.toString())
        }
        holder.add_textview.setOnClickListener {
            if(type==1)
            {
                holder.dateView.visibility=View.GONE
                //  holder.textforoutstation.text="Boarding and Lodging/Communication(Phone,Fax,Email)/Other Business Expenses"
            }
            else
            {
                holder.locaationView.visibility=View.GONE
                //  holder.textforoutstation.text="Fare(From-To)/Local Travel"
            }


            mCallback1.addrowcallback1(holder.add_textview,
                    holder.location_boarding.text.toString(),
                    holder.todate.text.toString(),
                    holder.fromdate.text.toString(),
                    holder.expenses_boarding.text.toString(),
                    holder.igst_boarding.text.toString(),
                    holder.cgst_boarding.text.toString(),
                    holder.sgst_boarding.text.toString(),
                    holder.gst_no_boarding.text.toString(),
                    holder.service_provider_boarding.text.toString(),
                    holder.paid_bytext.toString(),
                    holder.invoicetext.toString(),
                    holder.narration_boarding.text.toString(),
                    holder.browse_file_boarding.text.toString(),
                    type
                   )
                  //  array.get(position).location_boarding.toString())
        }

        holder.expenses_boarding.setOnTouchListener(View.OnTouchListener { view, event ->
            if (view.id == R.id.expenses_boarding) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })

        holder.service_provider_boarding.setOnTouchListener(View.OnTouchListener { view, event ->
            if (view.id == R.id.boarding_service_provider ) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })
        holder.narration_boarding.setOnTouchListener(View.OnTouchListener { view, event ->
            if (view.id == R.id.narrration_boarding ) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })
    }

    override fun getItemCount(): Int {
        return array.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var fromdate: EditText
        internal var todate: EditText
        internal var location_boarding: EditText
        internal var expenses_boarding: EditText
        internal var igst_boarding: EditText
        internal var cgst_boarding: EditText
        internal var sgst_boarding: EditText
        internal var gst_no_boarding: EditText
        internal var service_provider_boarding: EditText
     internal var paid_by_boarding: RadioGroup
     internal var r_first_paid: RadioButton
     internal var r_second_paid: RadioButton
     internal var innvoiceable_radio_grp: RadioGroup
        internal var invoice_yes: RadioButton
        internal var invoice_no: RadioButton
       // internal var invoiceable_boarding: EditText
        internal var narration_boarding: EditText
        internal var browse_file_boarding: TextView
        internal var dateView: LinearLayoutCompat
        internal var locaationView: LinearLayoutCompat
       // internal var textforoutstation:TextView
        internal var browse_file: TextView
        internal var add_textview: TextView
        internal var remove_textview: TextView
        var paid_bytext: String? =null
        var invoicetext: String? =null


       // internal  var radiobtn_paidby: RadioButton


        init {
            fromdate = itemView!!.findViewById(R.id.from_localtravel)
            todate = itemView!!.findViewById(R.id.to_localtravel)
            location_boarding = itemView!!.findViewById(R.id.location)
            expenses_boarding = itemView!!.findViewById(R.id.expenses_boarding)
            igst_boarding=itemView.findViewById(R.id.igst_boarding)
            cgst_boarding = itemView!!.findViewById(R.id.cgst_boarding)
            sgst_boarding = itemView!!.findViewById(R.id.sgst_boarding)
            gst_no_boarding = itemView!!.findViewById(R.id.boarding_gst_no)
            service_provider_boarding = itemView!!.findViewById(R.id.boarding_service_provider)
         paid_by_boarding = itemView!!.findViewById(R.id.expensesoutstation_radio_grp)
            r_first_paid = itemView!!.findViewById(R.id.r_first)
         r_second_paid = itemView!!.findViewById(R.id.r_second)
            innvoiceable_radio_grp = itemView!!.findViewById(R.id.innvoiceable_radio_grp)
            invoice_yes = itemView!!.findViewById(R.id.invoice_yes)
            invoice_no = itemView!!.findViewById(R.id.invoice_no)
          //  radiobtn_paidby = itemView!!.findViewById(R.id.r_first)

         //   invoiceable_boarding = itemView!!.findViewById(R.id.invoice_boarding)
            narration_boarding = itemView!!.findViewById(R.id.narrration_boarding)
            browse_file_boarding=itemView.findViewById(R.id.chose_file_boarding)
            dateView=itemView.findViewById(R.id.date)
            locaationView=itemView.findViewById(R.id.linearlayout_location)
          //  textforoutstation=itemView.findViewById(R.id.textforoutstation)
            browse_file=itemView.findViewById(R.id.chose_file_boarding)
            add_textview=itemView.findViewById(R.id.add_outstation)
            remove_textview=itemView.findViewById(R.id.delete_outstation)
            paid_bytext="Finance"
            invoicetext="Yes"
           paid_by_boarding.setOnCheckedChangeListener(
                    RadioGroup.OnCheckedChangeListener { group, checkedId ->

                     val  radiobtn_paidby:RadioButton = itemView.findViewById(checkedId)

                            paid_bytext= radiobtn_paidby.text.toString()


                    }
            )
            innvoiceable_radio_grp.setOnCheckedChangeListener(
                    RadioGroup.OnCheckedChangeListener { group, checkedId ->
                        val  radiobtn_invoice:RadioButton = itemView.findViewById(checkedId)

                        invoicetext= radiobtn_invoice.text.toString()

                    }
            )

        }
    }
    interface IListnerCallback
    {
    fun photoCallback(btn_textview: TextView)
   fun deleterowcallback1(addrow_textview:TextView,model:OutStationExpensesModel,type:Int,gst_no:String)
        fun addrowcallback1(delete_textview: TextView, location: String, todate: String, fromdate: String, expenses: String, igst: String, cgst: String, sgst: String, gstno: String, serviceprovider: String, paidby: String, invoice: String, narration: String, chosefile: String, type: Int)
    }

}
