package com.github.artfable.gradle.import.check

import java.io.File

class FileFinder(val rootPath: String) {
    private val files = mutableSetOf<File>()
    private val root = File(rootPath)

    init {
        if (!root.exists()) {
            throw IllegalArgumentException("$rootPath doesn't exist")
        }
    }

    private fun parse(file: File) {
        if (file.isDirectory) {
            file.listFiles()?.forEach(::parse)
        } else if (file.extension == "java") {
            files.add(file)
        }
    }

    fun findFiles(): Array<File> {
        files.clear()
        parse(root)
        return files.toTypedArray()
    }
}