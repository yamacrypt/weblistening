package com.yamacrypt.webaudionovel.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.media.session.MediaControllerCompat
import com.yamacrypt.webaudionovel.DataStore
import com.yamacrypt.webaudionovel.R
import com.yamacrypt.webaudionovel.ui.library.fileservice.FileChangeBroadcastReceiver

open class SleepTImerBroadcastReceiver(mediController:MediaControllerCompat) : BroadcastReceiver() {
    val mediaController=mediController
    //var am = getActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

    companion object {
        const val EXTRA_PATH = "com.yamacrypt.webaudionovel.sleep_timer"
        const val SLEEP_TIME = "sleep_Time"
    }
    var beforeIntent:Intent?=null
    override fun onReceive(context: Context?, intent: Intent?) {
        val filePath = intent?.extras?.getString(EXTRA_PATH)
        if (filePath.equals("stop")) {
            mediaController.transportControls.pause()
        }
        /*else {
            //アラーム用のPendingIntentを取得
            beforeIntent?.let {
                val pendingIntent = PendingIntent.getService(
                    context,
                    1,
                    it,
                    PendingIntent.FLAG_CANCEL_CURRENT
                )
                am.cancel(pendingIntent);
            }
            val broadcastIntent = Intent()
            broadcastIntent.action = context?.getString(R.string.sleep_timer_broadcast)
            broadcastIntent.putExtra(EXTRA_PATH, "stop")
            beforeIntent=broadcastIntent
            val target = intent?.extras?.getLong(SLEEP_TIME)!!
            val sender = PendingIntent.getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            //AlarmManagerを取得
            //次回サービス起動を予約
            am[AlarmManager.RTC, target] = sender
        }*/
        //DataStore.getSharedPreferences(context).edit().putLong(DataStore.sleepTime,time!!).apply();
    }
}