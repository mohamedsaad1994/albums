package com.app.albums.base.network

import com.app.albums.base.models.ResultOf
import com.app.albums.base.models.AlbumPhotoItem
import com.app.albums.base.models.UserAlbumItem
import com.app.albums.base.models.UserItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("users")
    suspend fun getAllUsers():List<UserItem>

    @GET("albums")
    suspend fun getUserAlbums(
        @Query("userId") userId: Int
    ): List<UserAlbumItem>

    @GET("photos")
    suspend fun getAlbumPhotos(
        @Query("albumId") albumId: Int
    ): List<AlbumPhotoItem>
}