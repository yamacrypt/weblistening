package com.yamacrypt.webaudionovel.htmlParser.Kakuyomu

import org.jsoup.nodes.Document

open class KakuyomuImp(doc: Document) {
    val doc=doc
    open fun getTitle():String? {
        TODO("Not yet implemented")
    }


    open fun getTexts() :MutableList<String>? {
        TODO("Not yet implemented")
    }

    open fun getChildURLs() : MutableList<String>? {
        TODO("Not yet implemented")
    }

    open fun getIndex(): Int? {
        TODO("Not yet implemented")
    }
}
