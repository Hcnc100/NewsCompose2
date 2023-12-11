package com.nullpointer.newscompose.domain.news

import com.nullpointer.newscompose.model.data.NewsData
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun requestNewNews()

    fun getListNewsCache():Flow<List<NewsData>>
}