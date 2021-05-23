package com.yamacrypt.webaudionovel.MusicService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media.AudioAttributesCompat
import androidx.media.AudioFocusRequestCompat
import androidx.media.AudioManagerCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.yamacrypt.webaudionovel.DataStore
import com.yamacrypt.webaudionovel.Database.DBProvider
import com.yamacrypt.webaudionovel.Database.DBTableName
import com.yamacrypt.webaudionovel.Database.StoryIndexDB
import com.yamacrypt.webaudionovel.PlayList
import com.yamacrypt.webaudionovel.R
import com.yamacrypt.webaudionovel.TTSController
import com.yamacrypt.webaudionovel.ui.library.fileservice.FileChangeBroadcastReceiver
import com.yamacrypt.webaudionovel.ui.library.models.BookMark
import com.yamacrypt.webaudionovel.ui.library.models.BookMark_Open
import com.yamacrypt.webaudionovel.ui.library.models.BookMark_Save
import java.lang.Exception
import kotlin.concurrent.thread
import androidx.media.app.NotificationCompat as MediaNotificationCompat


class MediaPlaybackService : MediaBrowserServiceCompat() {

    private lateinit var stateBuilder: PlaybackStateCompat.Builder
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var notificationManager: NotificationManagerCompat
    @PlaybackStateCompat.State
    private var mediaState: Int = PlaybackStateCompat.STATE_NONE
    private lateinit var audioManager: AudioManager
    private val audioNoisyFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)

    private val audioNoisyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            mediaSession.controller.transportControls.pause()
        }
    }
   
    private val audioFocusRequest = AudioFocusRequestCompat.Builder(AudioManagerCompat.AUDIOFOCUS_GAIN)
        .setAudioAttributes(AudioAttributesCompat.Builder()
            .setUsage(AudioAttributesCompat.USAGE_MEDIA)
            .setContentType(AudioAttributesCompat.CONTENT_TYPE_MUSIC)
            .build()
        )
        .setOnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> {
                    mediaSession.controller.transportControls.play()
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    mediaSession.controller.transportControls.pause()
                }
                AudioManager.AUDIOFOCUS_LOSS -> {
                    mediaSession.controller.transportControls.pause()
                    //mediaSession.controller.transportControls.stop()
                }
            }
        }
        .build()
    private fun updateContentOfCurrentFragment() {
        val broadcastIntent = Intent()
        broadcastIntent.action = applicationContext.getString(R.string.file_change_broadcast)
        broadcastIntent.putExtra(FileChangeBroadcastReceiver.EXTRA_PATH,"update")
        sendBroadcast(broadcastIntent)
    }
    fun _onBack1() {

            ttscontroller.move(-1)
        }

    fun _onNext1() {
        ttscontroller.move(1)
    }
    fun _onSkipToPrevious() {

        try {
            ttscontroller.back()//reset()
            mediaSession.controller.transportControls.prepare()
            mediaSession.controller.transportControls.play()
        } catch (e: Exception) {
        }
    }
    // override fun on
   fun _onSkipToNext() {
        try {
            var isEnd:Boolean=ttscontroller.next();
            if(!isEnd) {
                mediaSession.controller.transportControls.prepare()
                mediaSession.controller.transportControls.play()
            }
        } catch (e: Exception) {
        }
        // ttscontroller.setup(MusicLibrary.getTTS_Item(baseContext))
        // MusicLibrary.setdata(File(path),0);
        // TTSService.ttsController.setup(item);
        //super.onSkipToNext()
    }
    //private val mediaPlayer = MediaPlayer()
   lateinit var ttscontroller:TTSController//=TTSController();//=TTSController(applicationContext)
    private val callback = object : MediaSessionCompat.Callback() {
        override fun onSetPlaybackSpeed(speed: Float) {
            if(speed>=1000){
                ttscontroller.Change_Pitch(speed-1000);
            }
            else {
                ttscontroller.Change_Speed(speed);
            }
        }

        override fun onSeekTo(pos: Long) {
            ttscontroller.move(pos.toInt())
           // super.onSeekTo(pos)
        }
        override fun onRewind() {
            _onSkipToPrevious()
          //  ttscontroller.move(-1)
        }

        override fun onFastForward() {
            _onSkipToNext()

        }

        override fun onSkipToPrevious() {
            val buttonMode=DataStore.getSharedPreferences(baseContext).getBoolean("buttonMode",false)
           if(!buttonMode){
               _onSkipToPrevious()
           }
            else{
               _onBack1()
           }
           /* try {
                ttscontroller.back()//reset()
                mediaSession.controller.transportControls.prepare()
                mediaSession.controller.transportControls.play()
            } catch (e: Exception) {
            }*/
        }
       // override fun on
        override fun onSkipToNext() {
           val buttonMode=DataStore.getSharedPreferences(baseContext).getBoolean("buttonMode",false)
            if(!buttonMode){
                _onSkipToNext()
           }
            else{
                _onNext1()
           }
          /* try {
               ttscontroller.next()
               mediaSession.controller.transportControls.prepare()
               mediaSession.controller.transportControls.play()
           } catch (e: Exception) {
           }*/
           // ttscontroller.setup(MusicLibrary.getTTS_Item(baseContext))
           // MusicLibrary.setdata(File(path),0);
           // TTSService.ttsController.setup(item);
            //super.onSkipToNext()
        }
        override fun onPrepare() {

          //  ttscontroller.setContext(baseContext)//=TTSController(applicationContext)

            try {
                setNewState(PlaybackStateCompat.STATE_PAUSED)
                mediaSession.setMetadata(MusicLibrary.getMetadata(baseContext))
                val  item:tts_Item?=MusicLibrary.getTTS_Item(baseContext)
                if (item != null) {
                        ttscontroller.setup(item)

                    val db: StoryIndexDB =
                        DBProvider.of(DBTableName.storyindex,applicationContext) as StoryIndexDB
                    //val model:StoryIndexModel=libraryItemModel.model.copy

                    MusicLibrary.getStoryIndexModel()?.let {
                        if(it.isNew==1) {
                            db.updateData(it.copy(0))
                            updateContentOfCurrentFragment()
                        }
                    }
                }
            } catch (e: Exception) {
            }


        }

        override fun onPlay() {

            try {
                setNewState(PlaybackStateCompat.STATE_PLAYING)
                mediaSession.isActive = true
                ttscontroller.start()
                if(mediaSession.controller.metadata!=null) {
                    try {

                        notificationManager.notify(1, buildNotification())
                        startService(Intent(baseContext, MediaPlaybackService::class.java))
                        startForeground(1, buildNotification())
                    }
                    catch (e:Exception){}
                }
                if (gainAudioFocus()) {
                    registerReceiver(audioNoisyReceiver, audioNoisyFilter)
                }
            } catch (e: Exception) {
            }

           // val filecontroller=FileController(applicationContext);
           // filecontroller.OpenShortCut()
          // val tts=TTSController(applicationContext);
            //tts.setup()


        }

        override fun onPause() {
            try {
                setNewState(PlaybackStateCompat.STATE_PAUSED)
                ttscontroller.stop()
                // mediaPlayer.pause()
                try {
                    unregisterReceiver(audioNoisyReceiver)
                    notificationManager.notify(1, buildNotification())
                    stopForeground(false)
                }
                catch (e:Exception){}
            } catch (e: Exception) {
            }

        }

        override fun onStop() {
            try {
                setNewState(PlaybackStateCompat.STATE_STOPPED)
                mediaSession.isActive = false
                stopSelf()
                unregisterReceiver(audioNoisyReceiver)
                AudioManagerCompat.abandonAudioFocusRequest(audioManager, audioFocusRequest)
                ttscontroller.destroy()
            } catch (e: Exception) {
            }
            //  mediaPlayer.stop()
           // unregisterReceiver(audioNoisyReceiver)

        }
    }
    class NotificationConst{
        companion object{
            val CHANNEL_MUSIC="r";
        }

    }
    private fun gainAudioFocus(): Boolean {
        return when (AudioManagerCompat.requestAudioFocus(audioManager, audioFocusRequest)) {
            AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> true
            AudioManager.AUDIOFOCUS_REQUEST_FAILED -> {
               // Timber.w("requestAudioFocus failed.")
                false
            }
            else -> false
        }
    }
    private fun buildNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        val currentDescription = mediaSession.controller.metadata.description
        val notificationBuilder = NotificationCompat.Builder(baseContext, NotificationConst.CHANNEL_MUSIC)
            .setStyle(MediaNotificationCompat.MediaStyle()
                .setMediaSession(mediaSession.sessionToken)
                .setShowActionsInCompactView(1)
            )
            .setColor(getColor(R.color.design_default_color_on_primary))
            .setSmallIcon(R.mipmap.ic_launcher)
            //.setLargeIcon(BitmapFactory.decodeResource( getResources(), R.drawable.bookmark_player))
            .setContentTitle(currentDescription.title)
            .setContentText(currentDescription.subtitle)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            //.setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        val buttonMode=DataStore.getSharedPreferences(baseContext).getBoolean("buttonMode",false)
        var backicon=R.drawable.reset2
        if(buttonMode){
            backicon=R.drawable.back12
        }
        val action = if (mediaSession.controller.playbackState.state == PlaybackStateCompat.STATE_PLAYING) {
            NotificationCompat.Action(
                R.drawable.stop2,
                getString(R.string.app_name),
                MediaButtonReceiver.buildMediaButtonPendingIntent(baseContext, PlaybackStateCompat.ACTION_PAUSE)
            )
        } else {
            NotificationCompat.Action(
                R.drawable.playicon2,
                "",
                MediaButtonReceiver.buildMediaButtonPendingIntent(baseContext, PlaybackStateCompat.ACTION_PLAY)
            )
        }
        //val buttonMode=DataStore.getSharedPreferences(applicationContext).getBoolean("buttonMode",false)
        /*notificationBuilder.addAction(
            R.drawable.back12 ,
            getString(R.string.app_name),
            MediaButtonReceiver.buildMediaButtonPendingIntent(baseContext, PlaybackStateCompat.ACTION_REWIND)
        )*/
        notificationBuilder.addAction(
            backicon  ,
            getString(R.string.app_name),
            MediaButtonReceiver.buildMediaButtonPendingIntent(baseContext, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
        )


        notificationBuilder.addAction(action)
        var nexticon=R.drawable.end2
        if(buttonMode){
            nexticon=R.drawable.next12
        }
        notificationBuilder.addAction(
            nexticon ,
            getString(R.string.app_name),
            MediaButtonReceiver.buildMediaButtonPendingIntent(baseContext, PlaybackStateCompat.ACTION_SKIP_TO_NEXT)
        )

       /* notificationBuilder.addAction(
            R.drawable.next12 ,
            getString(R.string.app_name),
            MediaButtonReceiver.buildMediaButtonPendingIntent(baseContext, PlaybackStateCompat.)
        )*/
       /* notificationBuilder.addAction(
            R.drawable.bookmark2 ,
            getString(R.string.app_name),
            MediaButtonReceiver.buildMediaButtonPendingIntent(baseContext, PlaybackStateCompat.ACTION_FAST_FORWARD)
        )*/
        return notificationBuilder.build()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val manager = getSystemService(NotificationManager::class.java)
        if (manager.getNotificationChannel(NotificationConst.CHANNEL_MUSIC) == null) {
            val channel = NotificationChannel(
                NotificationConst.CHANNEL_MUSIC,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description =""
            }
            manager.createNotificationChannel(channel)
        }
    }
    override fun onCreate() {
        super.onCreate()
        ttscontroller=TTSController(baseContext)
        ttscontroller.setListener {
            mediaSession.controller.transportControls.fastForward()
        }
        notificationManager = NotificationManagerCompat.from(baseContext)
        mediaSession = MediaSessionCompat(baseContext, MediaPlaybackService::class.simpleName!!).apply {
            stateBuilder = PlaybackStateCompat.Builder()
            stateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_PLAY_PAUSE
            )
            setPlaybackState(stateBuilder.build())
            setCallback(callback)
            setSessionToken(sessionToken)
        }
        audioManager = getSystemService(AudioManager::class.java)

    }
  /*  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MediaButtonReceiver.handleIntent(mediaSession, intent)
        return super.onStartCommand(intent, flags, startId)
    }*/
    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return BrowserRoot("app-media-root", null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        val meta = MusicLibrary.getMetadata(baseContext)
        if (meta != null) {
            val list = mutableListOf(
                MediaBrowserCompat.MediaItem(meta.description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
            )
            result.sendResult(list)
        }
        else{
            result.sendResult(null)
        }


    }
    private fun setNewState(@PlaybackStateCompat.State newState: Int) {
        mediaState = newState
        stateBuilder = PlaybackStateCompat.Builder()
        stateBuilder
            .setActions(getAvailableActions())
            .setState(newState,0L,1.0f)
        mediaSession.setPlaybackState(stateBuilder.build())
    }
    @PlaybackStateCompat.Actions
    private fun getAvailableActions(): Long {
        var actions = (
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
                        or PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH
                        or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                        or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                )
        actions = when (mediaState) {
            PlaybackStateCompat.STATE_STOPPED -> actions or (
                    PlaybackStateCompat.ACTION_PLAY
                            or PlaybackStateCompat.ACTION_PAUSE
                    )
            PlaybackStateCompat.STATE_PLAYING -> actions or (
                    PlaybackStateCompat.ACTION_STOP
                            or PlaybackStateCompat.ACTION_PAUSE
                            or PlaybackStateCompat.ACTION_SEEK_TO
                    )
            PlaybackStateCompat.STATE_PAUSED -> actions or (
                    PlaybackStateCompat.ACTION_PLAY
                            or PlaybackStateCompat.ACTION_STOP
                    )
            else -> actions or (
                    PlaybackStateCompat.ACTION_PLAY
                            or PlaybackStateCompat.ACTION_PLAY_PAUSE
                            or PlaybackStateCompat.ACTION_STOP
                            or PlaybackStateCompat.ACTION_PAUSE
                    )
        }
        return actions
    }


}