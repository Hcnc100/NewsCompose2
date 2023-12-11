package com.nullpointer.newscompose.inject.news

import com.nullpointer.newscompose.data.news.remote.NewsApiServices
import com.nullpointer.newscompose.database.NewsCacheDAO
import com.nullpointer.newscompose.database.NewsDatabase
import com.nullpointer.newscompose.datasource.news.local.NewsLocalDataSource
import com.nullpointer.newscompose.datasource.news.local.NewsLocalDataSourceImpl
import com.nullpointer.newscompose.datasource.news.remote.NewsRemoteDataSource
import com.nullpointer.newscompose.datasource.news.remote.NewsRemoteDataSourceImpl
import com.nullpointer.newscompose.domain.news.NewsRepoImpl
import com.nullpointer.newscompose.domain.news.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NewsModule {

    @Singleton
    @Provides
    fun provideNewsApiServices(
        retrofit:Retrofit
    ): NewsApiServices= retrofit.create(NewsApiServices::class.java)


    @Provides
    @Singleton
    fun provideNewsCacheDAO(
        newsDatabase: NewsDatabase
    ):NewsCacheDAO= newsDatabase.getNewsDao()

    @Provides
    @Singleton
    fun provideNewsRemoteDataSource(
        newsApiServices: NewsApiServices
    ):NewsRemoteDataSource= NewsRemoteDataSourceImpl(newsApiServices = newsApiServices)

    @Provides
    @Singleton
    fun provideNewsLocalDataSource(
        newsCacheDAO: NewsCacheDAO
    ):NewsLocalDataSource=NewsLocalDataSourceImpl(newsCacheDAO)

    @Provides
    @Singleton
    fun provideNewsRepository(
        newsLocalDataSource: NewsLocalDataSource,
        newsRemoteDataSource: NewsRemoteDataSource,
    ):NewsRepository= NewsRepoImpl(
        newsLocalDataSource =  newsLocalDataSource,
        newsRemoteDataSource = newsRemoteDataSource
    )

}