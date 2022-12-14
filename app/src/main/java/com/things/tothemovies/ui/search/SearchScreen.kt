package com.things.tothemovies.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.things.tothemovies.R
import com.things.tothemovies.ui.SnackbarScreen

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    selected: Boolean = false
) {

    val items = viewModel.results.collectAsLazyPagingItems()
    val query = viewModel.currentQuery.collectAsState()

    viewModel.setWatchlistMode(selected)

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background
                ),
            horizontalAlignment = Alignment.End,
        ) {

            SearchView(query.value, viewModel, items.loadState.refresh)

            LazyVerticalGrid(
                columns = GridCells.Adaptive(100.dp),
                contentPadding = PaddingValues(10.dp),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items.itemCount) { index ->
                    Box(modifier = Modifier.aspectRatio(0.55f)) {
                        MovieItem(items[index]!!, onShowDetails = {
                            navController.navigate(
                                "details/${items[index]?.id ?: 0}/${items[index]?.mediaType.toString()}"
                            )
                        })
                    }
                }
            }
        }

        when (items.loadState.refresh) {
            is LoadState.Error -> SnackbarScreen(
                snackbarHostState,
                stringResource(id = R.string.oops_something_went_wrong)
            )
            else -> Unit
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(query: String, viewModel: SearchViewModel, loadState: LoadState) {

    val focusManager = LocalFocusManager.current

    TextField(
        value = query,
        onValueChange = { value ->
            viewModel.getSearchResults(value)
        },
        placeholder = { Text("Search for movies, shows") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            when (loadState) {
                is LoadState.Loading ->
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp)
                    )
                else -> {
                    if (query != "") {
                        IconButton(
                            onClick = {
                                viewModel.getSearchResults("")
                            }
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        }
                    }
                }
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        shape = MaterialTheme.shapes.large,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}

