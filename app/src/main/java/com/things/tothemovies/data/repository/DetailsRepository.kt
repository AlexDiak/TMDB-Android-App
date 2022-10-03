package com.things.tothemovies.data.repository

import com.things.tothemovies.R
import com.things.tothemovies.data.local.dao.WatchlistDao
import com.things.tothemovies.data.local.model.Show
import com.things.tothemovies.data.remote.TmdbApi
import com.things.tothemovies.data.remote.model.ApiDetails
import com.things.tothemovies.util.Resource
import com.things.tothemovies.util.UiText
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DetailsRepository @Inject constructor(
    private val api: TmdbApi,
    private val watchlistDao: WatchlistDao
) {

    suspend fun getMovieDetails(id: Int): Resource<ApiDetails> {
        return try {
            val movie = api.getMovieDetails(id)
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
            val movie = api.getTvShowDetails(id)
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

    suspend fun insert(watchlistItem: Show) {
        watchlistDao.insert(watchlistItem)
    }

    suspend fun delete(watchlistItem: Show) {
        watchlistDao.delete(watchlistItem)
    }
}