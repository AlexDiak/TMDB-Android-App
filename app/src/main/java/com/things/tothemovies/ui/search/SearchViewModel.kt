package com.things.tothemovies.ui.search

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.things.tothemovies.data.remote.model.Result
import com.things.tothemovies.data.repository.SearchRepository
import com.things.tothemovies.util.Resource
import com.things.tothemovies.util.UiText
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.launch

class SearchViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = SearchRepository()

    private val _state = MutableStateFlow<List<Result>>(listOf())
    val state = _state.asStateFlow()

    private val currentQuery = savedStateHandle.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _eventFlow = Channel<UiText>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private var remoteSearchJob: Job = Job()

    fun getSearchMultiResults(query: String) {
        remoteSearchJob.cancel()
        remoteSearchJob = viewModelScope.launch {

            if (query.isEmpty()) {
                _isLoading.emit(false)
                _state.emit(emptyList())
                return@launch
            }

            _isLoading.emit(true)

            delay(300)

            when (val result = repository.getSearchResults(query, 1)) {
                is Resource.Success -> {
                    _state.emit(result.data?.results.orEmpty())
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

    val results = currentQuery.switchMap {
        repository.getResults(it).cachedIn(viewModelScope)
    }

    fun getSearchResults(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "avatar"
    }
}