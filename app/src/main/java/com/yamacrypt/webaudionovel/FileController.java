package com.yamacrypt.webaudionovel;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.yamacrypt.webaudionovel.Database.DBProvider;
import com.yamacrypt.webaudionovel.Database.DBTableName;
import com.yamacrypt.webaudionovel.Database.StoryIndexDB;
import com.yamacrypt.webaudionovel.Database.StoryIndexModel;
import com.yamacrypt.webaudionovel.MusicService.tts_Item;
import com.yamacrypt.webaudionovel.htmlParser.LanguageData;
import com.yamacrypt.webaudionovel.ui.PlayerViewModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class FileController {

    Context context;
    File csvfile;
    public FileController(Context context){
        this.context=context;
     //   this.csvfile=csvfile;
    }
     InputStream ConvertingFiletoInputStream(File file)
            throws IOException {
        InputStream targetStream = new FileInputStream(file);
        return targetStream;
    }
    private static final String COMMA = ",";
    private static final String NEW_LINE= "\r\n";
    public void CSVInit(){
        try{
            FileWriter fileWriter = new FileWriter(csvfile);
            BufferedWriter bfWriter = new BufferedWriter(fileWriter);
            bfWriter.write(DataStore.header);
            bfWriter.close();
        }
        catch (Exception e){}
    }

    public void WriteMap(String num){
        try {
            FileWriter fileWriter = new FileWriter(csvfile,true);
            BufferedWriter bfWriter = new BufferedWriter(fileWriter);
            bfWriter.write(num+",");
            bfWriter.close();
        }
        catch (Exception e){}
    }
    public String ReadRawUrl(File shortcutpath){
        ShortCut sc=ReadShortCut(shortcutpath);
        String path=new File(sc.link).getName();
        String res="https:"+path.replace("-","/");
        return res;
    }
    String getchild(String s){
        String[] ls=s.split("/");
        String res="";
        if(ls.length>1) {
            for (int i = 0; i < ls.length - 1; i++) {
                res += ls[i] + "/";
            }
            return res.substring(0, res.length() - 1);
        }
        throw new Error();
    }
    public String ReadParentRawUrl(File shortcutpath) throws Exception {
        ShortCut sc=ReadShortCut(shortcutpath);
        String path=new File(sc.link).getName().replace("-","/");
        String prefix="//";
       switch (path.charAt(prefix.length())){
            case 'n' :return "https:"+getchild(path);
            case 'k' : return "https:"+getchild(getchild(path));
            case 'w' : throw new Exception();
        }
        String res="https:"+path;
        return res;
    }
    public void WriteNovel(File file,List<String> ls,String language){
        /*File root= context.getFilesDir();
        File dir=new File(root.getAbsolutePath().toString()+"/novel");
        if(!dir.exists())
            dir.mkdir();
        File file = new File(dir, filename);*/
        List<String> stringList=LanguageData.Companion.getSplitterList(ls,language);
        try {
            FileWriter fileWriter = new FileWriter(file,false);
            BufferedWriter bfWriter = new BufferedWriter(fileWriter);
            //bfWriter.write(num+",");
            for (String l : stringList) {
                if(!l.isEmpty()) {
                    bfWriter.write(l);
                    bfWriter.write("\n");
                }
            }
            bfWriter.write("");
            bfWriter.write("\n");
            bfWriter.close();
        }
        catch (Exception e){}
    }
   /* public List<String> Split(List<String> ls, String language){
        List<String > splitter_list= LanguageData.Companion.getSplitterList(ls,language);
       List<String > res=new ArrayList<>();
        for (String text : ls) {

            for(String splitter :splitter_list) {
             Collections.addAll(res, text.split(String.format("(?<=%d)", splitter)));
            }
        }
        return res;
    }*/
    public List<String > ReadLines(File file){
      /*  File root= context.getFilesDir();
        File dir=new File(root.getAbsolutePath().toString()+"/novel");
        File file = new File(dir, filename);*/
        BufferedReader br = null;
        List<String> res= new ArrayList<>();
        try {
            InputStream is = ConvertingFiletoInputStream(file);
            // fi = new FileInputStream("namelist.csv");
            //is = new InputStreamReader(fi);
            // br = new BufferedReader(is);
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            br = new BufferedReader(inputStreamReader);

            //読み込み行
            String line;
            //列名を管理する為の配列
            String[] arr = null;
            //1行ずつ読み込みを行う
            while ((line = br.readLine()) != null) {

                res.add(line);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }
    public Integer[] ReadMap(){
        BufferedReader br = null;
        List<Integer> res= new ArrayList<Integer>();
        //String[][] result=new String[max][]
        try {
            InputStream is = ConvertingFiletoInputStream(csvfile);
            // fi = new FileInputStream("namelist.csv");
            //is = new InputStreamReader(fi);
            // br = new BufferedReader(is);
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            br = new BufferedReader(inputStreamReader);

            //読み込み行
            String line;
            //列名を管理する為の配列
            String[] arr = null;
            //1行ずつ読み込みを行う
            while ((line = br.readLine()) != null) {

                    //カンマで分割した内容を配列に格納する
                    // arr = { "no","name","age","gender","bloodtype" };
                    arr = line.split(",");
                    for (String s : arr) {
                        try {
                            res.add(Integer.parseInt(s));
                        }
                        catch (Exception e){}
                    }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Integer[] ans=new Integer[res.size()];
        res.toArray(ans);
        //return res;
        Arrays.sort(ans);
        return ans;
    }

    public void WriteCSV(DataStore.BookData data){

        try {
            FileWriter fileWriter = new FileWriter(csvfile,true);
            BufferedWriter bfWriter = new BufferedWriter(fileWriter);
            bfWriter.write(data.getTitle());
            bfWriter.write(COMMA);
            bfWriter.write(data.getTitle_hiragana());
            bfWriter.write(COMMA);
            bfWriter.write(data.getAuthor());
            bfWriter.write(COMMA);
            bfWriter.write(data.getAuthor_hiragana());
            bfWriter.write(COMMA);
            bfWriter.write(data.getTitlenumber());
            bfWriter.write(NEW_LINE);
            bfWriter.close();
        }
        catch (Exception e){}

    }
    public List<DataStore.BookData> SearchCSV(String target){
        //ファイル読み込みで使用する３つのクラス
        //FileInputStream fi = null;
        //InputStreamReader is = null;
        BufferedReader br = null;
        int max=1000;
        //String[][] result=new String[max][];
        //DataStore.BookData[] res=new DataStore.BookData[max];
        List<DataStore.BookData> res=new ArrayList<DataStore.BookData>();
        //res[0].setTitlenumber("3");
       // AssetManager assetManager=context.getAssets();
        //読み込みファイルのインスタンス生成
        //ファイル名を指定する
        //FileInputStream fi = new FileInputStream(fileDescriptor.getFileDescriptor());
        //CSVParser csvParser = new CSVParser(this, "csvtest01.txt");
        //csvParser.parse();
        try {
            InputStream is = ConvertingFiletoInputStream(csvfile);
           // fi = new FileInputStream("namelist.csv");
            //is = new InputStreamReader(fi);
           // br = new BufferedReader(is);
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            br = new BufferedReader(inputStreamReader);

            //読み込み行
            String line;

            //読み込み行数の管理
            int i = 0;
            int j=0;
            //列名を管理する為の配列
            String[] arr = null;

            //1行ずつ読み込みを行う
            while ((line = br.readLine()) != null) {
                if(j>=max){
                    break;
                }
                //先頭行は列名
                if (i == 0) {

                    //カンマで分割した内容を配列に格納する
                    // arr = { "no","name","age","gender","bloodtype" };
                    arr = line.split(",");

                } else {

                    //データ内容をコンソールに表示する
                    //System.out.println("-------------------------------");

                    //データ件数を表示
                    //System.out.println("データ" + i + "件目");

                    //カンマで分割した内容を配列に格納する
                    String[] data = line.split(",");
                    for(String d:data){
                        if(d.contains(target)){
                            //result[j]=data;
                            DataStore.BookData   item=new DataStore.BookData();
                            item.setTitle(data[0]);
                            item.setTitle_hiragana(data[1]);
                            item.setAuthor(data[2]);
                            item.setAuthor_hiragana(data[3]);
                            item.setTitlenumber(data[4]);
                            res.add(item);
                            j++;
                            break;
                        }
                    }
                    //配列の中身を順位表示する。列数(=列名を格納した配列の要素数)分繰り返す
                    /*int colno = 0;
                    for (String column : arr) {
                        System.out.println(column + ":" + data[colno]);
                        colno++;

                    }*/

                }

                //行数のインクリメント
                i++;

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
       // DataStore.BookData[] ans=new DataStore.BookData[res.size()];
        //res.toArray(ans);
        return res;
    }
    public ShortCut ReadShortCut(File file){
       List<String> ls= ReadLines(file);
       String link=ls.get(0);
       int size= Integer.parseInt(ls.get(1));
        String language=LanguageData.Companion.getJP();
       try {
         language = ls.get(2);
       }
       catch (Exception e){}
       ShortCut sc=new ShortCut(link,size,language);
       return sc;
    }
  public  static class ShortCut{
        private  String language;
        private String novelname;

      public String getLink() {
          return link;
      }

      private String link;


        private String authorname;
        private int size;
        public ShortCut(String novelname, int size, String language){
            this.novelname=novelname;
            this.link=novelname;
            this.size=size;
            try {
                this.language = language;
            }
            catch (Exception e){
                this.language="jp";
            }
        }
        public ShortCut(String novelname,String authorname, String url,int size, String language){
            this.novelname=novelname;
            this.authorname=authorname;
            this.link=url;
            this.size=size;
            try {
                this.language = language;
            }
            catch (Exception e){
                this.language="jp";
            }
        }
        public String getNovelname() {
            return novelname;
        }

        public void setNovelname(String novelname) {
            this.novelname = novelname;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getLanguage() {
            return language;
        }
    }
    public tts_Item OpenShortCut(String url,int startindex){
        try {
            //ShortCut sc = ReadShortCut(file);
            StoryIndexDB db= (StoryIndexDB) DBProvider.Companion.of(DBTableName.storyindex,context);
            StoryIndexModel sc=db.getStoryIndexItem(url);
            PlayerViewModel model = new PlayerViewModel();
            PlayerViewModel.PlayData playData = new PlayerViewModel.PlayData(sc.getNovel_name());
           // PlayerViewModel playerViewModel = new PlayerViewModel();
            //playerViewModel.getPlaydata().setValue(new PlayerViewModel.PlayData(file.getName()););
            model.getPlaydata().postValue(playData);
            //DataStore.intro=file.getName();
            File novelfile = DataStore.getNovelFile(context, sc.getLink());
            List<String> ls = ReadLines(novelfile);
            //TTSController ttsController=new TTSController(context);
            //TTSService.Companion.setup(ls);
            //TTSController tts=TTSController();
            tts_Item item = new tts_Item(ls, sc.getLanguage(), startindex);
            return item;
        }
        catch (Exception e){
            return null;//ttsController.setup(ls,sc.language,startindex);
        }
       /* try {
            TextView title = MenuFragment.popupView.findViewById(R.id.dl_text);
            title.setText(sc.novelname);
        }
        catch (Exception e){}*///TTSService.ttsController.s();
        // ttsController.start();
    }
    public tts_Item OpenShortCut(String url){
        return OpenShortCut(url,0);
    }
    public tts_Item _OpenShortCut(File file,int startindex){
        try {
            ShortCut sc = ReadShortCut(file);
           //StoryIndexDB db= (StoryIndexDB) DBProvider.Companion.of(DBTableName.storyindex,context);
          //  LibraryItemModel sc=db.getItem(file)
            PlayerViewModel model = new PlayerViewModel();
            PlayerViewModel.PlayData playData = new PlayerViewModel.PlayData(file.getName());
            PlayerViewModel playerViewModel = new PlayerViewModel();
            //playerViewModel.getPlaydata().setValue(new PlayerViewModel.PlayData(file.getName()););
            model.getPlaydata().postValue(playData);
            //DataStore.intro=file.getName();
            File novelfile = DataStore.getNovelFile(context, sc.getNovelname());
            List<String> ls = ReadLines(novelfile);
            //TTSController ttsController=new TTSController(context);
            //TTSService.Companion.setup(ls);
            //TTSController tts=TTSController();
            tts_Item item = new tts_Item(ls, sc.language, startindex);
            return item;
        }
        catch (Exception e){
            return null;//ttsController.setup(ls,sc.language,startindex);
        }
       /* try {
            TextView title = MenuFragment.popupView.findViewById(R.id.dl_text);
            title.setText(sc.novelname);
        }
        catch (Exception e){}*///TTSService.ttsController.s();
       // ttsController.start();
    }
    public tts_Item _OpenShortCut(File file){
        return _OpenShortCut(file,0);
    }
    public void WriteShortCut(File file, int size, String url,String language) {
        /*File root= context.getFilesDir();
        File dir=new File(root.getAbsolutePath().toString()+"/shortcut");
        if(!dir.exists())
            dir.mkdir();
        File file = new File(dir, titlename);*/
        try {
            FileWriter fileWriter = new FileWriter(file,false);
            BufferedWriter bfWriter = new BufferedWriter(fileWriter);
            //bfWriter.write(num+",");
            bfWriter.write(url);
            bfWriter.write("\n");
            bfWriter.write( Integer.toString(size));
            bfWriter.write("\n");
            bfWriter.write(language);
            bfWriter.close();
        }
        catch (Exception e){}
    }
    public void WriteShortCut(File file,ShortCut sc) {
        /*File root= context.getFilesDir();
        File dir=new File(root.getAbsolutePath().toString()+"/shortcut");
        if(!dir.exists())
            dir.mkdir();
        File file = new File(dir, titlename);*/
        String url=sc.link ;
        int size=sc.size;
        String language=sc.language;
        try {
            FileWriter fileWriter = new FileWriter(file,false);
            BufferedWriter bfWriter = new BufferedWriter(fileWriter);
            //bfWriter.write(num+",");
            bfWriter.write(url);
            bfWriter.write("\n");
            bfWriter.write( Integer.toString(size));
            bfWriter.write("\n");
            bfWriter.write(language);
            bfWriter.close();
        }
        catch (Exception e){
            Log.d("filecreate",e.toString());}
    }
    public class CSVParser {

        private Context context;
        private String file;

        public CSVParser(Context context, String file){
            this.file = file;
            this.context = context;
        }

        public void parse() {
            // AssetManagerの呼び出し
            AssetManager assetManager = context.getResources().getAssets();
            try {
                // CSVファイルの読み込み
                InputStream is = assetManager.open(file);
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                BufferedReader bufferReader = new BufferedReader(inputStreamReader);
                String line = "";
                String txt = "";

                while ((line = bufferReader.readLine()) != null) {

                    // 各行が","で区切られていて5つの項目
                    StringTokenizer st = new StringTokenizer(line, ",");
                    String first = st.nextToken();
                    String second = st.nextToken();
                    String third = st.nextToken();
                    String fourth = st.nextToken();
                    String fifth = st.nextToken();

                    txt = first + " " + second + " " + third + " " + fourth + " " + fifth + "\n";

                    //文字列を追加する
                    //textView.append(txt);

                }

                bufferReader.close();

            } catch (IOException e) {
                //e.printStackTrace();
               // textView.setText("読み込みに失敗しました・・・");
            }
        }
    }
}
