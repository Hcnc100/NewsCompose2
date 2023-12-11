package com.nullpointer.newscompose.datasource.news.local

import com.nullpointer.newscompose.database.NewsCacheDAO
import com.nullpointer.newscompose.model.data.NewsData
import com.nullpointer.newscompose.model.entity.NewsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NewsLocalDataSourceImpl(
    private val newsCacheDAO: NewsCacheDAO
) : NewsLocalDataSource {
    override fun getNewsListCache(): Flow<List<NewsData>> =
        newsCacheDAO.getNewsListFlow().map { listNews ->
            listNews.map(NewsData::fromNewsEntity)
        }

    override suspend fun updateAllNews(listNews: List<NewsData>){
        val listNewsEntity= listNews.map(NewsEntity::fromNewData)
        newsCacheDAO.updateAllNews(listNewsEntity)
    }
}