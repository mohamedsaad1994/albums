package com.app.albums.features.fullScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.albums.R
import com.app.albums.databinding.ActivityFullScreenBinding
import com.bumptech.glide.Glide

class FullScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullScreenBinding
    private lateinit var imageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialization()
        actions()
    }

    private fun initialization() {
        imageUrl = intent.getStringExtra("url").toString()
        Glide.with(this).load(intent.getStringExtra("url")).placeholder(R.drawable.loader)
            .into(binding.ivFullScreen)
    }

    private fun actions() {
        binding.ivBack.setOnClickListener { finish() }
        binding.ivShare.setOnClickListener {
            shareIntent()
        }
    }

    private fun shareIntent() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.hey_see_my_photo_through) + imageUrl
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }
}