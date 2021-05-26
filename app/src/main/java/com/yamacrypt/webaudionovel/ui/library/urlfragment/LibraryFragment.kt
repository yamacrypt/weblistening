package com.yamacrypt.webaudionovel.ui.library.urlfragment




import android.content.Context
import android.content.DialogInterface
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.yamacrypt.webaudionovel.DataStore
import com.yamacrypt.webaudionovel.Database.DBProvider
import com.yamacrypt.webaudionovel.Database.DBTableName
import com.yamacrypt.webaudionovel.Database.StoryIndexDB
import com.yamacrypt.webaudionovel.R
import com.yamacrypt.webaudionovel.tts.layout.dictionary_dialog.DictionaryDialog
import com.yamacrypt.webaudionovel.ui.DictionaryFragment
import com.yamacrypt.webaudionovel.ui.library.fileservice.FileChangeBroadcastReceiver
import com.yamacrypt.webaudionovel.ui.library.models.BookMark_Open
import kotlinx.android.synthetic.main.dictionary_dialog.*
import kotlinx.android.synthetic.main.fragment_files_list.*
import kotlinx.android.synthetic.main.tool_bar.view.*
import kotlin.concurrent.thread


class LibraryFragment : Fragment(){
    private lateinit var mFilesAdapter: LibraryItemsRecyclerAdapter
    private lateinit var URL: String
    private lateinit var mCallback: OnItemClickListener
    private lateinit var mFileChangeBroadcastReceiver: FileChangeBroadcastReceiver

    override fun onResume() {
        super.onResume()
        context?.registerReceiver(mFileChangeBroadcastReceiver, IntentFilter(getString(R.string.file_change_broadcast)))
    }

    override fun onPause() {
        super.onPause()
        context?.unregisterReceiver(mFileChangeBroadcastReceiver)
    }
    interface OnItemClickListener {
        fun onClick(libraryItemModel: LibraryItemModel)

        fun onLongClick(libraryItemModel: LibraryItemModel)
        fun onBookMark(rootpath:String)
        fun onReload(url:String,newOnly:Boolean)
    }

    companion object {
        private const val ARG_URL: String = "com.yamacrypt.webaudionovel.ui.library.fileslist.path"
        /* fun build(block: Builder.() -> Unit) = Builder()
             .apply(block).build()*/
        fun build2(block: Builder.() -> Unit) = Builder()
            .apply(block).build2()

    }

    class Builder {
        var url: String = ""

