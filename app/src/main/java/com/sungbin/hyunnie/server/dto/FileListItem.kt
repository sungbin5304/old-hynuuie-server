package com.sungbin.hyunnie.server.dto

class FileListItem {
    var isFile: Boolean? = null
    var name: String? = null
    var count: String? = null

    constructor() {}
    constructor(isFile: Boolean?, name: String?, count: String?) {
        this.isFile = isFile
        this.name = name
        this.count = count
    }
}