package com.things.tothemovies.utils

private const val BASE_POSTER_PATH = "https://image.tmdb.org/t/p/w500/"
const val YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v="
const val YOUTUBE_VIDEO_APP_URL = "https://www.youtube.com/watch?v="
const val YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/"

fun getPosterPath(posterPath: String?): String {
    return BASE_POSTER_PATH + posterPath
}

fun getYoutubeVideoPath(videoPath: String?): String {
    return YOUTUBE_VIDEO_URL + videoPath
}

fun getYoutubeVideoAppPath(videoPath: String?): String {
    return YOUTUBE_VIDEO_APP_URL + videoPath
}


fun getYoutubeThumbnailPath(thumbnailPath: String?): String {
    return "$YOUTUBE_THUMBNAIL_URL$thumbnailPath/0.jpg"
}