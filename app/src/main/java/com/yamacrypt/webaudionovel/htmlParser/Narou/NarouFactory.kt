package com.yamacrypt.webaudionovel.htmlParser.Narou

import com.yamacrypt.webaudionovel.htmlParser.HTMLFactory
import com.yamacrypt.webaudionovel.htmlParser.LanguageData
import java.io.IOException
import java.lang.Exception

open class NarouFactory() : HTMLFactory() {
    var imp:NarouImp? = null

     init {
         defaulturl="https://yomou.syosetu.com/"
         impurl="https://ncode.syosetu.com"
         language=LanguageData.JP
     }
    protected fun Typecheck(url: String): NarouImp? {
            var resimp: NarouImp
            var number:String=""
            try {
               // val doc: Document = Jsoup.connect(url).get()
               val boxes=doc.getElementsByClass("index_box")
                val num=doc.getElementById("novel_no")
                val honbun=doc.getElementById("novel_honbun")
                //val maxnumber= boxes.size
               // BOXcheck=false
                //val ls=number.split("/")
                // val name=ls.get(0)
                if(boxes.size!=0){
                    //Set_downloadbutton((URL_BOX))
                    BOXcheck=true
                   return BoxNarouImp(doc,impurl)
                }
                else if(num!=null){
                    //Set_downloadbutton((URL_CONTENT))
                    BOXcheck=false
                   return NormalNarouImp(doc)

                }
                else if( honbun!=null){
                    BOXcheck=false
                    //Set_downloadbutton((URL_CONTENT))
                   return ShortNarouImp(doc,url)

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
        this.imp=Typecheck(url)
    }

    override fun downloadable(url: String?): Boolean {
        //return super.downloadable(url)
        try {
            if (url!!.contains("//ncode."))
                return true
        }
        catch (e :Exception){
        }
        return false
    }
    override fun getTitle(): String? {
       return imp?.getTitle()
    }

    override fun getIndex(): Int? {
        return imp?.getIndex();
    }
    override fun getTexts(): MutableList<String> {
        val content=doc.getElementById("novel_honbun")
        val texts=content.getElementsByTag("p")
        //val stringBuilder = StringBuilder()
        var stringList= mutableListOf<String>()
        for(text in texts) {
            //    val a=text.getElementsByTag("ruby")
            //  if(  a.size==0)
            stringList.add(text.text())
            //stringList.add(text.text())
            //stringBuilder.append(text.text()).append("\n")
        }
        return stringList
      //  return imp?.getTexts()
    }

    override fun getChildURLs(): MutableList<String>? {
       return  imp?.getChildURLs()
    }
}