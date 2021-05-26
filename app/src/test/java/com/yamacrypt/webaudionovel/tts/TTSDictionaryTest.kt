package com.yamacrypt.webaudionovel.tts

import junit.framework.TestCase
import org.junit.Test

class TTSDictionaryTest : TestCase(){
    @Test
    fun test(){
        val dic1= hashMapOf<Char,List<Node<Char>>>()
        dic1['五']= listOf(Node<Char>('五',Node<Char>('芒')))
        val dic2= hashMapOf<String,String>()
        dic2["五芒"]= "ごぼうせい"
        dic2["最高"]= "さいこう"
        dic2["あく"]= "さいこう"
        dic2["日"]= "デイ"
        val dictionry=TTSDictionary(dic2)
        val res=dictionry.convert("最高ああ最高五芒最高日")
        println(res)
        /*val dictionry=TTSDictionary(HashMap<Char,List<Node<Char>>(){
            "五芒星":Node("ご","ぼ")
        },HashMap(){

        })*/
    }
    @Test
    fun test2(){
        val dic1= hashMapOf<Char,List<Node<Char>>>()
        dic1['五']= listOf(Node<Char>('五',Node<Char>('芒')))
        val dic2= hashMapOf<Char,List<Node<Char>>>()
        dic2['五']= listOf(Node<Char>('1',Node<Char>('芒')))
        dic1+=dic2
        println(dic1)
        val dic22= hashMapOf<String,String>()
        dic22["五芒"]= "ごぼうせい"
        val dictionry=TTSDictionary(dic22)
    }
}