package com.nullpointer.newscompose.datasource.news.local

import com.nullpointer.newscompose.model.data.NewsData
import kotlinx.coroutines.flow.Flow

interface NewsLocalDataSource {

    fun getNewsListCache():Flow<List<NewsData>>

    suspend fun updateAllNews(listNews: List<NewsData>)
    suspend fun addNews(listNews: List<NewsData>)
}