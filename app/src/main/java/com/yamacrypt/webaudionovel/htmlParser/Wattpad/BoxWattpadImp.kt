package com.yamacrypt.webaudionovel.htmlParser.Wattpad

import org.jsoup.nodes.Document

class BoxWattpadImp(doc: Document) :WattpadImp(doc) {
    val doc=doc
    override fun getTitle(): String? {
        //val title = doc.getElementsByClass("novel_title").text()
        val title=doc.select("h1").text()
        return title
    }

    override fun getChildURLs(): MutableList<String>? {
        val list=doc.getElementsByClass("on-navigate-part")
        val yomou_url="https://www.wattpad.com"
        val urls=  list.map { it->yomou_url+it.select("a[href]").attr("href") }
        //urls.map { it->yomou_url+it }
        return urls as MutableList<String>
    }
}
