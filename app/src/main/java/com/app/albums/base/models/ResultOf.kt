package com.app.albums.base.models

sealed class ResultOf<out T> {
    data class Success<out R>(val data: R): ResultOf<R>()
    data class Failure(
        val message: String?,
        val throwable: Throwable?
    ): ResultOf<Nothing>()
    object Loading: ResultOf<Nothing>()
}