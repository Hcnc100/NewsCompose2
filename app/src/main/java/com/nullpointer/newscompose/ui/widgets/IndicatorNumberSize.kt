package com.nullpointer.newscompose.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun IndicatorNumberSize(
    count: Int
) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(40.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = "Number news $count")
    }
}