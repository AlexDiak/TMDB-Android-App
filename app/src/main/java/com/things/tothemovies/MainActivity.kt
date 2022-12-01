package com.things.tothemovies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.things.tothemovies.ui.details.DetailsScreen
import com.things.tothemovies.ui.details.DetailsViewModel
import com.things.tothemovies.ui.navigation.TOP_LEVEL_DESTINATIONS
import com.things.tothemovies.ui.search.SearchScreen
import com.things.tothemovies.ui.search.SearchViewModel
import com.things.tothemovies.ui.theme.Material3AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Material3AppTheme {
                BottomNavBar()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BottomNavBar() {
        val navController = rememberNavController()
        androidx.compose.material3.Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    TOP_LEVEL_DESTINATIONS.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                androidx.compose.material3.Icon(
                                    imageVector = screen.selectedIcon,
                                    contentDescription = stringResource(
                                        id = screen.iconTextId
                                    )
                                )
                            },
                            label = { androidx.compose.material3.Text(stringResource(screen.iconTextId)) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            MyAppNavHost(modifier = Modifier.padding(innerPadding), navController = navController)
        }
    }

    @Composable
    fun MyAppNavHost(
        modifier: Modifier = Modifier,
        navController: NavHostController = rememberNavController(),
        startDestination: String = "search"
    ) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination
        ) {
            composable("search") {
                val viewModel = hiltViewModel<SearchViewModel>()
                SearchScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable("watchlist") {
                val viewModel = hiltViewModel<SearchViewModel>()
                SearchScreen(
                    viewModel = viewModel,
                    navController = navController,
                    selected = true
                )
            }
            composable("details/{id}/{type}") { backStackEntry ->
                val viewModel = hiltViewModel<DetailsViewModel>()
                DetailsScreen(
                    viewModel = viewModel,
                    navController = navController,
                    type = backStackEntry.arguments?.getString("type") ?: ""
                )
            }
        }
    }
}