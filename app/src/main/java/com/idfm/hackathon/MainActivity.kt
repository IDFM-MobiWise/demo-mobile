package com.idfm.hackathon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.idfm.hackathon.ui.features.home.HomeScreen
import com.idfm.hackathon.ui.features.home.HomeScreenViewModelImpl
import com.idfm.hackathon.ui.nav.Screen
import com.idfm.hackathon.ui.nav.WithTopBar
import com.idfm.hackathon.ui.theme.HackathonIdFMTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val animDurations = 500
            val screenOffset = 800
            val navController: NavHostController = rememberNavController()

            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentScreen = Screen.valueOf(
                backStackEntry?.destination?.route?.split("/")?.first() ?: Screen.Home.name
            )

            HackathonIdFMTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.name,
                        modifier = Modifier,
                        enterTransition = {
                            // called, if the navigation is about to show the composable
                            slideInHorizontally(
                                initialOffsetX = { screenOffset },
                                animationSpec = tween(animDurations)
                            ) +
                                    fadeIn(animationSpec = tween(durationMillis = animDurations))
                        },
                        popEnterTransition = {
                            // called for the new composable, if the current one is about to be removed
                            slideInHorizontally(
                                initialOffsetX = { -screenOffset },
                                animationSpec = tween(animDurations)
                            ) +
                                    fadeIn(animationSpec = tween(durationMillis = animDurations))
                        },
                        exitTransition = {
                            // called, if the navigation is removing the current composable
                            slideOutHorizontally(
                                targetOffsetX = { -screenOffset },
                                animationSpec = tween(animDurations)
                            ) +
                                    fadeOut(animationSpec = tween(durationMillis = animDurations))
                        },
                        popExitTransition = {
                            // called for the old composable, if the new one is about to be shown
                            slideOutHorizontally(
                                targetOffsetX = { screenOffset },
                                animationSpec = tween(animDurations)
                            ) +
                                    fadeOut(animationSpec = tween(durationMillis = animDurations))
                        }
                    ) {
                        composable(route = Screen.Home.name) {
                            val homeScreenViewModel = koinViewModel<HomeScreenViewModelImpl>()
                            WithTopBar(currentScreen, navController, homeScreenViewModel) {
                                HomeScreen(navController, homeScreenViewModel)
                            }
                        }
                    }
//                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HackathonIdFMTheme {
        Greeting("Android")
    }
}