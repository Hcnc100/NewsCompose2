package com.nullpointer.newscompose.datasource.news.remote

import com.nullpointer.newscompose.BuildConfig
import com.nullpointer.newscompose.data.news.remote.NewsApiServices
import com.nullpointer.newscompose.model.data.NewsData

class NewsRemoteDataSourceImpl(
    private val newsApiServices: NewsApiServices
) : NewsRemoteDataSource {
    override suspend fun getListNews(): List<NewsData> {
        val response = newsApiServices.getListNews(
            apiKey = BuildConfig.NEWS_API_KEY,
            country = "us"
        )
        return response.articles?.map(NewsData::fromArticleResponse)?.toSet()?.toList()
            ?: emptyList()
    }
}