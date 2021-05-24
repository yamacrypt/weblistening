package com.yamacrypt.webaudionovel.Database

import android.content.ContentValues
import com.yamacrypt.webaudionovel.ui.library.models.BookMark

class UpdateCheckModel(url:String,updating:Int):DBModel(){
    val url=url
    val updating=updating // 0 or 1
    override fun toContentValues(): ContentValues {
        var hoge=ContentValues()
        hoge.put("url",url)
        hoge.put("updating",updating)
        return hoge
    }

}
class DBDataForBookMark(bk: BookMark) : DBModel() {
    val path=bk.path;
    val startindex=bk.startindex;
    val rootpath=bk.rootpath;
    val position=bk.position;


    override fun toContentValues():ContentValues{
        var values=ContentValues();
        values.put("path",path)
        values.put("startindex",startindex)
        values.put("rootpath",rootpath)
        values.put("position",position)
        return  values;
    }
}
class StoryIndexModel(path:String, parent_path:String, index:Int, url:String, novel_name :String, link:String, language:String,isNew:Int=1):DBModel(){
    val parent_path:String=parent_path;
    val path:String=path;//childpath
    val index=index;
    val url:String=url
    val novel_name:String=novel_name
    val link:String=link
    val language:String=language
    val isNew:Int=isNew
   /* companion object  {
        fun from(values:ContentValues): StoryIndexModel {
            return StoryIndexModel(values.getAsString("path"),values.getAsString("parent_path"),values.getAsInteger("story_index"));
        }
    }*/

   override fun toContentValues() :ContentValues{
        var values=ContentValues();
       values.put("path",path)
       values.put("story_index",index)
       values.put("parent_path",parent_path)
       values.put("url",url)
       values.put("language",language)
       values.put("link",link)
       values.put("novel_name",novel_name)
       values.put("isNew",isNew)
       return values;
    }
    fun copy(isNew:Int):StoryIndexModel{
        return StoryIndexModel(
            parent_path=parent_path,path = path,isNew = isNew,novel_name = novel_name,language = language,
            link = link,url = url,index = index
        )
    }

}
class DictionaryPairModel(target:String,read:String): DBModel() {
    val target=target
    val read=read
    override fun toContentValues(): ContentValues {
        var values=ContentValues();
        values.put("raw",target)
        values.put("convert",read)
        return values
    }

}
abstract class DBModel{
    abstract fun toContentValues():ContentValues;
}