package com.yamacrypt.webaudionovel.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yamacrypt.webaudionovel.FileController;
import com.yamacrypt.webaudionovel.DataStore;
import com.yamacrypt.webaudionovel.R;

import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SearchAdapter mAdapter;
    private FileController csvloader;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);

        setHasOptionsMenu(true);
        return root;
    }
    SearchView mSearchView;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        csvloader=new FileController(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.search_recycler_view);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        Search("");
    }
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


        List<DataStore.BookData> myDataset=csvloader.SearchCSV(s);
        /*BinarySearch binarySearch=new BinarySearch(ls);
        for (DataStore.BookData bookData : myDataset) {
            int num=Integer.parseInt(bookData.getTitlenumber());
            bookData.setDl_checked(binarySearch.binary_search(num));
        }*/
        mAdapter = new SearchAdapter(myDataset,getContext());
        /*recyclerView.addOnScrollListener(new EndlessScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page) {
                List<DataStore.BookData> Dataset=csvloader.SearchCSV("");
            mAdapter.notifyItemInserted(Dataset);

            }
        });*/
        recyclerView.setAdapter(mAdapter);
    }

}
