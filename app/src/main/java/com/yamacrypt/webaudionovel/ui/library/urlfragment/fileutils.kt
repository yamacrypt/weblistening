package com.yamacrypt.webaudionovel.ui.library.urlfragment

import android.content.Context
import com.yamacrypt.webaudionovel.Database.DBProvider
import com.yamacrypt.webaudionovel.Database.DBTableName
import com.yamacrypt.webaudionovel.Database.StoryIndexDB
import com.yamacrypt.webaudionovel.ui.library.common.FileType
import java.io.File


fun getURLsFromParentURL(url: String, showHiddenFiles: Boolean = false, onlyFolders: Boolean = false, context: Context): ArrayList<String> {
   // val file = File(path)
    val  db : StoryIndexDB = DBProvider.of(DBTableName.storyindex,context) as StoryIndexDB;
    val ls= db.getsortedAllurlsfromParenturl(url);
    /* val res=file.listFiles()
         .filter { showHiddenFiles || !it.name.startsWith(".") }
         .filter { !onlyFolders || it.isDirectory }
         .toList()*/
    return ls;
}
fun getlibraryitemModelsFromurls(urls: ArrayList<String>, context:Context): ArrayList<LibraryItemModel> {
    var models= mutableListOf<LibraryItemModel>()
    val  db : StoryIndexDB = DBProvider.of(DBTableName.storyindex,context) as StoryIndexDB;
    /*if(urls.isNotEmpty())
    urls.forEachIndexed{
            index,it ->
        run {
            val file=File(db.getPATH(it))
            models.add(LibraryItemModel(it, file.path,file.name,
                FileType.getFileType(file) ,db.getsubFiles(it)    , index))
        }
    }*/
    return ArrayList(models)
}
fun getLibraryItemsFromURL(url:String,context:Context): ArrayList<LibraryItemModel> {
    val  db : StoryIndexDB = DBProvider.of(DBTableName.storyindex,context) as StoryIndexDB;
   return db.getsortedLibraryItemsfromParenturl(url)
}