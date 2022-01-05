package com.yamacrypt.webaudionovel.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yamacrypt.webaudionovel.R;

public class SleepTimerAdapter extends RecyclerView.Adapter<SleepTimerAdapter.SleepTimerViewHolder> {
    int[] arr;
    Context context;
    public SleepTimerAdapter(int[] arr,Context context){
        this.arr=arr;
        this.context=context;
    }

    public static class SleepTimerViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View item;

        public SleepTimerViewHolder(View v) {
            super(v);
            item = v;
        }
    }
    @Override
    public SleepTimerAdapter.SleepTimerViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.speed_item ,parent, false);
        SleepTimerAdapter.SleepTimerViewHolder vh = new SleepTimerAdapter.SleepTimerViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final SleepTimerViewHolder holder, final int position) {
        final int sleepTimer=arr[position];
        String sleep_text=String.valueOf(sleepTimer);
        Button bt=holder.item.findViewById(R.id.speed_button);
        bt.setText(String.format("%s分",sleep_text));
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setTimer(sleepTimer);
            }
        });

    }
    SleepListener listener;
    public void setListener(SleepTimerAdapter.SleepListener listener) {
        this.listener = listener;
    }
    public interface SleepListener {
        void setTimer(int sleepTime);
    }

    @Override
    public int getItemCount() {
        return arr.length;
    }


}
