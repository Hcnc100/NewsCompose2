package com.nullpointer.newscompose.ui.screens.simpleNewsScreen.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.nullpointer.newscompose.domain.news.NewsRepository
import com.nullpointer.newscompose.model.data.NewsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    var newsState by mutableStateOf(NewsState())
        private set

    private val _message = Channel<String>()
    val message = _message.receiveAsFlow()

    val newsList = newsRepository.getListNewsCache()
        .onStart { 1500 }
        .flowOn(Dispatchers.IO)
        .catch {
            emit(emptyList())
            _message.trySend(it.toString())
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null,
        )

    val newsListPagingRoom = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            prefetchDistance = 5,
        ),
        pagingSourceFactory = newsRepository::getNewsPageSource
    ).flow.catch {
        emit(PagingData.empty())
        _message.trySend(it.toString())
    }.map { pagingData ->
        pagingData.map(NewsData::fromNewsEntity)
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope)


    val newsListPagingMediator = Pager(
        config = PagingConfig(
            initialLoadSize = 10,
            pageSize = 10,
            enablePlaceholders = true,
            prefetchDistance = 5,
        ),
        pagingSourceFactory = newsRepository::getNewsPageSource,
        remoteMediator = newsRepository.getNewsRemoteMediator(),
    ).flow.catch {
        emit(PagingData.empty())
        _message.trySend(it.toString())
    }.map { pagingData ->
        pagingData.map(NewsData::fromNewsEntity)
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope)

    fun requestAllNews() {
        if (newsState.isLoading) return
        viewModelScope.launch {
            newsState = newsState.copy(isLoading = true)
            runCatching {
                val countNews = withContext(Dispatchers.IO) {
                    newsRepository.requestNewNews()
                }
                newsState = newsState.copy(isConcatenateEnable = countNews != 0)
            }.onSuccess {
                if (!newsState.isInit) {
                    newsState = newsState.copy(isInit = true)
                }
            }.onFailure {
                _message.send(it.message.toString())
                print(it.message.toString())
            }

            newsState = newsState.copy(isLoading = false)

        }
    }

    fun concatenateNews() {
        if (!newsState.canConcatenate) return
        viewModelScope.launch {
            newsState = newsState.copy(isConcatenate = true)
            runCatching {
                val countNews = withContext(Dispatchers.IO) {
                    newsRepository.concatenateNews()
                }
                newsState = newsState.copy(isConcatenateEnable = countNews != 0)
            }.onFailure {
                _message.send(it.message.toString())
            }
            newsState = newsState.copy(isConcatenate = false)
        }
    }
}
