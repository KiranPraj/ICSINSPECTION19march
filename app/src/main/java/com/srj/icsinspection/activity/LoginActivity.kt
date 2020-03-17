package com.srj.icsinspection.activity

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle


import android.text.InputFilter
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.srj.icsinspection.MainActivity
import com.srj.icsinspection.R
import com.srj.icsinspection.dbhelper.DbHelper
import com.srj.icsinspection.handler.ApiService
import com.srj.icsinspection.model.*
import com.srj.icsinspection.utils.Common

import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.srj.icsinspection.constants.DbConstant
import com.srj.icsinspection.utils.toast
import com.srj.icsinspection.utils.toast_e
import dmax.dialog.SpotsDialog
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main1.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mService: ApiService
    private lateinit var mDialog: android.app.AlertDialog
    private lateinit var preferences: SharedPreferences
    private val mDisposable = CompositeDisposable()
    private lateinit var springForce: SpringForce

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)
        setContentView(R.layout.activity_main1)


        mDialog = SpotsDialog(this, "Please Wait..")
        mService = Common.getAPI()

        preferences = getSharedPreferences(getString(R.string.user), Context.MODE_PRIVATE)

        user.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        if (preferences.getString("user_name", null) != null && preferences.getString("pass", null) != null) {

            val i = Intent(this@LoginActivity, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            finish()
            startActivity(i)
        } else {
            login.setOnClickListener(View.OnClickListener {
                Common.showDialog(mDialog)
                mDialog.setMessage("SIGNING IN.....")

//                val mProgressDialog = DelayedProgressDialog()
//                mProgressDialog.isCancelable = false
//                mProgressDialog.show(supportFragmentManager, "progress")
                if (user.text.toString().isEmpty() && pass.text.toString().isEmpty()) {
                    toast_e("User Name and Password Required")
                    Common.disableDialog(mDialog)
                    mDialog.dismiss()
                    return@OnClickListener
                }

                if (user.text.toString().isEmpty()) {
                    toast_e("User Name Required")
                    Common.disableDialog(mDialog)
                    mDialog.dismiss()
                    return@OnClickListener
                }

                if (pass.text.toString().isEmpty()) {
                    toast_e("Password Required")
                    Common.disableDialog(mDialog)
                    mDialog.dismiss()
                    return@OnClickListener
                }

                // throw new RuntimeException("This is a crash");


                checkLoginDetails()
            })
        }


    }

    // check login details
    private fun checkLoginDetails() {

        Common.showDialog(mDialog)

        mDisposable.add(mService.login(user.text.toString().trim { it <= ' ' },
                pass.text.toString().trim { it <= ' ' })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    try {
                        if (s[0].sts > 0) {


                            /*  preferences.edit()
                                        .putString("user_name", user.getText().toString().trim())
                                        .putString("pass", pass.getText().toString().trim())
                                        .apply();

                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("user_name", user.getText().toString());
                                i.putExtra("pass", pass.getText().toString());
                                startActivity(i);
                                finish();*/
                            insertDescQuantity()
                            insertStation()
                            insertlocation()
                            insertdesc()
                            insertsiteincharge()
                            insertReports()


                        } else {
                            Common.disableDialog(mDialog)
                            mDialog.dismiss()
                            toast_e("Login Failed ")
                        }
                    } catch (e: Exception) {
                        Common.disableDialog(mDialog)
                        mDialog.dismiss()
                        toast_e("Failed: " + e.message)
                    } finally {
                        Log.i(TAG, "accept: Reached Final")
//                        Common.disableDialog(mDialog)
//                        mDialog.dismiss()
                    }
                }, { throwable ->
                    Common.disableDialog(mDialog)
                    toast_e("Error: " + throwable.message)
                    mDialog.dismiss()
                })
        )
    }

    private fun insertsiteincharge() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Common.showDialog(mDialog)
        mDialog.setMessage("Getting SiteIncharge Details...")

//        val mProgressDialog = DelayedProgressDialog()
//        mProgressDialog.isCancelable = false
//        mProgressDialog.show(supportFragmentManager, "progress")
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        mService.getSiteIncharge(user.text.toString().trim()).enqueue(object :Callback<List<SiteInchargeModel>>{


            override fun onFailure(call: Call<List<SiteInchargeModel>>, t: Throwable) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Toast.makeText(this@LoginActivity,"Fail to load site Incharge",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<SiteInchargeModel>>, response: Response<List<SiteInchargeModel>>) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                if (response.isSuccessful && response.body() != null) {
                    val mList = response.body()
                    if (mList!!.isNotEmpty())
                    //do desc
                        DbHelper(this@LoginActivity).SiteIncharge(mList
                                as ArrayList<SiteInchargeModel>?)
//                    Toast.makeText(this@LoginActivity,"site Incharge",Toast.LENGTH_LONG).show()
                }

            }
        })


    }

    private fun insertDescQuantity() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Common.showDialog(mDialog)
        mDialog.setMessage("Getting Location Details...")

