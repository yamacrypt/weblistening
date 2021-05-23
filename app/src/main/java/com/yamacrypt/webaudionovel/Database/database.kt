package com.yamacrypt.webaudionovel.Database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.yamacrypt.webaudionovel.BuildConfig.DEBUG_MODE
import com.yamacrypt.webaudionovel.DataStore
import com.yamacrypt.webaudionovel.FileController
import com.yamacrypt.webaudionovel.PlayList
import com.yamacrypt.webaudionovel.ui.library.common.FileType
import com.yamacrypt.webaudionovel.ui.library.models.BookMark
import com.yamacrypt.webaudionovel.ui.library.urlfragment.LibraryItemModel
import com.yamacrypt.webaudionovel.ui.library.utils.getAllFilesFromRoot
import java.io.File
import kotlin.collections.ArrayList

abstract class BaseDB(applicationContext:Context) {

    protected lateinit var tableName:String;
    protected lateinit var initsql:String;
    protected lateinit var upgradesql:String;
    val applicationContext:Context=applicationContext;
    var dbVersion=1
    var dbName = "weblisteningDB.db"
    fun loadHelper():DBHelper {
        val dbHelper =DBHelper(applicationContext, dbName, null, dbVersion,tableName,initsql,upgradesql);
        return dbHelper;
    }
    fun insertData(obj: DBModel)  {

            val dbHelper = loadHelper();
            val database = dbHelper.writableDatabase

            database.insertOrThrow(tableName, null, obj.toContentValues())
            database.close()
        //throw java.lang.Exception()
       /* }catch(exception: Exception) {
            Log.e("insertData", exception.toString())
        }*/
    }
    /*fun updateData(obj:DBModel) {
        try {
            val dbHelper = loadHelper();
            val database = dbHelper.writableDatabase

            val whereClauses = "url = ?"
            val whereArgs = arrayOf(obj.url)
            database.update(tableName,obj.toContentValues(), whereClauses, whereArgs)
        }catch(exception: Exception) {
            Log.e("updateData", exception.toString())
        }
    }*/
}

