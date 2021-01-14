package com.yamacrypt.webaudionovel
import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView


class IntentReceiveActivity : Activity() {
    /** Called when the activity is first created.  */
    fun receiver(){
        val intent = intent
        val action = intent.action
        if (Intent.ACTION_SEND == action) {
            val extras = intent.extras
            if (extras != null) {
                val ext = extras.getCharSequence(Intent.EXTRA_TEXT)
                if (ext != null) {
                    //val textView1 =
                   //     findViewById<View>(R.id.textView1) as TextView
                   // textView1.text = ext
                }
            }
        }
    }
}