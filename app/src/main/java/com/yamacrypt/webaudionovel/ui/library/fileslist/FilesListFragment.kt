package com.yamacrypt.webaudionovel.ui.library.fileslist

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yamacrypt.webaudionovel.*
import com.yamacrypt.webaudionovel.ui.library.fileservice.FileChangeBroadcastReceiver
import com.yamacrypt.webaudionovel.ui.library.models.BookMark_Open

import com.yamacrypt.webaudionovel.ui.library.utils.getFilesFromPath
import kotlinx.android.synthetic.main.fragment_files_list.*
import java.io.File
import kotlin.concurrent.thread
/*
class FilesListFragment : Fragment(){
    private lateinit var mFilesAdapter: FilesRecyclerAdapter
    private lateinit var PATH: String
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
        fun onReload(rootpath:String)
    }

    companion object {
        private const val ARG_PATH: String = "com.yamacrypt.webaudionovel.ui.library.fileslist.path"
       /* fun build(block: Builder.() -> Unit) = Builder()
            .apply(block).build()*/
        fun build2(block: Builder.() -> Unit) = Builder()
            .apply(block).build2()
    }

    class Builder {
        var path: String = ""

        fun build(): FilesListFragment {
            val fragment =
                FilesListFragment()
            val args = Bundle()
            args.putString(ARG_PATH, path)
            fragment.arguments = args;
            return fragment
        }
        fun build2(): Bundle {
            val args = Bundle()
            args.putString(ARG_PATH, path)
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

        var filePath = arguments?.getString(ARG_PATH)
       // filePath=path
        if (filePath == null) {
           // Toast.makeText(context, "Path should not be null!", Toast.LENGTH_SHORT).show()
           // return
            //val path = Environment.getExternalStorageDirectory().absolutePath
           val shortcut_file=DataStore.getShortCutFile(context,"")
            val path=shortcut_file.absolutePath
            val args = Bundle()
            args.putString(ARG_PATH, path)
            this.arguments = args;
            filePath = arguments?.getString(ARG_PATH)
        }
        PATH = filePath!!
       Log.d("PATH",PATH);
        mFileChangeBroadcastReceiver = FileChangeBroadcastReceiver(PATH) {
            updateDate()
        }
    }
    //lateinit var menuitem:MenuItem
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.library,menu)
        val menuitem=menu.findItem(R.id.library_menuitem);
            val bookmark=BookMark_Open(PATH,requireContext())
            if(File(bookmark.path).exists()){
                menuitem.isEnabled = true
                menuitem.isVisible = true
                Log.d("ENABLE","TRUE")
            }
            else{
                menuitem.isEnabled = false
                menuitem.isVisible = false
                Log.d("ENABLE","FALSE")
            }

        menuitem.setOnMenuItemClickListener {

            mCallback.onBookMark(PATH);
            true;
        }
        val reloaditem=menu.findItem(R.id.library_update)

            reloaditem.isEnabled=File(PATH).isDirectory
        reloaditem.setOnMenuItemClickListener {
            thread {
                mCallback.onReload(PATH);
            }
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       val filePath = arguments?.getString(ARG_PATH)
        if (filePath == null) {
            Toast.makeText(context, "Path should not be null!", Toast.LENGTH_SHORT).show()
            return
        }
        PATH = filePath

        initViews()
    }

    private fun initViews() {
        filesRecyclerView.layoutManager = LinearLayoutManager(context)
        mFilesAdapter = FilesRecyclerAdapter()
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
        val files = getFileModelsFromFiles(getFilesFromPath(PATH,context = requireContext()))

        if (files.isEmpty()) {
            emptyFolderLayout.visibility = View.VISIBLE
        } else {
            emptyFolderLayout.visibility = View.INVISIBLE
        }
       // val ls=files.map{ it ->it.path }
        mFilesAdapter.updateData(files)
    }

   /* fun speakNovel(filename:String){
        val ttsControler=TTSController(context)
        val downloader= FileController(context)
        val file=DataStore.getNovelFile(context,filename)
        val spealls=downloader.ReadLines(file)
      // ttsControler.speak(spealls)
    }*/
}*/

