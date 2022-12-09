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
            .clip(shape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp))
    ) {
        Column(
            modifier = Modifier
                .clickable { onShowDetails() }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(getPosterPath(show.posterPath),)
                    .crossfade(true)
                    .build(),
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)),
                contentDescription = "",
                contentScale = ContentScale.FillWidth
            )

            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
                Text(
                    text = show.title.toString(),
                    maxLines = 1,
                    style = MaterialTheme.typography.subtitle1,
                    overflow = TextOverflow.Ellipsis
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