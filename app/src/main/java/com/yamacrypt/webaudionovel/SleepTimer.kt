package com.yamacrypt.webaudionovel

import android.app.Activity
import android.os.CountDownTimer
import android.support.v4.media.session.MediaControllerCompat
import android.widget.Button
import android.widget.TextView

class SleepTimer(i: Long,activity: Activity) : CountDownTimer(i*1000,1000) {
    val sleepView: TextView = activity.findViewById(R.id.countDownTextView)
    val sleepButton: Button = activity.findViewById(R.id.sleepTimer)
    val sleepTime: Long = i
    val activity: Activity = activity
    init {
        sleepView?.isEnabled=true
        sleepButton?.isEnabled=false
        sleepView.text= (sleepTime).toString()
    }
    override fun onTick(p0: Long) {
        if(sleepView!=null)
            sleepView.text= (sleepTime-p0).toString()
    }

    override fun onFinish() {
        val mediaController = MediaControllerCompat.getMediaController(activity)
        mediaController.transportControls.pause()
        sleepView?.isEnabled=false
        sleepButton?.isEnabled=true
    }
}