package com.things.tothemovies.data.repository

import com.things.tothemovies.data.remote.TmdbApi

class DetailsRepository {

    private val kitApi = TmdbApi.getInstance()

    suspend fun getMovieDetails(){
        kitApi.getMovieDetails(1)
    }

    suspend fun getTvShowDetails(){
        kitApi.getTvShowDetails(1)
    }
}