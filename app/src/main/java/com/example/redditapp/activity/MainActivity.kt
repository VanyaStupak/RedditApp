package com.example.redditapp.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.redditapp.application.App
import com.example.redditapp.viewmodel.MainViewModel
import com.example.redditapp.adapter.PostAdapter
import com.example.redditapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val postAdapter = PostAdapter()
    private var isLoading = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.rcView.adapter = postAdapter

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.rcView.layoutManager = GridLayoutManager(this, 1)
        } else {
            binding.rcView.layoutManager = GridLayoutManager(this, 2)
        }

        if (viewModel.postsList != null) {
            postAdapter.addPosts(viewModel.postsList!!)
        }
        viewModel.loadPosts(App.getApi())
        viewModel.posts.observe(this, Observer { posts ->
            postAdapter.addPosts(posts)
            isLoading = false
        })

        binding.rcView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    isLoading = true
                    viewModel.loadPosts(App.getApi())
                }
            }
        })

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.postsList = postAdapter.getPosts()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val posts = viewModel.postsList
        if (posts != null) {
            postAdapter.setPosts(posts)
        }
    }
}