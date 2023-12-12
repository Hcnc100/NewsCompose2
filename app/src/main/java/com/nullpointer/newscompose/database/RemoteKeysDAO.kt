package com.nullpointer.newscompose.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.nullpointer.newscompose.model.entity.RemoteKeysNews

@Dao
interface RemoteKeysDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKeys(keysList: List<RemoteKeysNews>)

    @Query("DELETE from remote_keys_news")
    suspend fun deleterAllKeys()

    @Query("SELECT * from remote_keys_news WHERE newsId = (:newsId)")
    suspend fun getRemoteKeyByNews(newsId: Int): RemoteKeysNews

    @Transaction
    suspend fun updateAllKeys(keysList: List<RemoteKeysNews>) {
        deleterAllKeys()
        insertAllKeys(keysList)
    }
}