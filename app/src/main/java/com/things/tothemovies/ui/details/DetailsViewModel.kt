package com.things.tothemovies.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.things.tothemovies.data.remote.model.ApiDetails
import com.things.tothemovies.data.repository.DetailsRepository
import com.things.tothemovies.util.MOVIE
import com.things.tothemovies.util.Resource
import com.things.tothemovies.util.TV_SHOW
import com.things.tothemovies.util.UiText
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DetailsViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val repository = DetailsRepository()

    private val _state = MutableStateFlow<ApiDetails?>(null)
    val state = _state.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _eventFlow = Channel<UiText>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private var remoteSearchJob: Job = Job()

    init {
        val type = savedStateHandle.get<String>("type")
        val id = savedStateHandle.get<Int>("id")
        when (type) {
            MOVIE -> {
                id?.let { getMovieDetails(it) }
            }
            TV_SHOW -> {
                id?.let { getTvShowDetails(it) }
            }
        }
    }

    private fun getMovieDetails(id: Int) {
        remoteSearchJob = viewModelScope.launch {
            _isLoading.emit(true)
            when (val result = repository.getMovieDetails(id)) {
                is Resource.Success -> {
                    _state.emit(result.data)
                }
                is Resource.Error -> {
                    _eventFlow.send(
                        result.uiText ?: UiText.unknownError()
                    )
                }
            }
            _isLoading.emit(false)
        }
    }

    private fun getTvShowDetails(id: Int) {
        remoteSearchJob = viewModelScope.launch {
            _isLoading.emit(true)
            when (val result = repository.getTvShowDetails(id)) {
                is Resource.Success -> {
                    _state.emit(result.data)
                }
                is Resource.Error -> {
                    _eventFlow.send(
                        result.uiText ?: UiText.unknownError()
                    )
                }
            }
            _isLoading.emit(false)
        }
    }
}