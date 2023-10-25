package com.example.redditapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.redditapp.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor)
            .build()


        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.reddit.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        val redditApi = retrofit.create(RedditApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
                val info = redditApi.getTopFromReddit()
            val posts = info.data.children.map { it.data }
            Log.d("MyLog", info.toString())
            withContext(Dispatchers.Main){
                binding.author.text = posts[24].author
                binding.title.text = posts[24].title
                binding.coments.text = posts[24].num_comments.toString()
                Glide.with(this@MainActivity)
                    .load(posts[24].thumbnail)
                    .into(binding.image)

                val currentTimestamp = System.currentTimeMillis() / 1000
                val createdUtc = posts[24].created

                val timeDifference = currentTimestamp - createdUtc

                when {
                    timeDifference < 60 -> {
                        val timeAgo = timeDifference
                       binding.date.text = "$timeAgo seconds ago"
                    }
                    timeDifference < 3600 -> {
                        val timeAgo = timeDifference / 60
                        binding.date.text = "$timeAgo minutes ago"
                    }
                    timeDifference < 86400 -> {
                        val timeAgo = timeDifference / 3600
                        binding.date.text = "$timeAgo hours ago"
                    }
                    else -> {
                        val timeAgo = timeDifference / 86400
                        binding.date.text = "$timeAgo years ago"
                    }
                }
            }
        }

    }
}