package com.yamacrypt.webaudionovel.htmlParser.Kakuyomu

import com.yamacrypt.webaudionovel.htmlParser.HTMLFactory
import com.yamacrypt.webaudionovel.htmlParser.LanguageData
import com.yamacrypt.webaudionovel.htmlParser.Narou.BoxNarouImp
import com.yamacrypt.webaudionovel.htmlParser.Narou.NarouImp
import com.yamacrypt.webaudionovel.htmlParser.Narou.NormalNarouImp
import com.yamacrypt.webaudionovel.htmlParser.Narou.ShortNarouImp
import java.io.IOException

class KakuyomuFactory : HTMLFactory(){
    var imp:KakuyomuImp? = null
    init {
        defaulturl="https://kakuyomu.jp"
        impurl=defaulturl
        language=LanguageData.JP
    }

    override fun getIndex(): Int? {
        return imp?.getIndex()
    }
    /* init {
        imp= Typecheck()
       //  this.imp=imp
     }*/
    override fun downloadable(url: String?): Boolean {
        //return super.downloadable(url)
        if(url==null)
            return false
        if(url!!.contains("/works/"))
            return true
        return false
    }
    private fun Typecheck( url:String): KakuyomuImp? {
        var number:String=""
        try {
            // val doc: Document = Jsoup.connect(url).get()
            val boxes=doc.getElementsByClass("widget-toc-episode-episodeTitle")
            //val num=doc.getElementById("novel_no")
            val honbun=doc.getElementsByClass ("widget-episodeBody js-episode-body")
            //val maxnumber= boxes.size
            // BOXcheck=false
            //val ls=number.split("/")
            // val name=ls.get(0)
            if(honbun.size!=0){
                //Set_downloadbutton((URL_CONTENT))
                BOXcheck=false
                return NormalKakuyomuImp(doc,url)

            }
            else if(boxes.size!=0){
                //Set_downloadbutton((URL_BOX))
                BOXcheck=true
                return BoxKakuyomuImp(doc,impurl)
            }

            else{
                //  Set_downloadbutton(URL_ERROR)
            }
        } catch (e: IOException) {
            //stringBuilder.append("Error : ").append(e.message).append("\n")
        }
        return null
        //val factory=NarouFactory()
        //factory.setURL("")
        // factory.parse()
    }
    override fun parse(url:String?){
        if(url==null)
            return
        super.parse(url)
        this.imp=Typecheck(url!!)
    }

    override fun getTitle(): String? {
        return imp?.getTitle()
    }

    override fun getTexts(): MutableList<String>? {
          return imp?.getTexts()
    }

    override fun getChildURLs(): MutableList<String>? {
        return  imp?.getChildURLs()
    }
}