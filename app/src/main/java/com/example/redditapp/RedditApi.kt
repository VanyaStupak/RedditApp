package com.example.redditapp

import retrofit2.http.GET

interface RedditApi {

    @GET("/top.json")
    suspend fun getTopFromReddit(): RedditData
}