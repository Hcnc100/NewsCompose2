package com.nullpointer.newscompose.datasource.news.local

import androidx.paging.PagingSource
import com.nullpointer.newscompose.database.NewsCacheDAO
import com.nullpointer.newscompose.database.RemoteKeysDAO
import com.nullpointer.newscompose.model.data.NewsData
import com.nullpointer.newscompose.model.entity.NewsEntity
import com.nullpointer.newscompose.model.entity.RemoteKeysNews
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NewsLocalDataSourceImpl(
    private val newsCacheDAO: NewsCacheDAO,
    private val remoteKeysDAO: RemoteKeysDAO,
) : NewsLocalDataSource {
    override fun getNewsListCache(): Flow<List<NewsData>> =
        newsCacheDAO.getNewsListFlow().map { listNews ->
            listNews.map(NewsData::fromNewsEntity)
        }

    override suspend fun updateAllNews(listNews: List<NewsData>) {
        val listNewsEntity = listNews.map(NewsEntity::fromNewData)
        newsCacheDAO.updateAllNews(listNewsEntity)
    }

    override suspend fun addNews(listNews: List<NewsData>) {
        val listNewsEntity = listNews.map(NewsEntity::fromNewData)
        newsCacheDAO.addAllNews(listNewsEntity)
    }

    override suspend fun insertNewsAndKeys(
        listNews: List<NewsEntity>,
        isRefresh: Boolean,
        currentPage: Int
    ) {
        val prevKey = if (currentPage > 1) currentPage - 1 else null
        val nextPage = if (listNews.isEmpty()) null else currentPage + 1


        val newsRemoteKeysList = listNews.map {
            RemoteKeysNews(
                newsId = it.newsId,
                prevPage = prevKey,
                nextPage = nextPage,
                currentPage = currentPage,
            )
        }

        if (isRefresh) {
            newsCacheDAO.deleterAllNews()
            remoteKeysDAO.deleterAllKeys()
        }

        newsCacheDAO.addAllNews(listNews)
        remoteKeysDAO.addAllKeys(newsRemoteKeysList)
    }

    override fun getNewsPageSource(): PagingSource<Int, NewsEntity> =
        newsCacheDAO.getNewsPageSource()

    override suspend fun getRemoteKeyByNews(newsInt: String): RemoteKeysNews =
        remoteKeysDAO.getRemoteKeyByNews(newsId = newsInt)

}