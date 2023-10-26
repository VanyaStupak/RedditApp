package com.example.redditapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _posts = MutableLiveData<List<Children>>()
    val posts: LiveData<List<Children>> get() = _posts
    private var after: String? = null
    private var limit: Int = 10
    private var count: Int = 0
    fun loadPosts(redditApi: RedditApi) {
        viewModelScope.launch(Dispatchers.IO) {
            val info = redditApi.getTopFromReddit(after, limit, count)
            Log.d("MyLog", info.toString())
            after = info.data.after
            count += limit
            _posts.postValue(info.data.children)
        }
    }
}
