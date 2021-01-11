package com.yamacrypt.webaudionovel.htmlParser.Wattpad

import android.text.Editable
import com.yamacrypt.webaudionovel.htmlParser.HTMLFactory
import com.yamacrypt.webaudionovel.htmlParser.LanguageData
import com.yamacrypt.webaudionovel.htmlParser.Narou.BoxNarouImp
import com.yamacrypt.webaudionovel.htmlParser.Narou.NarouImp
import com.yamacrypt.webaudionovel.htmlParser.Narou.NormalNarouImp
import com.yamacrypt.webaudionovel.htmlParser.Narou.ShortNarouImp
import java.io.IOException

class WattpadFacotry() : HTMLFactory() {
    var imp: WattpadImp? = null
    var maxpage=1
    init {
        defaulturl="https://www.wattpad.com"
        language=LanguageData.EN
    }
    private fun Typecheck(): WattpadImp? {
       // on-language
        var resimp: WattpadImp
        var number:String=""
        try {
            // val doc: Document = Jsoup.connect(url).get()
            val boxes=doc.getElementsByClass("description")
            val num=doc.getElementsByClass("btn btn-link on-vote")


           // val honbun=doc.getElementById("novel_honbun")
            //val maxnumber= boxes.size
            // BOXcheck=false
            //val ls=number.split("/")
            // val name=ls.get(0)
            if(boxes.size!=0){
                //Set_downloadbutton((URL_BOX))
                BOXcheck=true
                return BoxWattpadImp(doc)
            }
            else if(num.size!=0){
                //Set_downloadbutton((URL_CONTENT))
                BOXcheck=false
               /* val language_str=doc.getElementsByClass("on-language")
                val lan= language_str[1].text().split(":")[1]
                language= LanguageData.Converter(lan)*/
                return NormalWattpadImp(doc,geturl,maxpage)

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

    override fun getIndex(): Int? {
        return imp?.getIndex()
    }
    var geturl=defaulturl
    override fun parse(url:String?){
        if (url == null) {
            return
        }
            val cuturl= url.split("/page/")[0]
            geturl=cuturl
        super.parse(cuturl)


        this.imp=Typecheck()
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

    override fun downloadable(url: String?): Boolean {
       /* val c=url?.split("www.wattpad.com/")?.last();
        if (c != null && c.isNotEmpty()) {
            return c.get(0).isDigit()
        }
        return false;*/
        return true;
    }
}