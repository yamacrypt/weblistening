package com.yamacrypt.webaudionovel.htmlParser

import android.content.Context
import com.yamacrypt.webaudionovel.R
import java.util.*

class LanguageData {
    companion object{
        val JP="ja"
        val EN="en"
        val Chinese="zh"
        fun getSplitterList(ls:List<String> , language:String): List<String> {
            val zenkaku_splitter =mutableListOf("。","、", "？", "！")
               // java.util.List.of("。", "、", "？", "！")
            val hankaku_splitter =mutableListOf(".", ",", "?", "!")
              //  java.util.List.of(".", ",", "?", "!")
            //val res: List<String> = ArrayList()
            var splitter_list = hankaku_splitter
            if (language === JP) {
                splitter_list = zenkaku_splitter
            }
         /*   var res= ls

            for (splitter: String in splitter_list) {
              //  ans=res
                var hoge= mutableListOf<String>()
                for(text:String  in res){
                  //  val hogelist= text.split(splitter).toMutableList()
                   /* for(i in 0..hogelist.size-1){
                        hogelist[i]+=splitter
                    }*/
                    val re= "(?<=。)".toRegex()
                      hoge.addAll(Split(text,splitter_list))

                }
                res=hoge
               // ls.split("(?<=" + splitter + ")");
            }*/
            var res= mutableListOf<String>()
            for(text:String  in ls){
                //  val hogelist= text.split(splitter).toMutableList()
                /* for(i in 0..hogelist.size-1){
                     hogelist[i]+=splitter
                 }*/
            //    val re= "(?<=。)".toRegex()
                if(text.length>0) {
                    res.addAll(Split(text, splitter_list))
                }
            }
            return res
        }
        fun Split(text:String,ls: List<String>):List<String>{
            val res= mutableListOf<String>()
            var former=-1
            for(i in 0..text.length-1){
                if(ls.contains(text[i].toString())){
                    res.add(text.substring(former+1,i+1))
                    former=i

                }
            }
            res.add(text.substring(former+1,text.length))
            return res
        }

        fun Converter(lan: String): String {
            val res=when(lan){
                "日本語" ->LanguageData.JP
                "English" ->LanguageData.EN
                else -> LanguageData.EN
            }
            return res
        }

    }

}