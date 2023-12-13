package com.nullpointer.newscompose.domain.news

import androidx.paging.PagingSource
import com.nullpointer.newscompose.model.data.NewsData
import com.nullpointer.newscompose.model.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun requestNewNews(): Int

    suspend fun concatenateNews(): Int

    fun getListNewsCache(): Flow<List<NewsData>>

    fun getNewsPageSource(): PagingSource<Int, NewsEntity>

    fun getNewsRemoteMediator(): NewsRemoteMediator
}