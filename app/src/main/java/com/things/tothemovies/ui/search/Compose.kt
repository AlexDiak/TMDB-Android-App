package com.things.tothemovies.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.things.tothemovies.data.local.model.Show
import com.things.tothemovies.utils.getPosterPath

@Composable
fun MovieItem(show: Show, onShowDetails: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(shape = RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier
                .clickable { onShowDetails() }
        ) {
            MovieImage(
                getPosterPath(show.posterPath),
                modifier = Modifier.fillMaxWidth()
            )

            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
                MovieTitle(
                    show.title.toString()
                )
                Text(
                    text = show.year?.split("-")
                        ?.get(0).toString(),
                    fontSize = 13.sp,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun MovieImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        modifier = modifier.clip(RoundedCornerShape(10.dp)),
        contentDescription = "",
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun MovieTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = title,
        maxLines = 1,
        style = MaterialTheme.typography.subtitle1,
        overflow = TextOverflow.Ellipsis
    )
}