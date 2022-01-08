package com.yamacrypt.webaudionovel.SleepTimer

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SleepTImerService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        return Service.START_NOT_STICKY
        //return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        //サービス終了時の記述
    }
    lateinit var sleepTimer: SleepTimer;
    fun execAlarm(sleepTime:Long) {
        //サービスの処理を記述
    }
}