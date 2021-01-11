package com.yamacrypt.webaudionovel.ui.library.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.yamacrypt.webaudionovel.R
import com.yamacrypt.webaudionovel.ui.library.urlfragment.LibraryItemModel
import kotlinx.android.synthetic.main.item_recycler_breadcrumb.view.*

class BreadcrumbRecyclerAdapter : RecyclerView.Adapter<BreadcrumbRecyclerAdapter.ViewHolder>() {

    var onItemClickListener: ((LibraryItemModel) -> Unit)? = null

    var files = listOf<LibraryItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_breadcrumb, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = files.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindView(position)

    fun updateData(libraryItems: List<LibraryItemModel>) {
        this.files = libraryItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onItemClickListener?.invoke(files[adapterPosition])
        }

        fun bindView(position: Int) {
            val file = files[position]
            itemView.nameTextView.text = file.model.novel_name//疑問点
        }
    }
}