package com.nullpointer.newscompose.navigation.destinations

import androidx.annotation.DrawableRes
import com.nullpointer.newscompose.R
import com.nullpointer.newscompose.ui.screens.destinations.MediatorNewsScreenDestination
import com.nullpointer.newscompose.ui.screens.destinations.RoomNewsScreenDestination
import com.nullpointer.newscompose.ui.screens.destinations.SimpleNewsScreenDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

sealed class DestinationsItems(
    val title: String,
    @DrawableRes
    val icon: Int,
    val destination: DirectionDestinationSpec
) {

    sealed class HomeDestinationsItems(
        title: String,
        icon: Int,
        destinations: DirectionDestinationSpec
    ) : DestinationsItems(title, icon, destinations) {

        companion object {
            val listScreens = listOf(
                SimpleNews(),
                RoomNews(),
                PaginationNews()
            )
        }

        class SimpleNews(
            title: String = "Simple News",
            icon: Int = R.drawable.baseline_analytics_24,
            destinations: DirectionDestinationSpec = SimpleNewsScreenDestination
        ) : HomeDestinationsItems(title, icon, destinations)

        class RoomNews(
            title: String = "Room News",
            icon: Int = R.drawable.baseline_adb_24,
            destinations: DirectionDestinationSpec = RoomNewsScreenDestination
        ) : HomeDestinationsItems(title, icon, destinations)

        class PaginationNews(
            title: String = "Pagination News",
            icon: Int = R.drawable.baseline_bubble_chart_24,
            destinations: DirectionDestinationSpec = MediatorNewsScreenDestination
        ) : HomeDestinationsItems(title, icon, destinations)
    }


}