package com.yamacrypt.webaudionovel.htmlParser

import androidx.annotation.MainThread
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


open class HTMLFactory(){
    protected  var  doc: Document= Document("")
    protected var defaulturl:String=""
    protected var language:String="JP"
    protected  var impurl=""
    public fun get_defaulturl(): String {
        return defaulturl
    }
    public fun get_language():String{
        return language
    }

    public  var BOXcheck:Boolean = false
    /* open fun setURL(url:String){
         val doc: Document = Jsoup.connect(url).get()
     }*/
    public open fun parse(url:String?){

        try {
            doc = Jsoup.connect(url).get()
        } catch (e: Exception) {

        }
    }
    open public  fun getTitle():String?{
        TODO()
    }
    open public fun getTexts():MutableList<String>?{
        TODO()
    }
    open fun getChildURLs():MutableList<String>? {
        TODO()
    }
    open fun downloadable(url:String?):Boolean{
        TODO()
    }
    open fun getIndex():Int?{
        TODO()
    }

}