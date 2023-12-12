package com.nullpointer.newscompose.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nullpointer.newscompose.model.entity.NewsEntity
import com.nullpointer.newscompose.model.entity.RemoteKeysNews

@Database(
    entities = [NewsEntity::class, RemoteKeysNews::class],
    exportSchema = false,
    version = 2
)
abstract class NewsDatabase:RoomDatabase() {

    abstract fun getNewsDao(): NewsCacheDAO
    abstract fun getRemoteKeyNewsDao(): RemoteKeysDAO
}