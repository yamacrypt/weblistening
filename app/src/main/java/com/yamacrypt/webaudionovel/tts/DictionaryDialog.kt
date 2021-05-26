package com.yamacrypt.webaudionovel.tts.layout.dictionary_dialog

//import androidx.fragment.app.DialogFragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.yamacrypt.webaudionovel.Database.DBProvider
import com.yamacrypt.webaudionovel.Database.DBTableName
import com.yamacrypt.webaudionovel.Database.DictionaryDB
import com.yamacrypt.webaudionovel.Database.DictionaryPairModel
import com.yamacrypt.webaudionovel.R
import com.yamacrypt.webaudionovel.TTSController
import com.yamacrypt.webaudionovel.tts.DictionaryListDialog
import kotlinx.android.synthetic.main.dictionary_dialog.*
import kotlinx.android.synthetic.main.dictionary_dialog.view.*
import kotlinx.android.synthetic.main.menu.*
import java.util.*


class DictionaryDialog(url:String)  : DialogFragment(){
     val url=url
     override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
         val layout=layoutInflater.inflate(R.layout.dictionary_dialog,container,false)
         val ttsController = TTSController.getInstance()
         layout.dic_test_before.setOnClickListener{
             ttsController.stop()
             speak(layout.dictionary_target.text.toString())//ttsController.speak(layout.dictionary_target.text.toString())
         }
         layout.dic_test_after.setOnClickListener{
             ttsController.stop()
             speak(layout.dictionary_read.text.toString())
         }
         val builder = AlertDialog.Builder(activity)
         builder.setView(layout)
            .setTitle("読み登録")
            .setNeutralButton("読み一覧", DialogInterface.OnClickListener{ dialog, which ->
                val dialogListFragment = DictionaryListDialog(url)
                dialogListFragment.show(childFragmentManager, "dictionaryList_dialog");
            })
            .setPositiveButton("登録する",DialogInterface.OnClickListener{dialog, which ->
                val database= DBProvider.of(DBTableName.dictionary,requireContext()) as DictionaryDB
                val target=layout.dictionary_target.text.toString()
                val read=layout.dictionary_read.text.toString()
                database.insertData(DictionaryPairModel(target,read,url))
                ttsController.updateDictionary();
            })
         return builder.create()
    }
    fun speak(text:String){
        val locale = Locale.forLanguageTag("ja")
        lateinit var tts:TextToSpeech
        tts = TextToSpeech(
            context
        ) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(locale)
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "TAG")
                //
                // speak(index);
                Log.i("TTS", "Initialization success.")
            } else {
                //Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}