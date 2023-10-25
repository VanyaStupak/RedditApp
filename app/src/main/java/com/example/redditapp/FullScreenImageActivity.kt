package com.example.redditapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.redditapp.databinding.ActivityFullScreenImageBinding


class FullScreenImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullScreenImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.fullscreenToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false);
        val imgUrl = intent.getStringExtra("imgUrl")

        Glide.with(this)
            .load(imgUrl)
            .into(binding.imageView)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }


}