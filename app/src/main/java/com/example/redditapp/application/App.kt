package com.example.redditapp.application

import android.app.Application
import com.example.redditapp.data.RedditApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {


    override fun onCreate() {
        super.onCreate()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.reddit.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        redditApi = retrofit.create(RedditApi::class.java)
    }

    companion object {

        private lateinit var redditApi: RedditApi
        fun getApi() = redditApi
    }
}
