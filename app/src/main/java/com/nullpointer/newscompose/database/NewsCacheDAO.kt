package com.nullpointer.newscompose.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.nullpointer.newscompose.model.entity.NewsEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface NewsCacheDAO {

    @Query("SELECT * FROM news ORDER BY dateCreate DESC")
    fun getNewsListFlow():Flow<List<NewsEntity>>

    @Query("DELETE FROM news")
    suspend fun deleterAllNews()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllNews(newsList:List<NewsEntity>)

    @Transaction
    suspend fun updateAllNews(newsList:List<NewsEntity>){
        deleterAllNews()
        addAllNews(newsList)
    }
}