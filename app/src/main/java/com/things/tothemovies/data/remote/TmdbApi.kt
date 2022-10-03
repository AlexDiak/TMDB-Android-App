package com.things.tothemovies.data.remote

import com.things.tothemovies.data.remote.model.ApiDetails
import com.things.tothemovies.data.remote.model.ApiSPaginatedSearch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface TmdbApi {

    @GET(MULTI_SEARCH)
    suspend fun getSearchResults(
        @Query(ApiParameters.QUERY) query: String,
        @Query(ApiParameters.PAGE) page: Int
    ): ApiSPaginatedSearch

    @GET(MOVIES_DETAILS)
    suspend fun getMovieDetails(
        @Path(MOVIE_ID) kitId: Int
    ): ApiDetails

    @GET(TV_SHOW_DETAILS)
    suspend fun getTvShowDetails(
        @Path(TV_SHOW_ID) kitId: Int
    ): ApiDetails
}