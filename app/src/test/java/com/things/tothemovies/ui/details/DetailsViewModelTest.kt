package com.things.tothemovies.ui.details

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.google.gson.Gson
import com.things.tothemovies.data.remote.model.ApiDetails
import com.things.tothemovies.data.remote.model.ApiVideos
import com.things.tothemovies.data.repository.DetailsRepository
import com.things.tothemovies.utils.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.rules.TestRule
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.MockitoAnnotations.openMocks
import java.io.FileInputStream

@OptIn(ExperimentalCoroutinesApi::class)
internal class DetailsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var fakeDetailsRepository: DetailsRepository
    private lateinit var apiDetails: ApiDetails
    private val apiVideos = ApiVideos(0, emptyList())
    private val gson = Gson()
    private lateinit var viewModel: DetailsViewModel

    private val savedStateHandle = SavedStateHandle().apply {
        set(TYPE, TV_SHOW)
        set(ID, "87096")
    }

    @Before
    fun setUp() {

        openMocks(this)

        Dispatchers.setMain(UnconfinedTestDispatcher())

        val detailBytes = FileInputStream("src/main/assets/details.json").readBytes()
        apiDetails = gson.fromJson(String(detailBytes), ApiDetails::class.java)
    }

    @After
    fun reset() {
        Dispatchers.resetMain()
    }

    @Test
    fun `for success, data must be available && isLoading should be false && errorMessage should be null`() =
        runTest {
            given(
                fakeDetailsRepository.getShowDetails(
                    savedStateHandle[ID],
                    savedStateHandle[TYPE]
                )
            ).willReturn(flowOf(Resource.Success(data = apiDetails)))

            given(
                fakeDetailsRepository.getShowVideos(
                    savedStateHandle[ID],
                    savedStateHandle[TYPE]
                )
            ).willReturn(flowOf(Resource.Success(apiVideos)))

            viewModel = DetailsViewModel(fakeDetailsRepository, savedStateHandle)

            assertFalse(viewModel.state.value.isLoading)
            assertNull(viewModel.state.value.errorMessage)
            assertNotNull(viewModel.state.value.videos)
            assertEquals(apiDetails, viewModel.state.value.show)
        }

    @Test
    fun `for error, data should be null && isLoading should be false && errorMessage should be not null`() =
        runTest {
            given(
                fakeDetailsRepository.getShowDetails(
                    savedStateHandle[ID],
                    savedStateHandle[TYPE]
                )
            ).willReturn(flowOf(Resource.Error(uiText = UiText.unknownError())))

            given(
                fakeDetailsRepository.getShowVideos(
                    savedStateHandle[ID],
                    savedStateHandle[TYPE]
                )
            ).willReturn(flowOf(Resource.Error(uiText = UiText.unknownError())))

            viewModel = DetailsViewModel(fakeDetailsRepository, savedStateHandle)

            assertNull(viewModel.state.value.show)
            assertNull(viewModel.state.value.videos)
            assertFalse(viewModel.state.value.isLoading)
            assertNotNull(viewModel.state.value.errorMessage)
        }

    @Test
    fun `for error details && success videos, show == null && videos == apiVideos && isLoading==false && errorMessage!=null`() =
        runTest {
            given(
                fakeDetailsRepository.getShowDetails(
                    savedStateHandle[ID],
                    savedStateHandle[TYPE]
                )
            ).willReturn(flowOf(Resource.Error(uiText = UiText.unknownError())))

            given(
                fakeDetailsRepository.getShowVideos(
                    savedStateHandle[ID],
                    savedStateHandle[TYPE]
                )
            ).willReturn(
                flowOf(Resource.Success(data = apiVideos))
            )

            viewModel = DetailsViewModel(fakeDetailsRepository, savedStateHandle)

            assertNull(viewModel.state.value.show)
            assertEquals(apiVideos, viewModel.state.value.videos)
            assertFalse(viewModel.state.value.isLoading)
            assertNotNull(viewModel.state.value.errorMessage)
        }

    @Test
    fun `for success details && error videos, show == apiDetails && videos == null && isLoading==false && errorMessage!=null`() =
        runTest {
            given(
                fakeDetailsRepository.getShowDetails(
                    savedStateHandle[ID],
                    savedStateHandle[TYPE]
                )
            ).willReturn(flowOf(Resource.Success(data = apiDetails)))

            given(
                fakeDetailsRepository.getShowVideos(
                    savedStateHandle[ID],
                    savedStateHandle[TYPE]
                )
            ).willReturn(flowOf(Resource.Error(uiText = UiText.unknownError())))

            viewModel = DetailsViewModel(fakeDetailsRepository, savedStateHandle)

            assertEquals(apiDetails, viewModel.state.value.show)
            assertNull(viewModel.state.value.videos)
            assertFalse(viewModel.state.value.isLoading)
            assertNotNull(viewModel.state.value.errorMessage)
        }
}