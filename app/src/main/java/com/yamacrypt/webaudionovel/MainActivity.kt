package com.yamacrypt.webaudionovel


//import com.yamacrypt.webaudionovel.TTSService.Companion.ttsController
import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import com.yamacrypt.webaudionovel.BuildConfig.DEBUG_MODE
import com.yamacrypt.webaudionovel.Database.DBProvider
import com.yamacrypt.webaudionovel.Database.DBTableName
import com.yamacrypt.webaudionovel.Database.StoryIndexDB
import com.yamacrypt.webaudionovel.MusicService.MediaPlaybackService
import com.yamacrypt.webaudionovel.MusicService.MusicLibrary
import com.yamacrypt.webaudionovel.htmlParser.HTMLFactory
import com.yamacrypt.webaudionovel.ui.*
import com.yamacrypt.webaudionovel.ui.library.common.FileType
import com.yamacrypt.webaudionovel.ui.library.fileservice.FileChangeBroadcastReceiver
import com.yamacrypt.webaudionovel.ui.library.main.BackStackManager
import com.yamacrypt.webaudionovel.ui.library.main.BreadcrumbRecyclerAdapter
import com.yamacrypt.webaudionovel.ui.library.main.FileOptionsDialog
import com.yamacrypt.webaudionovel.ui.library.models.BookMark
import com.yamacrypt.webaudionovel.ui.library.models.BookMark_Open
import com.yamacrypt.webaudionovel.ui.library.urlfragment.LibraryFragment
import com.yamacrypt.webaudionovel.ui.library.urlfragment.LibraryItemModel
import com.yamacrypt.webaudionovel.ui.library.urlfragment.getLibraryItemsFromURL
import com.yamacrypt.webaudionovel.ui.library.utils.FileUtilsDeleteFile
import com.yamacrypt.webaudionovel.ui.search.WebFragment
import com.yamacrypt.webaudionovel.ui.search.selector.SelectWebFragment
import com.yamacrypt.webaudionovel.ui.search.selector.WebItem
import kotlinx.android.synthetic.main.dialog_enter_name.view.*
import kotlinx.android.synthetic.main.dictionary_dialog.*
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.android.synthetic.main.fragment_web.*
import kotlinx.android.synthetic.main.smallwindow.*
import kotlinx.android.synthetic.main.tool_bar.view.*
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity: AppCompatActivity() ,ReviewDialogFragment.ReviewDialogFragmentListener ,SelectWebFragment.OnWebItemClickListener,LibraryFragment.OnItemClickListener{
    private val backStackManager = BackStackManager()
    private lateinit var mBreadcrumbRecyclerAdapter: BreadcrumbRecyclerAdapter
    companion object {
       lateinit var mInterstitialAd: InterstitialAd
        var isInit :Boolean=true;
        private const val OPTIONS_DIALOG_TAG: String = "com.yamacrypt.filebrowsertest.main.options_dialog"
    }

  //  private lateinit var mInterstitialAd: InterstitialAd
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setSupportActionBar(toolbar);
      setVolumeControlStream(AudioManager.STREAM_MUSIC)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this){}
        mInterstitialAd = InterstitialAd(this)
        //
      if(DEBUG_MODE) {
          mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
      }
      else {
          mInterstitialAd.adUnitId = getString(R.string.interstitial_ad_unit_id)
      }
     mInterstitialAd.loadAd(AdRequest.Builder().build())
      mInterstitialAd.adListener = object : AdListener() {
          override fun onAdClosed() {
              mInterstitialAd.loadAd(AdRequest.Builder().build())
          }
      }
      mediaBrowser = MediaBrowserCompat(
          applicationContext,
          ComponentName(this, MediaPlaybackService::class.java),
          connectionCallback,
          null
      )
        val pref = DataStore.getSharedPreferences(applicationContext)
      val initialized=pref.getBoolean("Initialized",false)
      if(!initialized) {
          val db = DBProvider.of(DBTableName.storyindex, applicationContext) as StoryIndexDB
          val url="root"
          val shortcut_file=DataStore.getShortCutFile(applicationContext,"")
         // db.insertData(StoryIndexModel(shortcut_file.parent,shortcut_file.path,0,url))
          db.loadInitData()
          val editor = pref.edit()
          editor.putBoolean("Initialized",true)
          editor.apply()
      }
        val model =PlayerViewModel()
        val windowObserver: Observer<PlayerViewModel.PlayData?> =
            Observer<PlayerViewModel.PlayData?> { playdata ->
                Reload(playdata)
               // positionBar.setProgress(integer)
                //  str.setText(MainActivity.);
            }

        model.playdata.observe(this,windowObserver)
       val stateObserver:Observer<Boolean> =
        Observer<Boolean> {
            state -> Play(state)
        }
       model.playingstate.observe(this,stateObserver)

        initBackStack()

      val intent = intent
      val action = intent.action
      if (Intent.ACTION_SEND == action) {

          intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
              val navController =
                  Navigation.findNavController(this,
                      R.id.nav_main_fragment
                  )
              navController.navigate(R.id.action_navigation_menu_to_navigation_web,
                  WebFragment.build {
                      defaulturl=it
                      //filesList[adapterPosition]
                      //mode=HTMLFactory.getMode(it)
                  })

          }

      }
    }

    private fun Play(state: Boolean) {
        val v=findViewById<Button>(R.id.play_button)
        val v2=findViewById<Button>(R.id.playBtn)
       // val ttsController=TTSService.ttsController
        if (!state) {
            //ttsController.stop()
            v?.foreground = getDrawable(R.drawable.playicon)
            v2?.foreground = getDrawable(R.drawable.playicon)

        } else {
            //ttsController. speak_continue()
            v?.foreground = getDrawable(R.drawable.stop)
            v2?.foreground = getDrawable(R.drawable.stop)

        }
    }

    private fun initBackStack() {
        backStackManager.onStackChangeListener = {
          // updateAdapterData(it)
        }

        //backStackManager.addToStack(libraryItemModel = LibraryItemModel(DataStore.getShortCutFile(this,"").absolutePath, FileType.FOLDER, "/", 0.0))
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuNewFile -> createNewFileInCurrentDirectory()
            R.id.menuNewFolder -> createNewFolderInCurrentDirectory()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createNewFileInCurrentDirectory() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_enter_name, null)
        view.createButton.setOnClickListener {
            val fileName = view.nameEditText.text.toString()
        }
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }
    private fun createNewFolderInCurrentDirectory() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_enter_name, null)
        view.createButton.setOnClickListener {
            val fileName = view.nameEditText.text.toString()
        }
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }
    private fun updateContentOfCurrentFragment() {
        val broadcastIntent = Intent()
        broadcastIntent.action = applicationContext.getString(R.string.file_change_broadcast)
        broadcastIntent.putExtra(FileChangeBroadcastReceiver.EXTRA_PATH,"update")
        sendBroadcast(broadcastIntent)
    }

    private lateinit var mediaBrowser: MediaBrowserCompat
    private val controllerCallback = object : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            when (state?.state) {
                PlaybackStateCompat.STATE_PLAYING -> {
                    playBtn?.foreground = getDrawable(R.drawable.stop)
                    play_button?.foreground = getDrawable(R.drawable.stop)
                }
                else -> {
                    playBtn?.foreground = getDrawable(R.drawable.playicon)
                    play_button?.foreground = getDrawable(R.drawable.playicon)
                }
            }
        }
        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {

            }
    }
    private val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {

        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaBrowserCompat.MediaItem>
        ) {
            // 曲リストを受け取ってますが、1曲だけなので、特に利用してません。とにかくprepareを呼び出します。
            //MediaControllerCompat.getMediaController(this@MainActivity)?.transportControls?.prepare()
        }
    }
    override fun onStart() {
        super.onStart()
        if(!isInit)
            return;
        isInit=false;
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.tool_bar)//.findViewById(R.id.tool_bar)
        mToolbar.toolbar_progress_bar?.isEnabled=false
        mToolbar.toolbar_progress_bar?.isVisible=false
        if(!mediaBrowser.isConnected)
        mediaBrowser.connect()
        prepareItemOnStart()

    }

    // お作法的なコード
    override fun onStop() {
        try {
            super.onStop()
            MediaControllerCompat.getMediaController(this)?.unregisterCallback(controllerCallback)
            mediaBrowser.disconnect()
            this.unregisterReceiver(mCreateSleepTimerBroadcastReceiver)
            //tts.destroy()
        } catch (e: Exception) {
        }
    }
    private val connectionCallback = object : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            // 接続後、受け取ったTokenで操作するようにします。
             try {
                 MediaControllerCompat.setMediaController(this@MainActivity,
                     MediaControllerCompat(applicationContext, mediaBrowser.sessionToken))
                 buildTransportControls()
                 mediaBrowser.subscribe(mediaBrowser.root, subscriptionCallback)
            } catch (e: Exception) {
            }
            // 接続したので、曲リストを購読します。ここでparentIdを渡しています。


        }
    }
    private fun buildTransportControls() {

        try {
            val mediaController = MediaControllerCompat.getMediaController(this)
            mCreateSleepTimerBroadcastReceiver= SleepTImerBroadcastReceiver(mediaController)
            this.registerReceiver(
                mCreateSleepTimerBroadcastReceiver,
                IntentFilter(getString(R.string.sleep_timer_broadcast))
            )
            mediaController.registerCallback(controllerCallback)
        } catch (e: Exception) {
        }
    }
    fun getchild(s: String): String {
        val ls = s.split("/".toRegex()).toTypedArray()
        var res = ""
        if (ls.size > 1) {
            for (i in 0 until ls.size - 1) {
                res += ls[i] + "/"
            }
            return res.substring(0, res.length - 1)
        }
        throw java.lang.Exception()
    }
    fun prepareItemOnStart(){
        val db = DBProvider.of(DBTableName.storyindex, applicationContext) as StoryIndexDB
        val rootpath=db.getPATH("root")
        val bookMark=_loadBookMark(rootpath)
        bookMark?.let {
            try {
               val hoge = db.getLibraryIndexItem(bookMark.path)//.copy(position = bookMark.position)
                _Prepare_Item(hoge,it.startindex)
                //hoge.position=bookMark.position
            }catch (e:java.lang.Exception){}
        }
    }
    fun _Prepare_Item(libraryItemModel: LibraryItemModel, startindex:Int){
        val link =DataStore.getNovelFile(applicationContext, DataStore.getConvertedURL( libraryItemModel.model.url))
        if(link.exists()){
            val  db : StoryIndexDB = DBProvider.of(DBTableName.storyindex,applicationContext) as StoryIndexDB;
            //val files=db.getsortedAllfromparent(bookMark.rootpath)
            //val items=db.getsortedLibraryItemsfromParenturl(libraryItemModel.parent_url)
            //val ls=items.map{ it ->it.model.url }

            var position=0
            PlayList.done()
            PlayList.urllist.forEachIndexed{index,it->run{
                if(it==libraryItemModel.model.url) {
                    position = index
                }
            }}
            PlayList.current_number=position

            //PlayList.setup(ls,position,libraryItemModel.model.parent_path)
            val file = File(libraryItemModel.model.path)
            MusicLibrary.setdata(db.getStoryIndexItem(libraryItemModel.model.url), startindex)
            MediaControllerCompat.getMediaController(this).transportControls.prepare()
        }
    }

    fun Play_Item(libraryItemModel: LibraryItemModel,startindex:Int){
        val link =DataStore.getNovelFile(applicationContext, DataStore.getConvertedURL( libraryItemModel.model.url))
        if(link.exists()){
            val  db : StoryIndexDB = DBProvider.of(DBTableName.storyindex,applicationContext) as StoryIndexDB;

            var position=0
            PlayList.done()
            PlayList.urllist.forEachIndexed{index,it->run{
                if(it==libraryItemModel.model.url) {
                    position = index
                }
            }}
            PlayList.current_number=position

            //PlayList.setup(ls,position,libraryItemModel.model.parent_path)
           val file = File(libraryItemModel.model.path)
            MusicLibrary.setdata(db.getStoryIndexItem(libraryItemModel.model.url), startindex)
            MediaControllerCompat.getMediaController(this).transportControls.prepare()
            MediaControllerCompat.getMediaController(this).transportControls.play()
            //  val item= fileController.OpenShortCut(file, bookMark.startindex)
            //ttsController.setup(item);
            Showsmallwindow(file.name, "")
        }
    }
        fun _Play_Item(bookMark: BookMark ){
        if(File(bookMark.path).exists()) {
            //val files = getlibraryitemModelsFromurls(getFilesFromPath(bookMark.rootpath,context=applicationContext))
            val  db : StoryIndexDB = DBProvider.of(DBTableName.storyindex,applicationContext) as StoryIndexDB;
            //val files=db.getsortedAllfromparent(bookMark.rootpath)
            val items=db.getsortedLibraryItemsfromParenturl(bookMark.rootpath)
            val ls=items.map{ it ->it.model.url }
            val fileController = FileController(this)
            //val file=File(this.filesDir,fileModel.path)
            val file = File(bookMark.path)
            MusicLibrary.setdata(db.getStoryIndexItem(bookMark.path), bookMark.startindex)
            MediaControllerCompat.getMediaController(this).transportControls.prepare()
            MediaControllerCompat.getMediaController(this).transportControls.play()
          //  val item= fileController.OpenShortCut(file, bookMark.startindex)
          //ttsController.setup(item);
            Showsmallwindow(file.name, "")
        }
        //val shortcut=fileController.ReadShortCut(file)
    }

    override fun onClick(libraryItemModel: LibraryItemModel) {

        if (libraryItemModel.fileType == FileType.FOLDER) {
          //  if(libraryItemModel.model.url!=null)
            addFileFragment(libraryItemModel)
            val db:StoryIndexDB=DBProvider.of(DBTableName.storyindex,applicationContext) as StoryIndexDB
            //val model:StoryIndexModel=libraryItemModel.model.copy
            db.updateData(libraryItemModel.model.copy(0))
        }else {
           // val file=File(fileModel.path)

          //  val bookMark = BookMark(libraryItemModel.path, 0, File(libraryItemModel.path).parent,libraryItemModel.position)
                //Play_Item(bookMark);
            Play_Item(libraryItemModel,0)
        }

    }

    override fun onClick(fileModel: WebItem) {
        val navController =
            Navigation.findNavController(this,
                R.id.nav_main_fragment
            )

        navController.navigate(R.id.action_navigation_menu_to_navigation_web,
            WebFragment.build {
                //filesList[adapterPosition]
                defaulturl=HTMLFactory.from(fileModel.mode)?.get_defaulturl() ?:""
            })
    }

    fun onClicked_Review(){
        val manager = ReviewManagerFactory.create(this)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task: Task<ReviewInfo?> ->
            when {
                task.isSuccessful -> {
                    val reviewInfo = task.result
                    val flow = manager.launchReviewFlow(this, reviewInfo)
                    flow.addOnCompleteListener { task1: Task<Void?>? ->
                        // The flow has finished.
                    }
                }
                else -> {
                    // error or something
                }
            }
        }
    }


    fun Showsmallwindow(introduction: String?, number: String?) {
        //mPopupWindow = new PopupWindow();
        //LinearLayout layout=view.findViewById(R.id.Lib_layout);
        val layout: LinearLayout = this.findViewById(R.id.Layout)


        //View popupView = getLayoutInflater().inflate(R.layout.smallwindow, null);
        // mPopupWindow.setContentView(popupView);
        val title = MenuFragment.popupView.findViewById<TextView>(R.id.dl_text)
        title.text = introduction
        val nextbt=MenuFragment.popupView.findViewById<Button>(R.id.backbutton)
      /*  nextbt.setOnClickListener(View.OnClickListener {
            TTSService.ttsController.end()
        })*/
        DataStore.intro = introduction
        val linearLayoutLayoutPrams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        // set the textView layout parameter  using the layout parameter we created earlier linearLayoutLayoutPrams
        MenuFragment.popupView.layoutParams = linearLayoutLayoutPrams
        val playBtn =
            MenuFragment.popupView.findViewById<Button>(R.id.play_button)

        if (DataStore.check_window) {
            //layout.removeViewAt(1)
        }
        else {
            layout.addView(MenuFragment.popupView, DataStore.smallwindow_position)
        }
        DataStore.check_window = true
       // smallwindow_enabled(true)
    }
    fun Reload(playdata: PlayerViewModel.PlayData?){
        val title = MenuFragment.popupView.findViewById<TextView>(R.id.dl_text)
        title.text =playdata?.intro// introduction
        DataStore.intro=playdata?.intro
    }



    private fun playing_novel(libraryItemModel: LibraryItemModel) {
      //  val path=libraryItemModel.path
        //audioController.play_music(path)
    }

    override fun onLongClick(libraryItemModel: LibraryItemModel) {
        val optionsDialog = FileOptionsDialog.build {}
        optionsDialog.onDeleteClickListener = {
            val path=libraryItemModel.model.url
           FileUtilDelete(libraryItemModel,this)
            //val db=DBProvider.of(DBTableName.storyindex,applicationContext) as StoryIndexDB
            //db.deleteData()

            updateContentOfCurrentFragment()

        }
        //SetIcon(true)
        optionsDialog.show(supportFragmentManager, OPTIONS_DIALOG_TAG)
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaControllerCompat.getMediaController(this).transportControls.stop()
       // unregisterReceiver();
        val tts=TTSController.getInstance()
        tts.destroy();
        DataStore.check_window=false
      //  MediaPlaybackService.NotificationConst
    }
    fun _loadBookMark(rootpath:String):BookMark?{
        val  db : StoryIndexDB = DBProvider.of(DBTableName.storyindex,applicationContext) as StoryIndexDB;
        //val files=db.getsortedAllfromparent(bookMark.rootpath)
        //val bk=BookMark_Open(URL,requireContext())
        //val parent=File(bookmark.path).parent+"/"
        val bookMark= BookMark_Open(rootpath,this)
            ?: return null//BookMark(PlayList.pathlist[0],10);
        val parent=db.getPARENTURL(bookMark.path)
        val ls=db.getsortedAllurlsfromParenturl(parent!!)
        PlayList.setup(ls,parent)


        PlayList.setCurrent_number(bookMark.position)//ブックマークボタン押したときのplaylistt対策
       return bookMark
    }
    override fun onBookMark(rootpath:String) {

        val bookMark=_loadBookMark(rootpath)
        bookMark?.let {
            try {
                val db = DBProvider.of(DBTableName.storyindex, applicationContext) as StoryIndexDB
                val hoge = db.getLibraryIndexItem(bookMark.path)//.copy(position = bookMark.position)
               Play_Item(hoge,it.startindex)
                //hoge.position=bookMark.position
            }catch (e:java.lang.Exception){}
        }

       // Play_Item(PlayList.pathlist[0],10)
    }

    override fun onReload(url: String,newOnly:Boolean) {
     //   TODO("Not yet implemented")
        NovelDownloader.update(url,newOnly,applicationContext, { updateContentOfCurrentFragment() })

    }

    fun FileUtilDelete(model:LibraryItemModel,context: Context){
        //val db=DBProvider.of(DBTableName.storyindex) as StoryIndexDB
       // val path=db.getPATH(url)
        val file = File(model.model.path)

        if (file.isDirectory) {
           // val list = File(path).list()
            val models= getLibraryItemsFromURL(model.model.url ,context)
            for(child in models) {
                FileUtilDelete(child,context)
            }
            FileUtilsDeleteFile(model,context)
        }
        else {
            fdelete(model,context)
        }

    }

    private val REQUEST_CODE_PICKER = 1
    private val REQUEST_CODE_PERMISSION = 2


    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICKER && resultCode == Activity.RESULT_OK) {
            //selectImageUri = data.data
            // 選択された画像の情報を取得（ストレージされたファイルは要READ_EXTERNAL_STORAGE）
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                // 許可されていない
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {

                    // すでに１度パーミッションのリクエストが行われていて、
                    // ユーザーに「許可しない（二度と表示しないは非チェック）」をされていると
                    // この処理が呼ばれます。
                    Toast.makeText(this, "パーミッションがOFFになっています。", Toast.LENGTH_SHORT).show()
                } else {
                    // パーミッションのリクエストを表示
                    ActivityCompat.requestPermissions(
                        this, arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_CODE_PERMISSION
                    )
                }
                return
            }
            // 許可されている、またはAndroid 6.0以前
            //mConsoleText.setText("Image Width:" + readPickerImageWidth(selectImageUri))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            // requestPermissionsで設定した順番で結果が格納されています。
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 許可されたので処理を続行
               // mConsoleText.setText("Image Width:" + readPickerImageWidth(selectImageUri))
            } else {
                // パーミッションのリクエストに対して「許可しない」
                // または以前のリクエストで「二度と表示しない」にチェックを入れられた状態で
                // 「許可しない」を押されていると、必ずここに呼び出されます。
                Toast.makeText(this, "パーミッションが許可されていません。", Toast.LENGTH_SHORT).show()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
    }

    fun fdelete(model:LibraryItemModel,context: Context){
        var novelpath:String=""
        try {
            val fileController = FileController(context)

             val novel= DataStore.getNovelFile(context, model.model.novel_name)
            //val security = System.getSecurityManager()
            //val a=security?.checkDelete(novel.path)
            try {
                novel.delete()
            } catch (e: Exception) {
                Log.d("deleteerror",e.toString())
            }
            //FileUtilsDeleteFile(novelpath,context)
        }
        catch (e:Exception){
            Log.d("TAG",novelpath)
        }
            FileUtilsDeleteFile(model,context)
    }


    private fun addFileFragment(libraryItemModel: LibraryItemModel) {

        val navController =
            Navigation.findNavController(this,
                R.id.nav_menu_fragment
            )
        navController.navigate(R.id.action_myself,LibraryFragment.build2 {
            url= libraryItemModel.model.url
        })


    }
    override fun onBackPressed() {
       // val f = getFragmentManager().findFragmentById(R.id.nav_main_fragment);
        //Log.d("TAG")
        if (search_wevview!=null&& search_wevview.canGoBack()) {
            search_wevview.goBack();
        }
        else {
            super.onBackPressed()
            // smallwindow_enabled(true)
            backStackManager.popFromStack()
        }

    }

    override fun onDoReviewButtonClick() {
        goToUrl("https://play.google.com/store/apps/details?id=com.yamacrypt.webaudionovel")
    }
    private var mCreateSleepTimerBroadcastReceiver: SleepTImerBroadcastReceiver? = null
    override fun onResume() {
        super.onResume()


    }

    override fun onPause() {
        super.onPause()
        //this.unregisterReceiver(mCreateSleepTimerBroadcastReceiver)
    }

    fun getNowDate(): String {
        val df: DateFormat = SimpleDateFormat("yyyy/MM/dd")
        val date = Date(System.currentTimeMillis())
        return df.format(date)
    }

    open fun createListener(): AudioController.Listener? {
        return AudioController.Listener {
            DataStore.check_window = false
            val navController =
                Navigation.findNavController(this@MainActivity, R.id.nav_main_fragment)
            navController.navigateUp()
        }
    }

    //var audioController: AudioController? = null
    val speedfragment: Fragment? = null
    val transaction: FragmentTransaction? = null
    fun translate_Speed() {}




    fun goToapp(view: View?) {
        goToUrl("https://www.yamacrypt.com/myapps/")
    }

    fun goTotwitter(view: View?) {
        goToUrl("https://twitter.com/yamacrypt")
    }
    fun playerToFullText(v:View?){
        val navController=Navigation.findNavController(this,R.id.nav_main_fragment);
        navController.navigate(R.id.action_navigation_player_to_text)
    }
    fun musicplayer_Click(v: View?) {
        /*LinearLayout layout=findViewById(R.id.Layout);
       if(DataStore.check_window) {
            layout.removeViewAt(1);
            DataStore.check_window=false;
        }*/
        /*val navController =
            Navigation.findNavController(this, R.id.nav_main_fragment)
        val action=MenuFragmentDirections.actionNavigationMenuToNavigationPlay(DataStore.intro)
        navController.navigate(action)*/
      //  val layout: LinearLayout = this.findViewById(R.id.Layout)
       val navController =
            Navigation.findNavController(this,
                R.id.nav_main_fragment
            )
        val action= MenuFragmentDirections.actionNavigationMenuToNavigationPlay(DataStore.intro)
      // val action=PlayerFragmentDirections.goPlayer(DataStore.intro)
        try {
            navController.navigate(action)
        } catch (e: Exception) {
        }
        // smallwindow_enabled(false)

        // layout.removeViewAt(DataStore.smallwindow_position)
        //DataStore.check_window=true
    }
    fun smallwindow_enabled(check:Boolean){
        val window=findViewById<ConstraintLayout>(R.id.lib_item_model)
        window?.isEnabled = check
        window?.isVisible=check
    }
    open fun goToUrl(url: String) {
        val uriUrl = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }

    fun playBtnClick(v: View) {
        val state = mediaController?.playbackState?.state
        if (state == PlaybackStateCompat.STATE_PLAYING) {
            mediaController.transportControls.pause()
           // mediaController.transportControls.
            //  playBtn?.foreground = this.getDrawable(R.drawable.stop)
        } else if(state==PlaybackStateCompat.STATE_PAUSED) {
            mediaController.transportControls.play()
            // playBtn?.foreground = this.getDrawable(R.drawable.play)
        }
    }
    fun backBtnClick(v: View?) {

        //val ttsController=TTSService.ttsController

      //  ttsController.back(1);
        //audioController?.back_music(30)
    }
    fun next1BtnClick(v:View?){
        mediaController.transportControls.seekTo(1)
    }
    fun back1BtnClick(v:View?){
     mediaController.transportControls.seekTo(-1)
    }
    fun resetBtnClick(v:View?){
        //mediaController.transportControls.skipToPrevious()
        mediaController.transportControls.rewind()
    }
    fun endBtnClick(v: View?){

        mediaController.transportControls.fastForward()

    }

    /*SearchView mSearchView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        return true;
    }*/
    //public static AudioController audioController;

     fun setupViews() {
        // TextViewがタップされたときにダイアログを表示

            val dialog = AlertDialog.Builder(this)
                .setTitle("title")
                .setMessage("message")
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null) // Toast.makeText(context, "OKがタップされた", Toast.LENGTH_SHORT).show()
            .create()
            dialog.show()
        }

}

