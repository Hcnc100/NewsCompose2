package com.nullpointer.newscompose.ui.screens.roomNewsScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.nullpointer.newscompose.model.data.NewsData
import com.nullpointer.newscompose.navigation.graphs.HomeNavGraph
import com.nullpointer.newscompose.navigation.interfaces.ActionsRootNavigation
import com.nullpointer.newscompose.ui.screens.destinations.WebViewScreenDestination
import com.nullpointer.newscompose.ui.screens.simpleNewsScreen.viewModel.NewsViewModel
import com.nullpointer.newscompose.ui.widgets.ConcatenateIndicator
import com.nullpointer.newscompose.ui.widgets.IndicatorNumberSize
import com.nullpointer.newscompose.ui.widgets.NewItemShimmer
import com.nullpointer.newscompose.ui.widgets.NewsItem
import com.ramcosta.composedestinations.annotation.Destination
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer


@OptIn(ExperimentalMaterialApi::class)
@Destination
@HomeNavGraph
@Composable
fun RoomNewsScreen(
    newsViewModel: NewsViewModel,
    actionsRootNavigation: ActionsRootNavigation,
    lazyListState: LazyListState = rememberLazyListState(),
) {

    val newsListPaging = newsViewModel.newsListPagingRoom.collectAsLazyPagingItems()

    val (_, isLoading, isConcatenate) = newsViewModel.newsState

    val refreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = newsViewModel::requestAllNews
    )

    val isAtEnd by remember {
        derivedStateOf {
            val lastItemInfo = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()
            val lastIndex = lastItemInfo?.index ?: return@derivedStateOf false
            lastIndex >= lazyListState.layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(key1 = isAtEnd) {
        if (isAtEnd) {
            newsViewModel.concatenateNews()
        }
    }

    Scaffold { paddingValues ->

        ContainerPadding(
            isLoading = isLoading,
            lazyPagingItems = newsListPaging,
            lazyListState = lazyListState,
            isConcatenate = isConcatenate,
            pullRefreshState = refreshState,
            clickNews = {
                actionsRootNavigation.navigateTo(WebViewScreenDestination(it))
            },
            modifier = Modifier
                .padding(paddingValues)
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
    clickNews: (NewsData) -> Unit
) {

    Box(
        modifier = modifier.then(
            if (lazyPagingItems.loadState.source.refresh is LoadState.Error)
                Modifier.verticalScroll(rememberScrollState()) else Modifier
        ),
        contentAlignment = Alignment.Center
    ) {
        val refreshState = lazyPagingItems.loadState.source.refresh
        val totalItemsCount by remember { derivedStateOf { lazyListState.layoutInfo.totalItemsCount } }
        when {
            refreshState is LoadState.Loading && totalItemsCount == 0 -> CircularProgressIndicator()
            else -> ListNewsPaging(
                lazyListState = lazyListState,
                lazyPagingItems = lazyPagingItems,
                isConcatenate = isConcatenate,
                clickNews = clickNews
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
    isConcatenate: Boolean,
    clickNews: (NewsData) -> Unit
) {

    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)

    Box(modifier = modifier) {
        if (lazyPagingItems.itemCount == 0) {
            Text(text = "No have news")
        } else {
            LazyColumn(
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(10.dp)
            ) {
                stickyHeader {
                    IndicatorNumberSize(count = lazyPagingItems.itemCount)
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
                        NewsItem(newsItem = currentNews, clickItem = { clickNews(currentNews) })
                    }
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
