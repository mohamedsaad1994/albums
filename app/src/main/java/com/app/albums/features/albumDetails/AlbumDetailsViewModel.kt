package com.app.albums.features.albumDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.albums.base.models.AlbumPhotoItem
import com.app.albums.base.models.ResultOf
import com.bumptech.glide.load.HttpException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AlbumDetailsViewModel @Inject constructor(private val repository: AlbumDetailsRepo) :
    ViewModel() {

    private var _albumPhotosResponse: MutableLiveData<ResultOf<List<AlbumPhotoItem>>> =
        MutableLiveData()
    val albumPhotosResponse: MutableLiveData<ResultOf<List<AlbumPhotoItem>>>
        get() = _albumPhotosResponse

    fun getAlbumPhotos(albumId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _albumPhotosResponse.postValue(ResultOf.Loading)
            try {
                val response = repository.getAlbumPhotos(albumId)
                _albumPhotosResponse.postValue(ResultOf.Success(response))
            } catch (ioe: IOException) {
                _albumPhotosResponse.postValue(ResultOf.Failure("[IO] error please retry", ioe))
            } catch (he: HttpException) {
                _albumPhotosResponse.postValue(ResultOf.Failure("[HTTP] error please retry", he))
            }
        }
    }
}