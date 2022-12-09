package com.things.tothemovies.data.remote

import com.things.tothemovies.data.remote.model.ApiDetails
import com.things.tothemovies.data.remote.model.ApiPaginatedSearch
import com.things.tothemovies.data.remote.model.ApiVideos
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET(MULTI_SEARCH)
    suspend fun getSearchResults(
        @Query(ApiParameters.QUERY) query: String,
        @Query(ApiParameters.PAGE) page: Int
    ): ApiPaginatedSearch

    @GET(TRENDING_MOVIES)
    suspend fun getTrendingMovies(
        @Query(ApiParameters.PAGE) page: Int
    ): ApiPaginatedSearch

    @GET(MOVIE_DETAILS)
    suspend fun getMovieDetails(
        @Path(MOVIE_ID) kitId: Int
    ): ApiDetails

    @GET(MOVIE_VIDEOS)
    suspend fun getMovieVideos(
        @Path(MOVIE_ID) kitId: Int
    ): ApiVideos

    @GET(TV_SHOW_DETAILS)
    suspend fun getTvShowDetails(
        @Path(TV_SHOW_ID) kitId: Int
    ): ApiDetails

    @GET(TV_SHOW_VIDEOS)
    suspend fun getTvShowVideos(
        @Path(TV_SHOW_ID) kitId: Int
    ): ApiVideos
}