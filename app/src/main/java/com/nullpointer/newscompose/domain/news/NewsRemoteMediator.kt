package com.nullpointer.newscompose.domain.news

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.APPEND
import androidx.paging.LoadType.PREPEND
import androidx.paging.LoadType.REFRESH
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.nullpointer.newscompose.datasource.news.local.NewsLocalDataSource
import com.nullpointer.newscompose.datasource.news.remote.NewsRemoteDataSource
import com.nullpointer.newscompose.model.entity.NewsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val newsLocalDataSource: NewsLocalDataSource,
    private val newsRemoteDataSource: NewsRemoteDataSource,
) : RemoteMediator<Int, NewsEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>
    ): MediatorResult = withContext(Dispatchers.IO) {
        val page = when (loadType) {
            REFRESH -> {
                state.anchorPosition?.let { anchor ->
                    state.closestItemToPosition(anchor)?.let {
                        newsLocalDataSource.getRemoteKeyByNews(it.newsId)
                    }
                }?.nextPage?.minus(1) ?: 1
            }

            PREPEND -> {
                state.pages.firstOrNull()?.data?.firstOrNull()?.let {
                    newsLocalDataSource.getRemoteKeyByNews(it.newsId)
                }?.prevPage
            }

            APPEND -> {
                state.pages.lastOrNull()?.data?.lastOrNull()?.let {
                    newsLocalDataSource.getRemoteKeyByNews(it.newsId)
                }?.nextPage

            }
        } ?: return@withContext MediatorResult.Success(endOfPaginationReached = true)

        return@withContext try {
            val newsList = newsRemoteDataSource.getListNews(page)
            val refresh = loadType == REFRESH
            val remoteKeysNews = newsList.map(NewsEntity::fromNewData)
            val endOfPaginationReached = newsList.isEmpty()
            newsLocalDataSource.insertNewsAndKeys(
                currentPage = page,
                isRefresh = refresh,
                listNews = remoteKeysNews
            )
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

}