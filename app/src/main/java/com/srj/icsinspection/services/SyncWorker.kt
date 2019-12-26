package com.srj.icsinspection.services

//import androidx.work.Worker
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.util.Log.i
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.srj.icsinspection.dbhelper.DbHelper
import com.srj.icsinspection.handler.ApiService
import com.srj.icsinspection.model.InsertionModel
import com.srj.icsinspection.utils.Common
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class SyncWorker(private val context: Context, private val params: WorkerParameters) : Worker(context, params) {

    private val mService: ApiService by lazy { Common.getAPI() }
    private lateinit var preferences: SharedPreferences
    private val mDisposable = CompositeDisposable()

    companion object {
        private val TAG = "SyncWorker"
    }

    override fun doWork(): Result {

        Log.i(TAG, "doWork: Some Data came in worker Services")
        /* mHelper = DbHelper(applicationContext)
         preferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.user), MODE_PRIVATE)

         )*/
       // requestNumbersFromServer()
        return Result.SUCCESS
    }


    // Requesting numbers
//    private fun requestNumbersFromServer() {
//
//        val cursor = DbHelper(context).getAllFinalData()
//
//        if (cursor.count > 0) {
//            while (cursor.moveToNext()) {
//
//                /*mService.irn(
//                        cursor.getString(cursor.getColumnIndex("balance_qty")),
//                        cursor.getString(cursor.getColumnIndex("ponumber")),
//                        cursor.getString(cursor.getColumnIndex("ics_reg_number")),
//                        cursor.getString(cursor.getColumnIndex("qty_description")),
//                        cursor.getString(cursor.getColumnIndex("report_no")),
//                        cursor.getString(cursor.getColumnIndex("clientname")),
//                        cursor.getString(cursor.getColumnIndex("vendorname")),
//                        SimpleDateFormat("mm/dd/yyyy", Locale.ENGLISH)
//                                .format(cursor.getString(cursor.getColumnIndex("inspection_date"))),
//                        cursor.getString(cursor.getColumnIndex("username")),
//                        cursor.getString(cursor.getColumnIndex("emp_code")),
//                        cursor.getString(cursor.getColumnIndex("emp_station")),
//                        cursor.getInt(cursor.getColumnIndex("po_qty")).toString(),
//                        cursor.getInt(cursor.getColumnIndex("rel_qty")).toString(),
//                        cursor.getInt(cursor.getColumnIndex("rej_qty")).toString(),
//                        "fdfd", "fgfg", "dfdfd",
//                        cursor.getInt(cursor.getColumnIndex("insp_qty")).toString()
//                ).enqueue(object : retrofit2.Callback<List<InsertionModel>> {
//
//                    override fun onResponse(call: Call<List<InsertionModel>>, response: Response<List<InsertionModel>>) {
//                        if (response.isSuccessful) {
//                            if (response.body()!!.get(0).sts == 1) {
//                                i(TAG, "Success")
//                            }
//                        }
//                    }
//
//                    override fun onFailure(call: Call<List<InsertionModel>>, t: Throwable) {
//                        i(TAG, "Failed: ${t.message}")
//                    }
//
//                })*/
//            }
//        }
//    }


}
