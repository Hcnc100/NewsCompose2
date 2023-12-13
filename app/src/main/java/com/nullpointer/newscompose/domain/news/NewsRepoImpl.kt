package com.nullpointer.newscompose.domain.news

import androidx.paging.PagingSource
import com.nullpointer.newscompose.datasource.news.local.NewsLocalDataSource
import com.nullpointer.newscompose.datasource.news.remote.NewsRemoteDataSource
import com.nullpointer.newscompose.model.data.NewsData
import com.nullpointer.newscompose.model.entity.NewsEntity
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

    override fun getNewsPageSource(): PagingSource<Int, NewsEntity> =
        newsLocalDataSource.getNewsPageSource()

    override fun getNewsRemoteMediator(): NewsRemoteMediator {
        return NewsRemoteMediator(
            newsRemoteDataSource = newsRemoteDataSource,
            newsLocalDataSource = newsLocalDataSource
        )
    }
}