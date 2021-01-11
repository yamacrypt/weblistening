package com.yamacrypt.webaudionovel

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.*

class ForegroundService : Service(), TextToSpeech.OnInitListener {
    private var tts : TextToSpeech? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(this, this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val context = applicationContext
        val channelId = "tts_channel"
        val title = context.getString(R.string.app_name)
        val requestCode = 1
        val pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        // 通知作成
        val notificationId = 1
        val notification = Notification.Builder(context, channelId)
            .setContentTitle(title)
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .setContentText("テキスト読み上げ")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setWhen(System.currentTimeMillis())
            .build()

        // foreground service実行
        startForeground(notificationId, notification)

        // 読み上げ実行
        val langLocale = intent!!.getSerializableExtra("langLocale") as Locale
        val inputText = intent.getStringExtra("input")
        speakText(langLocale, "inputText")

        return START_NOT_STICKY
    }

    // テキスト読み上げ
    private fun speakText(langLocale: Locale, inputText:String) {
        // 読み上げる言語を設定する
        tts!!.language = langLocale
        // 10回読み上げる
        for (i in 1..10) {
            tts!!.speak(inputText, TextToSpeech.QUEUE_ADD, null, "speech1")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        stopSelf()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            Log.d("TTS", "TextToSpeechが初期化されました。")

            // 音声再生のイベントリスナを登録
            //val listener : SpeechListener? = SpeechListener()
           // tts!!.setOnUtteranceProgressListener(listener)
        } else {
            Log.e("TTS", "TextToSpeechの初期化に失敗しました。")
        }
    }
}