package com.yamacrypt.webaudionovel.ui.search.selector

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yamacrypt.webaudionovel.R
import kotlinx.android.synthetic.main.fragment_selectweb.*

class SelectWebFragment : Fragment() {
    private lateinit var mCallback: OnWebItemClickListener
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_selectweb, container, false)
    }
    interface  OnWebItemClickListener{
        fun onClick(fileModel: WebItem)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        // val html=getHTML(url)
        //  Log.d("HTML",html)

    }
    override fun onAttach(context: Context) {//疑問点
        super.onAttach(context)

        try {
            mCallback = context as OnWebItemClickListener
        } catch (e: Exception) {
            throw Exception("${context} should implement FilesListFragment.OnItemCLickListener")
        }
    }
    private lateinit var mFilesAdapter: SelectWebRecyclerAdapter
    private fun initViews() {
        webselectRecyclerView.layoutManager = LinearLayoutManager(context)
        mFilesAdapter = SelectWebRecyclerAdapter()
        webselectRecyclerView.adapter = mFilesAdapter
        updateDate()
    }
    fun updateDate() {

        mFilesAdapter.updateData(getWebItemList())
        mFilesAdapter. OnWebItemClickListener = {
            mCallback.onClick(it)
        }

    }

}