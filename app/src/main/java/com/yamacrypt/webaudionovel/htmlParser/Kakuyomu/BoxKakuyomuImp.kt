package com.yamacrypt.webaudionovel.htmlParser.Kakuyomu

import org.jsoup.nodes.Document

class BoxKakuyomuImp(doc: Document,impurl:String) : KakuyomuImp(doc) {
    val url=impurl
    override fun getTitle(): String? {
        val title=doc.getElementById("workTitle")
        return title.select("a[href]").text()
    }


    override fun getChildURLs(): MutableList<String>? {
        val list=doc.getElementsByClass("widget-toc-episode-episodeTitle")
        val yomou_url=url
        val urls=  list.map { it->yomou_url+it.select("a[href]").attr("href") }
        //urls.map { it->yomou_url+it }
        return urls as MutableList<String>
    }
}