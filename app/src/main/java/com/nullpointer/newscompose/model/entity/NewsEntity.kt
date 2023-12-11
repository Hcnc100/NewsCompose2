package com.nullpointer.newscompose.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nullpointer.newscompose.model.data.NewsData

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey(autoGenerate = false)
    val newsId:String,
    val title:String,
    val description:String,
    val dateCreate:Long,
    val imageUrl:String?,
    val url:String
){
    companion object{
        fun fromNewData(newsData: NewsData):NewsEntity{
            return NewsEntity(
                newsId = newsData.newsId,
                title = newsData.title,
                url = newsData.url,
                description =  newsData.description,
                imageUrl =  newsData.imageUrl,
                dateCreate =  newsData.dateTime
            )
        }
    }
}
