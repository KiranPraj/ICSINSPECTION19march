package com.srj.icsinspection.utils

import android.app.Fragment
import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty

fun Context.toast(message: CharSequence) =
        Toasty.success(this, message, Toast.LENGTH_SHORT).show()

fun Context.toast_e(message: CharSequence) =
        Toasty.error(this, message, Toast.LENGTH_LONG).show()

fun Context.toast_w(message: CharSequence) =
        Toasty.warning(this, message, Toast.LENGTH_LONG).show()

fun Context.toast_i(message: CharSequence) =
        Toasty.info(this, message, Toast.LENGTH_LONG).show()

fun Fragment.toast(message: CharSequence,mContext:Context) =
        Toasty.success(mContext, message, Toast.LENGTH_SHORT).show()