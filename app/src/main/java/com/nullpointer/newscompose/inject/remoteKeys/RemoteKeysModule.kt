package com.nullpointer.newscompose.inject.remoteKeys

import com.nullpointer.newscompose.database.NewsDatabase
import com.nullpointer.newscompose.database.RemoteKeysDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RemoteKeysModule {


    @Singleton
    @Provides
    fun provideRemoteKeysDao(
        newsDatabase: NewsDatabase
    ): RemoteKeysDAO = newsDatabase.getRemoteKeyNewsDao()
}