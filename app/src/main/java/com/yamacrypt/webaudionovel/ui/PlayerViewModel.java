package com.yamacrypt.webaudionovel.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlayerViewModel extends ViewModel {
    static  MutableLiveData<Integer> speakingnumber;

    public MutableLiveData<Integer> getSpeakingnumber() {
        if(speakingnumber==null){
            speakingnumber=new MutableLiveData<>(0);
        }
        return speakingnumber;
    }
    static  MutableLiveData<Integer> maxnumber;

    public MutableLiveData<Integer> getMaxnumber() {
        if(maxnumber==null){
            maxnumber=new MutableLiveData<>(1);
        }
        return maxnumber;
    }
    public static class PlayData {
        public PlayData(String intro){
            this.intro=intro;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        String intro;
    }
    static MutableLiveData<Boolean> playingstate;
    public MutableLiveData<Boolean> getPlayingstate(){
        if (playingstate == null) {
            playingstate=new MutableLiveData<>(false);
        }
        return playingstate;
    }
    static MutableLiveData<PlayData> playdata;
    public MutableLiveData<PlayData> getPlaydata(){
        if (playdata == null) {
            playdata=new MutableLiveData<>();
        }
        return playdata;
    }
}
