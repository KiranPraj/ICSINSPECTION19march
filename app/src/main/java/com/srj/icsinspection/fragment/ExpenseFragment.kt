package com.srj.icsinspection.fragment


import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ClipData
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import java.util.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import com.srj.icsinspection.BuildConfig
import com.srj.icsinspection.R
import com.srj.icsinspection.adapter.ITopicClickListener
import com.srj.icsinspection.adapter.OutStationAdapter
import com.srj.icsinspection.adapter.WeeklyExpenseAdapter
import com.srj.icsinspection.dbhelper.DbHelper
import com.srj.icsinspection.model.OutStationExpensesModel
import com.srj.icsinspection.model.WeeklyExpenseModel
import com.srj.icsinspection.utils.Common
import com.srj.icsinspection.utils.FileManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_expense.*
import kotlinx.android.synthetic.main.fragment_expense.view.*


import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import kotlin.collections.ArrayList



class ExpenseFragment:Fragment(), ITopicClickListener, OutStationAdapter.IListnerCallback {
    override fun addrowcallback(text: TextView, date1: String, particularExpenses1: String, totalAmount1: String, igst1: String, cgst1: String, sgst1: String, gstNo1: String, serviceProvider1: String, invAmount1: String, clientNo1: String, browseFile1: String, paid1: String)
    {
        if(date1!="")
        {
            DbHelper(mContext).insertinExpenseTable(particularExpenses1,totalAmount1,igst1,cgst1,sgst1,gstNo1,serviceProvider1,invAmount1,clientNo1,browseFile1,paid1,date1)
            adddefaultrowforweeklyexpenses()
        }
        else
        {
            Toast.makeText(context,"Previous Row is Empty,fill it firstly ",Toast.LENGTH_LONG).show()
        }




    }

