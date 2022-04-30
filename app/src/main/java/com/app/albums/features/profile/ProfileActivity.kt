package com.app.albums.features.profile

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.albums.R
import com.app.albums.base.models.ResultOf
import com.app.albums.base.models.UserAlbumItem
import com.app.albums.base.models.UserItem
import com.app.albums.databinding.ActivityProfileBinding
import com.app.albums.utilities.UtilsFunctions
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeData()
        performLogic()
    }

    private fun performLogic() {
        if (UtilsFunctions.isNetworkAvailable(this)) {
            viewModel.getAllUsers()
        } else
            UtilsFunctions.showToast(this, getString(R.string.no_internet_connection))
    }

    private fun observeData() {
        viewModel.usersResponse.observe(this) {
            when (it) {
                is ResultOf.Loading -> {
                    showProgress()
                }
                is ResultOf.Success -> {
                    val users = it.data
                    val randomIndex = Random.nextInt(users.size)
                    setUserData(users[randomIndex])
                    getUserAlbums(users[randomIndex].id)
                    hideProgress()
                }
                is ResultOf.Failure -> {
                    UtilsFunctions.showToast(this, it.message.toString())
                    hideProgress()
                }
            }
        }
        viewModel.userAlbumsResponse.observe(this) {
            when (it) {
                is ResultOf.Loading -> {
                    showProgress()
                }
                is ResultOf.Success -> {
                    setupAlbumsRecyclerView(it.data)
                    hideProgress()
                }
                is ResultOf.Failure -> {
                    UtilsFunctions.showToast(this, it.message.toString())
                    hideProgress()
                }
            }
        }
    }

    private fun setUserData(userItem: UserItem) {
        binding.tvUserName.text = userItem.name
        binding.tvAddress.text = userItem.address.city.plus(",").plus(userItem.address.street)
    }

    private fun setupAlbumsRecyclerView(data: List<UserAlbumItem>) {
        binding.rvAlbums.adapter = AlbumsAdapter(data)
    }

    private fun getUserAlbums(userId: Int) {
        if (UtilsFunctions.isNetworkAvailable(this)) {
            viewModel.getUserAlbums(userId)
        } else
            UtilsFunctions.showToast(this, getString(R.string.no_internet_connection))
    }

    private fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }
}