//        val mProgressDialog = DelayedProgressDialog()
//        mProgressDialog.isCancelable = false
//        mProgressDialog.show(supportFragmentManager, "progress")
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        mService.getquantitydescription(user.text.toString().trim()).enqueue(object :Callback<DescriptionQuantityModel>{
            override fun onFailure(call: Call<DescriptionQuantityModel>, t: Throwable) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Toast.makeText(this@LoginActivity,"Fail to load QUantitydesc",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DescriptionQuantityModel>, response: Response<DescriptionQuantityModel>) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                if (response.isSuccessful && response.body() != null) {
                    //val cursor = mDatabase.rawQuery("SELECT " + DbConstant.REPORT_SP_ENTRY.Emp_Dt + " FROM " + DbConstant.REPORT_SP_ENTRY.TABLE_REPORT_SP + " ORDER BY " + DbConstant.REPORT_SP_ENTRY.Emp_Dt + " DESC LIMIT 1", null)
                    val mList = response.body()!!.dtqty

                    //do desc
                        DbHelper(this@LoginActivity).QuantityDescription(mList as ArrayList<DescriptionQuantityModel.Dtqty>?)
                    //    Toast.makeText(this@LoginActivity,"hell",Toast.LENGTH_LONG).show()
                }

            }
        })
    }

    private fun insertdesc() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Common.showDialog(mDialog)
        mDialog.setMessage("Getting Location Details...")