enum class DBTableName{
    storyindex,bookmark
}
class StoryIndexDB(applicationContext:Context) : BaseDB(applicationContext){
    init {
        dbVersion=3;
        tableName="StoryIndex";
        initsql="create table if not exists $tableName(parent_path TEXT , path TEXT,url TEXT PRIMARY KEY , story_index INTEGER, novel_name TEXT,link TEXT,language TEXT,isNew INTEGER)";
        upgradesql="ALTER TABLE $tableName ADD COLUMN isNew INTEGER DEFAULT 1";

    }
   /* fun insertData(obj:StoryIndexModel) {
        try {
            val dbHelper = loadHelper();
            val database = dbHelper.writableDatabase

            database.insertOrThrow(tableName, null, obj.toContentValues())
        }catch(exception: Exception) {
            Log.e("insertData", exception.toString())
        }
    }*/
    fun insert_and_update(obj:StoryIndexModel){
        try {
            val dbHelper = loadHelper();
            val database = dbHelper.writableDatabase

            database.insertOrThrow(tableName, null, obj.toContentValues())
            database.close()
        }catch(exception: Exception) {
            updateData(obj);
        }
    }
    fun updateData(obj:StoryIndexModel) {
        try {
            val dbHelper = loadHelper();
            val database = dbHelper.writableDatabase

            val whereClauses = "url = ?"
            val whereArgs = arrayOf(obj.url)
            database.update(tableName,obj.toContentValues(), whereClauses, whereArgs)
            database.close()
        }catch(exception: Exception) {
            Log.e("updateData", exception.toString())
        }
    }
    fun getsortedAllfromparent(parent:String): ArrayList<File> {
        val dbHelper =loadHelper();
        val database = dbHelper.readableDatabase;
        var res= mutableListOf<StoryIndexModel>();
        var pathls= mutableListOf<File>();
        val cursor:Cursor =database.rawQuery("SELECT * FROM $tableName WHERE parent_path=? ORDER BY story_index ASC",
            arrayOf(parent));
        try {
            while(cursor.moveToNext()) {
                if(DEBUG_MODE) {
                    val hoge: StoryIndexModel = StoryIndexModel(
                        path = cursor.getString(cursor.getColumnIndex("path")),
                        parent_path = cursor.getString(cursor.getColumnIndex("parent_path")),
                        index = cursor.getInt(cursor.getColumnIndex("story_index")),
                        url = cursor.getString(cursor.getColumnIndex("url")),
                                link = cursor.getString(cursor.getColumnIndex("link")),
                                language = cursor.getString(cursor.getColumnIndex("language")),
                                novel_name = cursor.getString(cursor.getColumnIndex("novel_name"))
                    )
                    res.add(hoge);
                }
                    pathls.add(
                        File(
                            cursor.getString(cursor.getColumnIndex("parent_path")),
                            cursor.getString(cursor.getColumnIndex("path"))
                        )
                    );
            }
        } finally {
            cursor.close()
            database.close()
        }
        return  ArrayList(pathls) ;
    }
    fun getsubFiles(url:String):Int
    {
        val dbHelper =loadHelper();
        val database = dbHelper.readableDatabase;
        var res=0;
        val parent=getPATH(url)
        val cursor:Cursor =database.rawQuery("SELECT * FROM $tableName WHERE parent_path=?",
            arrayOf(parent));
        try {
            while(cursor.moveToNext()) {
               res++;
            }
        } finally {
            cursor.close()
            database.close()
        }
        return  res ;
    }
    fun getsortedAllurlsfromParenturl(url:String): ArrayList<String> {
        val dbHelper =loadHelper();
        val database = dbHelper.readableDatabase;
        var res= mutableListOf<StoryIndexModel>();
        var pathls= mutableListOf<String>();
        val parent=getPATH(url)
        val cursor:Cursor =database.rawQuery("SELECT * FROM $tableName WHERE parent_path=? ORDER BY story_index ASC",
            arrayOf(parent));
        try {
            while(cursor.moveToNext()) {
                if(DEBUG_MODE) {
                    var isNew=0
                    try {
                       isNew= cursor.getInt(cursor.getColumnIndex("isNew"))
                    } catch (e: Exception) {
                    }
                    val hoge: StoryIndexModel = StoryIndexModel(
                        path = cursor.getString(cursor.getColumnIndex("path")),
                        parent_path = cursor.getString(cursor.getColumnIndex("parent_path")),
                        index = cursor.getInt(cursor.getColumnIndex("story_index")),
                        url = cursor.getString(cursor.getColumnIndex("url")),
                        link = cursor.getString(cursor.getColumnIndex("link")),
                        language = cursor.getString(cursor.getColumnIndex("language")),
                        novel_name = cursor.getString(cursor.getColumnIndex("novel_name")),
                        isNew = isNew
                    )
                    res.add(hoge);
                }
                pathls.add(
                    cursor.getString(cursor.getColumnIndex("url"))
                   /* File(
                        cursor.getString(cursor.getColumnIndex("parent_path")),
                        cursor.getString(cursor.getColumnIndex("path"))
                    )*/
                );
            }
        } finally {
            cursor.close()
            database.close()
        }
        return  ArrayList(pathls) ;
    }
    fun getsortedLibraryItemsfromParenturl(url:String): ArrayList<LibraryItemModel> {
        val dbHelper =loadHelper();
        val database = dbHelper.readableDatabase;
        //var res= mutableListOf<StoryIndexModel>();
        var pathls= mutableListOf<LibraryItemModel>();
        val parent=getPATH(url)
        val cursor:Cursor =database.rawQuery("SELECT * FROM $tableName WHERE parent_path=? ORDER BY story_index ASC",
            arrayOf(parent));
       // if(false)
        try {
            var index=0
            while(cursor.moveToNext()) {
               // if(DEBUG_MODE) {
                    var isNew=0
                    try {
                        isNew= cursor.getInt(cursor.getColumnIndex("isNew"))
                    } catch (e: Exception) {
                    }
                    val hoge = StoryIndexModel(
                        path = cursor.getString(cursor.getColumnIndex("path")),
                        parent_path = cursor.getString(cursor.getColumnIndex("parent_path")),
                        index = cursor.getInt(cursor.getColumnIndex("story_index")),
                        url = cursor.getString(cursor.getColumnIndex("url")),
                        link = cursor.getString(cursor.getColumnIndex("link")),
                        language = cursor.getString(cursor.getColumnIndex("language")),
                        novel_name = cursor.getString(cursor.getColumnIndex("novel_name")),
                        isNew = isNew
                    )
                   // res.add(hoge);
                //}
                //val fullpath=File(cursor.getString(cursor.getColumnIndex("parent_path")),cursor.getString(cursor.getColumnIndex("path")))
                val ur=cursor.getString(cursor.getColumnIndex("url"))
                var type=FileType.FILE
                var subfiles=0
                if(hoge.link==""){
                    subfiles= getsubFiles(ur)
                    type=FileType.FOLDER
                }
                pathls.add(LibraryItemModel(
                    type,
                    subfiles,
                    index,
                    hoge,
                    //parent_url=url
                ))
                //break
                    /* File(
                         cursor.getString(cursor.getColumnIndex("parent_path")),
                         cursor.getString(cursor.getColumnIndex("path"))
                     )*/
                index++
            }
        } finally {
            cursor.close()
            database.close()
        }
        val ls=pathls.map{ it ->it.model.url }
        PlayList.setup(ls,parent)
        return  ArrayList(pathls) ;
    }
    fun deleteData(whereId: String) {
        val dbHelper =loadHelper();
        val database = dbHelper.writableDatabase
        try {


            val whereClauses = "url = ?"
            val whereArgs = arrayOf(whereId)
            database.delete(tableName, whereClauses, whereArgs)
            database.close()
        }catch(exception: Exception) {
            Log.e("deleteData", exception.toString())
        }

    }
    fun del(whereId: String,whereId2: String){
        val dbHelper =loadHelper();
        val database = dbHelper.writableDatabase

        try {

            val whereClauses = "parent_path = ? AND path = ?"
            val whereArgs = arrayOf(whereId,whereId2)
            database.delete(tableName, whereClauses, whereArgs)
            database.close()
        }catch(exception: Exception) {
            Log.e("deleteData", exception.toString())
        }

    }
    fun geturls(root:String,childs:List<String>): ArrayList<String> {
        val urls= mutableListOf<String>()
        childs.forEach{
            it->urls.add(geturl(root,it))
        }
        return ArrayList(urls)
    }
    fun getPARENTURL(url:String):String{
        if(url=="root")
            throw java.lang.Exception()//return DataStore.getShortCutFile(applicationContext,"").path
        var res=""
        var parentURL=""
        try {
            val dbHelper =loadHelper();
            val database = dbHelper.readableDatabase

            val whereClauses = "url = ?"
            val whereArgs = arrayOf(url)


            val cursor=database.rawQuery("SELECT * FROM $tableName WHERE url=?", arrayOf(url))//(tableName, whereClauses, whereArgs)
            try{
                if(cursor.moveToNext()) {
                    val parent = cursor.getString(cursor.getColumnIndex("parent_path"))
                    val path = cursor.getString(cursor.getColumnIndex("path"))
                    res = parent
                }
            }
            finally{
                cursor.close()
                //Log.e("getPATH", exception.toString())
            }

            val cursor2=database.rawQuery("SELECT * FROM $tableName WHERE path=?", arrayOf(File(res).name))//(tableName, whereClauses, whereArgs)
            try{
                if(cursor2.moveToNext()) {
                    val parent = cursor2.getString(cursor2.getColumnIndex("url"))
                    val path = cursor2.getString(cursor2.getColumnIndex("path"))
                    parentURL= parent
                }
            }
            finally{
                database.close()
                cursor2.close()
                //Log.e("getPATH", exception.toString())
            }
        }
        catch (e:java.lang.Exception){
            Log.e("getPATH", e.toString())
        }
        return parentURL
    }
    fun getPATH(url:String):String{
        if(url=="root")
            return DataStore.getShortCutFile(applicationContext,"").path
        var res=""
        try {
            val dbHelper =loadHelper();
            val database = dbHelper.readableDatabase

            val whereClauses = "url = ?"
            val whereArgs = arrayOf(url)
            //var res=""
            val cursor=database.rawQuery("SELECT * FROM $tableName WHERE url=?", arrayOf(url))//(tableName, whereClauses, whereArgs)
            try{
                if(cursor.moveToNext()) {
                    val parent = cursor.getString(cursor.getColumnIndex("parent_path"))
                    val path = cursor.getString(cursor.getColumnIndex("path"))
                    res = File(parent, path).path
                }
            }
            finally{
                cursor.close()
                database.close()
                //Log.e("getPATH", exception.toString())
            }
        }
        catch (e:java.lang.Exception){
            Log.e("getPATH", e.toString())
        }
        return res
    }
    fun getStoryIndexItem(url:String): StoryIndexModel {
        val dbHelper =loadHelper();
        val database = dbHelper.writableDatabase

        val whereClauses = "parent_path = ? AND path = ?"

        lateinit var res:StoryIndexModel
        val cursor:Cursor=database.rawQuery("SELECT * FROM $tableName WHERE url = ? ", arrayOf(url))// (tableName, whereClauses, whereArgs)
        try {
            if(cursor.moveToNext()) {
                var isNew=0
                try {
                    isNew= cursor.getInt(cursor.getColumnIndex("isNew"))
                } catch (e: Exception) {
                }
                res = StoryIndexModel(
                    path = cursor.getString(cursor.getColumnIndex("path")),
                    parent_path = cursor.getString(cursor.getColumnIndex("parent_path")),
                    index = cursor.getInt(cursor.getColumnIndex("story_index")),
                    url = cursor.getString(cursor.getColumnIndex("url")),
                    link = cursor.getString(cursor.getColumnIndex("link")),
                    language = cursor.getString(cursor.getColumnIndex("language")),
                    novel_name = cursor.getString(cursor.getColumnIndex("novel_name")),
                    isNew = isNew
                )
                //res=cursor.getString(cursor.getColumnIndex("url"))
            }
        } finally {
            cursor.close()
            database.close()
        }
              return res//lateinit error possibility
      
    }
   fun geturl(root:String,child:String): String {

           val dbHelper =loadHelper();
           val database = dbHelper.writableDatabase

           val whereClauses = "parent_path = ? AND path = ?"

           var res=""
           val cursor:Cursor=database.rawQuery("SELECT * FROM $tableName WHERE parent_path = ? AND path = ?", arrayOf(root,child))// (tableName, whereClauses, whereArgs)
           try {
               if(cursor.moveToNext()) {
                   res=cursor.getString(cursor.getColumnIndex("url"))
               }
           } finally {
               cursor.close()
               database.close()
           }
      return res

    }
    fun loadInitData(){
        val path=DataStore.getShortCutFile(applicationContext,"");
       val files=getAllFilesFromRoot(path)
        files.forEach{it->
            run {
                val a=FileController(applicationContext)
                val item=a.ReadShortCut(it);
                try {
                    val u=DataStore.getShortCutFile(applicationContext, "").path;
                    
                    if(File(it.parent).name!="shortcut") {
                        insert_and_update(
                            StoryIndexModel(
                                File(it.parent).name,
                                u,
                                0,
                                a.ReadParentRawUrl(File(it.parent, it.name)),
                                language = item.language,
                                link = "",
                                novel_name = File(it.parent).name
                            )
                        )
                    }
                }
                catch (e:java.lang.Exception){}
                insert_and_update(StoryIndexModel(it.name, it.parent, 999,a.ReadRawUrl(File(it.parent,it.name)),
                    language = item.language,
                    link = item.link,
                    novel_name = it.name),
                )
            }
        }
    }

