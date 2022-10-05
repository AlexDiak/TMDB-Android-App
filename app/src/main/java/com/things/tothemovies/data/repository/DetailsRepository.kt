package com.things.tothemovies.data.repository

import com.things.tothemovies.R
import com.things.tothemovies.data.local.dao.WatchlistDao
import com.things.tothemovies.data.local.model.Show
import com.things.tothemovies.data.remote.TmdbApi
import com.things.tothemovies.data.remote.model.ApiDetails
import com.things.tothemovies.data.remote.model.ApiVideos
import com.things.tothemovies.utils.Resource
import com.things.tothemovies.utils.UiText
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

    suspend fun getMovieVideos(id: Int): Resource<ApiVideos> {
        return try {
            val movie = api.getMovieVideos(id)
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

    suspend fun getTvShowVideos(id: Int): Resource<ApiVideos> {
        return try {
            val tvShow = api.getTvShowVideos(id)
            Resource.Success(data = tvShow)

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

    suspend fun delete(show: Show) {
        watchlistDao.delete(show)
    }

    suspend fun showExists(id: Int) : Boolean {
        return watchlistDao.showExists(id)
    }

}