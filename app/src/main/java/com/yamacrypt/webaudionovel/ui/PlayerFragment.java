package com.yamacrypt.webaudionovel.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaMetadataCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.yamacrypt.webaudionovel.DataStore;
import com.yamacrypt.webaudionovel.MusicService.MusicLibrary;
import com.yamacrypt.webaudionovel.PlayList;
import com.yamacrypt.webaudionovel.R;
import com.yamacrypt.webaudionovel.TTSController;
import com.yamacrypt.webaudionovel.ui.library.models.BookMark;
import com.yamacrypt.webaudionovel.ui.library.models.BookMarkKt;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerFragment extends Fragment {
    private int totalTime;
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_player, container, false);
        setHasOptionsMenu(true);
        return root;
    }
    SeekBar positionBar;
    TextView remaining;
    TextView elapse;
    PlayerViewModel model;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       androidx.appcompat.widget.Toolbar mToolbar=getActivity().findViewById(R.id.tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
       //mToolbar.setTitle(DataStore.intro);
        model=new PlayerViewModel();
        remaining=view.findViewById(R.id.remainingTimeLabel);
        elapse=view.findViewById(R.id.elapsedTimeLabel);
        //TextView title=view.findViewById(R.id.player_title);
        //int elapsetime=MainActivity.audioController.getNowTime();
        FrameLayout speedLayout =view.findViewById(R.id.player_speed_layout);
        TextView speedText=view.findViewById(R.id.player_speed_layout);
        SharedPreferences prefs= DataStore.getSharedPreferences(getContext());
        String s=DataStore.getSpeed_text(prefs.getInt(DataStore.speedKey,10));
        speedText.setText(s);
        speedLayout.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      NavController navController = Navigation.findNavController(getActivity(), R.id.nav_menu_fragment);
                                      navController.navigate(R.id.action_navigation_player_to_navigation_speed);
                                  }
                              }
        );

        FrameLayout bookMarkLayout =view.findViewById(R.id.book_mark_layout);
        ImageView bookMarkImage=view.findViewById(R.id.bookMarkButton);
        bookMarkLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bookMarkLayout.setEnabled(false);
                                        BookMarkKt.manualSave(getContext());//saveBookMark();
                                        bookMarkImage.setForeground(getResources().getDrawable(R.drawable.checkicon));
                                        Timer timer=new Timer();
                                        Handler handler=new Handler();
                                        TimerTask timerTask=new TimerTask() {
                                            @Override
                                            public void run() {
                                                try {
                                                    handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        bookMarkImage.setForeground(getResources().getDrawable(R.drawable.bookmark));
                                                        timer.cancel();
                                                        bookMarkLayout.setEnabled(true);
                                                        }
                                                    });
                                                    }
                                                catch (Exception e){}
                                            }
                                        };
                                        timer.schedule(timerTask,500);;
                                    }
            }
        );

        FrameLayout sleepTimerLayout =view.findViewById(R.id.sleep_timer_layout);
        sleepTimerLayout.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               NavController navController = Navigation.findNavController(getActivity(), R.id.nav_menu_fragment);
                                               navController.navigate(R.id.action_navigation_player_to_navigation_sleep_timer);
                                           }
                                       }
        );

        TextView str=view.findViewById(R.id.novelText);
        TTSController ttsController=TTSController.getInstance();
        str.setText(ttsController.getCurrentstring());
        Button playBtn=view.findViewById(R.id.playBtn);

        if (ttsController.isSpeaking()) {
            playBtn.setForeground(getActivity().getDrawable(R.drawable.stop));
        } else {
            playBtn.setForeground(getActivity().getDrawable(R.drawable.playicon));
        }
        positionBar=view.findViewById(R.id.positionBar);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            ttsController.speak(progress);
                            model.getSpeakingnumber().setValue(progress);

                        }

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
        /*SeekBar volumeBar = (SeekBar) view.findViewById(R.id.volumeBar);
        AudioManager mAudioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        volumeBar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeBar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        //MainActivity.audioController.getMediaPlayer().setVolume(volumeNum, volumeNum);
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,AudioManager.FLAG_SHOW_UI);

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );*/
        final Observer<Integer> numberObserver =new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                int num=model.getSpeakingnumber().getValue();
                positionBar.setProgress(num);
                elapse.setText(Integer.toString(num));
               // positionBar.setMax(MainActivity.ttsController.getMaxnumber());
                //MainActivity.ttsController.stop();
                //model.getSpeakingnumber().setValue(progress);
                str.setText(ttsController.getCurrentstring());
                //  str.setText(MainActivity.);
            }
        } ;
        final Observer<Integer> maxObserver=new Observer<Integer>() {
            @Override
            public void onChanged(Integer max) {
                remaining.setText(Integer.toString(max));
                positionBar.setMax(max);;
                mToolbar.setTitle(DataStore.intro);
            }
        };
        model.getSpeakingnumber().observe(getViewLifecycleOwner(),numberObserver);
        model.getMaxnumber().observe(getViewLifecycleOwner(),maxObserver);
        mToolbar.setNavigationIcon(R.drawable.ic_group_collapse_13);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getActivity().onBackPressed();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        androidx.appcompat.widget.Toolbar mToolbar=getActivity().findViewById(R.id.tool_bar);
        try {
            mToolbar.setTitle(MusicLibrary.INSTANCE.getMetadata(requireContext()).getText(MediaMetadataCompat.METADATA_KEY_ARTIST));
        }
        catch (Exception e){}
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.player, menu);
        MenuItem menuitem = menu.findItem(R.id.bookmark);
        menuitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                menuitem.setEnabled(false);
                BookMarkKt.manualSave(getContext());//saveBookMark();
                menuitem.setIcon(R.drawable.checkicon);

                Timer timer=new Timer();
                Handler handler=new Handler();
                TimerTask timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    menuitem.setIcon(R.drawable.bookmark);
                                    timer.cancel();
                                    menuitem.setEnabled(true);
                                }
                            });
                          //  menuitem.setIcon(R.drawable.dlicon);
                        }
                        catch (Exception e){}
                    }
                };
                timer.schedule(timerTask,500);
                return false;
            }
        });
    }
    void saveBookMark(){
        BookMark bookmark=new BookMark(PlayList.getPlayingPath(),model.getSpeakingnumber().getValue(),PlayList.getRootpath(),PlayList.getCurrent_number());
        BookMarkKt.BookMark_Save(bookmark,getContext()); //BookMark_Save()
    }


    private void TutorialPopup() {
        PopupWindow turoial_popup = new PopupWindow();
        // レイアウト設定
        View popupView = getLayoutInflater().inflate(R.layout.dictionary, null);
        View tar=root.findViewById(R.id.playBtn);
        turoial_popup.setContentView(popupView);
        // 背景設定
       turoial_popup.setBackgroundDrawable(getResources().getDrawable(R.color.colorGrey));
        // タップ時に他のViewでキャッチされないための設定
        turoial_popup.setTouchable(true);
        turoial_popup.setFocusable(false);
         turoial_popup.setOutsideTouchable(false);

        turoial_popup.setWidth(root.getWidth()*3/4);
        turoial_popup.setHeight(root.getHeight()*3/4);


        turoial_popup.showAtLocation(root, Gravity.CENTER, 0, 0);
    }
}
