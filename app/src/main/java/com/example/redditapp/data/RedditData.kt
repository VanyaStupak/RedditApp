package com.example.redditapp.data


data class RedditData(
    val `data`: Data,
    val kind: String
)

data class Children(
    val `data`: DataX,
    val kind: String
)

data class Data(
    val after: String,
    val before: Any,
    val children: List<Children>,
)

data class DataX(
    val author: String,
    val selftext: String,
    val num_comments: Int,
    val url: String,
    val created: Int,
    val thumbnail: String,
    val title: String,
)


