package com.things.tothemovies.data.repository

import com.things.tothemovies.R
import com.things.tothemovies.data.remote.TmdbApi
import com.things.tothemovies.data.remote.model.ApiSPaginatedSearch
import com.things.tothemovies.util.Resource
import com.things.tothemovies.util.UiText
import retrofit2.HttpException
import java.io.IOException

class SearchRepository {

    private val kitApi = TmdbApi.getInstance()

    suspend fun getSearchResults(query: String,  pageToLoad: Int): Resource<ApiSPaginatedSearch>{

        return try {
            val searchResult = kitApi.getSearchResults(query,pageToLoad)
            Resource.Success(data = searchResult)

        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.networkError)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.networkError)
            )
        }
    }
}