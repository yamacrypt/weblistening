package com.yamacrypt.webaudionovel

import android.content.Context
import com.yamacrypt.webaudionovel.Database.DBProvider
import com.yamacrypt.webaudionovel.Database.DBTableName
import com.yamacrypt.webaudionovel.Database.StoryIndexDB
import com.yamacrypt.webaudionovel.Database.StoryIndexModel
import com.yamacrypt.webaudionovel.htmlParser.HTMLFactory
import java.io.File

class NovelDownloader {
    companion object {
        fun download(url: String?, threadContext: Context, onSavedListener:()->Unit) {
            val threadFactory = HTMLFactory.from(url!!)
            //val threadcontext=context
            if (threadFactory != null) {
                Filesave(threadFactory, threadContext, url,onSavedListener)
            }
        }
        fun update(url: String?, newOnly: Boolean, context: Context, onUpdatedListener:()->Unit) {
            val threadfactory = HTMLFactory.from(url)
            if(threadfactory!=null) {
                threadfactory.parse(url)
                var urlls = threadfactory.getChildURLs()
                val db = DBProvider.of(DBTableName.storyindex, context) as StoryIndexDB
                url?.let {
                    val parent = db.getPATH(it)
                    if (urlls != null) {
                        if (newOnly) {
                            val filter = db.getsortedAllurlsfromParenturl(it);
                            //val MutableList<String>? fil=
                            // val hoge=urlls
                            val ls = urlls!!.toMutableList().filter { !filter.contains(it) }
                            for (ele in ls) {
                                try {
                                    file_update(ele, context, parent)
                                } catch (e: Exception) {
                                    break
                                }
                                onUpdatedListener()
                            }
                        }
                    }

                }
            }
        }
        /*fun getUpdateList(url: String?, newOnly: Boolean, context: Context): List<String> {
            ///lateinit var threadfactory:HTMLFactory;
            val threadfactory = HTMLFactory.from(url)
            threadfactory.parse(url)
            var urlls = threadfactory.getChildURLs()
            val db = DBProvider.of(DBTableName.storyindex, context) as StoryIndexDB
            val parent = db.getPATH(url)
            if (urlls != null) {
                if (newOnly) {
                    url?.let {
                        val filter = db.getsortedAllurlsfromParenturl(url);
                        //val MutableList<String>? fil=
                        // val hoge=urlls
                        val res = urlls!!.toMutableList().filter { !filter.contains(it) }
                        return res
                    }
                }

                /*url?.let {
                    val parent = db.getPATH(it)
                    //val foldername=threadfactory.getTitle()
                    // val folder=DataStore.CreateFolder(threadcontext,foldername)
                    // val db:StoryIndexDB= DBProvider.of(DBTableName.storyindex,requireContext() as StoryIndexDB
                    /*db.insert_and_update(StoryIndexModel(path=foldername!!,
                    parent_path=DataStore.getShortCutFile(context, "").toString(),
                    index = 0,url=url!!))*/
                    for (childurl in urlls) {
                        threadfactory.parse(childurl)
                        //   Filesave(threadfactory,threadcontext,childurl)
                        try {
                            file_update(threadfactory, childurl, parent, context);
                        } catch (e: Exception) {
                            break
                        }
                        Thread.sleep(1000)
                    }
                }.*/


            }
            return listOf()
        }*/

         private fun file_update(
            url: String,
            context: Context,parent:String) {
             val factory=HTMLFactory.from(url)
             if (factory != null) {


             factory.parse(url)

             val db = DBProvider.of(DBTableName.storyindex, context) as StoryIndexDB
            val index = factory.getIndex()
            val title = factory.getTitle()
            try {
                db.insertData(
                    StoryIndexModel(
                        path = title!!,
                        parent_path = parent,
                        index = index!!,
                        url = url,
                        language = factory.get_language(),
                        novel_name = title,
                        link = DataStore.getConvertedURL(url)
                    )
                )
                } catch (e: Exception) {
                    db.updateData(
                        StoryIndexModel(
                            path = title!!,
                            parent_path = parent,
                            index = index!!,
                            url = url,
                            language = factory.get_language(),
                            novel_name = title,
                            link = DataStore.getConvertedURL(url)
                        )
                    )

                }
                 Save(url, factory, context, File(parent))
                val updated = false
                if (updated) {
                    //saveNovel()
                }
             }
        }

        private fun Filesave(
            threadFactory: HTMLFactory,
            threadContext: Context,
            url: String?,
            method: () -> Unit
        ) {

            threadFactory.parse(url)

            if (threadFactory.BOXcheck) {
                val urlls = threadFactory.getChildURLs()
                if (urlls != null) {
                    val foldername = threadFactory.getTitle()
                    val folder = DataStore.CreateFolder(threadContext, foldername)
                    val db: StoryIndexDB =
                        DBProvider.of(DBTableName.storyindex, threadContext) as StoryIndexDB
                    db.insert_and_update(
                        StoryIndexModel(
                            path = foldername!!,
                            parent_path = DataStore.getShortCutFile(threadContext, "").toString(),
                            index = 0, url = url!!,
                            language = threadFactory.get_language(),
                            novel_name = foldername,
                            link = ""
                        )
                    )
                    for (childurl in urlls) {
                        threadFactory.parse(childurl)

                        //   Filesave(threadfactory,threadcontext,childurl)
                        try {
                            Save(childurl, threadFactory, threadContext, folder)
                        } catch (e: Exception) {
                            break
                        }
                        method()
                        //Thread.sleep(1000)
                    }

                }
            } else {
                try {
                    Save(url, threadFactory, threadContext)
                } catch (e: Exception) {
                }
                method()
            }
        }

        private fun Save(
            url: String?,
            threadfactory: HTMLFactory,
            threadContext: Context,
            folder: File

        ) {
            val title = threadfactory.getTitle()
            val stringList = threadfactory.getTexts()
            val downloader = FileController(threadContext)
            val convertedurl = DataStore.getConvertedURL(url)
            val novelfile = DataStore.getNovelFile(threadContext, convertedurl)
            //val titlefile= DataStore.getShortCutFile(threadcontext,title,folder)
            val db = DBProvider.of(DBTableName.storyindex, threadContext) as StoryIndexDB

            downloader.WriteNovel(novelfile, stringList, threadfactory.get_language())
            //val sc= FileController.ShortCut(title,"",convertedurl,stringList!!.size,htmlfactory.get_language())
            //downloader.WriteShortCut(titlefile,sc)
            db.insert_and_update(
                StoryIndexModel(
                    title!!, folder.toString(), threadfactory.getIndex()!!, url!!,
                    language = threadfactory.get_language(),
                    novel_name = title,
                    link = convertedurl
                )
            )

        }

        private fun Save(url: String?, threadFactory: HTMLFactory, threadContext: Context) {

            val title = threadFactory.getTitle()
            // val author=threadfactory.getAuthor()
            val stringList = threadFactory.getTexts()
            val downloader = FileController(threadContext)
            val convertedurl = DataStore.getConvertedURL(url)
            val novelfile = DataStore.getNovelFile(threadContext, convertedurl)
            //val titlefile= DataStore.getShortCutFile(threadcontext,title)
            downloader.WriteNovel(novelfile, stringList, threadFactory.get_language())
            //val sc= FileController.ShortCut(title,"",convertedurl,stringList!!.size,htmlfactory.get_language())
            //downloader.WriteShortCut(titlefile,sc)
            val db = DBProvider.of(DBTableName.storyindex, threadContext) as StoryIndexDB
            db.insert_and_update(
                StoryIndexModel(
                    title!!,
                    DataStore.getShortCutFile(threadContext, "").toString(),
                    threadFactory.getIndex()!!,
                    url!!,
                    language = threadFactory.get_language(),
                    novel_name = title,
                    link = convertedurl
                )
            )
        }


    }
}