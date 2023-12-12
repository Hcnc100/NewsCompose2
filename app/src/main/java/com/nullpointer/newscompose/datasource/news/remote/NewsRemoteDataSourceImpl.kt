package com.nullpointer.newscompose.datasource.news.remote

import com.nullpointer.newscompose.BuildConfig
import com.nullpointer.newscompose.data.news.remote.NewsApiServices
import com.nullpointer.newscompose.model.data.NewsData
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class NewsRemoteDataSourceImpl(
    private val newsApiServices: NewsApiServices
) : NewsRemoteDataSource {

    private val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply{
        timeZone = TimeZone.getTimeZone("UTC")
    }
    override suspend fun getListNews(page: Int): List<NewsData> {
        val response = newsApiServices.getListNews(
            apiKey = BuildConfig.NEWS_API_KEY,
            country = "us",
            page = page,
            pageSize = 5
        )
        return response.articles?.map {
            NewsData.fromArticleResponse(it, format)
        }?.filter { it.url != "https://removed.com" } ?: emptyList()
    }
}