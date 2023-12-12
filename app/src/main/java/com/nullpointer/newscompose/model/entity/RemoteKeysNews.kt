package com.nullpointer.newscompose.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys_news")
data class RemoteKeysNews(
    @PrimaryKey(autoGenerate = false)
    val newsId: String,
    val nextPage: Int?,
    val prevPage: Int?,
    val currentPage: Int
)
