package com.yamacrypt.webaudionovel.htmlParser.Kakuyomu

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class NormalKakuyomuImp(doc: Document,url:String) : KakuyomuImp(doc) {
    override fun getTitle(): String? {
        val titles=doc.getElementsByClass("widget-episodeTitle js-vertical-composition-item")
        return titles[0].text()
    }

    override fun getTexts(): MutableList<String> {
        val content=doc.getElementsByClass("widget-episodeBody js-episode-body")
       val texts=content[0].getElementsByTag("p")
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
    }
    val url=url;
    override fun getIndex(): Int? {
        val targeturl=url
        var res=0;
        var boxurl=url.split("/episode").first();
        val doc2 = Jsoup.connect(boxurl).get()
        val list=doc2.getElementsByClass("widget-toc-episode-episodeTitle")
        val urls=  list.map { it->"https://kakuyomu.jp"+it.select("a[href]").attr("href") }
        for ( i in 0..urls!!.size-1){
            if(urls[i].equals(targeturl)) {
                res = i + 1;
                break;
            }
        }

        return res;
    }
}
