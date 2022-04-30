package com.app.albums.features.albumDetails

import com.app.albums.base.models.AlbumPhotoItem
import com.app.albums.base.models.UserAlbumItem
import com.app.albums.base.models.UserItem
import com.app.albums.base.network.Api
import javax.inject.Inject

class AlbumDetailsRepo @Inject constructor(private val api: Api) {

    suspend fun getAlbumPhotos(albumId: Int): List<AlbumPhotoItem> {
        return api.getAlbumPhotos(albumId)
    }
}