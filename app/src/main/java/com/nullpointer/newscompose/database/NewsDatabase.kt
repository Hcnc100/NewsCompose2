package com.nullpointer.newscompose.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nullpointer.newscompose.model.entity.NewsEntity

@Database(
    entities = [NewsEntity::class],
    exportSchema = false,
    version = 1
)
abstract class NewsDatabase:RoomDatabase() {

    abstract fun getNewsDao():NewsCacheDAO
}