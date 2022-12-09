package com.things.tothemovies.data.paging

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.google.gson.Gson
import com.things.tothemovies.data.local.model.Show
import com.things.tothemovies.data.remote.TmdbApi
import com.things.tothemovies.data.remote.model.ApiPaginatedSearch
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.BDDMockito.anyInt
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.io.FileInputStream

@RunWith(MockitoJUnitRunner::class)
internal class ResultPagingSourceTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var api: TmdbApi

    private lateinit var resultPagingSource: ResultPagingSource
    private lateinit var movieViewList: List<Show>
    private val gson = Gson()
    private lateinit var movieResponse: ApiPaginatedSearch

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        val listBytes = FileInputStream("src/main/assets/list.json").readBytes()

        movieResponse = gson.fromJson(String(listBytes), ApiPaginatedSearch::class.java)
        movieViewList = movieResponse.results.map { movie ->
            movie.toShow()
        }

        resultPagingSource = ResultPagingSource(api, "")
    }

    @Test
    fun `results paging source refresh - success`() = runBlocking {
        given(api.getTrendingMovies(anyInt())).willReturn(movieResponse)

        val expectedResult = PagingSource.LoadResult.Page(
            data = movieViewList,
            prevKey = null,
            nextKey = 2
        )

        assertEquals(
            expectedResult,
            resultPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 1,
                    loadSize = 20,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun `results paging source append - success`() = runBlocking {
        given(api.getTrendingMovies(anyInt())).willReturn(movieResponse)

        val expectedResult = PagingSource.LoadResult.Page(
            data = movieViewList,
            prevKey = 1,
            nextKey = 3
        )

        assertEquals(
            expectedResult, resultPagingSource.load(
                PagingSource.LoadParams.Append(
                    key = 2,
                    loadSize = 20,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun `results paging source prepend - success`() = runBlocking {
        given(api.getTrendingMovies(anyInt())).willReturn(movieResponse)

        val expectedResult = PagingSource.LoadResult.Page(
            data = movieViewList,
            prevKey = null,
            nextKey = 2
        )

        assertEquals(
            expectedResult, resultPagingSource.load(
                PagingSource.LoadParams.Prepend(
                    key = 1,
                    loadSize = 20,
                    placeholdersEnabled = false
                )
            )
        )
    }
}