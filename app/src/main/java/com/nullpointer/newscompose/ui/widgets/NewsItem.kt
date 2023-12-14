package com.nullpointer.newscompose.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nullpointer.newscompose.model.data.NewsData
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsItem(
    newsItem: NewsData,
    modifier: Modifier = Modifier,
    clickItem: () -> Unit
) {

    Card(onClick = clickItem, shape = RoundedCornerShape(10.dp), modifier = modifier) {
        Box(
            contentAlignment = Alignment.BottomCenter
        ) {
            AsyncImage(
                model = newsItem.imageUrl,
                contentDescription = newsItem.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = newsItem.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
                color = Color.White,
                modifier = Modifier
                    .background(
                        Color.Black.copy(alpha = 0.5F)
                    )
                    .padding(10.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewItemShimmer(
    modifier: Modifier = Modifier,
    shimmer: Shimmer
) {
    Card(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.shimmer(shimmer),
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Gray)
                .shimmer(),
        )

    }
}