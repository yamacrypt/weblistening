package com.yamacrypt.webaudionovel.tts

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.yamacrypt.webaudionovel.Database.DBProvider
import com.yamacrypt.webaudionovel.Database.DBTableName
import com.yamacrypt.webaudionovel.Database.DictionaryDB
import com.yamacrypt.webaudionovel.R
import kotlinx.android.synthetic.main.menu.*

class DictionaryListDialog(url:String):DialogFragment() {
    val url=url
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layout=layoutInflater.inflate(R.layout.dictionary_list,container,false)
        val database=DBProvider.of(DBTableName.dictionary,requireContext()) as DictionaryDB
        val generic=database.getDictionary("root")
        if(url!="root") {
            val content = database.getDictionary(url)
            generic+=content
        }
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("読み一覧")
               .setView(layout)
        return builder.create()
    }
}