    override fun deleterowcallback1(addrow_textview: TextView, model: OutStationExpensesModel,type:Int,gst_no:String) {
        val builder = AlertDialog.Builder(mContext)
        //set title for alert dialog
        builder.setTitle(R.string.dialogTitle)
        //set message for alert dialog
        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            if (type==1)
            {
                DbHelper(mContext).deleteBoardingOutstationRow(gst_no)
                outstation_list.remove(model)
                // adapter_outstation.notifyItemRemoved(outstation_list.size)
                adapter_outstation_boarding.notifyDataSetChanged()
            }
            else
            {
                DbHelper(mContext).deleteLocalOutstationRow(gst_no)
                local_outstationlist.remove(model)
                //  adapter_outstation.notifyItemRemoved(local_outstationlist.size)
                 adapter_outstation_local.notifyDataSetChanged()
            }


        }

        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
   }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun adddefaultrowforweeklyexpenses() {

       weekly_list.clear()
       addWeeklyRow()
       socVPModel = WeeklyExpenseModel("", "", "", "", "", "", "", "", "", "", "", "", "")
        (weekly_list as ArrayList<WeeklyExpenseModel>).add(socVPModel)
        mAdapter = WeeklyExpenseAdapter(mContext, weekly_list, this@ExpenseFragment)
        mView.rv_expenses.setLayoutManager(LinearLayoutManager(mContext))
        mView.rv_expenses.setAdapter(mAdapter)
        mAdapter.notifyDataSetChanged()

    }

    private fun adddefaultrowforOutstationLocal()
    {
        //outstation_list.clear()
        local_outstationlist.clear()
        addrowlocaloutstation()
        val local_outstation_socVPModel2 = OutStationExpensesModel(0,"", "", "", "", "", "", "", "", "", "", "", "", "")
       local_outstationlist.add(local_outstation_socVPModel2)
        adapter_outstation_local = OutStationAdapter(0,mContext,local_outstationlist, this@ExpenseFragment)
        mView.rv_expenses.setLayoutManager(LinearLayoutManager(mContext))
        mView.rv_expenses.setAdapter(adapter_outstation_local)
        adapter_outstation_local.notifyDataSetChanged()

    }

    private fun adddefaultrowForoutStationBoarding()
    {
       // local_outstationlist.clear()
        outstation_list.clear()
        addOutstationRow()
       socVPModel1 =
                OutStationExpensesModel(1,"", "", "", "", "", "", "", "", "", "", "", "")
        outstation_list.add(socVPModel1)
        adapter_outstation_boarding = OutStationAdapter(1,mContext, outstation_list, this@ExpenseFragment)
        mView.rv_expenses_outstation.setLayoutManager(LinearLayoutManager(mContext))
        mView.rv_expenses_outstation.setAdapter(adapter_outstation_boarding)
        adapter_outstation_boarding.notifyDataSetChanged()
    }

    override fun addrowcallback1(delete_textview: TextView, location: String, todate: String, fromdate: String, expenses: String, igst: String, cgst: String, sgst: String, gstno: String, serviceprovider: String, paidby: String, invoice: String, narration: String, chosefile: String, type: Int)
    {
         if (type ==1)
         {
             if (location!="")
             {
                 //addrowlocaloutstation()
                 DbHelper(mContext).insertInBoardingOutstationTable(location,expenses,igst,cgst,sgst,gstno,serviceprovider,paidby,invoice,narration,chosefile)
                 adddefaultrowForoutStationBoarding()
             }
             else
             {
                 Toast.makeText(context,"Fill Previous Row First...",Toast.LENGTH_LONG).show()
             }


         }
         else
         {
             if (fromdate!=""&& todate!="")
             {
                 DbHelper(mContext).insertInLocalOutstationTable(fromdate,todate,expenses,igst,cgst,sgst,gstno,serviceprovider,paidby,invoice,narration,chosefile)
                 adddefaultrowforOutstationLocal()
             }
             else
             {
                 Toast.makeText(context,"Fill Previous Row First...",Toast.LENGTH_LONG).show()
             }

         }

    }

    override fun deleterowcallback(text: TextView, selecteddate: String,postion: WeeklyExpenseModel) {
        val builder = AlertDialog.Builder(mContext)
        //set title for alert dialog
        builder.setTitle(R.string.dialogTitle)
        //set message for alert dialog
        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->

         //Toast.makeText(mContext,"reached here"+model.gst_no,Toast.LENGTH_SHORT).show()
         DbHelper(mContext).deleteweeklyrow(selecteddate)
            weekly_list.remove(postion)
         //   mAdapter.notifyItemRemoved(model)
           // mAdapter.notifyItemRangeChanged(Postion, weekly_list.size)
            mAdapter.notifyDataSetChanged()

        }

        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->

        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun photoCallback(btnPhoto: TextView) {

        checkPermissions(btnPhoto)
    }


    private lateinit var mContext: Context
    private lateinit var mView: View
    private lateinit var mActivity: FragmentActivity
    private lateinit var master_admin: String
    private lateinit var user: String
   // private lateinit var mLoginPreference: LoginPreference
    internal lateinit var picker: DatePickerDialog
    var radiobtn_text: String? = null
    private lateinit var weekly_list: ArrayList<WeeklyExpenseModel>
    private lateinit var outstation_list: ArrayList<OutStationExpensesModel>
    private lateinit var local_outstationlist: ArrayList<OutStationExpensesModel>
    private lateinit var mAdapter: WeeklyExpenseAdapter
    private lateinit var adapter_outstation_boarding: OutStationAdapter
    private lateinit var adapter_outstation_local: OutStationAdapter
    private val mRandom = Random()
    val viewHolder: RecyclerView.ViewHolder? = null
    private val mService by lazy { Common.getAPI() }
    private lateinit var imageBtnFile: File

    private lateinit var imageToUploadUri: Uri
    private lateinit var btnPhoto: TextView
    private var mDatabase: SQLiteDatabase? = null
    private var mHelper: DbHelper? = null
    private lateinit var socVPModel:WeeklyExpenseModel
    private lateinit var local_outstation_socVPModel2:OutStationExpensesModel
    private lateinit var socVPModel1:OutStationExpensesModel

    companion object {

        fun newInstance(): ExpenseFragment {
            return ExpenseFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mView = inflater.inflate(R.layout.fragment_expense, container, false)
        mContext = mView.context
        mActivity = activity as FragmentActivity
        mHelper = DbHelper(mContext)

        mView.selectdate.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)

            picker = DatePickerDialog(mContext,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        selectdate.text = (monthOfYear + 1).toString() + "-" + dayOfMonth + "-" + year
                    }, year, month, day
            )
            picker.datePicker.maxDate = cldr.timeInMillis
            picker.show()
            var calenderdate = selectdate.text.toString()

        }

        mView.text_bording.setOnClickListener {
            if (mView.linear_recycle.visibility == View.VISIBLE) {
                mView.linear_recycle.visibility = View.GONE
            } else {
                mView.linear_recycle.visibility = View.VISIBLE
            }

        }

        mView.text_local.setOnClickListener {
            if (mView.linear_recycle_local.visibility == View.VISIBLE) {
                mView.linear_recycle_local.visibility = View.GONE
            } else {
                mView.linear_recycle_local.visibility = View.VISIBLE
            }
        }

        init(mView)



        return mView
    }

    private fun checkPermissions(btnPhoto: TextView) {
        //check Permission

        Dexter.withActivity(mContext as Activity?)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        if (report!!.areAllPermissionsGranted()) {
                            pickerDialog(btnPhoto)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                            permissions: MutableList<PermissionRequest>?,
                            token: PermissionToken?
                    ) {
                        permissionDenied()
                    }

                }).check()
    }

    private fun howSettingDialog() {
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO Sertting") { dialog, _ ->
            run {
                dialog.dismiss()
                openSettings();
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> { dialog.cancel() } }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", mContext.getPackageName(), null)
        intent.setData(uri)
        startActivityForResult(intent, 101)
    }

    private fun permissionDenied() {
        SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                .with(rel, "Camera and audio access is needed to take pictures of your dog")
                .withOpenSettingsButton("Settings")
                .withCallback(object : Snackbar.Callback() {
                    override fun onShown(sb: Snackbar?) {
                        sb!!.setText("Permission Required, Please Give Permission")
                        howSettingDialog()

                    }

                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        transientBottomBar!!.setText("Permission Needed Compulsory")

                    }
                })
                .build();
    }

    private fun pickerDialog(btnPhoto: TextView) {
        this@ExpenseFragment.btnPhoto = btnPhoto
        val view = layoutInflater.inflate(R.layout.griddialog_filechooser, null)
        val cameraLinearLayout = view.findViewById(R.id.ll_camera) as LinearLayout
        val docLinearLayout = view.findViewById(R.id.ll_doccument) as LinearLayout
        val dialog = AlertDialog.Builder(mContext)
                .setView(view)
                .setPositiveButton(null, null)
                .setNegativeButton(null, null)

                .create()
        cameraLinearLayout.setOnClickListener {
            dialog.dismiss()
            pickImage(btnPhoto)
        }
        docLinearLayout.setOnClickListener {
            dialog.dismiss()
            picDocuments(btnPhoto)
        }

        dialog.show()
    }

    private val DOCCUMENT_REQUEST_CODE: Int = 2102
    private val IMAGE_REQUEST_CODE: Int = 2103

    private fun picDocuments(btnPhoto: TextView) {

        try {
            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
            //  val fileIntentt=Intent(Intent.ACTION_PICK,Intent.ACTION_CHOOSER)
            //                        Uri uri = Uri.parse(Environment.getRootDirectory().getAbsolutePath());
            //                        fileIntent.setData(uri);
            fileIntent.type = "image/*|application/pdf"
            // fileIntent.type="image/*"
            //   fileIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)

            val extraMimeTypes = arrayOf(
                    "image/*",
                    "audio/*",
                    "video/*",
                    "application/*",
                    "application/pdf",
                    "application/msword",
                    "application/vnd.ms-powerpoint",
                    "application/vnd.ms-excel",
                    "application/zip",
                    "audio/x-wav|text/plain",
                    "application/msword",
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.template",
                    "application/vnd.ms-word.document.macroEnabled.12",
                    "application/vnd.ms-word.template.macroEnabled.12",
                    "application/vnd.ms-excel",
                    "application/vnd.ms-excel",
                    "application/vnd.ms-excel",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.template",
                    "application/vnd.ms-excel.sheet.macroEnabled.12",
                    "application/vnd.ms-excel.template.macroEnabled.12",
                    "application/vnd.ms-excel.addin.macroEnabled.12",
                    "application/vnd.ms-excel.sheet.binary.macroEnabled.12",
                    "application/vnd.ms-powerpoint",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                    "application/vnd.openxmlformats-officedocument.presentationml.template",
                    "application/vnd.openxmlformats-officedocument.presentationml.slideshow",
                    "application/vnd.ms-powerpoint.addin.macroEnabled.12",
                    "application/vnd.ms-powerpoint.presentation.macroEnabled.12",
                    "application/vnd.ms-powerpoint.template.macroEnabled.12",
                    "application/vnd.ms-powerpoint.slideshow.macroEnabled.12",
                    "application/vnd.ms-access"
            )
            fileIntent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes)
            //   fileIntent.setAction(Intent.ACTION_OPEN_DOCUMENT)
            startActivityForResult(fileIntent, DOCCUMENT_REQUEST_CODE)
        } catch (e: Exception) {

            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }


    private fun pickImage(btnPhoto: TextView) {
        val dir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/ICS Connect/Images/"
        val newdir = File(dir)
        newdir.mkdirs()
        val file = dir + "Connect_" + DateFormat.format("yyyy-MM-dd_hhmmss", Date()).toString() + ".jpg"

        imageBtnFile = File(file)
        try {
            imageBtnFile.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        imageToUploadUri = FileProvider.getUriForFile(
                mContext, BuildConfig.APPLICATION_ID + ".provider", imageBtnFile
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUploadUri)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            cameraIntent.clipData = ClipData.newRawUri("", imageToUploadUri)
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivityForResult(cameraIntent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            IMAGE_REQUEST_CODE -> {
                imageActivityResult(btnPhoto)
            }
            DOCCUMENT_REQUEST_CODE -> {
                documentResultHandler(resultCode, data, btnPhoto)
            }
        }
    }

    private fun imageActivityResult(btnPhoto: TextView) {
        run {

            // Log.i(RaiseQueryActivity.TAG, "onActivityResult: $imageToUploadUri")

            try {
                object : AsyncTask<Void, Void, String>() {
                    override fun doInBackground(vararg params: Void): String {

                        val path = compressImage(imageBtnFile.toString())

                        // Log.i(RaiseQueryActivity.TAG, "doInBackground: path: $path")

                        return path
                    }

                    override fun onPostExecute(path: String) {

                        try {
                            if (path != null) {
                                btnPhoto.apply {
                                    text = "Attached"
                                    setBackgroundColor(
                                            ContextCompat.getColor(
                                                    mContext, android.R.color.holo_green_light
                                            )
                                    )
                                    btnPhoto.tag = path
                                    Toast.makeText(
                                            mContext, "File Attached Successfully", Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } else
                                Toast.makeText(
                                        mContext, "Failed to Attach File", Toast.LENGTH_SHORT
                                ).show()


                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(
                                    mContext, "Failed to Attach File", Toast.LENGTH_SHORT
                            ).show()
                        }


                    }
                }.execute()
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }

    // compress images
    fun compressImage(imageUri: String): String {

        val filePath = getRealPathFromURI(imageUri)
        var scaledBitmap: Bitmap? = null

        val options = BitmapFactory.Options()

        //      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        //      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        //      max Height and width values of the compressed image is taken as 816x612

        val maxHeight = 1500.0f
        val maxWidth = 2000.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

        //      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()

            }
        }

        //      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

        //      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

        //      this options allow android to claim the bitmap memory if it runs low on memory
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            //          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap!!)
        canvas.matrix = scaleMatrix
        canvas.drawBitmap(bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))

        //      check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)

            val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0
            )
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(
                    scaledBitmap, 0, 0,
                    scaledBitmap.width, scaledBitmap.height, matrix,
                    true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null
        val filename = getFilename()
        try {
            out = FileOutputStream(filename)

            //          write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return filename
    }

    fun getFilename(): String {
        val file = File(Environment.getExternalStorageDirectory().path, "ICS Connect/")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath + "/" + "Connect_" + UUID.randomUUID().toString() + "_IMG_" + System.currentTimeMillis() + ".jpg"
    }

    private fun getRealPathFromURI(contentURI: String): String? {
        val contentUri = Uri.parse(contentURI)
        Log.i(TAG, "getRealPathFromURI: $contentUri")


        val cursor = mContext.getContentResolver().query(contentUri, null, null, null, null)
        if (cursor == null) {
            return contentUri.path
        } else {
            cursor!!.moveToFirst()
            val index = cursor!!.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            //cursor.close();
            return cursor!!.getString(index)
        }
    }

    fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int,
            reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }

        return inSampleSize
    }

    /**
     * Handle File Doc image button clicked Result
     *
     * @param resultCode : onActivityResult resultcode
     * @param data       : onActivityResult intent data
     * @param fileButton : Button where to setting tag, changing background, text
     */
    private fun documentResultHandler(resultCode: Int, data: Intent?, fileButton: TextView) {
        if (resultCode == AppCompatActivity.RESULT_OK && data != null) {

            val selectedImage = data.data
            if (selectedImage != null) {
                val picturePath = FileManager.getPath(mContext, selectedImage)
                //     Log.i(RaiseQueryActivity.TAG, "documentResultHandler: " + picturePath!!)
                try {
                    val file = File(picturePath)
                    if (file.exists()) {
                        if (file.length() / 1024 >= 5120) { //15 mb
                            Toast.makeText(mContext, "File Size limit is upto 5MB", Toast.LENGTH_LONG)
                                    .show()
                            return
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

                println("picturePath +" + picturePath)  //path of sdcard
                if (picturePath != null && picturePath != "") {
                    fileButton.apply {
                        setBackgroundColor(
                                ContextCompat.getColor(
                                        mContext, android.R.color.holo_green_light
                                )
                        )
                        btnPhoto.tag = picturePath
                        text = mContext.getString(R.string.attached)
                        Toast.makeText(
                                mContext, "File Attached Successfully", Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    fileButton.apply {
                        setBackgroundColor(
                                ContextCompat.getColor(
                                        mContext, android.R.color.white
                                )
                        )
                        btnPhoto.tag = picturePath
                        text = mContext.getString(R.string.not_attached)

                        Toast.makeText(
                                mContext, "Failed To File Attached", Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

        } else
            Toast.makeText(
                    mContext,
                    getString(R.string.error_file_executong), Toast.LENGTH_LONG
            ).show()


    }


    private fun init(mView: View?) {
        mContext = mView!!.context
        weekly_list = ArrayList<WeeklyExpenseModel>()
        outstation_list = ArrayList<OutStationExpensesModel>()
       local_outstationlist = ArrayList<OutStationExpensesModel>()
        addWeeklyRow()
        mView.rv_expenses_outstation.visibility = View.GONE
        mView.expenses_radio_grp.check(R.id.r_first)
        radiobtn_text = mView.r_first.text.toString()
        mView.expenses_radio_grp.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radio: RadioButton = mView.findViewById(checkedId)
                    radiobtn_text = radio.text.toString()
                    Toast.makeText(mContext, " On checked change : $radiobtn_text",
                            Toast.LENGTH_SHORT).show()
                    if (radiobtn_text == "NA") {
                        mView.rv_expenses.visibility = View.GONE
                        mView.linear_na.visibility = View.VISIBLE
                          mView.rv_expenses_outstation.visibility = View.GONE
                        mView.card_for_boarding.visibility = View.GONE
                        mView.card_for_local.visibility = View.GONE

                    } else if (radiobtn_text == "OutStation") {
                        weekly_list.clear()
                       // outstation_list.clear()
                       // local_outstationlist.clear()
                        mView.table_row_local_weekly.visibility=View.GONE
                        mView.table_row_header_localoutstation.visibility=View.VISIBLE
                        mView.rv_expenses.visibility = View.VISIBLE
                        mView.linear_na.visibility = View.VISIBLE
                        mView.rv_expenses_outstation.visibility = View.VISIBLE
                        mView.card_for_boarding.visibility = View.VISIBLE
                        mView.card_for_local.visibility = View.VISIBLE
                        //expensedetails(radiobtn_text!!)
                        addrowlocaloutstation() // local outstation
                        addOutstationRow() //boarding


                    } else {
                        mView.table_row_local_weekly.visibility=View.VISIBLE
                        mView.table_row_header_localoutstation.visibility=View.GONE
                      //  mView.btn_removenewrow.visibility = View.VISIBLE
                        mView.rv_expenses.visibility = View.VISIBLE
                        mView.linear_na.visibility = View.VISIBLE
                       // mView.btn_addnewrow.visibility = View.VISIBLE
                        //  mView.btn_addnewrow_out.visibility = View.GONE
                      //  mView.btn_removenewrow_out.visibility = View.GONE
                        mView.rv_expenses_outstation.visibility = View.GONE
                        mView.card_for_boarding.visibility = View.GONE
                        mView.card_for_local.visibility = View.VISIBLE
                        weekly_list.clear()
                        outstation_list.clear()
                       local_outstationlist.clear()
                       addWeeklyRow()
                       // adddefaultrowforweeklyexpenses()  //added

                    }


                })


    }



    private fun addOutstationRow() {
       // local_outstationlist.clear()
     //   var cursor=DbHelper(mContext).outstationBoardingExpesesrow

        var cursor=DbHelper(mContext).getOutstationBoardingExpesesrow()
        if (cursor.count<1)
        {
            socVPModel1 = OutStationExpensesModel(1,"", "", "", "", "", "", "", "", "", "", "", "")
            outstation_list.add(socVPModel1)
        }
        else
        {
            for (i in 0..cursor.count-1)
            {
                if (cursor.count>=i)
                {
                    cursor.moveToNext()
                   // var fromdate= cursor.getString(cursor.getColumnIndex("from_local"))
                 //   var todate= cursor.getString(cursor.getColumnIndex("to_local"))

                    var expenses=cursor.getString(cursor.getColumnIndex("expenses"))
                    var location=cursor.getString(cursor.getColumnIndex("location"))
                    var igst= cursor.getString(cursor.getColumnIndex("igst"))
                    var cgst= cursor.getString(cursor.getColumnIndex("cgst"))
                    var sgst= cursor.getString(cursor.getColumnIndex("sgst"))
                    var gst_no= cursor.getString(cursor.getColumnIndex("gst_no"))
                    var service_provdr=cursor.getString(cursor.getColumnIndex("service_provider"))
                    var paid_by=  cursor.getString(cursor.getColumnIndex("paid_by"))
                     var invoiceable= cursor.getString(cursor.getColumnIndex("invoiceable"))
                  var narration=  cursor.getString(cursor.getColumnIndex("narration"))
                    var chosefile= cursor.getString(cursor.getColumnIndex("choose_file"))
                    var chosefile1=  cursor.getString(cursor.getColumnIndex("choose_file"))

                    socVPModel1= OutStationExpensesModel(1,location,expenses,igst,cgst,sgst,gst_no,

                            service_provdr,paid_by,invoiceable,narration,chosefile,chosefile1)
                }
                else
                {
                    socVPModel1= OutStationExpensesModel( 1,"", "", "", "", "", "", "", "", "", "", "", "")

                }

                (outstation_list as ArrayList<OutStationExpensesModel>).add(socVPModel1)
            }
        }

        adapter_outstation_boarding = OutStationAdapter(1,mContext, outstation_list, this@ExpenseFragment)
        mView.rv_expenses_outstation.setLayoutManager(LinearLayoutManager(mContext))
        mView.rv_expenses_outstation.setAdapter(adapter_outstation_boarding)
        adapter_outstation_boarding.notifyDataSetChanged()






    }

    private fun addWeeklyRow() {
       // weekly_list.clear()  // added
        var cursor=DbHelper(mContext).getweeklyexpenserows()
        if (cursor.count<1)
        {
            socVPModel = WeeklyExpenseModel("", "", "", "", "", "", "", "", "", "", "", "", "")
            (weekly_list as ArrayList<WeeklyExpenseModel>).add(socVPModel)
        }
        else
        {
            for (i in 0..cursor.count-1)
            {
                if (cursor.count>=i)
                {
                    cursor.moveToNext()
                    var selectdate= cursor.getString(cursor.getColumnIndex("select_date"))
                    var particular_expenses= cursor.getString(cursor.getColumnIndex("particular_expenses"))
                    var total_amnt=cursor.getString(cursor.getColumnIndex("total_amount"))
                    var igst= cursor.getString(cursor.getColumnIndex("igst"))
                    var cgst= cursor.getString(cursor.getColumnIndex("cgst"))
                    var sgst= cursor.getString(cursor.getColumnIndex("sgst"))
                    var gst_no= cursor.getString(cursor.getColumnIndex("gst_no"))
                    var service_provdr=cursor.getString(cursor.getColumnIndex("service_provider"))
                    var paid_by=  cursor.getString(cursor.getColumnIndex("paid_by"))
                    var invAmount= cursor.getString(cursor.getColumnIndex("inv_amount"))
                    var clientNo=  cursor.getString(cursor.getColumnIndex("client_no"))
                    var chosefile= cursor.getString(cursor.getColumnIndex("choose_file"))
                    var chosefile1=  cursor.getString(cursor.getColumnIndex("choose_file"))

                    socVPModel=WeeklyExpenseModel(selectdate,particular_expenses,total_amnt,igst,cgst,sgst,gst_no,

                            service_provdr,paid_by,invAmount,clientNo,chosefile,chosefile1)
                }
                else
                {
                    socVPModel = WeeklyExpenseModel("", "", "", "", "", "", "", "", "", "", "", "", "")

                }

                (weekly_list as ArrayList<WeeklyExpenseModel>).add(socVPModel)
        }
        }
        //   weekly_list.remove(socVPModel)
        mAdapter = WeeklyExpenseAdapter(mContext, weekly_list, this@ExpenseFragment)
        mView.rv_expenses.setLayoutManager(LinearLayoutManager(mContext))
        mView.rv_expenses.setAdapter(mAdapter)
        mAdapter.notifyDataSetChanged()
    }

    private fun removerow(radiobtnText: String) {
        if (radiobtnText == "Weekly") {
            if (weekly_list.size < 2) {
                return
            }

            var socVPModel = weekly_list.get(weekly_list.size - 1)
            weekly_list.remove(socVPModel)
            mAdapter.notifyItemRemoved(weekly_list.size)
            mAdapter.notifyDataSetChanged()
        }

        else {
            if (outstation_list.size < 2) {
                return
            }
            var socVPModel = outstation_list.get(outstation_list.size - 1)
            outstation_list.remove(socVPModel)
            adapter_outstation_boarding.notifyItemRemoved(outstation_list.size)
            adapter_outstation_boarding.notifyItemRemoved(outstation_list.size)
            adapter_outstation_boarding.notifyDataSetChanged()
            adapter_outstation_boarding.notifyDataSetChanged()
        }
    }

    private fun removerow2() {
        if (local_outstationlist.size < 2) {
            return
        }
        var local_outstation_socVPModel2 =local_outstationlist.get(local_outstationlist.size - 1)
       local_outstationlist.remove(local_outstation_socVPModel2)
        adapter_outstation_local.notifyItemRemoved(local_outstationlist.size)
        adapter_outstation_local.notifyDataSetChanged()
    }

    private fun addrowlocaloutstation() {

        var cursor=DbHelper(mContext).getOutstationlocalExpesesrow()
        if (cursor.count<1)
        {
            local_outstation_socVPModel2 = OutStationExpensesModel(0,"", "",  "", "", "", "", "", "", "", "", "", "", "")
          // local_outstationlist.add(local_outstation_socVPModel2)
            (local_outstationlist as ArrayList<OutStationExpensesModel>).add(local_outstation_socVPModel2)
        }
        else
        {
            for (i in 0..cursor.count-1)
            {
                if (cursor.count>=i)
                {
                    cursor.moveToNext()
                    var fromdate= cursor.getString(cursor.getColumnIndex("from_local"))
                    var todate= cursor.getString(cursor.getColumnIndex("to_local"))

                   var expenses=cursor.getString(cursor.getColumnIndex("expenses"))
                    var igst= cursor.getString(cursor.getColumnIndex("igst"))
                    var cgst= cursor.getString(cursor.getColumnIndex("cgst"))
                    var sgst= cursor.getString(cursor.getColumnIndex("sgst"))
                    var gst_no= cursor.getString(cursor.getColumnIndex("gst_no"))
                    var service_provdr=cursor.getString(cursor.getColumnIndex("service_provider"))
                    var paid_by=  cursor.getString(cursor.getColumnIndex("paid_by"))
                    var invoiceable= cursor.getString(cursor.getColumnIndex("invoiceable"))
                   var narration=  cursor.getString(cursor.getColumnIndex("narration"))
                    var chosefile= cursor.getString(cursor.getColumnIndex("choose_file"))
                    var chosefile1=  cursor.getString(cursor.getColumnIndex("choose_file"))

                    local_outstation_socVPModel2= OutStationExpensesModel(0,fromdate,todate,expenses,igst,cgst,sgst,gst_no,

                            service_provdr,paid_by,invoiceable,narration,chosefile,chosefile1)
                }
                else
                {
                   local_outstation_socVPModel2 = OutStationExpensesModel(0,"", "", "", "", "", "", "", "", "", "", "", "", "")

                }

                (local_outstationlist as ArrayList<OutStationExpensesModel>).add(local_outstation_socVPModel2)
            }
        }

        adapter_outstation_local = OutStationAdapter(0,mContext,local_outstationlist, this@ExpenseFragment)
        mView.rv_expenses.setLayoutManager(LinearLayoutManager(mContext))
        mView.rv_expenses.setAdapter(adapter_outstation_local)
        adapter_outstation_local.notifyDataSetChanged()
    }
}
