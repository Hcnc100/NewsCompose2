package com.nullpointer.newscompose.domain.news

import com.nullpointer.newscompose.datasource.news.local.NewsLocalDataSource
import com.nullpointer.newscompose.datasource.news.remote.NewsRemoteDataSource
import com.nullpointer.newscompose.model.data.NewsData
import kotlinx.coroutines.flow.Flow

class NewsRepoImpl(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsLocalDataSource: NewsLocalDataSource
) : NewsRepository {

    private var currentPage = 1
    override suspend fun requestNewNews(): Int {
        currentPage = 1
        val newListNews = newsRemoteDataSource.getListNews(currentPage)
        newsLocalDataSource.updateAllNews(newListNews)
        return newListNews.size
    }

    override suspend fun concatenateNews(): Int {
        currentPage++
        val listNews = newsRemoteDataSource.getListNews(currentPage)
        newsLocalDataSource.addNews(listNews)
        return listNews.size
    }

    override fun getListNewsCache(): Flow<List<NewsData>> =
        newsLocalDataSource.getNewsListCache()
}