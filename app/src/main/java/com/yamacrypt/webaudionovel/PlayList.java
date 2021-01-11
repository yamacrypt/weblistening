package com.yamacrypt.webaudionovel;

import java.util.List;

public class PlayList {
    public static int getCurrent_number() {
        return current_number;
    }

    public static void setCurrent_number(int current_number) {
        PlayList.current_number = current_number;
    }

    static int current_number;//ファイルの相対位置

    public static String getRootpath() {
        return rootpath;
    }

    static String rootpath,_rootpath;
   static List<String> urllist,_urllist;
    static int maxnumber;
    public static String getPlayingPath(){
        return urllist.get(current_number);
    }
   public static void  _setup(List<String> list , int number,String path){
        current_number=number;
        urllist = list;
        rootpath=path;
        maxnumber= urllist.size();
    }
    public static void  setup(List<String> list ,String path){
        _urllist = list;
        _rootpath=path;
       //_setup(list,0,path);
    }
    public static void done(){
        //current_number=number;
        urllist = _urllist;
        rootpath= _rootpath;
        maxnumber= urllist.size();
    }
}

