package com.yamacrypt.webaudionovel.ui.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yamacrypt.webaudionovel.AsyncFileDownload;
import com.yamacrypt.webaudionovel.BinarySearch;
import com.yamacrypt.webaudionovel.FileController;
import com.yamacrypt.webaudionovel.DataStore;
import com.yamacrypt.webaudionovel.R;
import com.yamacrypt.webaudionovel.MyAdapter;

import java.io.File;
import java.util.List;
//not used
public class SearchAdapter extends MyAdapter {
    private AsyncFileDownload asyncfiledownload;
     int title=0;
     int author=2;
     int titlenumber=4;
   public SearchAdapter(List<DataStore.BookData> myDataset, Context context) {
        super(myDataset,context);
    }
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item ,parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView tv= holder.item.findViewById(R.id.search_text);
        final Button dlbutton=holder.item.findViewById(R.id.search_button);
        tv.setText("null");

        FileController fileController =new FileController(context);
        Integer[] ls= fileController.ReadMap();
        /*BinarySearch binarySearch=new BinarySearch(ls);
        final String  number=get(position).getTitlenumber();
        if(binarySearch.binary_search(Integer.parseInt(number))){
            dlbutton.setClickable(false);
            dlbutton.setText("ダウンロード済み");
        }
        else{
            dlbutton.setClickable(true);
            dlbutton.setText("ダウンロード");
            if(DataStore.downloading_files.size()>0) {
                for (String downloading_file : DataStore.downloading_files) {
                    if (downloading_file.equals(number)) {
                        dlbutton.setClickable(false);
                        dlbutton.setText("ダウンロード中");
                    }
                }
            }
            dlbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences pref=DataStore.getSharedPreferences(context);
                    SharedPreferences.Editor editor= pref.edit() ;
                   // int count=pref.getInt(DataStore.dlcountKey,0);

                    /*if(count>0) {
                        DataStore.Decrement(pref,DataStore.dlcountKey);
                        Log.d("tag", number);
                        DataStore.downloading_files.add(number);
                        dlbutton.setClickable(false);
                        dlbutton.setText("ダウンロード中");
                        //  asyncfiledownload.download(getdlurl(mDataset[position].getTitlenumber()));
                        String DOWNLOAD_FILE_URL = getdlurl(number);

                        File outputFile = DataStore.getFile(context, DataStore.plus, DataStore.getFileName(number));
                        // createLi
                        asyncfiledownload = new AsyncFileDownload(DOWNLOAD_FILE_URL, outputFile);
                        asyncfiledownload.execute();
                        asyncfiledownload.setListener(createListener(dlbutton, get(position)));
                    }
                    else{
                        Toast.makeText(context, "DL制限に達しました", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }*/
        String introduction=get(position).getTitle()+"\n"+get(position).getAuthor();
        tv.setText(introduction);
       /* try {
            tv.setText(mDataset[position].getTitle());
        }
        catch (Exception e){}*/

    }
    DataStore.BookData get(int position){
       return mDataset.get(position);
    }

   /* private void onCLicked(String number,int position,Button dlbutton) {
        String DOWNLOAD_FILE_URL=getdlurl(number);

        File outputFile = DataStore.getFile(context,DataStore.plus,DataStore.getFileName(number));
        // createLi
        asyncfiledownload=new AsyncFileDownload(DOWNLOAD_FILE_URL,outputFile);
        asyncfiledownload.execute();
        asyncfiledownload.setListener(createListener(position, dlbutton));
    }*/

    private String getdlurl(String number){
       return DataStore.baseurl+number+DataStore.fileextension;

    }
    private AsyncFileDownload.Listener createListener(final Button dlbutton, final DataStore.BookData bookData) {
        return new  AsyncFileDownload.Listener() {
            @Override
            public void onSuccess(Boolean result,File file) {
                //Log.d("download",file);
                if(result){
                    FileController fileController;

                    fileController =new FileController(context);
                    fileController.WriteCSV(bookData);
                    fileController =new FileController(context);
                    fileController.WriteMap(bookData.getTitlenumber());
                    dlbutton.setClickable(false);
                    dlbutton.setText("ダウンロード済み");
                }
                else{
                    DataStore.downloading_files.remove(bookData.getTitlenumber());
                    dlbutton.setClickable(true);
                    dlbutton.setText("ダウンロード");
                }
            }
        };
    }

}
