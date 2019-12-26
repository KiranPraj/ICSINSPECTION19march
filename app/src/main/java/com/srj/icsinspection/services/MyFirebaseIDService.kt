package com.srj.icsinspection.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build

import android.util.Log
import android.util.Log.i
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.srj.icsinspection.R
import com.srj.icsinspection.activity.LoginActivity
import com.srj.icsinspection.utils.NotificationHelper
import java.util.*


class MyFirebaseIDService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFirebaseIDService"
    }

    override fun onNewToken(tokan: String?) {
        super.onNewToken(tokan)
        Log.d(TAG, "Refreshed token: " + tokan);
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage?.data != null) {

            if (remoteMessage.notification != null)
                i(TAG, "Body: ${remoteMessage.notification!!.body}")

            // check if message contains a data payload
            if (remoteMessage.data.isNotEmpty()) {
                i(TAG, "From: " + remoteMessage.from)
                i(TAG, "Message data payload: " + remoteMessage.data + remoteMessage.data["msg"])
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                sendNotificationToAPi26(remoteMessage)
            else
                sendNotification(remoteMessage)
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {

        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val resultIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val data = remoteMessage.data
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(ContextCompat.getColor(applicationContext, R.color.colorAccent))
                .setContentTitle(data["title"].toString())
                .setContentText(data["msg"].toString())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(resultIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotificationToAPi26(remoteMessage: RemoteMessage?) {
        val mHelper = NotificationHelper(this)
        val mBuilder: Notification.Builder
        val data = remoteMessage?.data
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        mBuilder = mHelper.getICSNotification(data?.get("title").toString(), data?.get("msg").toString(), defaultSoundUri)
        mHelper.manager.notify(Random().nextInt(), mBuilder.build())
    }
}
