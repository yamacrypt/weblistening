package com.yamacrypt.webaudionovel.ui.library.models

import android.content.ContentValues
import android.content.Context
import com.yamacrypt.webaudionovel.DataStore
import com.yamacrypt.webaudionovel.Database.*
import com.yamacrypt.webaudionovel.PlayList
import com.yamacrypt.webaudionovel.ui.PlayerViewModel

data class BookMark (
    val path:String,
    val startindex:Int=0,
    val rootpath:String,
    val position:Int=0,
)

 fun autoSave(context: Context){
     //mToolbar.setTitle(DataStore.intro);
     val model = PlayerViewModel()
     var va:Int?=model.getSpeakingnumber().getValue();
     if(va==null){
         va=0
     }
     val bookmark = BookMark(
         PlayList.getPlayingPath(),
         va,
         PlayList.getRootpath(),
         PlayList.getCurrent_number()
     )
     val continueBookmark = BookMark(
         PlayList.getPlayingPath(),
         va,
         DataStore.getShortCutFile(context,"").path,
         PlayList.getCurrent_number()
     )
     BookMark_Save(bookmark,context) //BookMark_Save()
     BookMark_Save(continueBookmark,context)

 }
fun manualSave(context: Context){
    val model = PlayerViewModel()
    var va:Int?=model.getSpeakingnumber().getValue();
    if(va==null){
        va=0
    }
    val bookmark = BookMark(
        PlayList.getPlayingPath(),
        va,
        PlayList.getRootpath(),
        PlayList.getCurrent_number()
    )
    val continueBookmark = BookMark(
        PlayList.getPlayingPath(),
        va,
        DataStore.getShortCutFile(context,"").path,
        PlayList.getCurrent_number()
    )
    BookMark_Save(bookmark,context) //BookMark_Save()
    BookMark_Save(continueBookmark,context)
}

 fun BookMark_Save(bookMark: BookMark,context: Context){
     val  db : BookMarkDB = DBProvider.of(DBTableName.bookmark,context) as BookMarkDB;
    db.insertAndUpdate(DBDataForBookMark(bookMark));
     /*val pref = context.getSharedPreferences("BookMark", 0)
    val editor = pref.edit()
    //editor.putString("path",bookMark.path);
    //editor.putInt("startindex",bookMark.startindex);
     editor.putString(bookMark.rootpath+"path",bookMark.path)
     editor.putInt(bookMark.rootpath+"startindex",bookMark.startindex)
     editor.putInt(bookMark.rootpath+"position",bookMark.position)
    editor.apply()*/
}
fun BookMark_Open(rootpath:String,context:Context):BookMark?{
    val  db : BookMarkDB = DBProvider.of(DBTableName.bookmark,context) as BookMarkDB;
    val bk=db.getByKey(rootpath)
    return bk
    /*val pref = context.getSharedPreferences("BookMark", 0)
    val path=pref.getString(rootpath+"path","")
    val startindex=pref.getInt(rootpath+"startindex",0)
    val position=pref.getInt(rootpath+"position",0);
    if(path=="")
        return null
    val bookmark=BookMark(path!!,startindex,rootpath,position)
    BookMark_Save(bookmark,context)
    return bookmark;*/
}
