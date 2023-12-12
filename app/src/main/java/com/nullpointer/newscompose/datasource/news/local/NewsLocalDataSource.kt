package com.nullpointer.newscompose.datasource.news.local

import androidx.paging.PagingSource
import com.nullpointer.newscompose.model.data.NewsData
import com.nullpointer.newscompose.model.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

interface NewsLocalDataSource {
    fun getNewsListCache(): Flow<List<NewsData>>
    suspend fun updateAllNews(listNews: List<NewsData>)
    suspend fun addNews(listNews: List<NewsData>)
    suspend fun insertNewsAndKeys(listNews: List<NewsData>)
    fun getNewsPageSource(): PagingSource<Int, NewsEntity>
}