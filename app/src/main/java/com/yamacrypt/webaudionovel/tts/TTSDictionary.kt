package com.yamacrypt.webaudionovel.tts


import java.lang.Exception
import kotlin.collections.HashMap


class TTSDictionary(dictionaryForReplacement:HashMap<String,String>) {
    private var dictionaryForSearch= hashMapOf<Char,List<Node<Char>>>();
    private val dictionaryForReplacement=dictionaryForReplacement
    init{
        for(key in dictionaryForReplacement.keys){
            val ele=LinkedNodeList.convert(key)
            if(ele.first()==null)
                continue
            val first=ele.first()!!
            if (!dictionaryForSearch.containsKey(first.nodeValue)) {
                dictionaryForSearch[first.nodeValue]= listOf(first)
            }
            else{
                var res=dictionaryForSearch[first.nodeValue]
                res!!.toMutableList().add(first)
                dictionaryForSearch[first.nodeValue]=res
            }
        }
    }
    fun convert(rawText:String):String{
        var res:String=""
        var convertableText= ""
        var splitText= ""
        var candidateList= mutableListOf<Node<Char>>()
        var convertableCandidateList= mutableListOf<String>()
        var current_index=0
        val paddingText= "$rawText@"
        while(current_index <paddingText.length){
            val char=paddingText[current_index]
            if (candidateList.size>0){
                //println(splitText)
                val newList= mutableListOf<Node<Char>>()
                for(candidate in candidateList){
                    val next=candidate.next
                    if(next==null){
                        convertableText=splitText
                    }
                    else if(next.nodeValue == char){
                        newList.add(next)
                    }
                }

                if(newList.size==0){
                    candidateList=mutableListOf<Node<Char>>()
                    if (convertableText.length>0) {
                        res += dictionaryForReplacement[convertableText]
                    }
                    res+=splitText.substring(convertableText.length)
                    convertableText=""
                    splitText=""
                    current_index--
                }
                else{
                    candidateList=newList.toMutableList()
                    splitText+=char
                }

            }
            else if (dictionaryForSearch.containsKey(char)){
                splitText+=char
                candidateList= dictionaryForSearch[char]!!.toMutableList()
            }
            else{
                res+=char
            }
            current_index++
        }
        return res.removeSuffix("@")
    }
}

data class Node<T>(var nodeValue: T, var next: Node<T>? = null)
class LinkedNodeList<T> {
    private var list= mutableListOf<Node<T>>()
    companion object{
        fun convert(s:String): LinkedNodeList<Char> {
            val res :LinkedNodeList<Char> =LinkedNodeList()
            for(char in s){
                res.add((char))
            }
            return res
        }
    }
    fun add(n: T) {
        if (list.size==0) {
            list.add(Node<T>(n))
        }
        else {
            val newNode =Node<T>(n)
            list.last().next=newNode
            list.add(newNode)
        }
    }
    fun count():Int{
        return list.size;
    }
    fun first():Node<T>?{
        return try {
            list.first()
        } catch (e:Exception){
            null
        }
    }
    fun last():Node<T>?{
        return try {
            list.last()
        } catch (e:Exception){
            null
        }
    }

}