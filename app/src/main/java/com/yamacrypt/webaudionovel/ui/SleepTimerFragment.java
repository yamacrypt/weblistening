package com.yamacrypt.webaudionovel.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.media.session.MediaControllerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yamacrypt.webaudionovel.DataStore;
import com.yamacrypt.webaudionovel.MusicService.MediaPlaybackService;
import com.yamacrypt.webaudionovel.R;
import com.yamacrypt.webaudionovel.SleepTimer;

public class SleepTimerFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sleep_timer, container, false);
        //setHasOptionsMenu(true);
        return root;
    }
    private int lastPosition;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.sleep_time_recycler_view);
        recyclerView.setHasFixedSize(true);
        int[] sleep_array={5,10,15,30,45,60,90,120};
        mAdapter = new SleepTimerAdapter(sleep_array,getContext());
        ((SleepTimerAdapter) mAdapter).setListener(createlistner());
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                lastPosition=mlayoutManager.findFirstVisibleItemPosition();
            }
        });
    }
    SleepTimer currentSleepTimer=null;
    private SleepTimerAdapter.SleepListener createlistner(){
        return new SleepTimerAdapter.SleepListener() {
            @Override
            public void setTimer(int sleepTime) {
                if(currentSleepTimer!=null){
                    currentSleepTimer.cancel();
                }
                currentSleepTimer=new SleepTimer(1000,getActivity());
                currentSleepTimer.start();
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_menu_fragment);
                navController.navigateUp();
            }

        };
    }

}

