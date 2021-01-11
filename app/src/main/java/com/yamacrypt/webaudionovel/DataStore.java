package com.yamacrypt.webaudionovel;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;

import com.yamacrypt.webaudionovel.ui.library.common.FileType;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    public static String baseurl="https://storage.googleapis.com/hogey/compressedmusic/";
    public static String fileextension=".ogg";
    public static String header="作品名,作品名読み,著者名,著者名読み,ID\r\n";
    public static String csv_library="lib.csv";
    public static String csv_search="dlsearch.csv";
    public static String url_dlsearch="https://storage.googleapis.com/hogey/dlsearch.csv";
    public static String plus="/music";
    public static String map_file="hashmap";
    public static String speedKey="Speed*10";
  //  public static String dlcountKey="dlcount";
    public static String dateKey="resetdate";
    public static List<String> downloading_files=new ArrayList<>();
    public static boolean check_window=false;
    public static String review="review";
    public static int review_span=10;


    public static String intro;
    public static String pitchKey="Pitch*10";
    public static final int yomou_mode =1;
    @Nullable
    public static final int kakuyomu_mode=2;
    @Nullable
    public static final int wattpadd_mode=3;
    public static final int noc_mode=4;
    public static final int mnlt_mode=5;
    public static final int mid_mode=6;
    public static String languageKey="Language";
    @Nullable
    public static final int smallwindow_position=1;
    public static String speed_position="speedposition";
    public static String pitch_position="pitchposition";
    public static int default_adtime=10;
    public static int ad_time=default_adtime;


    public static File getShortCutFile(Context context,String name,File dir){
       // File root= context.getFilesDir();
       // File dir=new File(root.getAbsolutePath().toString(),"shortcut");
        File file = new File(dir, name);
        try {
            file.createNewFile();
        }
        catch (Exception e){}
        //if(!file.exists())
        //    file.mkdir();
        //File file = new File(dir, name);
        return file;
    }
    public static File getShortCutFile(Context context,String name){
        File root= context.getFilesDir();
        File dir=new File(root.getAbsolutePath(),"shortcut");
        if (!dir.exists()) {
            dir.mkdir();
        }
      return getShortCutFile(context,name,dir);
    }
    public static String getConvertedURL(String url){
        String s=url.split(":")[1].replace("/","-");
        return s;
    }
    public static File getNovelFile(Context context,String name){
        File root= context.getFilesDir();
        File dir=new File(root.getAbsolutePath().toString(),"novel");
        if(!dir.exists())
            dir.mkdir();
        File file = new File(dir, name);
        return file;
    }
    public static File  CreateFolder(Context context,String name){
        File root= getShortCutFile(context,"");
       // File dir=new File(root.getAbsolutePath().toString()+"/"+name);
        File dir = new File(root, name);
       // if(!dir.exists()&& !dir.isDirectory())
          //  dir.mkdir();
        return dir;
    }
    public static File getFile(Context context,String folder_plus,String filename) {
        //File filedir =context.getFilesDir();
        File filedir =context.getExternalFilesDir(null) ;
        String base_folder="/Yukarireading";
        File directory = new File(filedir.getAbsolutePath() + base_folder+folder_plus);
        if (directory.exists() == false) {
            File formdir = new File(filedir.getAbsolutePath() + base_folder);
            formdir.mkdir() ;
            directory.mkdir() ;
            /*if (formdir.mkdir() == true && directory.mkdir() == true){

            }
            else{
                Toast.makeText(context , "トーストメッセージ", Toast.LENGTH_LONG).show();
            }*/

        }
        File file=new File(filedir.getAbsolutePath() + base_folder+folder_plus+"/"+filename);
        return file;
    }
    public static File getFile(Context context,String filename){
        return getFile(context,"",filename);
    }
   static public SharedPreferences getSharedPreferences(Context context){
        SharedPreferences prefs =context.getSharedPreferences("SaveData",0);
        return prefs;
    }
    public static String getSpeed_text(int speed10) {
        DecimalFormat df = new DecimalFormat("00");
        String speed_text = df.format(speed10);
        return String.format("%s.%sx",speed_text.charAt(0),speed_text.charAt(1));
    }
    public static String getFileName(String number) {
        return number+fileextension;
    }
    public static void Layout_SetEnabled(LinearLayout layout, boolean enabled){
        // LinearLayout layout = (LinearLayout) findViewById(R.id.my_layout);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setEnabled(enabled);
            if(enabled) {
                child.setVisibility(View.VISIBLE);
            }
            else{
                child.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static void Decrement(SharedPreferences pref,String key) {
        SharedPreferences.Editor editor=pref.edit();
        editor.putInt(key,pref.getInt(key,1)-1);
        editor.apply();
    }

    public static class BookData{
       public BookData(){
            dl_checked =false;
        }
        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAuthor_hiragana() {
            return author_hiragana;
        }

        public void setAuthor_hiragana(String author_hiragana) {
            this.author_hiragana = author_hiragana;
        }

        public String getTitlenumber() {
            return titlenumber;
        }

        public void setTitlenumber(String titlenumber) {
            this.titlenumber = titlenumber;
        }

        public String getTitle_hiragana() {
            return title_hiragana;
        }

        public void setTitle_hiragana(String title_hiragana) {
            this.title_hiragana = title_hiragana;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        private String title;
        private String title_hiragana;
        private String titlenumber;
        private String author;
        private String author_hiragana;

        public Boolean getDl_checked() {
            return dl_checked;
        }

        public void setDl_checked(Boolean dl_checked) {
            this.dl_checked = dl_checked;
        }

        private  Boolean dl_checked;
    }
}
