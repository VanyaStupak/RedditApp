package com.example.redditapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.redditapp.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val postAdapter = PostAdapter()
    private var isLoading = false
    private var isActionBarHidden = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val toolbar = binding.materialToolbar
//        setSupportActionBar(toolbar)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.reddit.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        val redditApi = retrofit.create(RedditApi::class.java)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]


        binding.rcView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = postAdapter
        }

        viewModel.loadPosts(redditApi)
        viewModel.posts.observe(this, Observer { posts ->
            postAdapter.addPosts(posts)
            isLoading = false
        })

        binding.rcView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

//                if (dy > 0) {
//                    binding.materialToolbar.animate().translationY(-binding.materialToolbar.height.toFloat()).setInterpolator(
//                        AccelerateInterpolator()
//                    ).start()
//                    binding.materialToolbar.visibility = View.GONE
//                } else {
//                    binding.materialToolbar.animate().translationY(0f).setInterpolator(
//                        DecelerateInterpolator()
//                    ).start()
//                    binding.materialToolbar.visibility = View.VISIBLE
//                }
//
//                if (dy > 0) {
//                    // Hide the toolbar if it is not already hidden.
//                    if (!isActionBarHidden) {
//                        isActionBarHidden = true
//                        binding.materialToolbar.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down))
//                    }
//                } else {
//                    // Show the toolbar if it is hidden.
//                    if (isActionBarHidden) {
//                        isActionBarHidden = false
//                        binding.materialToolbar.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.slide_up))
//                    }
//                }


                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    isLoading = true
                    viewModel.loadPosts(redditApi)
                }
            }
        })


    }
}