        fun build(): LibraryFragment {
            val fragment =
                LibraryFragment()
            val args = Bundle()
            args.putString(ARG_URL, url)
            fragment.arguments = args;
            return fragment
        }
        fun build2(): Bundle {
            val args = Bundle()
            args.putString(ARG_URL, url)
            return args
        }
    }
    override fun onAttach(context: Context) {//疑問点
        super.onAttach(context)

        try {
            mCallback = context as OnItemClickListener
        } catch (e: Exception) {
            throw Exception("${context} should implement FilesListFragment.OnItemCLickListener")
        }
    }
    // var filePath:String=""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        var filePath = arguments?.getString(ARG_URL)
        // filePath=path
        if (filePath == null) {
            // Toast.makeText(context, "Path should not be null!", Toast.LENGTH_SHORT).show()
            // return
            //val path = Environment.getExternalStorageDirectory().absolutePath
            val shortcut_file=DataStore.getShortCutFile(context,"")
            val path=shortcut_file.absolutePath
            //Itializeでの処理がよいかも
            val url="root"
            //DBProvider.of(DBTableName.storyindex,requireContext()).insertData(StoryIndexModel(shortcut_file.parent,shortcut_file.path,0,url))
            val args = Bundle()
            args.putString(ARG_URL, url)
            this.arguments = args;
            filePath = arguments?.getString(ARG_URL)
        }
        URL = filePath!!
        Log.d("URL",URL);
        mFileChangeBroadcastReceiver = FileChangeBroadcastReceiver("update") {
            updateDate()
        }
    }

    //lateinit var menuitem:MenuItem
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.library,menu)
        val menuitem=menu.findItem(R.id.library_menuitem);
        val db=  DBProvider.of(DBTableName.storyindex,requireContext()) as StoryIndexDB
        val PATH=db.getPATH(URL)
        val bookmark=BookMark_Open(PATH,requireContext())
        menuitem.isEnabled = true
        menuitem.isVisible = true
        if(bookmark!=null&&URL!="root"){
           menuitem.icon = resources.getDrawable(R.drawable.bookmark);

            menuitem.setOnMenuItemClickListener {

                mCallback.onBookMark(PATH);
                true;
            }

            Log.d("ENABLE","TRUE")
        }
        else if(bookmark!=null&&URL=="root"){
            menuitem.icon = resources.getDrawable(R.drawable.continueicon);
            menuitem.setOnMenuItemClickListener {

                try {
                   /* val  db : StoryIndexDB = DBProvider.of(DBTableName.storyindex,requireContext()) as StoryIndexDB;
                    //val files=db.getsortedAllfromparent(bookMark.rootpath)
                    //val bk=BookMark_Open(URL,requireContext())
                    //val parent=File(bookmark.path).parent+"/"
                   val parent=db.getPARENTURL(bookmark.path)
                    val ls=db.getsortedAllurlsfromParenturl(parent!!)
                    PlayList.setup(ls,parent)*/
                    mCallback.onBookMark(PATH);
                } catch (e: Exception) {
                }
                true;
            }

            Log.d("ENABLE", "FALSE")
        }
        else{
            menuitem.isEnabled = false
            menuitem.isVisible = false
        }
        val reloaditem=menu.findItem(R.id.library_update)
        val check=URL!="root"&& mToolbar.toolbar_progress_bar?.isVisible==false
        val dictionaryItem=menu.findItem(R.id.library_dictionary)
        dictionaryItem.setOnMenuItemClickListener {
            val dialogFragment = DictionaryDialog(URL)
            dialogFragment.show(childFragmentManager, "my_dialog");
            /*AlertDialog.Builder(requireContext())
                .setView(R.layout.dictionary_dialog)
                .setTitle("読み登録")

                .setNeutralButton("読み一覧",DialogInterface.OnClickListener{dialog, which ->
                })
                .setPositiveButton("登録する",DialogInterface.OnClickListener{dialog, which ->
                    
                })
                .show()*/
            true
        }
        reloaditem.isEnabled=check
        reloaditem.isVisible=check
        reloaditem.setOnMenuItemClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("小説の更新")
                .setMessage("更新を確認しますか?(全ての場合DL済みのものを含めてチェック)") // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setNeutralButton("全て更新", DialogInterface.OnClickListener {dialog,which->
                    run{
                       val handler =  Handler(Looper.getMainLooper());
                        thread {
                            handler.post(Runnable {
                                reloaditem.isEnabled = false
                                reloaditem.isVisible = false
                                mToolbar.toolbar_progress_bar?.isVisible = true
                            })

                            mCallback.onReload(URL,false);
                            handler.post(
                            Runnable {
                                mToolbar.toolbar_progress_bar?.isVisible = false
                                reloaditem.isEnabled = true
                                reloaditem.isVisible = true
                            }
                            )
                        }
                    }
                })
                .setPositiveButton("更新") { dialog, which->
                    run{
                        val handler =  Handler(Looper.getMainLooper());
                        thread {
                            handler.post(Runnable {
                                reloaditem.isEnabled = false
                                reloaditem.isVisible = false
                                mToolbar.toolbar_progress_bar?.isVisible = true
                            })

                            mCallback.onReload(URL,true);
                            handler.post(
                                Runnable {
                                    mToolbar.toolbar_progress_bar?.isVisible = false
                                    reloaditem.isEnabled = true
                                    reloaditem.isVisible = true
                                }
                            )
                        }
                    }
                }


                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Cancel", null)
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show()

            true
        }
    }
    /* override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
         inflater.inflate(R.menu.main_menu, menu);

         super.onCreateOptionsMenu(menu, inflater)
     }*/
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_files_list, container, false)
    }
    lateinit var mToolbar: Toolbar
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mToolbar = requireActivity().findViewById(R.id.tool_bar)
       // mToolbar.toolbar_progress_bar?.isEnabled=false
       // mToolbar.toolbar_progress_bar?.isVisible=false
        val filePath = arguments?.getString(ARG_URL)
        if (filePath == null) {
            Toast.makeText(context, "Path should not be null!", Toast.LENGTH_SHORT).show()
            return
        }
        URL = filePath

        initViews()
    }

    private fun initViews() {
        filesRecyclerView.layoutManager = LinearLayoutManager(context)
        mFilesAdapter =LibraryItemsRecyclerAdapter()
        filesRecyclerView.adapter = mFilesAdapter
        mFilesAdapter.onItemClickListener = {
            mCallback.onClick(it)
        }

        mFilesAdapter.onItemLongClickListener = {
            mCallback.onLongClick(it)
        }
        updateDate()
    }

    fun updateDate() {
       // val items = getlibraryitemModelsFromurls(getURLsFromParentURL(URL,context = requireContext()),requireContext())
        val runnable= Runnable {
            val items = getLibraryItemsFromURL(URL, requireContext())
            if (items.isEmpty()) {
                emptyFolderLayout?.visibility = View.VISIBLE
            } else {
                emptyFolderLayout?.visibility = View.INVISIBLE
            }
            // val ls=files.map{ it ->it.path }
            mFilesAdapter.updateData(items)
        }
        val handler =  Handler(Looper.getMainLooper());
        thread {

            handler.post(runnable)
        }
    }

    /* fun speakNovel(filename:String){
         val ttsControler=TTSController(context)
         val downloader= FileController(context)
         val file=DataStore.getNovelFile(context,filename)
         val spealls=downloader.ReadLines(file)
       // ttsControler.speak(spealls)
     }*/
}

