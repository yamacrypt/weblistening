package com.yamacrypt.webaudionovel.ui.library.urlfragment


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.yamacrypt.webaudionovel.R
import com.yamacrypt.webaudionovel.ui.library.common.FileType
import kotlinx.android.synthetic.main.item_recycler_file.view.*


class LibraryItemsRecyclerAdapter : RecyclerView.Adapter<LibraryItemsRecyclerAdapter.ViewHolder>() {

    var onItemClickListener: ((LibraryItemModel) -> Unit)? = null
    var onItemLongClickListener: ((LibraryItemModel) -> Unit)? = null

    var filesList = listOf<LibraryItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_file, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = filesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindView(position)

    fun updateData(filesList: List<LibraryItemModel>) {
        this.filesList = filesList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            /* val ls=filesList.map{ it ->it.path }
             PlayList.setup(ls,adapterPosition)*/
            //  PlayList.pathlist.addAll(filesList.map{ it ->it.path })
            //  PlayList.setCurrent_number(adapterPosition)
            onItemClickListener?.invoke(filesList[adapterPosition])
        }

        override fun onLongClick(v: View?): Boolean {
            onItemLongClickListener?.invoke(filesList[adapterPosition])
            return true
        }

        fun bindView(position: Int) {
            val fileModel = filesList[position]

            itemView.nameTextView.text = fileModel.model.novel_name

            if (fileModel.fileType == FileType.FOLDER) {
                itemView.folderTextView.visibility = View.VISIBLE
                //itemView.totalSizeTextView.visibility = View.GONE
                itemView.folderTextView.text = "(${fileModel.subFiles} stories)"
            } else {
                itemView.folderTextView.visibility = View.GONE
                //itemView.totalSizeTextView.visibility = View.VISIBLE
                // itemView.totalSizeTextView.text = "${String.format("%.2f", fileModel.sizeInMB)} mb"
            }
            if(fileModel.model.isNew==1)
                itemView.isNewTextView.visibility=View.VISIBLE//fileModel.model.isNew
            else
                itemView.isNewTextView.visibility=View.INVISIBLE
        }
    }
}