package com.yamacrypt.webaudionovel.ui.library.main


import com.yamacrypt.webaudionovel.ui.library.urlfragment.LibraryItemModel


class BackStackManager {
    private var files = mutableListOf<LibraryItemModel>()
    var onStackChangeListener: ((List<LibraryItemModel>) -> Unit)? = null

    val top: LibraryItemModel
        get() = files[files.size - 1]

    fun addToStack(libraryItemModel: LibraryItemModel) {
        files.add(libraryItemModel)
        onStackChangeListener?.invoke(files)
    }

    fun popFromStack() {
        if (files.isNotEmpty())
            files.removeAt(files.size - 1)
        onStackChangeListener?.invoke(files)
    }

    fun popFromStackTill(libraryItemModel: LibraryItemModel) {
        files = files.subList(0, files.indexOf(libraryItemModel) + 1)
        onStackChangeListener?.invoke(files)
    }
}