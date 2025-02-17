package com.yamacrypt.webaudionovel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    protected List<DataStore.BookData> mDataset;
    protected Context context;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View item;
        public TextView textView;

        public MyViewHolder(View v) {
            super(v);
            item = v;
           // textView=v.findViewById(R.id.dl_text);
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<DataStore.BookData> myDataset, Context context)
    {
        mDataset = myDataset;
        this.context=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
       View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lib_item ,parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
       // holder.textView.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public void notifyItemInserted(List<DataStore.BookData> data){
        mDataset.addAll(data);
    }
}
