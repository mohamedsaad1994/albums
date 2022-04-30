package com.app.albums.features.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.albums.base.models.ResultOf
import com.app.albums.base.models.UserAlbumItem
import com.app.albums.base.models.UserItem
import com.bumptech.glide.load.HttpException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: ProfileRepo) :
    ViewModel() {
    private var _usersResponse: MutableLiveData<ResultOf<List<UserItem>>> = MutableLiveData()
    val usersResponse: MutableLiveData<ResultOf<List<UserItem>>>
        get() = _usersResponse

    fun getAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            _usersResponse.postValue(ResultOf.Loading)
            try {
                val response = repository.getAllUsers()
                _usersResponse.postValue(ResultOf.Success(response))
            } catch (ioe: IOException) {
                _usersResponse.postValue(ResultOf.Failure("[IO] error please retry", ioe))
            } catch (he: HttpException) {
                _usersResponse.postValue(ResultOf.Failure("[HTTP] error please retry", he))
            } catch (ex: Exception) {
                _usersResponse.postValue(
                    ResultOf.Failure(
                        "[exception] unknown error please retry",
                        ex
                    )
                )
            }
        }
    }

    private var _userAlbumsResponse: MutableLiveData<ResultOf<List<UserAlbumItem>>> =
        MutableLiveData()
    val userAlbumsResponse: MutableLiveData<ResultOf<List<UserAlbumItem>>>
        get() = _userAlbumsResponse

    fun getUserAlbums(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _usersResponse.postValue(ResultOf.Loading)
            try {
                val response = repository.getUserAlbums(userId)
                _userAlbumsResponse.postValue(ResultOf.Success(response))
            } catch (ioe: IOException) {
                _userAlbumsResponse.postValue(ResultOf.Failure("[IO] error please retry", ioe))
            } catch (he: HttpException) {
                _userAlbumsResponse.postValue(ResultOf.Failure("[HTTP] error please retry", he))
            }
        }
    }
}