package com.things.tothemovies.data.repository

import com.things.tothemovies.data.remote.TmdbApi

class SearchRepository {

    private val kitApi = TmdbApi.getInstance()

    suspend fun getSearchResults(){
        kitApi.getSearchResults("",1)
    }
}