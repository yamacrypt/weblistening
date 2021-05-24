package com.yamacrypt.webaudionovel.tts

import android.content.Context
import com.yamacrypt.webaudionovel.Database.DBProvider
import com.yamacrypt.webaudionovel.Database.DBTableName
import com.yamacrypt.webaudionovel.Database.DictionaryDB
import com.yamacrypt.webaudionovel.Database.StoryIndexDB

class TTSDictionaryBuilder(context: Context) {
    lateinit var baseHashmap:HashMap<String,String>
    val context=context
    init {
        val database=DBProvider.of(DBTableName.dictionary,context) as DictionaryDB
        baseHashmap=database.getDictionary("generic")
    }
    fun build(path:String): TTSDictionary {
        val database=DBProvider.of(DBTableName.dictionary,context) as DictionaryDB
        val storydatabase=DBProvider.of(DBTableName.storyindex,context) as StoryIndexDB
        var parentpath=path
        if(path!="root") {
            parentpath = storydatabase.getPARENTURL(path)
        }
        println(parentpath)
        val additionalHashmap=database.getDictionary(parentpath)
        additionalHashmap+=baseHashmap
        val baseDictionary=TTSDictionary(baseHashmap)
        return baseDictionary
    }
}