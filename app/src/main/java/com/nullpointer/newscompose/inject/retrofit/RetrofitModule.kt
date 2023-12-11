package com.nullpointer.newscompose.inject.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    const val BASE_NEWS = "https://newsapi.org/v2/"
    const val KEY_BASE_NEWS = "KEY_BASE_NEWS"

    @Singleton
    @Provides
    @Named(KEY_BASE_NEWS)
    fun provideBaseNews(): String = BASE_NEWS

    @Singleton
    @Provides
    fun provideSerialization(): Converter.Factory {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
        return json.asConverterFactory(contentType)
    }

    @Singleton
    @Provides
    fun provideLoggerClient(): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        ).build()


    @Singleton
    @Provides
    fun provideRetrofit(
        @Named(KEY_BASE_NEWS) base: String,
        convertFactory: Converter.Factory,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(convertFactory)
        .client(client)
        .baseUrl(base).build()
}