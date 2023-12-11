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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    var isInit by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    val _message= Channel<String>()
    val message= _message.receiveAsFlow()

    val newsList = newsRepository.getListNewsCache()
        .onStart { delay(1500) }
        .flowOn(Dispatchers.IO)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null,
        )

    fun requestAllNews()=viewModelScope.launch {
        isLoading=true

        runCatching {
            withContext(Dispatchers.IO){
                newsRepository.requestNewNews()
            }
        }.onSuccess {
            if(!isInit){
                isInit=true
            }
        }.onFailure {
            _message.send(it.message.toString())
            print(it.message.toString())
        }

        isLoading=false

    }
}