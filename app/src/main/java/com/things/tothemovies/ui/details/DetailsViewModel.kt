package com.things.tothemovies.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.things.tothemovies.data.local.model.Show
import com.things.tothemovies.data.remote.model.ApiDetails
import com.things.tothemovies.data.remote.model.ApiVideos
import com.things.tothemovies.data.repository.DetailsRepository
import com.things.tothemovies.utils.ID
import com.things.tothemovies.utils.Resource
import com.things.tothemovies.utils.TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: DetailsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsScreenState())
    val state = _state.asStateFlow()

    private val _details = MutableStateFlow<ApiDetails?>(null)
    private val details = _details.asStateFlow()

    private val _videos = MutableStateFlow<ApiVideos?>(null)
    private val videos = _videos.asStateFlow()

    private val _showExists = MutableStateFlow(false)
    val showExists = _showExists.asStateFlow()

    init {
        details.combine(videos) { details, videos ->
            _state.update {
                state.value.copy(
                    show = details,
                    videos = videos,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)

        val type = checkNotNull(savedStateHandle[TYPE]).toString()
        val id = checkNotNull(savedStateHandle[ID]).toString()
        getShowDetails(id, type)
        getShowVideos(id, type)
        showExists(id.toInt())
    }

    private fun getShowDetails(id: String, type: String) {
        viewModelScope.launch {

            _state.update {
                state.value.copy(
                    isLoading = true
                )
            }

            repository.getShowDetails(id, type).collect { resource ->
                when (resource) {
                    is Resource.Success -> _details.update { resource.data }
                    is Resource.Error -> _state.update {
                        state.value.copy(
                            show = null,
                            isLoading = false,
                            errorMessage = resource.uiText
                        )
                    }
                }
            }
        }
    }

    private fun getShowVideos(id: String, type: String) {
        viewModelScope.launch {

            _state.update {
                state.value.copy(
                    isLoading = true
                )
            }

            repository.getShowVideos(id, type).collect { resource ->
                when (resource) {
                    is Resource.Success -> _videos.update { resource.data }
                    is Resource.Error -> _state.update {
                        state.value.copy(
                            videos = null,
                            isLoading = false,
                            errorMessage = resource.uiText
                        )
                    }
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