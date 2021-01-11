package com.yamacrypt.webaudionovel.htmlParser.Narou

import org.jsoup.nodes.Document

class BoxNarouImp(doc: Document,impurl:String) : NarouImp(doc) {
    val url=impurl
    val doc=doc
    override fun getTitle(): String? {
        val title = doc.getElementsByClass("novel_title").text()
        return title
    }

    override fun getChildURLs(): MutableList<String>? {
       val list=doc.getElementsByClass("subtitle")
        val yomou_url=url
        val urls=  list.map { it->yomou_url+it.select("a[href]").attr("href") }
        //urls.map { it->yomou_url+it }
        return urls as MutableList<String>
    }
}