package com.nullpointer.newscompose.data.news.remote

import com.nullpointer.newscompose.model.api.news.NewsListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiServices {

    @GET("top-headlines")
    suspend fun getListNews(
        @Query("apiKey") apiKey:String,
        @Query("country") country:String
    ): NewsListResponse
}