    fun getLibraryIndexItem(url: String): LibraryItemModel {
        val dbHelper =loadHelper();
        val database = dbHelper.writableDatabase

        val whereClauses = "parent_path = ? AND path = ?"

        lateinit var res:LibraryItemModel
        val cursor:Cursor=database.rawQuery("SELECT * FROM $tableName WHERE url = ? ", arrayOf(url))// (tableName, whereClauses, whereArgs)
        try {
            if(cursor.moveToNext()) {
                val fullpath=File(cursor.getString(cursor.getColumnIndex("parent_path")),cursor.getString(cursor.getColumnIndex("path")))
                val ur=cursor.getString(cursor.getColumnIndex("url"))
                val type=FileType.getFileType(fullpath)
                var subfiles=0
                if(type==FileType.FOLDER){
                    subfiles= getsubFiles(ur)
                }
              res=LibraryItemModel(
                    type,
                    subfiles,
                    0,
                  getStoryIndexItem(url),
                    //url
                )
                //res=cursor.getString(cursor.getColumnIndex("url"))
            }
        } finally {
            cursor.close()
            database.close()
        }
        return res//lateinit error possibility

    }
    //tableName=""
}
class BookMarkDB (applicationContext:Context) : BaseDB(applicationContext){
    init {
        dbName = "BookMarkDB.db"
        tableName="BookMark";
        initsql="create table if not exists $tableName(path TEXT,startindex INTEGER,position INTEGER,rootpath TEXT PRIMARY KEY)";
        upgradesql=""
    }

