package com.nullpointer.newscompose.ui.screens.mediatorNewsScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.nullpointer.newscompose.model.data.NewsData
import com.nullpointer.newscompose.navigation.graphs.HomeNavGraph
import com.nullpointer.newscompose.ui.screens.simpleNewsScreen.viewModel.NewsViewModel
import com.nullpointer.newscompose.ui.widgets.ConcatenateIndicator
import com.nullpointer.newscompose.ui.widgets.NewItemShimmer
import com.nullpointer.newscompose.ui.widgets.NewsItem
import com.ramcosta.composedestinations.annotation.Destination
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


@OptIn(ExperimentalMaterialApi::class)
@Destination
@HomeNavGraph
@Composable
fun MediatorNewsScreen(
    newsViewModel: NewsViewModel,
    lazyListState: LazyListState = rememberLazyListState()
) {

    val newsListPaging = newsViewModel.newsListPagingMediator.collectAsLazyPagingItems()

    var isLoading by remember {
        mutableStateOf(true)
    }
    var isConcatenate by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = newsListPaging) {
        snapshotFlow { newsListPaging.loadState.append }
            .map {
                it is LoadState.Loading
            }
            .distinctUntilChanged()
            .debounce(200)
            .collect {
                isConcatenate = it
            }
    }

    LaunchedEffect(key1 = newsListPaging) {
        snapshotFlow { newsListPaging.loadState }
            .map {
                it.refresh == LoadState.Loading
            }
            .distinctUntilChanged()
            .debounce(200)
            .collect {
                isLoading = it
            }
    }

    val refreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = {
            isLoading = true
            newsListPaging.refresh()
        }
    )

    Scaffold {

        ContainerPadding(
            isLoading = isLoading,
            lazyPagingItems = newsListPaging,
            lazyListState = lazyListState,
            isConcatenate = isConcatenate,
            pullRefreshState = refreshState,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .pullRefresh(refreshState)
        )

    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ContainerPadding(
    isLoading: Boolean,
    isConcatenate: Boolean,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<NewsData>,
    pullRefreshState: PullRefreshState,
) {
    Box(
        modifier = modifier.then(
            if (lazyPagingItems.itemSnapshotList.isEmpty() && !isLoading)
                Modifier.verticalScroll(rememberScrollState()) else Modifier
        ),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading && lazyPagingItems.itemSnapshotList.isEmpty() -> CircularProgressIndicator()
            lazyPagingItems.itemSnapshotList.isEmpty() -> Text(text = "No have news")
            else -> ListNewsPaging(
                lazyListState = lazyListState,
                lazyPagingItems = lazyPagingItems,
                isConcatenate = isConcatenate
            )
        }

        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListNewsPaging(
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<NewsData>,
    isConcatenate: Boolean
) {

    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)

    Box(modifier = modifier) {

        LazyColumn(
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(10.dp)
        ) {

            stickyHeader {
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .height(40.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(text = "Numero de elementos actuales ${lazyPagingItems.itemCount}")
                }
            }

            items(
                count = lazyPagingItems.itemCount,
                key = lazyPagingItems.itemKey { it.newsId },
                contentType = lazyPagingItems.itemContentType { "news" }
            ) { index ->

                val currentNews = lazyPagingItems[index]

                if (currentNews == null) {
                    NewItemShimmer(shimmer = shimmer)
                } else {
                    NewsItem(newsItem = currentNews)
                }
            }
        }


        AnimatedVisibility(
            visible = isConcatenate,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ConcatenateIndicator()
        }
    }

}
