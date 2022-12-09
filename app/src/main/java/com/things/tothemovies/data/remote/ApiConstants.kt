package com.things.tothemovies.data.remote

const val BASE_URL = "https://api.themoviedb.org/3/"

const val MOVIE_ID = "movieId"
const val TV_SHOW_ID = "tvShowId"

const val MULTI_SEARCH = "search/multi"
const val TRENDING_MOVIES = "trending/all/day"
const val MOVIE_DETAILS = "movie/{$MOVIE_ID}"
const val MOVIE_VIDEOS = "movie/{$MOVIE_ID}/videos"
const val TV_SHOW_DETAILS = "tv/{$TV_SHOW_ID}"
const val TV_SHOW_VIDEOS = "tv/{$TV_SHOW_ID}/videos"

object ApiParameters {
    const val API_KEY = "api_key"
    const val KEY = "6b2e856adafcc7be98bdf0d8b076851c"
    const val QUERY = "query"
    const val PAGE = "page"
}