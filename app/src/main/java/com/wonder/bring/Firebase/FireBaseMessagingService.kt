package com.wonder.bring.Firebase

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.PendingIntent
import android.graphics.Color
import android.nfc.Tag
import com.wonder.bring.MainActivity
import com.wonder.bring.R

import android.os.PowerManager
import android.text.TextUtils
import java.io.UnsupportedEncodingException
import java.net.URLDecoder


class FireBaseMessagingService : FirebaseMessagingService() {



    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Log.d(TAG, token!!)


    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)


        val notification = remoteMessage!!.notification
        Log.d(TAG, "From: ${remoteMessage?.from}")

        // Check if message contains a data payload.
        remoteMessage?.data?.isNotEmpty()?.let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
        }


        // Check if message contains a notification payload.
        remoteMessage?.notification?.let {
            Log.d(TAG, "Message Notification Body:"+notification!!)


            var title = notification!!.title
            Log.d(TAG,title)
            var body = notification!!.body
           Log.d(TAG,body)

            try {
                if (!TextUtils.isEmpty(title)) title = URLDecoder.decode(title, "UTF-8")  // 한글깨짐으로 서버에서 URLEncoding해서 보냄..
                if (!TextUtils.isEmpty(body)) body = URLDecoder.decode(body, "UTF-8")
                Log.d(TAG,"여기로 들어옴 한글 변환")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                Log.d(TAG,"여기로 들어옴")
            }

            sendNotification(title!!, body!!)
        }

//        if (remoteMessage!!.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification()!!.getBody());
//        }
//
//        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
//        val wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG")
//        wakeLock.acquire(3000)
//




    }


    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
    }

    private fun sendNotification(title: String, body: String?) {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setVibrate(longArrayOf(1000, 1000))
            .setLights(Color.BLUE, 1, 1)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())


    }



    companion object {

        private val TAG = "MyFirebaseMsgService"


        fun isAppRunning(context: Context): Boolean {                        // 어플이 실행 중인지 확인 하는 함수.
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val procInfos = activityManager.runningAppProcesses
            for (i in procInfos.indices) {
                if (procInfos[i].processName == context.packageName) {
                    return true
                }
            }

            return false
        }
    }

}
