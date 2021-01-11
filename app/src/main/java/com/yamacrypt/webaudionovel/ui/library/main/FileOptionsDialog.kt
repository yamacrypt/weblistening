package com.yamacrypt.webaudionovel.ui.library.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yamacrypt.webaudionovel.R

import kotlinx.android.synthetic.main.dialog_file_options.*
import kotlinx.android.synthetic.main.item_recycler_file.*

class FileOptionsDialog : BottomSheetDialogFragment() {

    var onDeleteClickListener: (() -> Unit)? = null

    companion object {
        fun build(block: Builder.() -> Unit): FileOptionsDialog = Builder().apply(block).build()
    }

    class Builder {
        fun build(): FileOptionsDialog {
            val fragment = FileOptionsDialog()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_file_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        deleteTextView.setOnClickListener {
            onDeleteClickListener?.invoke()
            dismiss()
        }
    }

}