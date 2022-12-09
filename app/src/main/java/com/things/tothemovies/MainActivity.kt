package com.things.tothemovies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    TOP_LEVEL_DESTINATIONS.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = screen.selectedIcon,
                                    contentDescription = stringResource(
                                        id = screen.iconTextId
                                    )
                                )
                            },
                            label = { Text(stringResource(screen.iconTextId)) },
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
            MyAppNavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                snackbarHostState
            )
        }
    }

    @Composable
    fun MyAppNavHost(
        modifier: Modifier = Modifier,
        navController: NavHostController = rememberNavController(),
        snackbarHostState: SnackbarHostState,
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
                    navController = navController,
                    snackbarHostState = snackbarHostState
                )
            }
            composable("watchlist") {
                val viewModel = hiltViewModel<SearchViewModel>()
                SearchScreen(
                    viewModel = viewModel,
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    selected = true
                )
            }
            composable("details/{id}/{type}") { backStackEntry ->
                val viewModel = hiltViewModel<DetailsViewModel>()
                DetailsScreen(
                    viewModel = viewModel,
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    type = backStackEntry.arguments?.getString("type") ?: ""
                )
            }
        }
    }
}