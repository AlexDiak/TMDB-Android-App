package com.things.tothemovies.utils

import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

private const val BASE_POSTER_PATH = "https://image.tmdb.org/t/p/w500/"
const val YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v="
const val YOUTUBE_VIDEO_APP_URL = "https://www.youtube.com/watch?v="
const val YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/"

fun getPosterPath(posterPath: String?): String {
    return BASE_POSTER_PATH + posterPath
}

fun getYoutubeVideoPath(videoPath: String?): String {
    return YOUTUBE_VIDEO_URL + videoPath
}

fun getYoutubeVideoAppPath(videoPath: String?): String {
    return YOUTUBE_VIDEO_APP_URL + videoPath
}


fun getYoutubeThumbnailPath(thumbnailPath: String?): String {
    return "$YOUTUBE_THUMBNAIL_URL$thumbnailPath/0.jpg"
}

fun <T> SavedStateHandle.getStateFlow(
    scope: CoroutineScope,
    key: String,
    initialValue: T
): MutableStateFlow<T> {
    val liveData = getLiveData(key, initialValue)
    val stateFlow = NoCompareMutableStateFlow(initialValue)

    val observer = Observer<T> { value ->
        if (value != stateFlow.value) {
            stateFlow.value = value
        }
    }
    liveData.observeForever(observer)

    stateFlow.onCompletion {
        withContext(Dispatchers.Main.immediate) {
            liveData.removeObserver(observer)
        }
    }.onEach { value ->
        withContext(Dispatchers.Main.immediate) {
            if (liveData.value != value) {
                liveData.value = value
            }
        }
    }.launchIn(scope)

    return stateFlow
}

class NoCompareMutableStateFlow<T>(
    value: T
) : MutableStateFlow<T> {

    override var value: T = value
        set(value) {
            field = value
            innerFlow.tryEmit(value)
        }

    private val innerFlow = MutableSharedFlow<T>(replay = 1)

    override fun compareAndSet(expect: T, update: T): Boolean {
        value = update
        return true
    }

    override suspend fun emit(value: T) {
        this.value = value
    }

    override fun tryEmit(value: T): Boolean {
        this.value = value
        return true
    }

    override val subscriptionCount: StateFlow<Int> = innerFlow.subscriptionCount
    @ExperimentalCoroutinesApi
    override fun resetReplayCache() = innerFlow.resetReplayCache()
    override suspend fun collect(collector: FlowCollector<T>): Nothing = innerFlow.collect(collector)
    override val replayCache: List<T> = innerFlow.replayCache
}