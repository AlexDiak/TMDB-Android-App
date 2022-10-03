package com.things.tothemovies.ui.search

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.things.tothemovies.data.remote.model.Result
import com.things.tothemovies.data.repository.SearchRepository
import com.things.tothemovies.util.Resource
import com.things.tothemovies.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _watchListModeState = MutableStateFlow(false)
    val watchListModeState = _watchListModeState.asStateFlow()

    private val currentQuery = savedStateHandle.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    private val _eventFlow = Channel<UiText>()
    val eventFlow = _eventFlow.receiveAsFlow()

    val results = currentQuery.switchMap {
        repository.getResults(it, watchListModeState.value).cachedIn(viewModelScope).asLiveData()
    }

    fun getSearchResults(query: String) {
        currentQuery.value = query
    }

    fun setWatchlistMode(watchlistMode: Boolean) {
        _watchListModeState.value = watchlistMode
        getSearchResults(currentQuery.value.toString())
    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = ""
    }
}