package com.nullpointer.newscompose.ui.screens.simpleNewsScreen.viewModel

data class NewsState(
    val isInit: Boolean = false,
    val isLoading: Boolean = false,
    val isConcatenate: Boolean = false,
    val isConcatenateEnable: Boolean = false
) {
    val canConcatenate get() = isConcatenateEnable && !isConcatenate && !isLoading
}
