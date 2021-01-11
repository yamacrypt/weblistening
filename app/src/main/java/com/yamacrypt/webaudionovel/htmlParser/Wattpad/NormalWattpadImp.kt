package com.yamacrypt.webaudionovel.htmlParser.Wattpad

import com.yamacrypt.webaudionovel.htmlParser.LanguageData
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.math.max

class NormalWattpadImp(doc: Document,url:String,maxpage:Int) : WattpadImp(doc) {
    var doc=doc
    val url=url
    val maxpage=maxpage
    override fun getTitle(): String? {
        //val title= doc.getElementsByClass("novel_subtitle").text()
       // val title=doc.select("h3").text()
        val title=doc.getElementsByClass("h2").first().text();
    //   val number=doc.getElementById("novel_no").text()
      //  val ls = number.split("/")
      //  val name = ls.get(0)
        //val titlename = name + ".　" + title
        return title
    }
    override fun getChildURLs(): MutableList<String>? {

        var urls= mutableListOf<String>()
        for (i in 1..maxpage){
            val u=url+"/page/"+i.toString()
              urls.add(u)
        }


        return urls
    }

    override fun getIndex(): Int? {
       var index=0;
        val title=getTitle();
        val ls=doc.getElementsByClass("part-title")
        val urls=  ls.map { it->it.text() }
        for ( i in 0..urls!!.size-1){
            if(urls[i].equals(title)) {
                index = i + 1;
                break;
            }
        }
        return index
    }
 override fun getTexts(): MutableList<String> {
      //  val facotry=WattpadFacotry()
     var number=1
     var stringList= mutableListOf<String>()
     var ur=url
     while(true){
           doc = Jsoup.connect(ur).get()

             val content=doc.select("pre")
            val texts=content[0].getElementsByTag("p")
            if(texts.size==0)
                break
         for(text in texts) {
                 //    val a=text.getElementsByTag("ruby")
                 //  if(  a.size==0)
                 stringList.addAll(text.text().split("。"))
                 //stringList.add(text.text())
                 //stringBuilder.append(text.text()).append("\n")
             }
         //val link=doc.select("[rel=next]")
         number++
        /* if (link.size==0) {
             break
         }*/
         ur=url+"/page/"+number.toString()
            // super.parse( u)
         }

       // val texts=content.getElementsByTag("p")
        //val stringBuilder = StringBuilder()

       // val texts=doc.getElementById("novel_honbun").text()
        return stringList
    }
}
