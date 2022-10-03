package com.things.tothemovies.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.things.tothemovies.data.local.dao.WatchlistDao
import com.things.tothemovies.data.local.model.Show
import com.things.tothemovies.data.remote.TmdbApi
import com.things.tothemovies.data.paging.ResultPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SearchRepository
@Inject constructor(
    private val api: TmdbApi,
    private val watchlistDao: WatchlistDao
) {

    fun getResults(query: String, watchlistMode: Boolean): Flow<PagingData<Show>> {

        if(query.isEmpty() && !watchlistMode)
            return MutableStateFlow(PagingData.empty())

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