//        val mProgressDialog = DelayedProgressDialog()
//        mProgressDialog.isCancelable = false
//        mProgressDialog.show(supportFragmentManager, "progress")
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        mService.getdescription(user.text.toString().trim()).enqueue(object :Callback<List<DescriptionModel>>{
            override fun onFailure(call: Call<List<DescriptionModel>>, t: Throwable) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Toast.makeText(this@LoginActivity,"Fail to load location",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<DescriptionModel>>, response: Response<List<DescriptionModel>>) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                if (response.isSuccessful && response.body() != null) {
                    val mList = response.body()
                    if (mList!!.isNotEmpty())
                    //do desc
                        DbHelper(this@LoginActivity).Description(mList
                                as ArrayList<DescriptionModel>?)
                //    Toast.makeText(this@LoginActivity,"hell",Toast.LENGTH_LONG).show()
                }

            }
        })
    }

    private fun insertlocation() {
        Common.showDialog(mDialog)
        mDialog.setMessage("Getting Location Details...")

//        val mProgressDialog = DelayedProgressDialog()
//        mProgressDialog.isCancelable = false
//        mProgressDialog.show(supportFragmentManager, "progress")
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        mService.getlocation("City").enqueue(object :Callback<List<Location>>{
            override fun onFailure(call: Call<List<Location>>, t: Throwable) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Toast.makeText(this@LoginActivity,"Fail to load location",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Location>>, response: Response<List<Location>>) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                if (response.isSuccessful && response.body() != null) {
                    val mList = response.body()
                    if (mList!!.isNotEmpty())
                        DbHelper(this@LoginActivity).location(mList
                                as ArrayList<Location>?)

                }
              //  mProgressDialog.cancel()
            }
        })
    }

    /**
     * Insert Station
     */
    private fun insertStation() {

        Common.showDialog(mDialog)
        mDialog.setMessage("Getting Station Details...")
//
//        val mProgressDialog = DelayedProgressDialog()
//        mProgressDialog.isCancelable = false
//        mProgressDialog.show(supportFragmentManager, "progress")

        mService.getFinalDescription(user.text.toString().trim()).enqueue(object : Callback<List<GetFinalDescModel>> {
                    override fun onResponse(call: Call<List<GetFinalDescModel>>, response: Response<List<GetFinalDescModel>>) {
                        if (response.isSuccessful && response.body() != null) {
                            val mList = response.body()
                            if (mList!!.isNotEmpty()){
                                DbHelper(this@LoginActivity).insertFinalDescription(mList
                                        as ArrayList<GetFinalDescModel>?)}
                            else
                            {
                                toast_e("Final list is empty")
                            }

                        }
                        else
                        {
                            Toast.makeText(this@LoginActivity,"no response",Toast.LENGTH_LONG).show()
                        }
//                        mProgressDialog.cancel()
                    }

                    override fun onFailure(call: Call<List<GetFinalDescModel>>, t: Throwable) {
                        t.printStackTrace()
                        toast_e("Server Not Responding your data, try again after some time")
//                        mProgressDialog.cancel()
                    }
                })
    }

    private fun insertReports() {
        Common.showDialog(mDialog)
        mDialog.setMessage("Getting Reports Details...")
//        val mProgressDialog = DelayedProgressDialog()
//        mProgressDialog.isCancelable = false
//        mProgressDialog.show(supportFragmentManager, "progress")

        mService.getreportSp(user.text.toString().trim())
                .enqueue(object : Callback<List<ReportDetailsModel>> {
                    override fun onResponse(call: Call<List<ReportDetailsModel>>, response: Response<List<ReportDetailsModel>>) {
                        if (response.isSuccessful && response.body() != null) {
                            val mList = response.body()
                            if (mList!!.isNotEmpty()) {
                                for (m in mList) {
                                    val consult:String
                                    if(m.consultantname.equals("")||m.consultantname.isNullOrBlank())
                                    {
                                        consult="NA";
                                    }
                                    else
                                    {
                                        consult=m.consultantname.toString()
                                    }
                                    DbHelper(this@LoginActivity).insertReportSP(
                                            m.olName, m.clientRegnum, m.reportNo, m.vendorName, m.pONumber, m.empDt,
                                            m.empCode, m.pOCopy, consult, m.qAPCopy, m.standardCopy, m.empName, m.station,m.projectType)
                                }
                                if (cb_rememmber_me.isChecked) {
                                    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.ENGLISH)
                                    // Decode: passwd = new String( Base64.decode( passwd, Base64.DEFAULT ) );
                                    preferences.edit()
                                            .putString("user_name", user.text.toString().trim { it <= ' ' })
                                            .putString("pass", Base64.encodeToString(pass.text.toString().toByteArray(),
                                                    Base64.DEFAULT))
                                            .putString("station", mList[0].station)
                                            .putString("emp_name", mList[0].empName)
                                            .putString("login_time", sdf.format(Date()))
                                            .apply()
                                }
                            }
                            val i = Intent(this@LoginActivity, MainActivity::class.java)
                            Common.disableDialog(mDialog)
                            mDialog.dismiss()
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            i.putExtra("user_name", user.text.toString())
                            i.putExtra("pass", pass.text.toString())
                       //     mProgressDialog.cancel()
                            toast("Login Successfully")
                            handleSuccessAnimation(i)
                        }
                      //  mProgressDialog.cancel()
                    }

                    override fun onFailure(call: Call<List<ReportDetailsModel>>, t: Throwable) {
                       // mProgressDialog.cancel()
                        toast_e("fail to go main activity,please try after some time")
                        Common.disableDialog(mDialog)
                        mDialog.dismiss()
                        t.printStackTrace()
                    }
                })
    }


    /* Animate Screen After login Success */
    private fun handleSuccessAnimation(i: Intent) {
        springForce = SpringForce(0f)
        rl_login.pivotX = 0f
        rl_login.pivotY = 0f
        val springAnim = SpringAnimation(rl_login, DynamicAnimation.ROTATION).apply {
            springForce.dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
            springForce.stiffness = SpringForce.STIFFNESS_VERY_LOW
        }
        springAnim.spring = springForce
        springAnim.setStartValue(80f)
        springAnim.addEndListener { _,
                                    _, _, _ ->
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels.toFloat()
            val width = displayMetrics.widthPixels
            rl_login.animate()
                    .setStartDelay(1)
                    .translationXBy(width.toFloat() / 2)
                    .translationYBy(height)
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(p0: Animator?) {}

                        override fun onAnimationEnd(p0: Animator?) {
                            finish()
                            startActivity(i)
                            overridePendingTransition(0, 0)
                        }

                        override fun onAnimationCancel(p0: Animator?) {}

                        override fun onAnimationStart(p0: Animator?) {

                        }

                    })
                    .setInterpolator(DecelerateInterpolator(1f))
                    .start()
        }
        springAnim.start()


    }


    override fun onStop() {
        super.onStop()
        mDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable.clear()
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}