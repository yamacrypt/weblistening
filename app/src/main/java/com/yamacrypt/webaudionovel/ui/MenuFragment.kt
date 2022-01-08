package com.yamacrypt.webaudionovel.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yamacrypt.webaudionovel.DataStore
import com.yamacrypt.webaudionovel.R
import com.yamacrypt.webaudionovel.TTSController

class MenuFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.menu, container, false)
    }

    // View root;
    var bt: Button? = null
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val navView: BottomNavigationView = view.findViewById(R.id.nav_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_home,
            R.id.navigation_dashboard,
            R.id.navigation_notifications
        )
            .build()
        val navController =
            Navigation.findNavController(requireActivity(),
                R.id.nav_menu_fragment
            )
      /*  NavigationUI.setupActionBarWithNavController(
            (activity as AppCompatActivity?)!!,
            navController,
            appBarConfiguration
        )*/
      // bar.setupWithNavController(navController)
        NavigationUI.setupWithNavController(navView, navController)
        val  popupView = layoutInflater.inflate(R.layout.smallwindow, null)
            Companion.popupView =popupView;

     /* val filesListFragment =FilesListFragment.build {
          path=Environment.getExternalStorageDirectory().absolutePath
      }*/
        //navController.navigate(R.id.action_myself,FilesListFragment.build2 {     path=Environment.getExternalStorageDirectory().absolutePath })
     //  requireActivity().supportFragmentManager.beginTransaction().replace(R.id.nav_menu_fragment,filesListFragment).setPrimaryNavigationFragment().commit()
        /*   if (savedInstanceState == null) {
            val filesListFragment = FilesListFragment.build {
                path = Environment.getExternalStorageDirectory().absolutePath
            }*/
        /*   if (MainActivity.audioController.isPlaying()) {
            playBtn.setForeground(getActivity().getDrawable(R.drawable.stop));
        } else {
            playBtn.setForeground(getActivity().getDrawable(R.drawable.playicon));

        }*/
        if (DataStore.check_window) {
            val layout = requireActivity().findViewById<LinearLayout>(R.id.Layout)
            val playBtn =
                popupView.findViewById<Button>(R.id.play_button)
          val ttsController=  TTSController.getInstance()
            if (ttsController.isSpeaking) {
                playBtn.foreground = requireActivity().getDrawable(R.drawable.stop)
            } else {
                playBtn.foreground = requireActivity().getDrawable(R.drawable.playicon)
            }

            (layout as LinearLayout).addView(popupView, DataStore.smallwindow_position)
            (popupView.findViewById<View>(R.id.dl_text) as TextView).text =
                DataStore.intro
        }
        /*SharedPreferences pref= DataStore.getSharedPreferences(getContext());
        if(pref.getInt(DataStore.review,0)==0) {
            ReviewDialogFragment reviewDialogFragment =
                    ReviewDialogFragment.newInstance(this);
            reviewDialogFragment.show(getSupportFragmentManager(), "ReviewDialogFragment");
        }*/

        val bar = activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.tool_bar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(bar)
        bar?.setupWithNavController(navController, appBarConfiguration)
       // val a:Toolbar
        //setSupportActionBar(bar);
        ////NavigationUI.setupActionBarWithNavController(this, navController);
      //  MainActivity.toolbar.setupWithNavController(navController)
       // view.findViewById<Toolbar>(R.id.toolbar)
      //      .setupWithNavController(navController)

    }

    fun Reload(playdata: PlayerViewModel.PlayData?){
        val title = MenuFragment.popupView.findViewById<TextView>(R.id.dl_text)
        title.text =playdata?.intro// introduction
    }
    companion object {
        lateinit var popupView: View
    }
}

