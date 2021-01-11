package com.yamacrypt.webaudionovel.ui.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yamacrypt.webaudionovel.FileController;
import com.yamacrypt.webaudionovel.DataStore;
import com.yamacrypt.webaudionovel.MainActivity;
import com.yamacrypt.webaudionovel.R;

import java.util.List;

import static com.yamacrypt.webaudionovel.ui.MenuFragment.popupView;


public class unusedLibraryFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FileController csvloader;
    View view;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_library, container, false);
        setHasOptionsMenu(true);
        return root;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.view=view;
        super.onViewCreated(view, savedInstanceState);
        csvloader=new FileController(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.library_recycler_view);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        Search("");


    }
    SearchView mSearchView;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        //mSearchView.clearFocus();
        mSearchView.clearFocus();
        mSearchView.setQuery("", false);
        mSearchView.setIconified(true);
        mSearchView.onActionViewCollapsed();

        mSearchView.setFocusable(true);
        mSearchView.setIconified(false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Search(s);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
    private void Search(String s){


        List<DataStore.BookData> myDataset;
        myDataset = csvloader.SearchCSV(s);
        /*BinarySearch binarySearch=new BinarySearch(ls);
        for (DataStore.BookData bookData : myDataset) {
            int num=Integer.parseInt(bookData.getTitlenumber());
            bookData.setDl_checked(binarySearch.binary_search(num));
        }*/
        mAdapter = new LibAdapter(myDataset,getContext());
        ((LibAdapter)mAdapter).setListenr(createListener());
        /*recyclerView.addOnScrollListener(new EndlessScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page) {
                List<DataStore.BookData> Dataset=csvloader.SearchCSV("");
            mAdapter.notifyItemInserted(Dataset);

            }
        });*/
        recyclerView.setAdapter(mAdapter);
    }
  LibAdapter.Listener createListener(){
        return new LibAdapter.Listener() {
            @Override
            public void Showsmallwindow(String introduction, String number) {
                //mPopupWindow = new PopupWindow();
                //LinearLayout layout=view.findViewById(R.id.Lib_layout);
                LinearLayout layout=getActivity().findViewById(R.id.Layout);
                if(DataStore.check_window){
                    layout.removeViewAt(1);
                }

                //View popupView = getLayoutInflater().inflate(R.layout.smallwindow, null);
                // mPopupWindow.setContentView(popupView);
                TextView title= popupView.findViewById(R.id.dl_text);
                title.setText(introduction);
                DataStore.intro=introduction;
                LinearLayout.LayoutParams linearLayoutLayoutPrams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.WRAP_CONTENT );
                // set the textView layout parameter  using the layout parameter we created earlier linearLayoutLayoutPrams
                popupView.setLayoutParams(linearLayoutLayoutPrams);
                Button playBtn=popupView.findViewById(R.id.play_button);
              /*  if (MainActivity.audioController.isPlaying()) {
                    playBtn.setForeground(getActivity().getDrawable(R.drawable.stop));
                } else {
                    playBtn.setForeground(getActivity().getDrawable(R.drawable.playicon));
                }*/
                ((LinearLayout)layout).addView(popupView,1);
                DataStore.check_window=true;

                //ConstraintLayout.LayoutParams layoutParams =
                  //      (ConstraintLayout.LayoutParams)popupView.getLayoutParams();
             //   layoutParams.;


                /*float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
                mPopupWindow.setWindowLayoutMode((int) width, WindowManager.LayoutParams.WRAP_CONTENT);
                mPopupWindow.setWidth((int) width);
                mPopupWindow.setHeight((int)width);*/
               // Button play=popupView.findViewById(R.id.play_button);
               // Button stop=popupView.findViewById(R.id.backbutton);

              /* play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(MainActivity.audioController.getMediaPlayer().isPlaying()){
                            MainActivity.audioController.stop_music();
                            view.setForeground(getContext().getDrawable(R.drawable.stop));
                        }
                        else{
                            MainActivity.audioController.start_music();
                            view.setForeground(getContext().getDrawable( R.drawable.playicon));
                        }
                    }
                });
                stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.audioController.back_music(30);
                    }
                });*/
                //mPopupWindow.showAtLocation(recyclerView, Gravity.BOTTOM, 0, 0);
            }



        };
  }




}