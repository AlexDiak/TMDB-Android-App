package com.things.tothemovies.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.things.tothemovies.data.local.dao.WatchlistDao
import com.things.tothemovies.data.local.model.Show
import com.things.tothemovies.data.paging.ResultPagingSource
import com.things.tothemovies.data.remote.TmdbApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepository
@Inject constructor(
    private val api: TmdbApi,
    private val watchlistDao: WatchlistDao
) {

    fun getResults(query: String, watchlistMode: Boolean): Flow<PagingData<Show>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 1
            ),
            pagingSourceFactory = {
                ResultPagingSource(api, watchlistDao, query, watchlistMode)
            }, initialKey = 1
        ).flow
    }
}