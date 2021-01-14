package com.yamacrypt.webaudionovel.htmlParser.Narou

import com.yamacrypt.webaudionovel.htmlParser.LanguageData
import com.yamacrypt.webaudionovel.htmlParser.Narou.NarouFactory
import org.jsoup.Jsoup
import java.lang.Exception

class NocFactory() : NarouFactory(){
    init {
        defaulturl="https://noc.syosetu.com/"
        impurl="https://novel18.syosetu.com/"
        language= LanguageData.JP
    }
    override fun parse(url:String?){
        if(url==null)
            return
        doc=Jsoup.connect(url).cookie("over18","yes").get()
        this.imp=Typecheck(url)
    }
    override fun downloadable(url: String?): Boolean {
        //return super.downloadable(url)
        try {
            if (url!!.contains("novel18.syosetu.com"))
                return true
        }
        catch (e : Exception){
        }
        return false
    }

}