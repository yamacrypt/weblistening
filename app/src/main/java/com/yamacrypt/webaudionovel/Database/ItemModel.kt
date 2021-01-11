package com.yamacrypt.webaudionovel.Database

import android.content.ContentValues

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
class StoryIndexModel(path:String, parent_path:String, index:Int, url:String, novel_name :String, link:String, language:String):DBModel(){
    val parent_path:String=parent_path;
    val path:String=path;//childpath
    val index=index;
    val url:String=url
    val novel_name:String=novel_name
    val link:String=link
    val language:String=language
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
       return values;
    }

}
abstract class DBModel{
    abstract fun toContentValues():ContentValues;
}