package com.nullpointer.newscompose.ui.screens.simpleNewsScreen.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.newscompose.domain.news.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    var newsState by mutableStateOf(NewsState())
        private set

    private val _message = Channel<String>()
    val message = _message.receiveAsFlow()

    val newsList = newsRepository.getListNewsCache()
        .flowOn(Dispatchers.IO)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null,
        )

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
                    delay(1500)
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
