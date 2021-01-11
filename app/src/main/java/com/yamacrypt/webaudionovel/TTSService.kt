package com.yamacrypt.webaudionovel

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat


class TTSService : JobIntentService() {

    companion object {
        const val TAG = "MyJobIntentService"
        const val JOB_ID = 1001


    }
    lateinit var ttsController:TTSController
 /*   fun getTTSInstance(context:Context) {
        if(ttsController==null)
            ttsController=TTSController(context)
       // return ttsController
    }*/
    // 1
    fun enqueueWork(context: Context, work: Intent) {
       /* ttsController=TTSController(context)
        val notificationIntent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context, 0,
            notificationIntent, 0
        )

        val notification: Notification = Notification.Builder(context, "TAG")
            .setContentIntent(pendingIntent)
            .build()


        startForeground(1337, notification)*/
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name =" getString(R.string.channel_name)"
            val descriptionText =" getString(R.string.channel_description)"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("JOB_ID", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager =context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }*/

        enqueueWork(context, TTSService::class.java, JOB_ID, work)

    }

    // 2
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Job execution started")
    }

    // 3
    override fun onHandleWork(intent: Intent) {
       /* Log.d(TAG, "MSG: ${intent?.getStringExtra("MSG")}")
        for (i in 1..10) {
            Thread.sleep(1000)
            Log.d(TAG, "onHandleWork() : $i")
        }*/
    }

    // 4
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Job execution finished")
    }
}