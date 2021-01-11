package com.yamacrypt.webaudionovel.ui.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.media.session.MediaControllerCompat;
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

public class SpeedFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_speed, container, false);
        //setHasOptionsMenu(true);
        return root;
    }
    private int lastPosition;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.speed_recycler_view2);
        recyclerView.setHasFixedSize(true);
        int len=36;
        int[] sp_array=new int[len];
        for (int i = 0; i < len; i++) {
            sp_array[i]=5+i;
        }
        mAdapter = new SpeedAdapter(sp_array,getContext());
        ((SpeedAdapter) mAdapter).setListener(createlistner());
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mlayoutManager);
        SharedPreferences pref= DataStore.getSharedPreferences(getContext());
        lastPosition=pref.getInt(DataStore.speed_position,0);
        recyclerView.scrollToPosition(lastPosition);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                lastPosition=mlayoutManager.findFirstVisibleItemPosition();
            }
        });
       /* bt.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {
                                    //  fragmentControll.showfragment();
                                  }
                              }
        );*/

    }

    private SpeedAdapter.Listener createlistner(){
        return new SpeedAdapter.Listener() {
            @Override
            public void transition(int sp10) {

                // Button bt=(Button)root.findViewById(R.id.speed_button);
                SharedPreferences pref= DataStore.getSharedPreferences(getContext());
                SharedPreferences.Editor editor=pref.edit();
                editor.putInt(DataStore.speed_position,lastPosition);
                editor.apply();
                //bt.setText(DataStore.getSpeed_text(sp));
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_menu_fragment);
                navController.navigateUp();
               // speedListener.onSelected(sp10);
            }

            @Override
            public void changeSpeed(float speed) {
                MediaControllerCompat.getMediaController(getActivity()).getTransportControls().setPlaybackSpeed(speed);

            }
        };
    }

}

