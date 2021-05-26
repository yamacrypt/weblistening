package com.yamacrypt.webaudionovel.tts

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yamacrypt.webaudionovel.Database.DBProvider
import com.yamacrypt.webaudionovel.Database.DBTableName
import com.yamacrypt.webaudionovel.Database.DictionaryDB
import com.yamacrypt.webaudionovel.Database.DictionaryPairModel
import com.yamacrypt.webaudionovel.R
import com.yamacrypt.webaudionovel.TTSController
import com.yamacrypt.webaudionovel.ui.library.urlfragment.getLibraryItemsFromURL
import kotlinx.android.synthetic.main.dictionary_list.view.*
import kotlinx.android.synthetic.main.fragment_files_list.*
import kotlinx.android.synthetic.main.menu.*
import kotlin.concurrent.thread

class DictionaryListDialog(url:String):DialogFragment() {
    val url=url
    lateinit var adapter:DictionaryListAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layout=layoutInflater.inflate(R.layout.dictionary_list,container,false)
        val database=DBProvider.of(DBTableName.dictionary,requireContext()) as DictionaryDB
        var generic=database.getAllBypath("root")
        if(url!="root") {
            val content = database.getAllBypath(url)
            generic=listOf(generic,content).flatten()

        }
        adapter=DictionaryListAdapter()
        adapter.onDeleteClickListener={deleteItem(it)}
        //adapter.updateData(generic)
        val recyclerView=layout.dictionaryView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter=adapter
        val runnable= Runnable {
           adapter.updateData(generic)
        }
        val handler =  Handler(Looper.getMainLooper());
        thread {
            handler.post(runnable)
        }
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("読み一覧")
               .setView(layout)
        return builder.create()
    }
    public fun deleteItem(item:DictionaryPairModel){
        val database=DBProvider.of(DBTableName.dictionary,requireContext()) as DictionaryDB
        database.delete(item)
        adapter.delete(item)
        TTSController.getInstance()?.updateDictionary();
    }
}