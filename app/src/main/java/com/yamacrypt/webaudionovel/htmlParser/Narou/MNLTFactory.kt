package com.yamacrypt.webaudionovel.htmlParser.Narou

import com.yamacrypt.webaudionovel.htmlParser.LanguageData
import org.jsoup.Jsoup

class MNLTFactory ():NarouFactory(){
    init {
        defaulturl="https://mnlt.syosetu.com/"
        impurl="https://novel18.syosetu.com/"
        language= LanguageData.JP
    }
    override fun parse(url:String?){
        if(url==null)
            return
        doc= Jsoup.connect(url).cookie("over18","yes").get()
        this.imp=Typecheck(url)
    }

}