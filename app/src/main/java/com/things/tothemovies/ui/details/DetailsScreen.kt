package com.things.tothemovies.ui.details

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.things.tothemovies.ui.search.MovieImage
import com.things.tothemovies.utils.*

@Composable
fun DetailsScreen(viewModel: DetailsViewModel, navController: NavController, type: String) {

    val state = viewModel.state.collectAsState()
    val stateVideos = viewModel.stateVideos.collectAsState()
    val itExists = viewModel.showExists.collectAsState()

    val context = LocalContext.current

    if (state.value == null)
        return

    Column {

        DetailsAppBar(navController = navController)

        Column(Modifier.verticalScroll(rememberScrollState())) {

            Row(modifier = Modifier.padding(10.dp)) {

                MovieImage(
                    imageUrl = getPosterPath(state.value?.poster_path),
                    modifier = Modifier
                        .width(150.dp)
                        .widthIn(220.dp)
                )

                Column {
                    Text(
                        text = (state.value?.name ?: state.value?.title).toString(),
                        Modifier.padding(start = 10.dp, top = 15.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Row {

                        Text(
                            text = (state.value?.release_date
                                ?: state.value?.first_air_date)?.split("-")
                                ?.get(0).toString(),
                            Modifier.padding(start = 10.dp)
                        )

                        if (type == MOVIE) {
                            Text(
                                text = "|",
                                Modifier.padding(start = 5.dp, end = 5.dp)
                            )

                            Text(
                                text = "${(state.value?.runtime.toString())} min",
                            )
                        }
                    }

                    if (!itExists.value)
                        OutlinedButton(
                            onClick = {
                                val show = state.value
                                viewModel.addToWatchlist(
                                    show?.id ?: -1,
                                    (show?.name ?: show?.title).toString(),
                                    (show?.release_date ?: show?.first_air_date).toString(),
                                    show?.poster_path.toString(),
                                    mediaType = type
                                )
                            },
                            Modifier.padding(start = 10.dp, top = 10.dp)
                        ) {
                            Text(text = "Add to watchlist")
                        } else {
                        Button(
                            onClick = {
                                state.value?.toShow(type)?.let { it1 ->
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
                text = state.value?.overview.toString(),
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

                val list = state.value?.genres ?: emptyList()

                itemsIndexed(list) { index, genre ->
                    Text(
                        text = if (index == list.size - 1) genre.name else "${genre.name}, ",
                        fontSize = 14.sp
                    )
                }
            }

            if (stateVideos.value?.results?.isNotEmpty() == true) {

                val list = stateVideos.value?.results ?: emptyList()

                Text(
                    text = if (list.size > 1) "Trailers" else "Trailer",
                    Modifier.padding(start = 10.dp, top = 10.dp),
                    fontWeight = FontWeight.Bold,
                )

                LazyRow(
                    contentPadding = PaddingValues(5.dp)
                ) {

                    itemsIndexed(list) { index, video ->
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
                                    stateVideos.value?.results?.get(0)?.key?.let {
                                        openYoutubeLink(
                                            it,
                                            context
                                        )
                                    }
                                }
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }
                }
            }
        }
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