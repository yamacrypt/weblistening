package com.yamacrypt.webaudionovel.ui.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yamacrypt.webaudionovel.DataStore;
import com.yamacrypt.webaudionovel.R;
import com.yamacrypt.webaudionovel.TTSService;

import java.text.DecimalFormat;

public class SpeedAdapter extends RecyclerView.Adapter<SpeedAdapter.SpeedViewHolder> {
    int[] arr;
    Context context;
    public SpeedAdapter(int[] arr,Context context){
        this.arr=arr;
        this.context=context;

    }

    public static class SpeedViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View item;
        public TextView textView;

        public SpeedViewHolder(View v) {
            super(v);
            item = v;
            textView=v.findViewById(R.id.dl_text);
        }
    }
    @Override
    public SpeedAdapter.SpeedViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.speed_item ,parent, false);
        SpeedAdapter.SpeedViewHolder vh = new SpeedAdapter.SpeedViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final SpeedViewHolder holder, final int position) {
       final int speed10=arr[position];
       //Change_speed(speed/
        DecimalFormat df = new DecimalFormat("00");
       String speed_text=df.format(speed10);
       // Log.d(Integer.toString(position), speed_text);
        Button bt=holder.item.findViewById(R.id.speed_button);
        bt.setText(String.format("%s.%sx",speed_text.charAt(0),speed_text.charAt(1)));
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Change_speed(speed10/10f);
                SharedPreferences prefs =DataStore.getSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(DataStore.speedKey, speed10);
                editor.apply();
               listener.transition(speed10);
            }
        });

    }
    Listener listener;
    public void setListener(SpeedAdapter.Listener listener) {
        this.listener = listener;
    }
    public interface Listener {
        void transition(int sp10);
        void changeSpeed(float speed);
    }
    void Change_speed(float speed){
        //AudioController audioController=new AudioController(context);
        //audioController.Speed_Change(speed);
       // MainActivity.audioController.Speed_Change(speed);
        //TTSService.ttsController.Change_Speed(speed);
        listener.changeSpeed(speed);
    }
    @Override
    public int getItemCount() {
        return arr.length;
    }


}
