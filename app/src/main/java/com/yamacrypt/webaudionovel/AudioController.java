package com.yamacrypt.webaudionovel;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.io.File;

import static android.media.MediaPlayer.SEEK_PREVIOUS_SYNC;

public class AudioController {
    Context context;
    float speed;

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    MediaPlayer  mediaPlayer=new MediaPlayer();
    public AudioController(Context context,final int speed10) {
        this.context = context;
       // mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
               // mediaPlayer.release();
                //mediaPlayer.stop();
                mediaPlayer.pause();
                //listener.Completed();
            }
        });
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer arg0) {
               SystemClock.sleep(200);
                mediaPlayer.start();
            }
        });
        speed=speed10/10f;
      //  mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(2f));

    }
    Listener listener;
    public void setListener(AudioController.Listener listener) {
        this.listener = listener;
    }
    public interface Listener {
        void Completed();
    }


   String file;
    public void play_music(String file){
       // mediaPlayer=new MediaPlayer();
        this.file=file;
            mediaPlayer.stop();
            mediaPlayer.reset();
        Uri myUri = Uri.parse(file);
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        try {
            mediaPlayer.setDataSource(context, myUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
        }
        catch (Exception e){
            Toast ts = Toast.makeText(context, "オーデイオ再生に失敗", Toast.LENGTH_LONG);
            ts.show();
        }

    }
    public void Speed_Change(final float speed){
        this.speed=speed;
        try {
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
           // mediaPlayer.pause();
        }
        catch (Exception e){
           /* mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
                public void onPrepared(MediaPlayer var1) {
                    PlaybackParams params =  mediaPlayer.getPlaybackParams();
                    params.setSpeed(speed);
                    mediaPlayer.setPlaybackParams(params);
                }
            });*/
        }
    }

    public void stop_music() {
        mediaPlayer.pause();
    }
    public void start_music() {
            mediaPlayer.start();

    }
    public int getDuration() {
        try {
            return mediaPlayer.getDuration();
        }
        catch (Exception e){
            return 0;
        }
    }

    public int getNowTime() {
        try {
            return mediaPlayer.getCurrentPosition();
        }
        catch (Exception e){
            return  getDuration();
        }
    }

    public void back_music(int i) {

        mediaPlayer.pause();
        int now_time = mediaPlayer.getCurrentPosition();
        // Log.d("Time",Integer.toString(mediaPlayer.getDuration()));
        int seek_time = Math.max(0, now_time - i * 1000);
        // Log.d("Time",Integer.toString(seek_time));
        seekTo(seek_time);
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            long now_time = mediaPlayer.getCurrentPosition();
            // Log.d("Time",Integer.toString(mediaPlayer.getDuration()));
            long seek_time = Math.max(0L, now_time - i * 1000);
            // Log.d("Time",Integer.toString(seek_time));
            mediaPlayer.seekTo(seek_time);
        }
        else{
            int now_time = mediaPlayer.getCurrentPosition();
            // Log.d("Time",Integer.toString(mediaPlayer.getDuration()));
            int seek_time = Math.max(0, now_time - i * 1000);
            // Log.d("Time",Integer.toString(seek_time));
            mediaPlayer.seekTo(seek_time);
        }*/
        //Log.d("Time",Integer.toString(mediaPlayer.getCurrentPosition()));

       // mediaPlayer.start();
    }

    public void seekTo(int progress) {
        if(progress==0)
            restart();
        else
        mediaPlayer.seekTo(progress);
    }

    public void restart() {
       play_music(this.file);
    }

    public boolean isPlaying() {
        try { return mediaPlayer.isPlaying();
        }
        catch (Exception e){
            return false;
        }

    }
}
