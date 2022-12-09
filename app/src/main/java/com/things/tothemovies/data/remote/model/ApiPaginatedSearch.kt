package com.things.tothemovies.data.remote.model

import com.things.tothemovies.data.local.model.Show

data class ApiPaginatedSearch(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)

data class Result(
    val adult: Boolean,
    val backdrop_path: String,
    val first_air_date: String,
    val gender: Int,
    val genre_ids: List<Int>,
    val id: Int,
    val known_for: List<KnownFor>,
    val known_for_department: String,
    val media_type: String,
    val name: String,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val profile_path: String,
    val release_date: String?,
    val title: String?,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
){
    fun toShow(): Show {
        return Show(
            id = id,
            title = title?:name,
            year = release_date?:first_air_date,
            posterPath = poster_path,
            mediaType = media_type
        )
    }
}

data class KnownFor(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val media_type: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)