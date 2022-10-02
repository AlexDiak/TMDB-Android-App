package com.things.tothemovies.data.repository.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.things.tothemovies.data.remote.TmdbApi
import com.things.tothemovies.data.remote.model.Result

class ResultPagingSource(
    private val api: TmdbApi,
    private val query: String
) : PagingSource<Int, Result>() {

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val position = params.key ?: 1
            val response = api.getSearchResults(query, position)
            LoadResult.Page(
                data = response.results, prevKey = if (position == 1) null else position - 1,
                nextKey = position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}