package com.app.albums.features.albumDetails

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.app.albums.R
import com.app.albums.base.models.AlbumPhotoItem
import com.app.albums.base.models.ResultOf
import com.app.albums.databinding.ActivityAlbumDetailsBinding
import com.app.albums.utilities.UtilsFunctions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlbumDetailsBinding
    private val viewModel: AlbumDetailsViewModel by viewModels()
    private lateinit var photosAdapter: PhotosAdapter
    private val allPhotos = ArrayList<AlbumPhotoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        observeData()
        performLogic()
        actions()
    }

    private fun actions() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(mEdit: Editable) {
                val query = mEdit.toString()
                setNewList(query)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setNewList(query: String) {
        val newList = ArrayList<AlbumPhotoItem>()
        for (item in allPhotos) {
            if (query == item.title || item.title.contains(query)) {
                newList.add(item)
            }
        }

        addDataToRecyclerView(newList)
    }

    private fun initView() {
        binding.tvToolbarTitle.text = intent.getStringExtra("title")
        setupPhotosRecyclerView()
    }

    private fun performLogic() {
        if (UtilsFunctions.isNetworkAvailable(this)) {
            viewModel.getAlbumPhotos(intent.getIntExtra("id", 0))
        } else
            UtilsFunctions.showToast(this, getString(R.string.no_internet_connection))
    }

    private fun observeData() {
        viewModel.albumPhotosResponse.observe(this) {
            when (it) {
                is ResultOf.Loading -> {
                    showProgress()
                }
                is ResultOf.Success -> {
                    allPhotos.addAll(it.data)
                    addDataToRecyclerView(it.data)
                    hideProgress()
                }
                is ResultOf.Failure -> {
                    UtilsFunctions.showToast(this, it.message.toString())
                    hideProgress()
                }
            }
        }
    }

    private fun addDataToRecyclerView(data: List<AlbumPhotoItem>) {
        photosAdapter.addData(data)
    }

    private fun setupPhotosRecyclerView() {
        photosAdapter = PhotosAdapter()
        binding.rvAlbumPhotos.adapter = photosAdapter
        binding.rvAlbumPhotos.layoutManager = GridLayoutManager(this, 3)
    }

    private fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }

}