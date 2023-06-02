package com.example.shoppinglisttestingexample.data.remote

import com.example.shoppinglisttestingexample.BuildConfig
import com.example.shoppinglisttestingexample.data.remote.responses.UnsplashResponse
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnsplashApi {

    companion object {
        const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_API_KEY
    }

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    suspend fun searchForImage(
        @Query("query") query: String
    ): Response<UnsplashResponse>
}