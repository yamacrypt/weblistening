package com.yamacrypt.webaudionovel.ui.library.models

import android.content.Context
import android.os.Handler
import com.yamacrypt.webaudionovel.PlayList
import java.util.*

data class BookMark (
    val path:String,
    val startindex:Int=0,
    val rootpath:String,
    val position:Int=0,
)
 fun BookMark_Save(bookMark: BookMark,context: Context){
    val pref = context.getSharedPreferences("BookMark", 0)
    val editor = pref.edit()
    //editor.putString("path",bookMark.path);
    //editor.putInt("startindex",bookMark.startindex);
     editor.putString(bookMark.rootpath+"path",bookMark.path)
     editor.putInt(bookMark.rootpath+"startindex",bookMark.startindex)
     editor.putInt(bookMark.rootpath+"position",bookMark.position)
    editor.apply()
}
fun BookMark_Open(rootpath:String,context:Context):BookMark{
    val pref = context.getSharedPreferences("BookMark", 0)
    val path=pref.getString(rootpath+"path","")
    val startindex=pref.getInt(rootpath+"startindex",0)
    val position=pref.getInt(rootpath+"position",0);
    val bookmark=BookMark(path!!,startindex,rootpath,position)
    return bookmark;
}
