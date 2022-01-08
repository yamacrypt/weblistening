package com.yamacrypt.webaudionovel.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yamacrypt.webaudionovel.DataStore;
import com.yamacrypt.webaudionovel.R;
import com.yamacrypt.webaudionovel.SleepTimer.SleepTImerService;
import com.yamacrypt.webaudionovel.SleepTimer.SleepTimer;

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
        int[] sleep_array={-1,5,10,15,30,45,60,90,120};
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
    private SleepTimerAdapter.SleepListener createlistner(){
        return new SleepTimerAdapter.SleepListener() {
            @Override
            public void setTimer(int sleepTime) {
                /*if(currentSleepTimer!=null){
                    currentSleepTimer.cancel();
                }
                currentSleepTimer=new SleepTimer(sleepTime,getActivity());
                currentSleepTimer.start();*/
                long now = System.currentTimeMillis();
                long target=now+sleepTime*60*1000;
                //アラーム用のPendingIntentを取得
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(getContext().getString(R.string.sleep_timer_broadcast));
                broadcastIntent.putExtra(SleepTImerBroadcastReceiver.EXTRA_PATH, "stop");
                /*{
                    PendingIntent pendingIntent = PendingIntent.getService(getContext(), 1, broadcastIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    am.cancel(pendingIntent);
                }*/
                PendingIntent sender = PendingIntent.getBroadcast(getContext(), 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                if(sleepTime>0) {
                    //AlarmManagerを取得
                    AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    //次回サービス起動を予約
                    am.set(AlarmManager.RTC, target, sender);
                }
                else{
                    AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    am.cancel(sender);
                }
                DataStore.getSharedPreferences(getContext()).edit().putLong(DataStore.sleepTime,target).apply();

                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_main_fragment);
                navController.navigateUp();
            }

        };
    }

}

