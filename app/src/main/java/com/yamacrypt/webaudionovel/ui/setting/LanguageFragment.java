package com.yamacrypt.webaudionovel.ui.setting;

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

import com.yamacrypt.webaudionovel.R;
import com.yamacrypt.webaudionovel.TTSController;
import com.yamacrypt.webaudionovel.htmlParser.LanguageData;

public class LanguageFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_language, container, false);
        //setHasOptionsMenu(true);
        return root;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.language_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        int len=36;
        String[] sp_array=new String[len];
        sp_array= new String[]{LanguageData.Companion.getJP(), LanguageData.Companion.getEN(),LanguageData.Companion.getChinese()};
        mAdapter = new LanguageAdapter(sp_array,getContext());
        ((LanguageAdapter) mAdapter).setListener(createlistner());
        recyclerView.setAdapter(mAdapter);
       /* bt.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {
                                    //  fragmentControll.showfragment();
                                  }
                              }
        );*/

    }
    private LanguageAdapter.Listener createlistner(){
        return new LanguageAdapter.Listener() {
            @Override
            public void transition(String sp10) {

                // Button bt=(Button)root.findViewById(R.id.speed_button);
                // SharedPreferences pref= DataStore.getSharedPreferences(getContext());
                //int sp= pref.getInt(DataStore.speedKey,10);
                //bt.setText(DataStore.getSpeed_text(sp));
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_menu_fragment);
                navController.navigateUp();
                // speedListener.onSelected(sp10);
            }

            @Override
            public void changePitch(String language) {
                TTSController ttsController=TTSController.getInstance();
                ttsController.Change_Language(language);
            }
        };
    }

}

