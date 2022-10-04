package com.things.tothemovies.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.things.tothemovies.data.local.model.Show
import com.things.tothemovies.data.remote.model.ApiDetails
import com.things.tothemovies.data.remote.model.ApiVideos
import com.things.tothemovies.data.repository.DetailsRepository
import com.things.tothemovies.util.MOVIE
import com.things.tothemovies.util.Resource
import com.things.tothemovies.util.TV_SHOW
import com.things.tothemovies.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: DetailsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<ApiDetails?>(null)
    val state = _state.asStateFlow()

    private val _stateVideos = MutableStateFlow<ApiVideos?>(null)
    val stateVideos = _stateVideos.asStateFlow()

    private val _showExists = MutableStateFlow(false)
    val showExists = _showExists.asStateFlow()

    private val _eventFlow = Channel<UiText>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private var remoteSearchJob: Job = Job()

    init {
        val type = savedStateHandle.get<String>("type")
        val id = savedStateHandle.get<Int>("id")
        when (type) {
            MOVIE -> {
                id?.let {
                    getMovieDetails(it)
                    getMovieVideos(it)
                }
            }
            TV_SHOW -> {
                id?.let {
                    getTvShowDetails(it)
                    getTvShowVideos(it)
                }
            }
        }

        id?.let { showExists(it) }
    }

    private fun getMovieDetails(id: Int) {
        remoteSearchJob = viewModelScope.launch {

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
        }
    }

    private fun getMovieVideos(id: Int) {
        remoteSearchJob = viewModelScope.launch {

            when (val result = repository.getMovieVideos(id)) {
                is Resource.Success -> {
                    _stateVideos.emit(result.data)
                }
                is Resource.Error -> {
                    _eventFlow.send(
                        result.uiText ?: UiText.unknownError()
                    )
                }
            }
        }
    }

    private fun getTvShowDetails(id: Int) {
        remoteSearchJob = viewModelScope.launch {

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
        }
    }

    private fun getTvShowVideos(id: Int) {
        remoteSearchJob = viewModelScope.launch {

            when (val result = repository.getTvShowVideos(id)) {
                is Resource.Success -> {
                    _stateVideos.emit(result.data)
                }
                is Resource.Error -> {
                    _eventFlow.send(
                        result.uiText ?: UiText.unknownError()
                    )
                }
            }
        }
    }

    fun addToWatchlist(id: Int, title: String, year: String, imgPath: String, mediaType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (id != -1) {
                repository.insert(Show(id, title, year, imgPath, mediaType))
                showExists(id)
            }
        }
    }

    fun removeFromWatchlist(show: Show) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(show)
            showExists(show.id)
        }
    }

    private fun showExists(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (id != -1)
                _showExists.emit(repository.showExists(id))
        }
    }
}