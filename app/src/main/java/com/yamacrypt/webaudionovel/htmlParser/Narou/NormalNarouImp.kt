package com.yamacrypt.webaudionovel.htmlParser.Narou

import org.jsoup.nodes.Document

class NormalNarouImp(doc: Document) :NarouImp(doc) {
    val doc=doc
    override fun getTitle(): String? {
        val title= doc.getElementsByClass("novel_subtitle").text()
        val number=doc.getElementById("novel_no").text()
        val ls = number.split("/")
        val name = ls.get(0)
        //val titlename = name + ".　" + title
        return title
    }

    override fun getIndex(): Int? {
        val number=doc.getElementById("novel_no").text()
        val ls = number.split("/")
        val name = ls.get(0)
        return name.toInt()
    }
    override fun getTexts(): String? {
        val texts=doc.getElementById("novel_honbun").text()
        return texts
    }
}