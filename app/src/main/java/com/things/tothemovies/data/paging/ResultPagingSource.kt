package com.things.tothemovies.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.things.tothemovies.data.local.dao.WatchlistDao
import com.things.tothemovies.data.local.model.Show
import com.things.tothemovies.data.remote.TmdbApi

class ResultPagingSource(
    private val api: TmdbApi,
    private val watchlistDao: WatchlistDao,
    private val query: String,
    private val watchlistMode: Boolean
) : PagingSource<Int, Show>() {

    override fun getRefreshKey(state: PagingState<Int, Show>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Show> {
        return try {
            val position = params.key ?: 1

            val shows = if (watchlistMode)
                watchlistDao.findByTitle(query)
            else {
                if(query.isBlank()){
                    api.getTrendingMovies(position).results.map {
                        it.toShow()
                    }
                }else{
                    api.getSearchResults(query, position).results.map {
                        it.toShow()
                    }
                }
            }

            LoadResult.Page(
                data = shows, prevKey = if (position == 1) null else position - 1,
                nextKey = if (shows.isEmpty() || shows.size < 20) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}