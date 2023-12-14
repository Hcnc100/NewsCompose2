package com.nullpointer.newscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.nullpointer.newscompose.navigation.interfaces.ActionsRootNavigation
import com.nullpointer.newscompose.ui.screens.NavGraphs
import com.nullpointer.newscompose.ui.theme.NewsComposeTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var showSplash = true
        installSplashScreen().setKeepOnScreenCondition { showSplash }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                delay(1500)
                showSplash = false
            }
        }
        setContent {
            NewsComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    val actionsRootNavigation = remember(navController) {
                        ActionsRootNavigation { direction -> navController.navigate(direction) }
                    }


                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        navController = navController,
                        dependenciesContainerBuilder = {
                            dependency(actionsRootNavigation)
                        }
                    )
                }
            }
        }
    }
}

