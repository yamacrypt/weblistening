package com.yamacrypt.webaudionovel.htmlParser.Wattpad
import org.jsoup.nodes.Document
class ShortWattpadImp(doc: Document) : WattpadImp(doc) {
    val doc=doc
    override fun getIndex(): Int? {
        return 0
    }
    override fun getTexts(): MutableList<String> {
        val content=doc.select("pre")
        var stringList= mutableListOf<String>()
        val texts=content[0].getElementsByTag("p")
        for(text in texts) {
            //    val a=text.getElementsByTag("ruby")
            //  if(  a.size==0)
            stringList.addAll(text.text().split("。"))
            //stringList.add(text.text())
            //stringBuilder.append(text.text()).append("\n")
        }
        /*content.forEach{
                it ->
            val texts=it.getElementsByTag("p")
            for(text in texts) {
                //    val a=text.getElementsByTag("ruby")
                //  if(  a.size==0)
                stringList.addAll(text.text().split("。"))
                //stringList.add(text.text())
                //stringBuilder.append(text.text()).append("\n")
            }
        }*/
        // val texts=content.getElementsByTag("p")
        //val stringBuilder = StringBuilder()

        // val texts=doc.getElementById("novel_honbun").text()
        return stringList
    }
    fun next():Boolean{
        val link=doc.select("link [rel=next] ")
        if (link.size!=0)
            return true
        return false

    }
}
