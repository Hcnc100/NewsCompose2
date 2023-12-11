package com.nullpointer.newscompose.model.api.news

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class NewsListResponse (
    val status: String? = null,
    val totalResults: Long? = null,
    val articles: List<Article>? = null
)

@Serializable
data class Article (
    val source: Source? = null,
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null,
    val content: String? = null
)

@Serializable
data class Source (
    val id: String? = null,
    val name: String? = null
)
