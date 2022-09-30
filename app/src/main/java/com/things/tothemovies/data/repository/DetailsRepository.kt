package com.things.tothemovies.data.repository

import com.things.tothemovies.R
import com.things.tothemovies.data.remote.TmdbApi
import com.things.tothemovies.data.remote.model.ApiDetails
import com.things.tothemovies.util.Resource
import com.things.tothemovies.util.UiText
import retrofit2.HttpException
import java.io.IOException

class DetailsRepository {

    private val kitApi = TmdbApi.getInstance()

    suspend fun getMovieDetails(id: Int): Resource<ApiDetails> {
        return try {
            val movie = kitApi.getMovieDetails(id)
            Resource.Success(data = movie)

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

    suspend fun getTvShowDetails(id: Int): Resource<ApiDetails> {
        return try {
            val movie = kitApi.getTvShowDetails(id)
            Resource.Success(data = movie)

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