package com.yamacrypt.webaudionovel.MusicService

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.support.v4.media.MediaMetadataCompat
import com.yamacrypt.webaudionovel.Database.StoryIndexModel
import com.yamacrypt.webaudionovel.FileController
import com.yamacrypt.webaudionovel.R
import java.io.File

class tts_Item(texts: List<String>,language:String="ja",startindex:Int=0,url:String="root") {
    val texts: List<String> =texts;
    val language:String=language;
    val start_index=startindex;
    val url:String=url
}
object MusicLibrary {
    private lateinit var storyIndexModel:StoryIndexModel
    private var startindex:Int=0;
    fun setdata(file: StoryIndexModel, startindex: Int){
        this.storyIndexModel=file;
        this.startindex=startindex;
    }
    fun getAssetsFile(context: Context): AssetFileDescriptor {
        return context.resources.openRawResourceFd(R.raw.exmple)
    }
    fun getTTS_Item(context: Context): tts_Item? {
        val c=FileController(context);
        if(this::storyIndexModel.isInitialized) {
            return c.OpenShortCut(storyIndexModel.url  , startindex);
        }
        return null
    }
    fun getStoryIndexModel():StoryIndexModel?{
        if(this::storyIndexModel.isInitialized) {
            return storyIndexModel;
        }
        return null
    }
    fun getMetadata(context: Context): MediaMetadataCompat? {
        val c=FileController(context)
        val meta:MediaMetadataCompat
        if(this::storyIndexModel.isInitialized){
            try {
               // val sc = c.ReadShortCut(storyIndexModel)
               /* val retriever = MediaMetadataRetriever().apply {
                    val afd = getAssetsFile(context)
                    setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                }*/
                meta = MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "exmple.mp3")
                    .putString(
                        MediaMetadataCompat.METADATA_KEY_TITLE,
                       File(storyIndexModel.parent_path).name
                    )
                    .putString(
                        MediaMetadataCompat.METADATA_KEY_ARTIST,
                        storyIndexModel.novel_name
                    )
                    /*.putBitmap(MediaMetadataCompat.METADATA_KEY_ART,
                    createArt(retriever))*/
                    .build()
            }
            catch (e:java.lang.Exception){return null}
            }
        else{
            return null
        }
        //retriever.release()
       /*val meta = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "file.name")
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST," file.name")*/
        return meta
    }

    private fun createArt(retriever: MediaMetadataRetriever): Bitmap? {
        return try {
            val pic = retriever.embeddedPicture
            BitmapFactory.decodeByteArray(pic, 0, pic!!.size)
        } catch (e: Exception) {
           // Timber.w(e)
            null
        }
    }
}