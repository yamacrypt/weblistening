package com.yamacrypt.webaudionovel.ui.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.yamacrypt.webaudionovel.DataStore;
import com.yamacrypt.webaudionovel.MainActivity;
import com.yamacrypt.webaudionovel.R;

public class SettingFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_setting, container, false);

        return root;
    }
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout layout;
   // View root;
    Button bt;
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout=(LinearLayout)view.findViewById(R.id.speed__layout);
        bt= view.findViewById(R.id.speed_button);
        Button pitchbt=view.findViewById(R.id.pitch_button);
        Button languagebt=view.findViewById(R.id.language_button);
        SharedPreferences prefs= DataStore.getSharedPreferences(getContext());
        String s=DataStore.getSpeed_text(prefs.getInt(DataStore.speedKey,10));
        String pitchs=DataStore.getSpeed_text(prefs.getInt(DataStore.pitchKey,10));
        String lan=prefs.getString(DataStore.languageKey,"ja");
        SharedPreferences pref= DataStore.getSharedPreferences(getContext());
        Switch isTitleSpeakingSwitch=view.findViewById(R.id.isTitleSpeakingSwitch);
        boolean isTitleSpeaking=pref.getBoolean("isTitleSpeaking",false);
        isTitleSpeakingSwitch.setChecked(isTitleSpeaking);
        isTitleSpeakingSwitch.setOnClickListener(
               new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       SharedPreferences.Editor editor=pref.edit();
                       editor.putBoolean("isTitleSpeaking",isTitleSpeakingSwitch.isChecked());
                       editor.apply();
                   }
               }
        );
        Switch buttonModeSwitch=view.findViewById(R.id.buttonModeSwitch);
        //isTitleSpeakingButton.Listen
        boolean buttonMode=pref.getBoolean("buttonMode",false);
        buttonModeSwitch.setChecked(buttonMode);
        buttonModeSwitch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor=pref.edit();
                        editor.putBoolean("buttonMode",buttonModeSwitch.isChecked());
                        editor.apply();
                    }
                }
        );
        // int count=prefs.getInt(DataStore.dlcountKey,0);
       // TextView tv=view.findViewById(R.id.count_text);
       // tv.setText(Integer.toString(count)+"回");
        bt.setText(s);
        pitchbt.setText(pitchs);
        languagebt.setText(lan);
        bt.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      NavController navController = Navigation.findNavController(getActivity(), R.id.nav_menu_fragment);
                                      navController.navigate(R.id.action_navigation_notifications_to_navigation_speed);
                                  }
                              }
        );
        pitchbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController=Navigation.findNavController(getActivity(),R.id.nav_menu_fragment);
                navController.navigate(R.id.action_navigation_notifications_to_navigation_pitch);
            }
        });
        languagebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController=Navigation.findNavController(getActivity(),R.id.nav_menu_fragment);
                navController.navigate(R.id.action_navigation_notifications_to_navigation_language);
            }
        });
        Button author=view.findViewById(R.id.author_button);
        author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_menu_fragment);
                navController.navigate(R.id.action_navigation_notifications_to_navigation_author);

            }
        });
        Button review=view.findViewById(R.id.review_button);
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReviewManager manager = ReviewManagerFactory.create(getContext());
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
                    @Override
                    public void onComplete(Task<ReviewInfo> task) {
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(getActivity(), reviewInfo);
                        //flow.addOnCompleteListener
                        flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {

                            }
                        });
                    }
                });

            }
        });
    }
}