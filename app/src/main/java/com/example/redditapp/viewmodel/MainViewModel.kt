package com.example.redditapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redditapp.data.Children
import com.example.redditapp.data.RedditApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _posts = MutableLiveData<List<Children>>()
    val posts: LiveData<List<Children>> get() = _posts
    private var after: String? = null
    private var limit: Int = 15
    private var count: Int = 0
    var postsList: List<Children>? = null

    fun loadPosts(redditApi: RedditApi) {
        viewModelScope.launch(Dispatchers.IO) {
            val info = redditApi.getTopFromReddit(after, limit, count)
            after = info.data.after
            count += limit
            _posts.postValue(info.data.children)
        }
    }
}