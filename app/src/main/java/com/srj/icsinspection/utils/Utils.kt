package com.srj.icsinspection.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

import com.srj.icsinspection.R
import java.text.SimpleDateFormat

object Utils {


    // checking network available or not
   public fun isNetworkAvailable(mActivity: FragmentActivity): Boolean {
        val connectivityManager = mActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    // refreshing the layout if connection is offline
    public fun inflateOfflineLayout(context: Context, frameLayout: FrameLayout) {
        frameLayout.removeAllViewsInLayout()
        frameLayout.addView(LayoutInflater.from(context).inflate(R.layout.offline_layout, frameLayout, false))
    }

     fun reverseString(s:String):String{
         val split = s.split(" ")
         var result = ""
         for (i in split.indices.reversed()) {
             result += split[i] + " "
         }
         return result
     }

}
