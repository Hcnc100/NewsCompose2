package com.nullpointer.newscompose.model.data

import com.nullpointer.newscompose.model.api.news.Article
import com.nullpointer.newscompose.model.entity.NewsEntity
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.UUID

data class NewsData(
    val newsId:String,
    val dateTime:Long,
    val title:String,
    val description:String,
    val imageUrl:String?,
    val url:String,
){
    companion object{
        fun fromNewsEntity(newsEntity: NewsEntity): NewsData {
            return NewsData(
                 title = newsEntity.title,
                newsId = newsEntity.newsId,
                dateTime = newsEntity.dateCreate,
                description =  newsEntity.description,
                imageUrl =  newsEntity.imageUrl,
                url = newsEntity.url
            )
        }



        fun fromArticleResponse(article: Article, simpleDateFormat: SimpleDateFormat):NewsData{
            val date = simpleDateFormat.parse(article.publishedAt)
            val timeInMillis= date?.time ?: 0

            val uuidBites= article.url?.toByteArray()
            val uuid= UUID.nameUUIDFromBytes(uuidBites).toString()

            return  NewsData(
                newsId = uuid,
                dateTime = timeInMillis,
                title =  article.title?:"",
                imageUrl =  article.urlToImage,
                description =  article.description?: "[Without Description]",
                url = article.url?:""
            )
        }
    }
}
