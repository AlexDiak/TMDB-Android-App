package com.things.tothemovies.ui.details

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.things.tothemovies.R
import com.things.tothemovies.ui.SnackbarScreen
import com.things.tothemovies.utils.*

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    type: String
) {

    val state = viewModel.state.collectAsState().value.show
    val stateVideos = viewModel.state.collectAsState().value.videos
    val itExists = viewModel.showExists.collectAsState()
    val loading = viewModel.state.collectAsState().value.isLoading
    val connectionError = viewModel.state.collectAsState().value.errorMessage

    val context = LocalContext.current

    Column {

        DetailsAppBar(navController = navController)

        if (loading)
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    Modifier
                        .size(45.dp)
                        .align(Alignment.Center)
                )
            }

        if (state != null)
            Column(Modifier.verticalScroll(rememberScrollState())) {

                Row(modifier = Modifier.padding(10.dp)) {

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(getPosterPath(state.poster_path))
                            .crossfade(true)
                            .build(),
                        modifier = Modifier
                            .width(150.dp)
                            .widthIn(220.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentDescription = "",
                        contentScale = ContentScale.FillWidth
                    )

                    Column {
                        Text(
                            text = (state.name ?: state.title).toString(),
                            Modifier.padding(start = 10.dp, top = 15.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )

                        Row {
                            Text(
                                text = (state.release_date
                                    ?: state.first_air_date).split("-")[0],
                                Modifier.padding(start = 10.dp)
                            )

                            if (type == MOVIE) {
                                Text(
                                    text = "|",
                                    Modifier.padding(start = 5.dp, end = 5.dp)
                                )

                                Text(
                                    text = "${(state.runtime.toString())} min",
                                )
                            }
                        }

                        if (!itExists.value)
                            OutlinedButton(
                                onClick = {
                                    viewModel.addToWatchlist(
                                        state.id,
                                        (state.name ?: state.title).toString(),
                                        (state.release_date ?: state.first_air_date).toString(),
                                        state.poster_path,
                                        mediaType = type
                                    )
                                },
                                Modifier.padding(start = 10.dp, top = 10.dp)
                            ) {
                                Text(text = "Add to watchlist")
                            } else {
                            Button(
                                onClick = {
                                    state.toShow(type).let { it1 ->
                                        viewModel.removeFromWatchlist(
                                            it1
                                        )
                                    }
                                },
                                Modifier.padding(start = 10.dp, top = 10.dp)
                            ) {
                                Text(text = "Remove from watchlist")
                            }
                        }
                    }
                }

                Text(
                    text = state.overview,
                    Modifier.padding(start = 15.dp, top = 10.dp, end = 10.dp)
                )

                Text(
                    text = "Genres",
                    Modifier.padding(start = 10.dp, top = 15.dp),
                    fontWeight = FontWeight.Bold,
                )

                LazyRow(
                    modifier = Modifier.padding(start = 10.dp)
                ) {

                    val list = state.genres

                    itemsIndexed(list) { index, genre ->
                        Text(
                            text = if (index == list.size - 1) genre.name else "${genre.name}, ",
                            fontSize = 14.sp
                        )
                    }
                }

                if (stateVideos?.results?.isNotEmpty() == true) {

                    val list = stateVideos.results

                    Text(
                        text = if (list.size > 1) "Trailers" else "Trailer",
                        Modifier.padding(start = 10.dp, top = 10.dp),
                        fontWeight = FontWeight.Bold,
                    )

                    LazyRow(
                        contentPadding = PaddingValues(5.dp)
                    ) {

                        items(list) { video ->
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(getYoutubeThumbnailPath(video.key))
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .padding(start = 5.dp, end = 5.dp, bottom = 10.dp)
                                    .clickable {
                                        openYoutubeLink(
                                            stateVideos.results[0].key,
                                            context
                                        )
                                    }
                                    .clip(RoundedCornerShape(10.dp))
                            )
                        }
                    }
                }
            }

        if (connectionError != null)
            SnackbarScreen(snackbarHostState, connectionError.asString(context))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsAppBar(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    TopAppBar(
        title = { stringResource(R.string.details_screen) },
        modifier = modifier,
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        navigationIcon = {
            FilledIconButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier.padding(8.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    )
}

private fun openYoutubeLink(videoKey: String, context: Context) {
    val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse(getYoutubeVideoAppPath(videoKey)))
    val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(getYoutubeVideoPath(videoKey)))
    try {
        context.startActivity(intentApp)
    } catch (ex: ActivityNotFoundException) {
        context.startActivity(intentBrowser)
    }
}