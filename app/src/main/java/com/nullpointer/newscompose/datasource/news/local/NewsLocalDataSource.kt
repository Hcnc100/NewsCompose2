package com.nullpointer.newscompose.datasource.news.local

import androidx.paging.PagingSource
import com.nullpointer.newscompose.model.data.NewsData
import com.nullpointer.newscompose.model.entity.NewsEntity
import com.nullpointer.newscompose.model.entity.RemoteKeysNews
import kotlinx.coroutines.flow.Flow

interface NewsLocalDataSource {
    fun getNewsListCache(): Flow<List<NewsData>>
    suspend fun updateAllNews(listNews: List<NewsData>)
    suspend fun addNews(listNews: List<NewsData>)
    suspend fun insertNewsAndKeys(
        listNews: List<NewsEntity>,
        isRefresh: Boolean,
        currentPage: Int
    )

    fun getNewsPageSource(): PagingSource<Int, NewsEntity>
    suspend fun getRemoteKeyByNews(newsInt: String): RemoteKeysNews?
}