    fun insertAndUpdate(obj:DBDataForBookMark){
        val dbHelper = loadHelper();
        val database = dbHelper.writableDatabase
        try {


            database.replaceOrThrow(tableName, null, obj.toContentValues())

        }catch(exception: Exception) {

        }
        database.close()

    }
    fun getByKey(rootpath:String): BookMark? {
        val dbHelper =loadHelper();
        val database = dbHelper.writableDatabase

        val whereClauses = "rootpath = ? "

        lateinit var  bookmark:BookMark
        val cursor:Cursor=database.rawQuery("SELECT * FROM $tableName WHERE rootpath = ?", arrayOf(rootpath))// (tableName, whereClauses, whereArgs)
        try {
            if(cursor.moveToNext()) {
               val path=cursor.getString(cursor.getColumnIndex("path"))
                val startindex=cursor.getInt(cursor.getColumnIndex("startindex"))
                val position=cursor.getInt(cursor.getColumnIndex("position"))
                bookmark= BookMark(path,startindex,rootpath,position)
                //val path=cursor.getString(cursor.getColumnIndex("rootpath"))

            }
        } finally {
            cursor.close()
            database.close()
        }
        try {
            return bookmark
        } catch (e: Exception) {
            return null
        }
    }
}
class DBProvider(applicationContext:Context){
     val applicationContext:Context=applicationContext;
     companion object {
         fun of(dbname: DBTableName,applicationContext:Context):BaseDB  {
             when(dbname){
                 DBTableName.storyindex->return  StoryIndexDB(applicationContext);
                 DBTableName.bookmark->return BookMarkDB(applicationContext)
                 else-> error("invalid DBTableName");
             }
         }
     }
     val dbVersion=1
     var dbName = "weblisteningDB"
     //val tableName=tableName
}
class DBHelper(context: Context, databaseName:String, factory: SQLiteDatabase.CursorFactory?, version: Int,tableName:String,initsql:String,upgradesql:String) : SQLiteOpenHelper(context, databaseName, factory, version) {
    val tableName=tableName;
    val initsql=initsql;
    val upgradesql=upgradesql;
    override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL(initsql);
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            try {
                database?.execSQL(upgradesql)
            }
            catch(e:Exception){}
        }
    }
}