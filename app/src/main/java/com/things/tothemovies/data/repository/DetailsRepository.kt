package com.things.tothemovies.data.repository

import com.things.tothemovies.R
import com.things.tothemovies.data.local.dao.WatchlistDao
import com.things.tothemovies.data.local.model.Show
import com.things.tothemovies.data.remote.TmdbApi
import com.things.tothemovies.utils.MOVIE
import com.things.tothemovies.utils.Resource
import com.things.tothemovies.utils.TV_SHOW
import com.things.tothemovies.utils.UiText
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DetailsRepository @Inject constructor(
    private val api: TmdbApi,
    private val watchlistDao: WatchlistDao
) {

    fun getShowDetails(id: String?, type: String?) = flow {

        if (id == null || type == null) {
            emit(Resource.Error(uiText = UiText.unknownError()))
            return@flow
        }
        try {
            val movie = when (type) {
                MOVIE -> api.getMovieDetails(id.toInt())
                TV_SHOW -> api.getTvShowDetails(id.toInt())
                else -> null
            }
            emit(Resource.Success(data = movie))

        } catch (e: IOException) {
            emit(
                Resource.Error(
                    uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    uiText = UiText.StringResource(R.string.oops_something_went_wrong)
                )
            )
        }
    }

    suspend fun getShowVideos(id: String?, type: String?) = flow {

        if (id == null || type == null) {
            emit(Resource.Error(uiText = UiText.unknownError()))
            return@flow
        }
        try {
            val videos = when (type) {
                MOVIE -> api.getMovieVideos(id.toInt())
                TV_SHOW -> api.getTvShowVideos(id.toInt())
                else -> null
            }
            emit(Resource.Success(data = videos))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    uiText = UiText.StringResource(R.string.oops_something_went_wrong)
                )
            )
        }
    }

    suspend fun insert(watchlistItem: Show) {
        watchlistDao.insert(watchlistItem)
    }

    suspend fun delete(show: Show) {
        watchlistDao.delete(show)
    }

    suspend fun showExists(id: Int): Boolean {
        return watchlistDao.showExists(id)
    }

}