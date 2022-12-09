package com.things.tothemovies.ui.details

import com.things.tothemovies.data.remote.model.ApiDetails
import com.things.tothemovies.data.remote.model.ApiVideos
import com.things.tothemovies.utils.UiText

data class DetailsScreenState(
    val show: ApiDetails? = null,
    val videos: ApiVideos? = null,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)
