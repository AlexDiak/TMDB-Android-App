package com.things.tothemovies.data.remote

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
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
    ): ResponseBody

    @GET(MOVIES_DETAILS)
    suspend fun getMovieDetails(
        @Path(MOVIE_ID) kitId: Int
    ): ResponseBody

    @GET(TV_SHOW_DETAILS)
    suspend fun getTvShowDetails(
        @Path(TV_SHOW_ID) kitId: Int
    ): ResponseBody

    companion object {
        var retrofitService: TmdbApi? = null
        fun getInstance(): TmdbApi {
            if (retrofitService == null) {

                val client = OkHttpClient.Builder()

                client.retryOnConnectionFailure(true)
                client.readTimeout(60, TimeUnit.SECONDS)
                client.writeTimeout(60, TimeUnit.SECONDS)
                client.addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )

                client.addInterceptor(AuthenticationInterceptor())

                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client.build())
                    .build()
                retrofitService = retrofit.create(TmdbApi::class.java)
            }
            return retrofitService!!
        }
    }
}