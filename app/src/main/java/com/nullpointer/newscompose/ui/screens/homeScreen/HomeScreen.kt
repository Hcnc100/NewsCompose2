package com.nullpointer.newscompose.ui.screens.homeScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.nullpointer.newscompose.navigation.destinations.DestinationsItems
import com.nullpointer.newscompose.navigation.interfaces.ActionsRootNavigation
import com.nullpointer.newscompose.ui.screens.NavGraphs
import com.nullpointer.newscompose.ui.screens.appCurrentDestinationAsState
import com.nullpointer.newscompose.ui.screens.simpleNewsScreen.viewModel.NewsViewModel
import com.nullpointer.newscompose.ui.screens.startAppDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate


@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    actionsRootNavigation: ActionsRootNavigation,
    scaffoldState: ScaffoldState = rememberScaffoldState(),

    newsViewModel: NewsViewModel = hiltViewModel()
) {

    val navController = rememberNavController()

    val (isInitViewModel) = newsViewModel.newsState

    LaunchedEffect(key1 = Unit) {
        newsViewModel.message.collect {
            scaffoldState.snackbarHostState.showSnackbar(it)
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (!isInitViewModel) {
            newsViewModel.requestAllNews()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = { Text(text = "News") }) },
        bottomBar = { HomeBottomNavDestination(navController = navController) }
    ) {
        DestinationsNavHost(
            navController = navController,
            navGraph = NavGraphs.home,
            modifier = Modifier.padding(it),
            dependenciesContainerBuilder = {
                dependency(NavGraphs.home) {
                    actionsRootNavigation
                }
                dependency(NavGraphs.home) {
                    newsViewModel
                }
            }
        )
    }
}

@Composable
fun HomeBottomNavDestination(
    navController: NavController
) {

    val currentDestination =
        navController.appCurrentDestinationAsState().value ?: NavGraphs.home.startAppDestination

    BottomAppBar {
        DestinationsItems.HomeDestinationsItems.listScreens.map {
            BottomNavigationItem(
                selected = it.destination == currentDestination,
                onClick = {
                    navController.navigate(it.destination) {

                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = it.icon),
                        contentDescription = it.title,
                    )
                },
            )
        }
    }
}