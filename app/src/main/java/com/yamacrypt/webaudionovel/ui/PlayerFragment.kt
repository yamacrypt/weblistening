package com.yamacrypt.webaudionovel.ui

import com.yamacrypt.webaudionovel.ui.library.models.manualSave
import com.yamacrypt.webaudionovel.MusicService.MusicLibrary.getMetadata
import com.yamacrypt.webaudionovel.ui.library.models.BookMark_Save
import android.os.Bundle
import com.yamacrypt.webaudionovel.R
import com.yamacrypt.webaudionovel.ui.PlayerViewModel
import com.yamacrypt.webaudionovel.ui.SleepTImerBroadcastReceiver
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.yamacrypt.webaudionovel.DataStore
import androidx.navigation.NavController
import com.yamacrypt.webaudionovel.TTSController
import android.widget.SeekBar.OnSeekBarChangeListener
import com.yamacrypt.webaudionovel.MusicService.MusicLibrary
import android.support.v4.media.MediaMetadataCompat
import android.view.*
import android.widget.*
import com.yamacrypt.webaudionovel.ui.library.models.BookMark
import com.yamacrypt.webaudionovel.PlayList
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_fulltextplayer.*
import java.lang.Exception
import java.util.*
import kotlin.concurrent.thread

class PlayerFragment : Fragment() {
    private val totalTime = 0
    var root: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_player, container, false)
        setHasOptionsMenu(true)
        return root
    }

    lateinit var positionBar: SeekBar
    lateinit var remaining: TextView
    lateinit var elapse: TextView
    var model: PlayerViewModel? = null
    private val mCreateSleepTimerBroadcastReceiver: SleepTImerBroadcastReceiver? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mToolbar: Toolbar = requireActivity()!!.findViewById(R.id.tool_bar)
        (requireActivity() as AppCompatActivity?)!!.setSupportActionBar(mToolbar)
        //mToolbar.setTitle(DataStore.intro);
        model = PlayerViewModel()
        remaining = view.findViewById(R.id.remainingTimeLabel)
        elapse = view.findViewById(R.id.elapsedTimeLabel)
        //TextView title=view.findViewById(R.id.player_title);
        //int elapsetime=MainActivity.audioController.getNowTime();
        val speedLayout = view.findViewById<FrameLayout>(R.id.player_speed_layout)
        val prefs = DataStore.getSharedPreferences(context)
        val s = DataStore.getSpeed_text(prefs.getInt(DataStore.speedKey, 10))
        val speedText = view.findViewById<TextView>(R.id.speed_text)
        speedText.text=s
        val sleepTIme = DataStore.getSharedPreferences(context).getLong(DataStore.sleepTime, -1)
        val now = System.currentTimeMillis()
        val sleepText = view.findViewById<TextView>(R.id.countDownTextView)
        val sleepButton = view.findViewById<ImageView>(R.id.sleepTimer)
        if (sleepTIme > now) {

            sleepText.isVisible = true
            sleepButton.isVisible = false
            val handler=Handler(Looper.getMainLooper());
            thread{
                val r=object : Runnable {
                    override fun run() {
                        val now = System.currentTimeMillis()
                        val rest = (sleepTIme - now) / 1000
                        val s: String = "%d:%d"
                        sleepText.text=s.format(rest/60,rest%60)
                        if(rest>0) {
                            handler.postDelayed(this, 1000)
                        }
                        else{
                            sleepText.isVisible = false
                            sleepButton.isVisible = true
                        }
                    }
                }
                handler.post(r)
            }
        } else {
            sleepText.isVisible = false
            sleepButton.isVisible = true
        }
        speedLayout.setOnClickListener {
            val navController = Navigation.findNavController(
                requireActivity(), R.id.nav_main_fragment
            )
            navController.navigate(R.id.action_navigation_play_to_navigation_speed)
        }
        val sleepTimerLayout = view.findViewById<FrameLayout>(R.id.sleep_timer_layout)
        sleepTimerLayout.setOnClickListener {
            val navController = Navigation.findNavController(
                requireActivity(), R.id.nav_main_fragment
            )
            navController.navigate(R.id.action_navigation_play_to_navigation_sleep_timer)
        }
        /*val bookMarkLayout = view.findViewById<FrameLayout>(R.id.book_mark_layout)
        val bookMarkImage = view.findViewById<ImageView>(R.id.bookMarkButton)
        bookMarkLayout.setOnClickListener {
            bookMarkLayout.isEnabled = false
            manualSave(requireContext()) //saveBookMark();
            bookMarkImage.foreground = resources.getDrawable(R.drawable.checkicon)
            val timer = Timer()
            val handler = Handler()
            val timerTask: TimerTask = object : TimerTask() {
                override fun run() {
                    try {
                        handler.post {
                            bookMarkImage.foreground = resources.getDrawable(R.drawable.bookmark)
                            timer.cancel()
                            bookMarkLayout.isEnabled = true
                        }
                    } catch (e: Exception) {
                    }
                }
            }
            timer.schedule(timerTask, 500)
        }*/

        val str = view.findViewById<TextView>(R.id.novelText)
        val ttsController = TTSController.getInstance()
        str.text = ttsController.currentstring
        val playBtn = view.findViewById<Button>(R.id.playBtn)
        if (ttsController.isSpeaking) {
            playBtn.foreground = requireActivity()!!.getDrawable(R.drawable.stop)
        } else {
            playBtn.foreground = requireActivity()!!.getDrawable(R.drawable.playicon)
        }
        positionBar = view.findViewById(R.id.positionBar)
        positionBar.setOnSeekBarChangeListener(
            object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        ttsController.speak(progress)
                        model!!.speakingnumber.setValue(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            }
        )
        val numberObserver: Observer<Int> = Observer {
            val num = model!!.speakingnumber.value!!
            positionBar.setProgress(num)
            elapse.text=(Integer.toString(num))
            str.text = ttsController.currentstring
        }
        val maxObserver: Observer<Int> = Observer { max ->
            remaining.setText(Integer.toString(max!!))
            positionBar.setMax(max)
            mToolbar.title = DataStore.intro
        }
        model!!.speakingnumber.observe(viewLifecycleOwner, numberObserver)
        model!!.maxnumber.observe(viewLifecycleOwner, maxObserver)
        mToolbar.setNavigationIcon(R.drawable.ic_group_collapse_13)
        mToolbar.setNavigationOnClickListener { requireActivity()!!.onBackPressed() }
    }

    override fun onResume() {
        super.onResume()
        val mToolbar: Toolbar = requireActivity()!!.findViewById(R.id.tool_bar)
        try {
            mToolbar.title =
                getMetadata(requireContext())!!.getText(MediaMetadataCompat.METADATA_KEY_ARTIST)
        } catch (e: Exception) {
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.player, menu)
        val menuitem = menu.findItem(R.id.bookmark)
        menuitem.setOnMenuItemClickListener {
            menuitem.isEnabled = false
            manualSave(requireContext()) //saveBookMark();
            menuitem.setIcon(R.drawable.checkicon)
            val timer = Timer()
            val handler = Handler()
            val timerTask: TimerTask = object : TimerTask() {
                override fun run() {
                    try {
                        handler.post {
                            menuitem.setIcon(R.drawable.bookmark)
                            timer.cancel()
                            menuitem.isEnabled = true
                        }
                        //  menuitem.setIcon(R.drawable.dlicon);
                    } catch (e: Exception) {
                    }
                }
            }
            timer.schedule(timerTask, 500)
            false
        }
    }

    fun saveBookMark() {
        val bookmark = BookMark(
            PlayList.getPlayingPath(),
            model!!.speakingnumber.value!!,
            PlayList.getRootpath(),
            PlayList.getCurrent_number()
        )
        BookMark_Save(bookmark, requireContext()) //BookMark_Save()
    }

    private fun TutorialPopup() {
        val turoial_popup = PopupWindow()
        // レイアウト設定
        val popupView = layoutInflater.inflate(R.layout.dictionary, null)
        val tar = root!!.findViewById<View>(R.id.playBtn)
        turoial_popup.contentView = popupView
        // 背景設定
        turoial_popup.setBackgroundDrawable(resources.getDrawable(R.color.colorGrey))
        // タップ時に他のViewでキャッチされないための設定
        turoial_popup.isTouchable = true
        turoial_popup.isFocusable = false
        turoial_popup.isOutsideTouchable = false
        turoial_popup.width = root!!.width * 3 / 4
        turoial_popup.height = root!!.height * 3 / 4
        turoial_popup.showAtLocation(root, Gravity.CENTER, 0, 0)
    }
}