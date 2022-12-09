package com.things.tothemovies.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.things.tothemovies.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _watchListModeState = MutableStateFlow(false)
    private val watchListModeState = _watchListModeState.asStateFlow()

    val currentQuery = savedStateHandle.getStateFlow(CURRENT_QUERY, DEFAULT_QUERY)

    @OptIn(ExperimentalCoroutinesApi::class)
    val results = savedStateHandle.getStateFlow(CURRENT_QUERY, DEFAULT_QUERY).flatMapLatest {
        repository.getResults(it, watchListModeState.value)
    }.cachedIn(viewModelScope)

    fun getSearchResults(query: String) {
        savedStateHandle[CURRENT_QUERY] = query
    }

    fun setWatchlistMode(watchlistMode: Boolean) {
        _watchListModeState.value = watchlistMode
    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = ""
    }
}