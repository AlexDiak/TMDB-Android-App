package com.things.tothemovies.data.remote

const val BASE_URL = "https://www.themoviedb.org/3/"

const val MOVIE_ID = "movieId"
const val TV_SHOW_ID = "tvShowId"

const val MULTI_SEARCH = "search/multi"
const val MOVIES_DETAILS = "movie/{$MOVIE_ID}"
const val TV_SHOW_DETAILS = "tv/{$TV_SHOW_ID}"

object ApiParameters {
    const val API_KEY = "api_key"
    const val KEY = ""
    const val QUERY = "query"
    const val PAGE = "page"
}