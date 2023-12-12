package com.nullpointer.newscompose.ui.screens.simpleNewsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nullpointer.newscompose.model.data.NewsData
import com.nullpointer.newscompose.ui.screens.simpleNewsScreen.composables.NewsItem
import com.nullpointer.newscompose.ui.screens.simpleNewsScreen.viewModel.NewsViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsScreen(
    newsViewModel: NewsViewModel = viewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),

    ) {

    val isInitViewModel = newsViewModel.isInit
    val newsList by newsViewModel.newsList.collectAsState()
    val isLoading = newsViewModel.isLoading

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = newsViewModel::requestAllNews,
    )


    LaunchedEffect(key1 = Unit) {
        if (!isInitViewModel) {
            newsViewModel.requestAllNews()
        }
    }

    LaunchedEffect(key1 = Unit) {
        newsViewModel.message.collect {
            scaffoldState.snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = { Text(text = "News Simple") }) }
    ) {

            NewsListComposable(
                isLoading = isLoading,
                newsList = newsList,
                pullRefreshState = pullRefreshState,
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .pullRefresh(state = pullRefreshState)
            )

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsListComposable(
    isLoading: Boolean,
    newsList: List<NewsData>?,
    pullRefreshState: PullRefreshState,
    modifier: Modifier
) {
    Box(
        modifier = modifier.then(
            if (newsList.isNullOrEmpty()) Modifier.verticalScroll(rememberScrollState()) else Modifier
        ),
        contentAlignment = Alignment.Center,
    ) {
        when {
            newsList==null && isLoading -> CircularProgressIndicator()
            newsList.isNullOrEmpty() -> Text(text = "No have news")
            else ->
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                ) {

                    item(contentType = "spacer") { Spacer(modifier = Modifier.height(10.dp)) }

                    items(newsList, key = {it.newsId}, contentType = {"New"}) { news ->
                        NewsItem(newsItem = news)
                    }

                    item(contentType = "spacer") { Spacer(modifier = Modifier.height(10.dp)) }
                }
        }

        if(!newsList.isNullOrEmpty()){
            PullRefreshIndicator(
                refreshing = isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }

    }
}