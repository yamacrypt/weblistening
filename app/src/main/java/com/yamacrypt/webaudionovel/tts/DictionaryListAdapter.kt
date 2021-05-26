package com.yamacrypt.webaudionovel.tts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.yamacrypt.webaudionovel.Database.DictionaryPairModel
import com.yamacrypt.webaudionovel.R
import com.yamacrypt.webaudionovel.ui.library.urlfragment.LibraryItemModel
import com.yamacrypt.webaudionovel.ui.library.urlfragment.LibraryItemsRecyclerAdapter
import kotlinx.android.synthetic.main.dictionary_item.view.*

class DictionaryListAdapter: RecyclerView.Adapter<DictionaryListAdapter.ViewHolder>() {
    var itemList= mutableListOf<DictionaryPairModel>();
    var onDeleteClickListener: ((DictionaryPairModel) -> Unit)? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.dictionary_item, parent, false)
        return ViewHolder(view)
    }
    fun updateData(filesList: List<DictionaryPairModel>) {
        this.itemList = filesList as MutableList<DictionaryPairModel>
        notifyDataSetChanged()
    }
    fun delete(item :DictionaryPairModel){
        itemList.remove(item)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {
        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {

        }

        override fun onLongClick(v: View?): Boolean {
         return true
        }

        fun bindView(position: Int) {
            val item=itemList[position]
            itemView.targetText.text=item.target
            itemView.readText.text=item.read
            itemView.genericText.isVisible=(item.url=="root")
            itemView.delete_dictionary_item.setOnClickListener{
                onDeleteClickListener?.invoke(item)
            }
        }
    }

    override fun onBindViewHolder(holder: DictionaryListAdapter.ViewHolder, position: Int) = holder.bindView(position)

    override fun getItemCount(): Int {
        return itemList.size
    }
}