package com.nullpointer.newscompose.inject.database

import android.content.Context
import androidx.room.Room
import com.nullpointer.newscompose.database.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    const val NAME_DATABASE="NAME_DATABASE"
    const val NEWS_DATABASE="News_database"


    @Provides
    @Singleton
    @Named(NAME_DATABASE)
    fun provideNameDatabase():String = NEWS_DATABASE


    @Singleton
    @Provides
    fun provideNewsDatabase(
        @Named(NAME_DATABASE) nameDatabase:String,
        @ApplicationContext context: Context
    ):NewsDatabase = Room.databaseBuilder(context, NewsDatabase::class.java,nameDatabase)
        .fallbackToDestructiveMigration()
        .build()
}