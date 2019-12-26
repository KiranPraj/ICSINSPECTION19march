package com.srj.icsinspection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.crashlytics.android.Crashlytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import com.srj.icsinspection.activity.LoginActivity;
import com.srj.icsinspection.constants.DbConstant;
import com.srj.icsinspection.dbhelper.DbHelper;
import com.srj.icsinspection.fragment.DoneSyncFragment;
import com.srj.icsinspection.fragment.InspectionReportFragment;
import com.srj.icsinspection.fragment.SyncFragment;
import com.srj.icsinspection.handler.ApiService;
import com.srj.icsinspection.handler.SyncHandler;
import com.srj.icsinspection.model.DescriptionQuantityModel;
import com.srj.icsinspection.model.GetFinalDescModel;
import com.srj.icsinspection.model.ReportDetailsModel;
import com.srj.icsinspection.utils.Common;
import com.srj.icsinspection.utils.UpdateHelper;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.fabric.sdk.android.Fabric;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;*/


public class MainActivity extends AppCompatActivity implements UpdateHelper.OnUpdateCheckListener, SyncHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    //static FrameLayout mFrameLayout;
    // public static Context mContext;
    // private static MainActivity mainActivityRunningInstance;
    private SharedPreferences mPreference;
    private ApiService mService;
    private AlertDialog mDialog;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    final int[] a = {0};
    final int[] b = {0};
    final int[] c = {0};
    private DbHelper mHelper;
    private SQLiteDatabase mDatabase;
    ProgressDialog progressBar;

    SyncHandler mSyncHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        progressBar= new ProgressDialog(this);
        setSupportActionBar(mToolbar);
        mHelper = new DbHelper(MainActivity.this);
        mDatabase = mHelper.getWritableDatabase();
        mPreference = getSharedPreferences(getString(R.string.user), Context.MODE_PRIVATE);
        //mainActivityRunningInstance = this;
        //mFrameLayout = findViewById(R.id.main_container);
        //mSyncHandler = this;
        //mContext = this.getApplicationContext();
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        mService = Common.getAPI();
        mDialog = new SpotsDialog(this, "Syncing data..");
        requestStoragePermission();
        checkUpdate();

    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
        getMenuInflater().inflate(R.menu.toolbar_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void checkUpdate() {
        UpdateHelper.with(this)
                .onUpdateCheck(this)
                .check();
    }

    /* public static MainActivity getInstace() {
         return mainActivityRunningInstance;
     }
 */
    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.main_container, new InspectionReportFragment(), getString(R.string.frag_insp))
                                    .commit();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {

                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.frag_finding));
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void CheckOnUpdateCheckListener(final String uriApp) {

        new AlertDialog.Builder(this)
                .setTitle("New Version Available")
                .setMessage("Please update to new version to continue use")
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://" + uriApp));
                        startActivity(mIntent);
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        })
                .setCancelable(false)
                .show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.tb_logout:
                logoutHandler();
                break;
            case R.id.tb_home:
                HomeFragment();
                break;
            case R.id.tb_donesync:
                donesynchandler();
                break;
            case R.id.tb_synhandle:
                syncativity();
                break;
            case R.id.tb_newplan:
                progressBar.setTitle("Getting new plan");
                progressBar.setMessage("please wait....");
                progressBar.setCancelable(false);
                progressBar.show();
                fetchNewData();
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    private void donesynchandler() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new DoneSyncFragment(), getString(R.string.frag_insp))
                .commit();

    }

    private void fetchNewData() {
        mDialog.setTitle("Getting New Plan details");
        insertDescQuantity();
        mService.getFinalDescription(mPreference.getString("user_name", "-"))
                .enqueue(new Callback<List<GetFinalDescModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<GetFinalDescModel>> call, @NonNull Response<List<GetFinalDescModel>> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            List<GetFinalDescModel> mList = response.body();
                            Cursor cursor= mDatabase.rawQuery("SELECT "+DbConstant.REPORT_SP_ENTRY.Emp_Dt+" FROM "+DbConstant.REPORT_SP_ENTRY.TABLE_REPORT_SP +" ORDER BY "+DbConstant.REPORT_SP_ENTRY.Emp_Dt+" DESC LIMIT 1",null);
                            cursor.moveToFirst();

                            if (mList.size() > 0) {
                                new DbHelper(MainActivity.this).getNewDescriptionDataAfterInsert((ArrayList<GetFinalDescModel>) mList);
                            }
                            a[0] =1;
                            dialogclose();
                        }
                        else
                        {
                            progressBar.dismiss();
                            Toasty.error(MainActivity.this,"No paln Available or reponse error please logout and login ",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<GetFinalDescModel>> call, @NonNull Throwable t) {
                        progressBar.dismiss();
                        Toasty.error(MainActivity.this,"Network error ",Toast.LENGTH_SHORT).show();
                    }
                });


        mService.getreportSp(mPreference.getString("user_name", "-"))
                .enqueue(new Callback<List<ReportDetailsModel>>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onResponse(@NonNull Call<List<ReportDetailsModel>> call, @NonNull Response<List<ReportDetailsModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<ReportDetailsModel> mList = response.body();
                           Cursor cursor= mDatabase.rawQuery("SELECT "+DbConstant.REPORT_SP_ENTRY.Emp_Dt+" FROM "+DbConstant.REPORT_SP_ENTRY.TABLE_REPORT_SP +" ORDER BY "+DbConstant.REPORT_SP_ENTRY.Emp_Dt+" DESC LIMIT 1",null);
                            String date="";
                           if(cursor.getCount()>0)
                           {
                               cursor.moveToFirst();
                               date= cursor.getString(cursor.getColumnIndex(DbConstant.REPORT_SP_ENTRY.Emp_Dt));
                           }
                            String ndate="";
                           if(mList.size()>0)
                           {
                               ndate=mList.get(mList.size()-1).getEmpDt();
                           }
                           if(ndate.equals(date))
                            {
                                b[0] =1;
                                dialogclose();
                                Toasty.info(MainActivity.this, "no new data", Toast.LENGTH_SHORT).show();
                            }
                           else
                           {
                               if (mList.size() > 0){
                                   new DbHelper(getApplicationContext()).getNewDataAfterInsert(mList);
                               }
                               b[0] =1;
                               dialogclose();
                           }
                            //  mDialog.cancel();
                        }
                        else
                        {
                            progressBar.dismiss();
                            Toasty.error(MainActivity.this,"No paln Available or reponse error please logout and login ",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @SuppressLint("CheckResult")
                    @Override
                    public void onFailure(@NonNull Call<List<ReportDetailsModel>> call, @NonNull Throwable t) {
                        progressBar.dismiss();
                        Toasty.error(MainActivity.this,"Network error ",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void insertDescQuantity() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.


//        val mProgressDialog = DelayedProgressDialog()
//        mProgressDialog.isCancelable = false
//        mProgressDialog.show(supportFragmentManager, "progress")
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        mService.getquantitydescription(mPreference.getString("user_name", "-")).enqueue(new Callback<DescriptionQuantityModel>() {
            @Override
            public void onResponse(Call<DescriptionQuantityModel> call, Response<DescriptionQuantityModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + DbConstant.QuantityDescription_Entry.TABLE_QUANTITY_DESCRIPTION , null);
                    if(cursor.getCount()!=response.body().getDtqty().size())
                    {
                        long id=mDatabase.delete(DbConstant.QuantityDescription_Entry.TABLE_QUANTITY_DESCRIPTION,null,null);
                        if(id>0)
                        {
                            ArrayList<DescriptionQuantityModel.Dtqty> mList = response.body().dtqty;
                            new DbHelper(getApplicationContext()).QuantityDescription(mList);
                            c[0]=1;
                        }

                    }
                    else
                    {
                        c[0]=1;
                    }
                }
                    //do desc
            }

            @Override
            public void onFailure(Call<DescriptionQuantityModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Fail to load QUantitydesc",Toast.LENGTH_LONG).show();
                c[0]=1;
            }
        });
    }
    private void dialogclose() {
        if(a[0] ==1&& b[0] ==1&&c[0]==1)
        {
            progressBar.dismiss();
            Toasty.success(getApplicationContext(), "All data fetched", Toast.LENGTH_SHORT).show();
        }
    }

    private void syncativity() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new SyncFragment(), getString(R.string.frag_insp))
                .commit();
    }

    // Handle Logout menu item clicked
    private void logoutHandler() {
        String msg;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            msg = String.valueOf(Html.fromHtml(getString(R.string.logout_alert), Html.FROM_HTML_MODE_LEGACY));
        } else {
            msg = String.valueOf(getString(R.string.logout_alert1));
        }

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Please Note:")
                .setMessage(msg)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPreference.edit().clear().apply();
                        dialogInterface.dismiss();
                        new DbHelper(MainActivity.this).deleteReportAndFinal();
                        Toasty.success(MainActivity.this, "Logout Successfully", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    // Handle Sync Button Click
    private void HomeFragment() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new InspectionReportFragment(), getString(R.string.frag_insp))
                .commit();

    }


    @Override
    protected void onStart() {
        super.onStart();
        mDisposable.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }

    @Override
    public void syncCallback() {
        Log.i(TAG, "syncCallback: shoing progress");
        Common.showDialog(mDialog);
    }

    @Override
    public void completedSyncCallback() {
        Log.i(TAG, "completedSyncCallback: disabling progress");
        Common.disableDialog(mDialog);
    }

    @Override
    public void syncIRAgainCallback() {

        //sendIRData();
    }

    @Override
    public void syncIRANgainCallback() {
//        try {
////            sendQTYDATA();
////        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void cancelsyncCallback() {
        Log.i(TAG, "cancelsyncCallback: disabling progress");
        Common.disableDialog(mDialog);
        Toasty.error(MainActivity.this, "Error Occurred, Sync again", Toast.LENGTH_LONG).show();
    }
}

