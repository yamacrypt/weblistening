package com.yamacrypt.webaudionovel.htmlParser

import com.yamacrypt.webaudionovel.htmlParser.Kakuyomu.KakuyomuFactory
import com.yamacrypt.webaudionovel.htmlParser.Narou.NarouFactory
import com.yamacrypt.webaudionovel.htmlParser.Narou.NocFactory
import com.yamacrypt.webaudionovel.htmlParser.Wattpad.WattpadFacotry
import com.yamacrypt.webaudionovel.ui.search.selector.*
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
    companion object{
        public fun from(path:String?): HTMLFactory {
            if(path==null)
                return NarouFactory()
            val prefix = "://"
            //val split=path.split(prefix)
            if(path.contains("ncode.syosetu.com/")) {
                return NarouFactory()
            }
            else if(path.contains("novel18.syosetu.com")){
                    return NocFactory()
            }
            else if(path.contains("kakuyomu.jp")){
                return  KakuyomuFactory()
            }
            else if(path.contains("wattpad.com"))
            {
                return WattpadFacotry();
            }
            return NarouFactory()
            /*else if(path.contains("mid.syosetu.com")){
                return MID
            }
            else if(path.contains("mnlt.syosetu.com"))
            {
                return MNLT
            }*/
           // return YOMOU
        }
        public fun from(mode:Int):HTMLFactory{
            var htmlfactory:HTMLFactory=NarouFactory();
            when(mode){
                YOMOU->{
                    htmlfactory=NarouFactory()
                    // defaulturl=YOMOU_URL
                }
                KAKUYOMU ->{
                    htmlfactory= KakuyomuFactory()
                }
                WATTPAD ->{
                    htmlfactory=WattpadFacotry()
                }
                NOC->{
                    htmlfactory= NocFactory()
                }
            }
            return htmlfactory;

        }
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