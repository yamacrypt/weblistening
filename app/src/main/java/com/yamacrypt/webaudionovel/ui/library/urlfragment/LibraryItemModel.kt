package com.yamacrypt.webaudionovel.ui.library.urlfragment

import com.yamacrypt.webaudionovel.Database.StoryIndexModel
import com.yamacrypt.webaudionovel.ui.library.common.FileType

data class LibraryItemModel(
    val fileType: FileType,
    val subFiles: Int = 0,
    val position:Int=0,
    val model: StoryIndexModel,
   // val parent_url:String
)