package com.srj.icsinspection.fragment


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle



import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager

import com.srj.icsinspection.R
import com.srj.icsinspection.adapter.PoAdapter
import com.srj.icsinspection.constants.DbConstant
import com.srj.icsinspection.dbhelper.DbHelper
import com.srj.icsinspection.handler.ApiService
import com.srj.icsinspection.model.*
import com.srj.icsinspection.utils.Common
import com.srj.icsinspection.utils.toast_e
import com.srj.icsinspection.utils.toast_i
import dmax.dialog.SpotsDialog
import es.dmoral.toasty.Toasty
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_inspection_report.*
import kotlinx.android.synthetic.main.fragment_inspection_report.view.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.security.auth.callback.Callback
import kotlin.collections.ArrayList


class InspectionReportFragment :Fragment(),
        //  DatePickerDialog.OnDateSetListener ,
        AdapterView.OnItemSelectedListener, PoAdapter.AddClickListener {
// Meta <meta name="popads-verification-1788612" value="177dfa24cce6871d42ba9a7675c21f45" />


    override fun clickListener(position: Int, mvView: View?) {
    }

    private var loadTempId: String? = null

    private var mPoAdapter: PoAdapter? = null
    private var mPoViewHolder: PoAdapter.PoViewHolder? = null
    private var mDescList: ArrayList<DescriptionDataModel.Data>? = null
    internal val mycalender = Calendar.getInstance()
    internal val mycalender1 = Calendar.getInstance()
    //  private HorizontalScrollView hsv_po_list;
    private lateinit var mContext: Context
    private var mPreference: SharedPreferences? = null
    private var mActivity: FragmentActivity? = null
    private lateinit var preferences: SharedPreferences
    private var mDatabase: SQLiteDatabase? = null
    private var mHelper: DbHelper? = null
    private var mCustList: ArrayList<String>? = null
    private var mDialog: AlertDialog? = null
    private var mService: ApiService? = null

    private val mDisposable = CompositeDisposable()
    private var empCode: String? = null
    private var icsReeNum: String? = null
    private lateinit var mView: View
    private var rangeselected: RadioButton? = null
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    var dayofweek: String? = null
    var holiday: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_inspection_report, container, false)
        init()


        return mView
    }

    @SuppressLint("WrongConstant")
    private fun init() {
//mView.date.visibility = View.GONE
        activity!!.toast_i("Please choose date for inspection ")
        //      Toast.makeText(context,"Please choose date for inspection",Toast.LENGTH_LONG).show()

        mView.spin_Description.setBackgroundColor(Color.WHITE)
        mView.spin_siteIncharge.setBackgroundColor(Color.WHITE)


        val dateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            mycalender.set(Calendar.YEAR, year)
            mycalender.set(Calendar.MONTH, month)
            mycalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            tv_dtinspection.setError(null)
            datef()
            if (dayofweek.equals("Sunday")) {
                val cursor = DbHelper(mContext).LoadholidayData(tv_dtinspection.text.toString())

                if (cursor != null && cursor.count > 0) {
                    cursor.moveToFirst()
                    loadSavedData(cursor)
                    cursor.close()
                }
            }
        }

        mView.tv_dtinspection.setOnClickListener(View.OnClickListener {

            val dpd=DatePickerDialog(activity, dateSetListener, mycalender.get(Calendar.YEAR), mycalender.get(Calendar.MONTH), mycalender.get(Calendar.DAY_OF_MONTH))
                dpd.datePicker.setMaxDate(mycalender1.timeInMillis)
                dpd.show()
            //datePicker.setMaxDate(mycalender.timeInMillis)
        })


        mContext = mView.context
        mActivity = (activity) as FragmentActivity
        mHelper = DbHelper(mContext)
        preferences = mContext.getSharedPreferences(getString(R.string.user), MODE_PRIVATE)
        mDialog = SpotsDialog(mContext, getString(R.string.get_data))
        mDescList = ArrayList()

        empCode = preferences.getString("user_name", null)

        //ET

        //RV
        mView.rv_po_list.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(mContext.applicationContext, LinearLayoutManager.VERTICAL, false)
        mView.rv_po_list.layoutManager = layoutManager
        mView.rv_po_list.itemAnimator = DefaultItemAnimator()


        mView.sp_cust_name?.onItemSelectedListener = this
        //sp_item_name.setOnItemSelectedListener(this);
        // sp_vendar_name.setOnItemSelectedListener(this);


        mView.sp_vendar_name.setBackgroundColor(Color.WHITE)
        mView.sp_cust_name?.setBackgroundColor(Color.WHITE)
        mView.spin_location.setBackgroundColor(Color.WHITE)



        mCustList = ArrayList()
        //rb

        mService = Common.getAPI()
        mView.iv_ir_next.setOnClickListener {
            // validateInputs();
            if (dayofweek.equals("Sunday") && sp_cust_name.selectedItem.toString().equals("Select")) {
                mActivity!!.toast_i("This planning date " + tv_dtinspection.text.toString() + " will be consider as an holiday")
                holiday = 1
            } else if (!tv_dtinspection.text.toString().equals(tv_dt_of_insp.text.toString())) {
                mActivity!!.toast_i("Please Fill the details of the previous planning of date " + tv_dt_of_insp.text.toString())
                tv_dtinspection.setError("please Fill the details of the previous planning")
                return@setOnClickListener
            }
            else if(spin_location.selectedItem.toString().equals("Select")) {
                mActivity!!.toast_i("please select location")
                return@setOnClickListener
            }
            else if(!sp_cust_name.selectedItem.toString().equals("Select")&&spin_Description.selectedItem.toString().equals("Select")){
                mActivity!!.toast_i("Please select the Description")
                return@setOnClickListener
            }
            else {
                holiday = 0
            }
            checkRvText()
        }

        mDatabase = mHelper!!.writableDatabase
        // mDialog.show();
        mDatabase!!.delete(DbConstant.Material_Data_Entry.TABLE_MATERIAL_DATA, null, null)



        getlocation()




    }

    private fun getSiteIncharge() {
        //   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val dataAdapter = ArrayAdapter(mContext,
                android.R.layout.simple_spinner_item, DbHelper(mContext).getsiteincharge(preferences.getString("ics_reg_num", null)))
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mView.spin_siteIncharge!!.adapter = dataAdapter

    }


    private fun getDescription() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val dataAdapter = ArrayAdapter(mContext,
                android.R.layout.simple_spinner_item, DbHelper(mContext).getdescription(preferences.getString("ics_reg_num", null)))
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mView.spin_Description!!.adapter = dataAdapter

    }

    override fun onStart() {
        super.onStart()
        mView.rg.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rb_insp_type_single -> {
                    if (mView.hsv_rv_desc.visibility == View.VISIBLE)
                        mView.hsv_rv_desc.visibility = View.GONE
                }
                R.id.rb_insp_type_final -> {

                    finalRowHandler()
                }
            }
        }
    }



    private fun getlocation() {

        val dataAdapter = ArrayAdapter(mContext,
                android.R.layout.simple_spinner_item, DbHelper(mContext).geLocations())
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mView.spin_location!!.adapter = dataAdapter
    }

    private fun datef() {
        val Myformat = "yyyy-MM-dd"
        val day = "EEEE"
        val simpleDateFormat = SimpleDateFormat(Myformat, Locale.US)
        tv_dtinspection.setText(simpleDateFormat.format(mycalender.time))
        if (tv_dtinspection.text.toString().equals("choose date of inspection")) {
            mActivity!!.toast_i("Please Select Inspection date")
        } else {
            getCommonDetails(tv_dtinspection.text.toString())
        }

        val simpleDay = SimpleDateFormat(day)
        dayofweek = simpleDay.format(mycalender.time)

    }

    private fun finalRowHandler() {
        if (sp_cust_name.selectedItem != "Select" && mView.et_po_number.text.toString() != "" && icsReeNum != null) {
            getDescriptionRV(mView.et_po_number.text.toString())
        } else
            mActivity!!.toast_i("Please choose Customer Name and Supplier name to get Details...")
    }


    private fun getCommonDetails(date: String) {

        val dataAdapter = ArrayAdapter(mContext,
                android.R.layout.simple_spinner_item, DbHelper(mContext).getCustomerName(date))
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mView.sp_cust_name!!.adapter = dataAdapter



    }


    // Check if recyclerview items are not empty and validated
    private fun checkRvText() {
     if (tv_dtinspection.text.toString().equals("choose date of inspection")) {
            mActivity!!.toast_e("Inspection Date. cannot be null")
            return
        }
        val uuid = if (loadTempId != null) loadTempId else UUID.randomUUID().toString()

        val modelArrayList = ArrayList<DescriptionDataModel.Data>()
        Common.TAG_TYPE = "Vendor"
        if ((sp_cust_name.selectedItem.toString().equals("Select") || sp_vendar_name.selectedItem.toString().equals("Select")) && (!dayofweek.equals("Sunday"))) {
            finalRowHandler()

        } else {
//            var Outside = "Outside"
//            var Within = "Within"
            var range = range.checkedRadioButtonId
            rangeselected = mView.findViewById(range)


            val mdl = FirstPageTempModel(sp_cust_name.selectedItem.toString(), sp_vendar_name.selectedItem.toString(),
                    et_po_number.text.toString(),et_sub_ven_po_number.text.toString(), tv_dtinspection!!.text.toString(), et_cnslt_name.text.toString(),
                    if (rb_insp_type_single.isChecked) "Stage" else "Final", et_iitem_name.text.toString(),
                    et_batch_no.text.toString(), et_quantity.text.toString(), et_sp_drawing.text.toString(),
                    et_codes_stand.text.toString(), Common.TAG_TYPE, empCode!!, uuid!!,spin_location.selectedItem.toString(), rangeselected!!.text.toString(),
                    spin_Description.selectedItem.toString(), spin_siteIncharge.selectedItem.toString(), et_noofinspection.text.toString())


            DbHelper(mContext).updateFirstPage(mdl)
            // ---------------Checking if  type is Stage
            if (mView.rb_insp_type_single.isChecked) {
                val mdata = DescriptionDataModel().Data("",
                        (0.0), // poqty
                        (0.0), // release
                        (0.0),
                        (0.0),
                        (0.0))
                modelArrayList.add(mdata)
                val model = InspectionMoodel(sp_cust_name!!.selectedItem as String,
                        giveMeString(et_cnslt_name!!),
                        "-",
                        mView.sp_vendar_name.selectedItem.toString(),
                        mView.et_iitem_name.text.toString(),
                        giveMeString(et_batch_no!!),
                        giveMeString(et_sp_drawing!!),
                        giveMeString(et_codes_stand!!),
                        mView.tv_dtinspection!!.text.toString(),
                        mView.rb_insp_type_single.text.toString(),
                        preferences.getString(getString(R.string.ics_reg_num), null),
                        mView.et_po_number.text.toString(),
                        mView.et_sub_ven_po_number.text.toString(),
                        (et_quantity!!.text.toString()), uuid,
                        mView.spin_location.selectedItem.toString()
                        , spin_Description.selectedItem.toString(),
                        spin_siteIncharge.selectedItem.toString(),
                        et_noofinspection.text.toString(),
                        rangeselected!!.text.toString(),
                        preferences.getString("projectType", "")
                )

                val mFragment = FindinObsFragment()
                val mBundle = Bundle()
                mBundle.putInt("holiday", holiday)
                mBundle.putParcelable(getString(R.string.desc_model), model)
                mBundle.putSerializable(getString(R.string.desc_data), modelArrayList)
                mFragment.arguments = mBundle

                mActivity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, mFragment, getString(R.string.frag_finding))
                        .addToBackStack(getString(R.string.frag_insp))
                        .commit()

            } else if (mView.rb_insp_type_final.isChecked) {

                try {

                    for (i in 0 until mPoAdapter!!.itemCount) {

                        mPoViewHolder = mView.rv_po_list.findViewHolderForAdapterPosition(i) as PoAdapter.PoViewHolder
                        Log.i(TAG, "checkRvText: Items Count: " + mPoAdapter!!.itemCount)
                        if (i > 0) {
                            if (mPoViewHolder != null) {
                                Log.i(TAG, "checkRvText: Description: " + mPoViewHolder!!.et_item_desc.text.toString()
                                        + " Total qty: " + mPoViewHolder!!.et_item_tot_q.text.toString()
                                        + " Balance Qty : " + mPoViewHolder!!.et_item_bal_q.text.toString())
                            } else {
                                Log.i(TAG, "checkRvText: View is Null")
                            }




                            if (!TextUtils.isEmpty(mPoViewHolder!!.et_item_insp_q.text.toString()) &&
                                    Integer.parseInt(mPoViewHolder!!.et_item_insp_q.text.toString()) > 0 &&
                                    !TextUtils.isEmpty(mPoViewHolder!!.et_item_released_q.text.toString()) &&
                                    Integer.parseInt(mPoViewHolder!!.et_item_released_q.text.toString()) > 0)

                                if (!TextUtils.isEmpty(mPoViewHolder!!.et_item_insp_q.text.toString()) &&
                                        !TextUtils.isEmpty(mPoViewHolder!!.et_item_released_q.text.toString())) {


                                    val mdata = DescriptionDataModel()
                                            .Data(mPoViewHolder?.et_item_desc?.text.toString(),
                                                    (mPoViewHolder!!.et_item_tot_q.text.toString().toDouble()), // poqty
                                                    (mPoViewHolder!!.et_item_released_q.text.toString().toDouble()), //
                                                    (mPoViewHolder!!.et_item_rejected_q.text.toString().toDouble()),
                                                    (mPoViewHolder!!.et_item_bal_q.text.toString().toDouble()),
                                                    (mPoViewHolder!!.et_item_insp_q.text.toString().toDouble()))

                                    modelArrayList.add(mdata)
                                } else {
                                    mActivity!!.toast_e("Inspection and Released Qty. cannot be 0 OR null")
                                    return
                                }
                        }


                    }

                    // read recyclerview list data...
                    if (modelArrayList.size > 0) { // user have filled data
                        for (j in modelArrayList.indices) {
                            try {
                                if (modelArrayList[j].balQty < 0) {
                                    i(TAG, "Balanced Qty: " + modelArrayList[j].balQty)
                                    mActivity!!.toast_i("-ceed Further")
                                    return
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                mActivity!!.toast_i("Cannot proceed Further")
                            }

                        }

                        // Radio Button
                        var inspType: String? = null
                        if (rb_insp_type_final!!.isChecked) {
                            inspType = rb_insp_type_final!!.text.toString()
                        } else if (rb_insp_type_single!!.isChecked) {
                            inspType = rb_insp_type_single!!.text.toString()
                        }


                        val model = InspectionMoodel(sp_cust_name!!.selectedItem as String,
                                giveMeString(et_cnslt_name!!),
                                "-",
                                mView.sp_vendar_name.selectedItem.toString(),
                                mView.et_iitem_name.text.toString(),
                                giveMeString(et_batch_no!!),
                                giveMeString(et_sp_drawing!!),
                                giveMeString(et_codes_stand!!),
                                mView.tv_dt_of_insp!!.text.toString(),
                                inspType,
                                preferences.getString(getString(R.string.ics_reg_num), null),
                                mView.et_po_number.text.toString(),
                                mView.et_sub_ven_po_number.text.toString(),
                                (et_quantity!!.text.toString()), uuid,
                                mView.spin_location.selectedItem.toString()
                                , spin_Description.selectedItem.toString(),
                                spin_siteIncharge.selectedItem.toString()
                                , et_noofinspection.text.toString(),
                                rangeselected!!.text.toString(),
                                preferences.getString("projectType", "")
                        )

                        val mFragment = FindinObsFragment()
                        val mBundle = Bundle()
                        mBundle.putInt("holiday", holiday)
                        mBundle.putParcelable(getString(R.string.desc_model), model)
                        mBundle.putSerializable(getString(R.string.desc_data), modelArrayList)
                        mFragment.arguments = mBundle
                        mActivity!!.supportFragmentManager.beginTransaction()
                                .replace(R.id.main_container, mFragment, getString(R.string.frag_finding))
                                .addToBackStack(getString(R.string.frag_insp))
                                .commit()

                    } else
                        mActivity!!.toast_e("Description Fields Cannot be null")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
//        } else mActivity!!.toast_e("Fields are required !!!")
    }


    // validating inputs of et
    private fun isEmpty(etText: EditText): Boolean {
        return etText.text.toString().trim { it <= ' ' }.isNotEmpty()
    }

    private fun giveMeString(s: EditText): String {
        return s.text.toString()
    }



    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, l: Long) {

        when (adapterView.id) {

            R.id.sp_cust_name -> {
                if (mView.sp_cust_name != null) {
                    Log.i(TAG, "onItemSelected: Customer Text: " + mView.sp_cust_name!!.selectedItem.toString())
                    if (position > 0) {
                        getVender(mView.sp_cust_name!!.selectedItem.toString())
                    }
                }

            }

        }
    }

    // Get Inspection Date


    // Get PO Number
    private fun getVender(cust_name: String) {

        val dataAdapter = ArrayAdapter(mContext,
                android.R.layout.simple_spinner_item, DbHelper(mContext).getVendorsName(cust_name))
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mView.sp_vendar_name!!.adapter = dataAdapter

        mView.sp_vendar_name.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {

                bindData(sp_vendar_name.selectedItem.toString(), cust_name)
                if (!preferences.getString("projectType", "").equals("Quantity")) {
                    noofquantity.visibility = View.GONE
                }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            WorkingUnder.text = Html.fromHtml("The undersigned Surveyor working under general T &amp; C of ICS,\n" +
                                    "        did visit at <b>"+sp_vendar_name.selectedItem.toString()+"</b> for inspection of the above.", Html.FROM_HTML_MODE_LEGACY);
                        } else {
                            WorkingUnder.text = Html.fromHtml("The undersigned Surveyor working under general T &amp; C of ICS,\n" +
                                    "        did visit at <b>"+sp_vendar_name.selectedItem.toString()+"</b> for inspection of the above.");
                        }

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }



    }


    /**
     * Binding All Text
     * @param vendor_name         : vendor_name
     * @param cust_name:cust_name
     */
    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun bindData(vendor_name: String, cust_name: String) {

        val mCursor = DbHelper(mContext).getbindPoata(cust_name, vendor_name)

        if (mCursor.count > 0) {
            if (mCursor.moveToFirst()) {
                i(TAG, "gfgf")
                Common.CUSTOMER_NAME = mCursor.getString(mCursor.getColumnIndex(DbConstant.REPORT_SP_ENTRY.Ol_Name))
                Common.PO_NO = mCursor.getString(mCursor.getColumnIndex(DbConstant.REPORT_SP_ENTRY.PONumber))
                Common.CONSULTANT_NAME = mCursor.getString(mCursor.getColumnIndex(DbConstant.REPORT_SP_ENTRY.ConsultantName))
                Common.VENDOR_NAME = mCursor.getString(mCursor.getColumnIndex(DbConstant.REPORT_SP_ENTRY.VendorName))
                Common.POCOPY = mCursor.getString(mCursor.getColumnIndex(DbConstant.REPORT_SP_ENTRY.POCopy))
                Common.QAPCOPY = mCursor.getString(mCursor.getColumnIndex(DbConstant.REPORT_SP_ENTRY.QAPCopy))
                Common.STANDARDCOPY = mCursor.getString(mCursor.getColumnIndex(DbConstant.REPORT_SP_ENTRY.StandardCopy))


                preferences.edit()
                        .putString(getString(R.string.ics_reg_num), mCursor.getString(mCursor.getColumnIndex(DbConstant.REPORT_SP_ENTRY.ClientRegnum)))
                        .putString("projectType", mCursor.getString(mCursor.getColumnIndex(DbConstant.REPORT_SP_ENTRY.Project_Type)))
                        .putString(getString(R.string.report_no), mCursor.getString(mCursor.getColumnIndex(DbConstant.REPORT_SP_ENTRY.ReportNo)))
                        .apply()
                if (!mCursor.getString(mCursor.getColumnIndex(DbConstant.REPORT_SP_ENTRY.ConsultantName)).isNullOrEmpty()) {
                    et_cnslt_name!!.setText(mCursor.getString(mCursor.getColumnIndex(DbConstant.REPORT_SP_ENTRY.ConsultantName)))
                    et_cnslt_name.isFocusable = false
                }

                et_po_number!!.setText(mCursor.getString(mCursor.getColumnIndex(DbConstant.REPORT_SP_ENTRY.PONumber)))
                et_cnslt_name!!.setText(mCursor.getString(mCursor.getColumnIndex(DbConstant.REPORT_SP_ENTRY.ConsultantName)))
                et_po_number.isFocusable = false
                icsReeNum = mCursor.getString(mCursor.getColumnIndex(DbConstant.REPORT_SP_ENTRY.ClientRegnum))

                mView.tv_dt_of_insp!!.text = mCursor.getString(mCursor.getColumnIndex(DbConstant.REPORT_SP_ENTRY.Emp_Dt)).substring(0, 10)
                val cursor = DbHelper(mContext).loadFirstPageData(mView.sp_cust_name.selectedItem.toString(),
                        mView.sp_vendar_name.selectedItem.toString(), et_po_number.text.toString(),
                        tv_dt_of_insp.text.toString(), et_cnslt_name.text.toString(), empCode)

                if (cursor != null && cursor.count > 0) {
                    cursor.moveToFirst()
                    loadSavedData(cursor)
                    cursor.close()
                }
                else
                {
                    getDescription()
                    getSiteIncharge()
                }
                if (!tv_dt_of_insp.text.toString().equals(tv_dtinspection.text.toString())) {
                    mActivity!!.toast_i("Please Fill the details of the previous planning of date " + tv_dt_of_insp.text.toString())
                    tv_dtinspection.setError("please Fill the details of the previous planning")

                }
            }
        }
        mCursor?.close()


    }


    /**
     * Load Saved Data from temp table
     */
    private fun loadSavedData(cursor: Cursor) {
        //tv_dtinspection.setText(cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_1ST_DATA_ENTRY.DATE_OF_INSP)))
        val dataAdapter2 = ArrayAdapter(mContext,
                android.R.layout.simple_spinner_item, DbHelper(mContext).getdescription(preferences.getString("ics_reg_num", null)))
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mView.spin_Description!!.adapter = dataAdapter2
        var compareValue2 = cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_1ST_DATA_ENTRY.DESCRIPTION))
        if (compareValue2 != null) {
            var spinnerPosition = dataAdapter2.getPosition(compareValue2.toString());
            spin_Description.setSelection(spinnerPosition);
        }
        val dataAdapter = ArrayAdapter(mContext,
                android.R.layout.simple_spinner_item, DbHelper(mContext).geLocations())
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mView.spin_location!!.adapter = dataAdapter
        var compareValue = cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_1ST_DATA_ENTRY.LOCATION))
        if (compareValue != null) {
            var spinnerPosition = dataAdapter.getPosition(compareValue.toString());
            spin_location.setSelection(spinnerPosition);
        }



        val dataAdapter3 = ArrayAdapter(mContext,android.R.layout.simple_spinner_item, DbHelper(mContext).getsiteincharge(preferences.getString("ics_reg_num", null)))
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mView.spin_siteIncharge!!.adapter = dataAdapter3
        var compareValue3 = cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_1ST_DATA_ENTRY.SITEINCHARGE))
        if (compareValue3 != null) {
            var spinnerPosition = dataAdapter3.getPosition(compareValue3.toString());
            spin_siteIncharge.setSelection(spinnerPosition);
        }


        // spin_location.setSelection(cursor.getInt(cursor.getColumnIndex(DbConstant.TEMP_1ST_DATA_ENTRY.LOCATION)))
        //  spin_Description.setSelection(cursor.getInt(cursor.getColumnIndex(DbConstant.TEMP_1ST_DATA_ENTRY.DESCRIPTION)))
        et_sub_ven_po_number.setText(cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_1ST_DATA_ENTRY.SUB_VENDOR_PO_NUM)))
        et_cnslt_name.setText(cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_1ST_DATA_ENTRY.CONSULTANT_NAME)))
        et_noofinspection.setText(cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_1ST_DATA_ENTRY.No_OF_INSPECTION)))
        et_iitem_name.setText(cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_1ST_DATA_ENTRY.ITEM_METARIAL)))
        et_batch_no.setText(cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_1ST_DATA_ENTRY.BATCH_NO)))
        et_quantity.setText(cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_1ST_DATA_ENTRY.QUANTITY)))
        et_sp_drawing.setText(cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_1ST_DATA_ENTRY.SPEC_DRAWINGS)))
        et_codes_stand.setText(cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_1ST_DATA_ENTRY.CODES_STANDARD)))
        loadTempId = cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_1ST_DATA_ENTRY.INSPECTION_ID))
        if (cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_1ST_DATA_ENTRY.RANGE)) == "WITH IN MUNICIPLE LIMIT") {
            r_first.isChecked = true
        }
        else {
            r_second.isChecked = true
        }
        if (cursor.getString(cursor.getColumnIndex(DbConstant.TEMP_1ST_DATA_ENTRY.INSP_TYPE)) == "Final") {
            finalRowHandler()
        }
    }


    // Get Descriptions
    private fun getDescriptionRV(getpONumber: String?) {
        val mCursor = DbHelper(mContext).getFinalList(getpONumber, mView.sp_vendar_name.selectedItem.toString())
        if (mCursor?.count!! > 0) {
            if (mDescList?.size!! > 0) mDescList!!.clear()
            while (mCursor.moveToNext()) {
                mDescList!!.add(DescriptionDataModel().Data(
                        mCursor.getString(mCursor.getColumnIndex(DbConstant.Final_DATA_ENTRY.QTY_DESCRIPTION)),
                        mCursor.getString(mCursor.getColumnIndex(DbConstant.Final_DATA_ENTRY.PO_QTY)).toDouble(),
                        mCursor.getString(mCursor.getColumnIndex(DbConstant.Final_DATA_ENTRY.REL_QTY)).toDouble(),
                        mCursor.getString(mCursor.getColumnIndex(DbConstant.Final_DATA_ENTRY.REJ_QTY)).toDouble(),
                        mCursor.getString(mCursor.getColumnIndex(DbConstant.Final_DATA_ENTRY.BALANCE_QTY)).toDouble(),
                        0.0))
            }
            mPoAdapter = PoAdapter(mDescList, mContext, this)
            mView.rv_po_list.adapter = mPoAdapter
            mPoAdapter!!.notifyDataSetChanged()
            mView.hsv_rv_desc.visibility = View.VISIBLE
        }


    }

    override fun onNothingSelected(adapterView: AdapterView<*>) {

    }


    override fun onStop() {
        super.onStop()
        mDisposable.clear()
    }

    companion object {
        private const val TAG = "InspectionReportFragmen"
    }
}
