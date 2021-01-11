package com.yamacrypt.webaudionovel.ui.search.selector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yamacrypt.webaudionovel.R
import com.yamacrypt.webaudionovel.ui.library.urlfragment.LibraryItemModel

import kotlinx.android.synthetic.main.webselect_item.view.*

class SelectWebRecyclerAdapter() : RecyclerView.Adapter<SelectWebRecyclerAdapter .ViewHolder>() {
    var OnWebItemClickListener: ((WebItem) -> Unit)? = null
    var onItemLongClickListener: ((LibraryItemModel) -> Unit)? = null

    var filesList = listOf<WebItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.webselect_item, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount() = filesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindView(position)

    fun updateData(filesList: List<WebItem>) {
        this.filesList = filesList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
          /*  val navController =
                Navigation.findNavController(activity,
                    R.id.nav_menu_fragment
                )
            navController.navigate(R.id.action_navigation_dashboard_to_navigation_web,
                WebFragment.build {
                    //filesList[adapterPosition]
                })*/
           // val ls=filesList.map{ it ->it.path }
            //PlayList.setup(ls,adapterPosition)
            //  PlayList.pathlist.addAll(filesList.map{ it ->it.path })
            OnWebItemClickListener?.invoke(filesList[adapterPosition])
        }

        override fun onLongClick(v: View?): Boolean {
            //onItemLongClickListener?.invoke(filesList[adapterPosition])
            return true
        }

        fun bindView(position: Int) {
            val fileModel = filesList[position]
            itemView.webTextView.text = fileModel.title

        }
    }
}