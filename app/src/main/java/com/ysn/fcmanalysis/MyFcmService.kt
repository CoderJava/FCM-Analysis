package com.ysn.fcmanalysis

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

class MyFcmService : FirebaseMessagingService() {

    private val tag = javaClass.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(tag, "From: ${remoteMessage.from}")

        if (remoteMessage.data != null) {
            val remoteData = remoteMessage.data
            Log.d(tag, "remoteData: $remoteData")

            val jsonData = JSONObject(remoteData)
            createNotification(jsonData)
        }
    }

    override fun onNewToken(token: String) {
        Log.d(tag, "token: $token")
        super.onNewToken(token)
    }

    private fun createNotification(jsonData: JSONObject) {
        val titleNotification = jsonData.getString("title")
        val notificationChannelId = BuildConfig.APPLICATION_ID + ".myfcmservice"
        val notificationChannelName = "Testing FCM"

        val contentNotification = jsonData.getString("message")
        val alarmNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                notificationChannelId,
                notificationChannelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            alarmNotificationManager.createNotificationChannel(mChannel)
        }

        val alarmNotificationBuilder = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle(titleNotification)
            .setContentText(contentNotification)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setAutoCancel(true)
        alarmNotificationManager.notify(100, alarmNotificationBuilder.build())
    }
}
