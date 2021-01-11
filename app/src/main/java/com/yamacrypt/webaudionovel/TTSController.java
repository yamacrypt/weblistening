package com.yamacrypt.webaudionovel;

import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import com.yamacrypt.webaudionovel.Database.DBProvider;
import com.yamacrypt.webaudionovel.Database.DBTableName;
import com.yamacrypt.webaudionovel.Database.StoryIndexDB;
import com.yamacrypt.webaudionovel.Database.StoryIndexModel;
import com.yamacrypt.webaudionovel.MusicService.MusicLibrary;
import com.yamacrypt.webaudionovel.MusicService.tts_Item;
import com.yamacrypt.webaudionovel.ui.PlayerViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class TTSController {
    Context context;
    private static TextToSpeech tts;
    static boolean isInitialized=false;
    static boolean startrequest=false;

    public void shutdown() {
        tts.shutdown();
    }

    public interface onFinishedListener{
        void onFinished();
    }
    public void setListener(onFinishedListener listener) {
        this.listener = listener;
    }
    static onFinishedListener listener;
    void Init(String language) {
        Init(language,0);
    }
    void Init(String language,int index){
        isInitialized=false;
        //Locale locale= Locale.forLanguageTag(language);
        SharedPreferences prefs= DataStore.getSharedPreferences(context);
        String lan=prefs.getString(DataStore.languageKey,"ja");
        Locale locale= Locale.forLanguageTag(lan);
        try{
            tts.stop();
        }
        catch (Exception e){}
        tts=new TextToSpeech(context,new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = tts.setLanguage(locale);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    isInitialized=true;
                    if(startrequest){
                        speak(index);
                    }
                   //
                    // speak(index);
                    Log.i("TTS", "Initialization success.");
                } else {
                    //Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                //number++;
                //next();
                int number=playerViewModel.getSpeakingnumber().getValue();
                number++;
                speak(number);
                  /*  if (number < maxnumber) {
                        playerViewModel.getSpeakingnumber().postValue(number + 1);
                        number++;
                        tts.speak(stringList.get(number), TextToSpeech.QUEUE_FLUSH, null, "TAG");
                        //speak();
                    }
                    else{
                        next();
                    }*/
            }

            @Override
            public void onError(String utteranceId) {

            }
        });
        SharedPreferences pref= DataStore.getSharedPreferences(context);
        int speed10=pref.getInt(DataStore.speedKey,10);
        Change_Speed(speed10/10f);
        //SharedPreferences pref2= DataStore.getSharedPreferences(context);
        int pitch10=pref.getInt(DataStore.pitchKey,10);
        Change_Pitch(pitch10/10f);
    }
   /* public TTSController(Context context){
        this.context=context;
        playerViewModel=new PlayerViewModel();
       mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                nextwithoutad();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

    }*/
    public TTSController(Context context){
        this.context=context;
        playerViewModel=new PlayerViewModel();

       /* mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                nextwithoutad();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });*/

    }
    public TTSController(){
        //this.context=context;
        //playerViewModel=new PlayerViewModel();

       /* mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                nextwithoutad();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });*/

    }
    //int number;

  /*  public int getMaxnumber() {
        return maxnumber;
    }

    int maxnumber;*/
    //int speakingnumber=0;
    public String getCurrentstring() {
        int number =playerViewModel.getSpeakingnumber().getValue();
        int maxnumber=playerViewModel.getMaxnumber().getValue();
        if(number<maxnumber)
        return stringList.get(number);
        else
            return "";
    }
    public  int getCurrentIndex(){
        return playerViewModel.getSpeakingnumber().getValue();
    }
    static PlayerViewModel playerViewModel;
    static List<String> stringList;
    public void setup(@NotNull List<String> stringList,String language,int startindex){
        Init(language,startindex);
        playerViewModel.getSpeakingnumber().postValue(startindex);
       // number =playerViewModel.getSpeakingnumber().getValue();
        this.stringList=stringList;
        //this.maxnumber=stringList.size()-2;
        playerViewModel.getMaxnumber().postValue(stringList.size()-1);
        //speakingnumber=startindex;
       //playerViewModel.getPlayingstate().setValue(false);
       //final Observer<Integer>
      // playerViewModel.getPlayingstate().observe(observer);

    }
    public void setup(@NotNull List<String> stringList,String language){
      setup(stringList,language,0);

    }
    public void setup(tts_Item item){
        Init(item.getLanguage(),item.getStart_index());
        playerViewModel.getSpeakingnumber().postValue(item.getStart_index());
        // number =playerViewModel.getSpeakingnumber().getValue();
        this.stringList=item.getTexts();
        //this.maxnumber=stringList.size()-2;
        playerViewModel.getMaxnumber().postValue(stringList.size()-1);
    }
    public void speak(int number) {
     {
            if (isInitialized) {


                //i=0;
                // int number =playerViewModel.getSpeakingnumber().getValue();
                // int number=0;
                // number=playerViewModel.getSpeakingnumber().getValue();
                int maxnumber = playerViewModel.getMaxnumber().getValue();
                if (number < maxnumber) {
                    playerViewModel.getSpeakingnumber().postValue(number);
                    // speakingnumber=number;
                    //number++;
                    try {
                        tts.speak(stringList.get(number), TextToSpeech.QUEUE_FLUSH, null, "TAG");//speak();
                        playerViewModel.getPlayingstate().postValue(true);
                    } catch (Exception e) {
                    }

                } else {
                    //next();
                    listener.onFinished();
                }
            } else {
                startrequest = true;
            }
        }

    }
    public void setContext(Context context){
        this.context=context;
      //  listener= (onFinishedListener) context;
    }

    public void speak_continue(){

        speak(playerViewModel.getSpeakingnumber().getValue());
    }
    public void s(){
        tts.speak(stringList.get(0),TextToSpeech.QUEUE_FLUSH,null,"TAG");

    }
    public void stop(){
        if(isInitialized) {
            tts.stop();
            playerViewModel.getPlayingstate().setValue(false);
        }
        else{
            startrequest=false;
        }
    }
    public void start(){
        speak(playerViewModel.getSpeakingnumber().getValue());
    }
    public boolean isSpeaking(){
        try {
            return tts.isSpeaking();
        }
       catch (Exception e) {
           return false;
       }
    }
    public void Change_Speed(float speed){
        try {
            tts.setSpeechRate(speed);
        }
        catch (Exception e){}
    }
    public void Change_Pitch(float pitch){
        try {
            tts.setPitch(pitch);
        }
        catch (Exception e){}
      //  tts.setPitch(pitch);
    }
    public void Change_Language(String language){
        Locale loc= Locale.forLanguageTag(language);
        try {
            tts.setLanguage(loc);
        }
        catch (Exception e){}
       // tts.setLanguage(loc);
    }
    public void back(int i) {
        stop();
        int number=playerViewModel.getSpeakingnumber().getValue();
        if(number>0) {
            playerViewModel.getSpeakingnumber().setValue(number - i);
            number--;
        }
        start();
        //tts.speak(stringList.get(number), TextToSpeech.QUEUE_FLUSH, null, "TAG");
    }
    public void reset(){
        stop();
        playerViewModel.getSpeakingnumber().setValue(0);
        start();
    }
    public void end(){
        int maxnumber=playerViewModel.getMaxnumber().getValue();
        stop();
        playerViewModel.getSpeakingnumber().setValue(maxnumber);
        //start();
        next();
    }
    void nextwithoutad(){
        while(!nextcheck()){}

    }
    public void next(){
        nextwithoutad();
      /*  if(DataStore.ad_time==0) {
            DataStore.ad_time=DataStore.default_adtime;
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
        }
        else{
            DataStore.ad_time-=1;
            nextwithoutad();
        }*/



    }
    Boolean nextcheck(){
        if(PlayList. current_number<PlayList.maxnumber-1) {
            PlayList.current_number++;
            String url = PlayList.urllist.get(PlayList.current_number);

                //FileController fileController = new FileController(context);
               // if(fileController.OpenShortCut( url)!=null) {
                    //setup(MusicLibrary.INSTANCE.getTTS_Item(context));
                    // MediaControllerCompat.getMediaController(MainActivity).transportControls.play();
                    //TTSService.ttsController.setup(item);
                    try {
                        StoryIndexDB db = (StoryIndexDB) DBProvider.Companion.of(DBTableName.storyindex, context);
                        StoryIndexModel sc=db.getStoryIndexItem(url);
                        MusicLibrary.INSTANCE.setdata(sc, 0);
                        return true;
                    }
                    catch (Exception e){

                    }
                    return false;

              //  }
               // else{

                //}
        }
        return true;
    }

}
