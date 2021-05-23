package com.yamacrypt.webaudionovel.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaMetadataCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.yamacrypt.webaudionovel.DataStore;
import com.yamacrypt.webaudionovel.MainActivity;
import com.yamacrypt.webaudionovel.MusicService.MusicLibrary;
import com.yamacrypt.webaudionovel.PlayList;
import com.yamacrypt.webaudionovel.R;
import com.yamacrypt.webaudionovel.TTSController;
import com.yamacrypt.webaudionovel.TTSService;
import com.yamacrypt.webaudionovel.ui.library.models.BookMark;
import com.yamacrypt.webaudionovel.ui.library.models.BookMarkKt;

import org.w3c.dom.Text;

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

        //int max=MainActivity.ttsController.getMaxnumber();
        //remaining.setText(Integer.toString(max));
        TextView str=view.findViewById(R.id.novelText);
        TTSController ttsController=new TTSController();
        str.setText(ttsController.getCurrentstring());
       // str.setText(MainActivity.ttsController.getCurrentstring());
      //  totalTime=MainActivity.audioController.getDuration();
       // int remaingtime=totalTime-elapsetime;
       /* elapsetime/=1000;
        remaingtime/=1000;
        int emin=elapsetime/60;
        int esec=elapsetime%60;
        int rmin=remaingtime/60;
        int rsec=remaingtime%60;
        elapse.setText(ItoS(emin)+":"+ItoS(esec));
        remaining.setText(ItoS(rmin)+":"+ItoS(rsec));*/
       Button playBtn=view.findViewById(R.id.playBtn);

        if (ttsController.isSpeaking()) {
            playBtn.setForeground(getActivity().getDrawable(R.drawable.stop));
        } else {
            playBtn.setForeground(getActivity().getDrawable(R.drawable.playicon));
        }
        positionBar=view.findViewById(R.id.positionBar);
        //positionBar.setMax(MainActivity.ttsController.getMaxnumber());
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            //MainActivity.audioController.seekTo(progress);

                           //positionBar.setProgress(progress);
                            //TTSService.ttsController.stop();
                            ttsController.speak(progress);
                            model.getSpeakingnumber().setValue(progress);

                        }
                        //saveBookMark();
                        /* int num=model.getSpeakingnumber().getValue();
                        elapse.setText(Integer.toString(num));
                        //MainActivity.ttsController.stop();
                        //model.getSpeakingnumber().setValue(progress);
                        str.setText(MainActivity.ttsController.getCurrentstring());*/
                       // MainActivity.ttsController.start();

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
        SeekBar volumeBar = (SeekBar) view.findViewById(R.id.volumeBar);
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
        );
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
                //  TextView str=view.findViewById(R.id.novelText);
                //title.setText(PlayerViewModel.PlayData);
            }
        };
        model.getSpeakingnumber().observe(getViewLifecycleOwner(),numberObserver);
        model.getMaxnumber().observe(getViewLifecycleOwner(),maxObserver);
        mToolbar.setNavigationIcon(R.drawable.ic_group_collapse_13);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getActivity().onBackPressed();
            //    ~Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
             //           .setAction("Action", null).show();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        androidx.appcompat.widget.Toolbar mToolbar=getActivity().findViewById(R.id.tool_bar);
      //  ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
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
              //  BookMark bookmark=new BookMark(PlayList.getPlayingPath(),model.getSpeakingnumber().getValue(),PlayList.getRootpath(),PlayList.getCurrent_number());
              // BookMarkKt.BookMark_Save(bookmark,getContext()); //BookMark_Save()
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
        /*for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }*/
        //  dictionary=menu.findItem(R.id.menu_dictionary);

    }
    void saveBookMark(){
        BookMark bookmark=new BookMark(PlayList.getPlayingPath(),model.getSpeakingnumber().getValue(),PlayList.getRootpath(),PlayList.getCurrent_number());
        BookMarkKt.BookMark_Save(bookmark,getContext()); //BookMark_Save()
    }

    /*TimerTask timercallback(){
        Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {

            }
        };
        handler.post(runnable);
    }*/
   /* @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //NavController controller= Navigation.findNavController(requireActivity(), R.id.nav_menu_fragment);
        try {
            switch(item.getItemId()) {
                case R.id.menu_dictionary:
                   // TutorialPopup();
                  //  MainActivity.
                    break;
                case R.id.menu_language:
                    break;
            }
        } catch(Exception e) {
        }
        return true;
    }*/

    private void TutorialPopup() {
        PopupWindow turoial_popup = new PopupWindow();
        // レイアウト設定
        View popupView = getLayoutInflater().inflate(R.layout.dictionary, null);
        View tar=root.findViewById(R.id.playBtn);
        /*popupView.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                ReturnHome();

            }
        });*/
      /*  popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turoial_popup.dismiss();
                ac.click_effect();
            }
        });*/

        turoial_popup.setContentView(popupView);
        // 背景設定
        //mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));
       turoial_popup.setBackgroundDrawable(getResources().getDrawable(R.color.colorGrey));
        // タップ時に他のViewでキャッチされないための設定
        turoial_popup.setTouchable(true);
        turoial_popup.setFocusable(false);
         turoial_popup.setOutsideTouchable(false);

        // 表示サイズの設定 今回は幅300dp
        //float width = penview.getWidth()*2;
        //float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
        //turoial_popup.setWindowLayoutMode((int) width, WindowManager.LayoutParams.WRAP_CONTENT);
        turoial_popup.setWidth(root.getWidth()*3/4);
        turoial_popup.setHeight(root.getHeight()*3/4);


        // 画面中 if(f instanceof CustomFragmentClass) 央に表示
        //turoial_popup.showAsDropDown(root, Gravity.CENTER, 0, 0);
        turoial_popup.showAtLocation(root, Gravity.CENTER, 0, 0);
        //TextView view = popupView.findViewById(R.id.tutorial_view);
        //view.setText(text);
       /* TextView scoreview=popupView.findViewById(R.id.score_view);
        scoreview.setText(String.format("あなたのスコアは%d点です",model.getScore().getValue()));
        SharedPreferences prefs = getContext().getSharedPreferences(prefsname, MODE_PRIVATE);
        int maxScore = prefs.getInt(keyword, 0);
        TextView maxscore=popupView.findViewById(R.id.maxscore_view);
        maxscore.setText(String.format("ハイスコア %d点",maxScore));*/
    }
}
