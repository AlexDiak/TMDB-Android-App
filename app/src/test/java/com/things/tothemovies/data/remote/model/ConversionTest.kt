package com.things.tothemovies.data.remote.model

import com.google.gson.Gson
import com.things.tothemovies.data.local.model.Show
import com.things.tothemovies.utils.MOVIE
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations.openMocks
import org.mockito.junit.MockitoJUnitRunner
import java.io.FileInputStream

@RunWith(MockitoJUnitRunner::class)
internal class ConversionTest {


    private val gson = Gson()
    private lateinit var apiDetails: ApiDetails
    private lateinit var result: Result

    @Before
    fun setup() {
        openMocks(this)

        val detailBytes = FileInputStream("src/main/assets/details.json").readBytes()
        val listBytes = FileInputStream("src/main/assets/list.json").readBytes()

        apiDetails = gson.fromJson(String(detailBytes), ApiDetails::class.java)
        result = gson.fromJson(String(listBytes), ApiPaginatedSearch::class.java).results[0]

    }

    @Test
    fun test_detail_conversion_should_pass() {
        val show = apiDetails.toShow(MOVIE)
        val expectedMovieDetail = Show(
            title = "Avatar: The Way of Water",
            id = 76600,
            year = "2022-12-14",
            posterPath = "/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg",
            mediaType = "movie",
            runtime = 192
        )
        assertEquals(expectedMovieDetail, show)
    }

    @Test
    fun test_list_conversion_should_pass() {
        val show = result.toShow()
        val expectedMovieView = Show(
            title = "Wednesday",
            id = 119051,
            year = "2022-11-23",
            posterPath = "/jeGtaMwGxPmQN5xM4ClnwPQcNQz.jpg",
            mediaType = "tv",
            runtime = -1
        )
        assertEquals(expectedMovieView, show)
    }
}