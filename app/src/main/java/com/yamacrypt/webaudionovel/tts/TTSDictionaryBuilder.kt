package com.yamacrypt.webaudionovel.tts

import android.content.Context
import com.yamacrypt.webaudionovel.Database.DBProvider
import com.yamacrypt.webaudionovel.Database.DBTableName
import com.yamacrypt.webaudionovel.Database.DictionaryDB
import com.yamacrypt.webaudionovel.Database.StoryIndexDB

class TTSDictionaryBuilder(context: Context) {
    val context=context
    fun build(path:String): TTSDictionary {
        val database=DBProvider.of(DBTableName.dictionary,context) as DictionaryDB
        val baseHashmap=database.getDictionary("root")

        if(path!="root") {
            val storydatabase=DBProvider.of(DBTableName.storyindex,context) as StoryIndexDB
            val parentpath = storydatabase.getPARENTURL(path)
            println(parentpath)
            val additionalHashmap=database.getDictionary(parentpath)
            baseHashmap+=additionalHashmap
        }
        return TTSDictionary(baseHashmap)

    }
}