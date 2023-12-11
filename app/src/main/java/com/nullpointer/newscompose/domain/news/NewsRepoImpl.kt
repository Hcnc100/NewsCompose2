package com.nullpointer.newscompose.domain.news

import com.nullpointer.newscompose.datasource.news.local.NewsLocalDataSource
import com.nullpointer.newscompose.datasource.news.remote.NewsRemoteDataSource
import com.nullpointer.newscompose.model.data.NewsData
import kotlinx.coroutines.flow.Flow

class NewsRepoImpl(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsLocalDataSource: NewsLocalDataSource
) : NewsRepository {
    override suspend fun requestNewNews() {
        val newListNews= newsRemoteDataSource.getListNews()
        newsLocalDataSource.updateAllNews(newListNews)
    }

    override fun getListNewsCache(): Flow<List<NewsData>> =
        newsLocalDataSource.getNewsListCache()
}