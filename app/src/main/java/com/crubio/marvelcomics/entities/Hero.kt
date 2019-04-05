package com.crubio.marvelcomics.entities

class Hero(val id: Int, val name: String?, val description: String?, val thumbnail: Thumbnail?) {
    fun getThumbnailUrl(): String? {
        return thumbnail?.path + "." + thumbnail?.extension
    }
}