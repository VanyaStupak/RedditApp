package com.example.redditapp

import retrofit2.http.GET
import retrofit2.http.Query

interface RedditApi {

    @GET("top.json")
    suspend fun getTopFromReddit(
        @Query("after") after: String?,
        @Query("limit") limit: Int,
        @Query("count") count: Int
    ): RedditData
}