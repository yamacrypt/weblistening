package com.yamacrypt.webaudionovel.ui.library;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.yamacrypt.webaudionovel.DataStore;
import com.yamacrypt.webaudionovel.MainActivity;
import com.yamacrypt.webaudionovel.MyAdapter;
import com.yamacrypt.webaudionovel.R;

import java.io.File;
import java.util.List;

public class LibAdapter extends MyAdapter {
    public LibAdapter(List<DataStore.BookData> myDataset, Context context) {
        super(myDataset,context);

    }
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lib_item ,parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView tv= holder.item.findViewById(R.id.dl_text);
        Button play_button=holder.item.findViewById(R.id.play_button);
        tv.setText("null");
        try {
            final String introduction=get(position).getTitle()+"\n"+get(position).getAuthor();
            tv.setText(introduction);
            play_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String number=get(position).getTitlenumber();
                    Log.d("tag",number);
                    play(DataStore.getFile(context,DataStore.plus,number+DataStore.fileextension));
                    listener.Showsmallwindow(introduction,number);
                }
            });
        }
        catch (Exception e){}

    }
    public void setListenr(LibAdapter.Listener listener){
        this.listener=listener;
    }
    Listener listener;
    public interface Listener{
        void Showsmallwindow(String introduction,String number);

    }

    DataStore.BookData get(int position){
        return mDataset.get(position);
    }

    //AudioController audioController=new AudioController(context);
    private  void play(File file){
     //  audioController=new AudioController(context);
      //AudioController audioController=new AudioController(context);
   //  MainActivity. audioController.play_music(file.toString());
    }
   /* private AsyncFileDownload.Listener createListener() {
        return new  AsyncFileDownload.Listener() {
            @Override
            public void onSuccess(Boolean result,File file) {
                //Log.d("download",file);
                if(result){
                    AudioController audioController=new AudioController(context);
                    audioController.play_music(file);
                }
            }
        };
    }*/
}
