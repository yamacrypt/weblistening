package com.yamacrypt.webaudionovel.ui.library.common

import java.io.File

enum class FileType {
    FILE,
    FOLDER;

    companion object {
        fun getFileType(file: File) = when (file.isDirectory) {
            true -> FOLDER
            false -> FILE
        }
    }
}