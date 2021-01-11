package com.yamacrypt.webaudionovel.ui.library.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import com.yamacrypt.webaudionovel.DataStore
import com.yamacrypt.webaudionovel.Database.DBProvider
import com.yamacrypt.webaudionovel.Database.DBTableName
import com.yamacrypt.webaudionovel.Database.StoryIndexDB
import com.yamacrypt.webaudionovel.FileController
import com.yamacrypt.webaudionovel.ui.library.common.FileType
import com.yamacrypt.webaudionovel.ui.library.urlfragment.LibraryItemModel

import java.io.File

fun getFilesFromPath(path: String, showHiddenFiles: Boolean = false, onlyFolders: Boolean = false,context:Context): ArrayList<File> {
    val file = File(path)
   val  db : StoryIndexDB= DBProvider.of(DBTableName.storyindex,context) as StoryIndexDB;
   val ls= db.getsortedAllfromparent(path);
   /* val res=file.listFiles()
        .filter { showHiddenFiles || !it.name.startsWith(".") }
        .filter { !onlyFolders || it.isDirectory }
        .toList()*/
    return ls;
}
fun getAllFilesFromRoot(root:File): MutableList<File> {
    val showHiddenFiles: Boolean = false
    val onlyFolders: Boolean = true
    var ls= mutableListOf<File>();
    val folders=root.listFiles()
        .filter { showHiddenFiles || !it.name.startsWith(".") }
        .filter { !onlyFolders || it.isDirectory }
        .toList()
    val files=root.listFiles()
        .filter { showHiddenFiles || !it.name.startsWith(".") }
        .filter { !it.isDirectory }
        .toList()
    ls.addAll(files);
    folders.forEach{it ->ls.addAll(it.listFiles()
        .filter { showHiddenFiles || !it.name.startsWith(".") }
        .filter { !it.isDirectory }
        .toList()) }
    return ls
}
/*fun getFileModelsFromFiles(files: ArrayList<File>): List<LibraryItemModel> {
    return files.mapIndexed { index, it ->
        LibraryItemModel(it.path, FileType.getFileType(it), it.name, convertFileSizeToMB(it.length()), it.extension, it.listFiles()?.size
            ?: 0,index)
    }
}*/

fun convertFileSizeToMB(sizeInBytes: Long): Double {
    return (sizeInBytes.toDouble()) / (1024 * 1024)
}

/*fun Context.launchFileIntent(libraryItemModel: LibraryItemModel) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = FileProvider.getUriForFile(this, packageName, File(libraryItemModel.path))
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    startActivity(Intent.createChooser(intent, "Select Application"))
}*/
fun createNewFile(fileName: String, path: String, callback: (result: Boolean, message: String) -> Unit) {
    val fileAlreadyExists = File(path).listFiles().map { it.name }.contains(fileName)
    if (fileAlreadyExists) {
        callback(false, "'${fileName}' already exists.")
    } else {
        val file = File(path, fileName)
        try {
            val result = file.createNewFile()
            if (result) {
                callback(result, "File '${fileName}' created successfully.")
            } else {
                callback(result, "Unable to create file '${fileName}'.")
            }
        } catch (e: Exception) {
            callback(false, "Unable to create file. Please try again.")
            e.printStackTrace()
        }
    }
}
fun createNewFolder(folderName: String, path: String, callback: (result: Boolean, message: String) -> Unit) {
    val folderAlreadyExists = File(path).listFiles().map { it.name }.contains(folderName)
    if (folderAlreadyExists) {
        callback(false, "'${folderName}' already exists.")
    } else {
        val file = File(path, folderName)
        try {
            val result = file.mkdir()
            if (result) {
                callback(result, "Folder '${folderName}' created successfully.")
            } else {
                callback(result, "Unable to create folder '${folderName}'.")
            }
        } catch (e: Exception) {
            callback(false, "Unable to create folder. Please try again.")
            e.printStackTrace()
        }
    }
}
fun FileUtilsDeleteFile(model:LibraryItemModel,context:Context) {
    //val file = File(model.model.path)
    val db=DBProvider.of(DBTableName.storyindex,context) as StoryIndexDB
   // val a=FileController(context)
    try {
        db.deleteData(model.model.url)
       // file.delete()
    } catch (e: Exception) {
        Log.d("delete",e.toString())
    }
   /* if(file.isDirectory){
        try {
            db.del(DataStore.getShortCutFile(context,"").path,file.name)
            file.delete()
        } catch (e: Exception) { Log.e("delete", e.toString())
        }
    }
    else {
        try {
            db.deleteData(a.ReadRawUrl(file))
            file.delete()
        } catch (e: Exception) {

                db.del(DataStore.getShortCutFile(context,"").path,file.name)
                //db.loadHelper().writableDatabase.rawQuery("DELETE FROM ${db.loadHelper().tableName} WHERE parent_path = ? AND path= ?",
               //     arrayOf(DataStore.getShortCutFile(context,"").path,file.name)).close()
        }
    }*/


    /* if (file.isDirectory) {
         file.deleteRecursively()
     } else {
         file.delete()
     }*/

}
