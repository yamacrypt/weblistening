package com.yamacrypt.webaudionovel.htmlParser.Narou

import org.jsoup.nodes.Document

class ShortNarouImp(doc: Document): NarouImp(doc) {
    val doc=doc
    override fun getTitle(): String? {
       val title= doc.getElementsByClass("novel_title").text()
        return title
    }

    override fun getIndex(): Int? {
        return 0
    }
    override fun getTexts(): String? {
        val texts=doc.getElementById("novel_honbun").text()
        return texts
    }
}