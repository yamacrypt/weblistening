package com.yamacrypt.webaudionovel.ui.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yamacrypt.webaudionovel.DataStore;
import com.yamacrypt.webaudionovel.R;
import com.yamacrypt.webaudionovel.TTSService;

import java.text.DecimalFormat;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {
    String[] arr;
    Context context;
    public LanguageAdapter(String[] arr,Context context){
        this.arr=arr;
        this.context=context;

    }
    public static class LanguageViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View item;
        public TextView textView;

        public LanguageViewHolder(View v) {
            super(v);
            item = v;
           // textView=v.findViewById(R.id.dl_text);
        }
    }
    @Override
    public LanguageAdapter.LanguageViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.language_item ,parent, false);
        LanguageAdapter.LanguageViewHolder vh = new LanguageAdapter.LanguageViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final LanguageViewHolder holder, final int position) {
        final String speed_text=arr[position];
        //Change_speed(speed/
       // DecimalFormat df = new DecimalFormat("00");
       // String speed_text=df.format(speed10);
        // Log.d(Integer.toString(position), speed_text);
        Button bt=holder.item.findViewById(R.id.language_button);
        bt.setText( speed_text);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Change_pitch(speed10/10f);
                SharedPreferences prefs = DataStore.getSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(DataStore.languageKey, speed_text);
                editor.apply();
                //TTSService.ttsController.Change_Language(speed_text);
                Change_pitch(speed_text);
                listener.transition(speed_text);
            }
        });

    }
    Listener listener;
    public void setListener(LanguageAdapter.Listener listener) {
        this.listener = listener;
    }
    public interface Listener {
        void transition(String sp10);
        void changePitch(String language);
    }
    void Change_pitch(String language){
        //AudioController audioController=new AudioController(context);
        //audioController.Speed_Change(speed);
        // MainActivity.audioController.Speed_Change(speed);
       listener.changePitch(language);
        // TTSService.ttsController.Change_Language(language);
    }
    @Override
    public int getItemCount() {
        return arr.length;
    }

}
