package com.nullpointer.newscompose.datasource.news.remote

import com.nullpointer.newscompose.model.data.NewsData

interface NewsRemoteDataSource {

    suspend fun getListNews(page: Int): List<NewsData>
}