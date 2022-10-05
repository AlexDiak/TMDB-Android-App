package com.things.tothemovies.ui.details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.things.tothemovies.databinding.FragmentDetailsBinding
import com.things.tothemovies.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentDetailsBinding? = null

    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {
        observeViewStateUpdates()
    }

    private fun observeViewStateUpdates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.state.collect {
                        binding.photo.setImage(getPosterPath(it?.poster_path))
                        binding.genre.text = it?.genres?.getOrNull(0)?.name
                        binding.name.text = it?.name ?: it?.title
                        binding.summary.text = it?.overview
                        binding.addToWatchlist.setOnClickListener { _ ->
                            viewModel.addToWatchlist(
                                it?.id ?: -1,
                                (it?.name ?: it?.title).toString(),
                                (it?.release_date ?: it?.first_air_date).toString(),
                                it?.poster_path.toString(),
                                mediaType = arguments?.getString(TYPE) ?: ""
                            )
                        }
                        binding.removeFromWatchlist.setOnClickListener { _ ->
                            it?.toShow(arguments?.getString(TYPE) ?: "")?.let { it1 ->
                                viewModel.removeFromWatchlist(
                                    it1
                                )
                            }
                        }
                    }
                }

                launch {
                    viewModel.stateVideos.collect {

                        if (it?.results?.isEmpty() == true) {
                            binding.trailerTitle.isVisible = false
                            binding.trailerThumbnail.isVisible = false
                            return@collect
                        }

                        binding.trailerThumbnail.setImage(getYoutubeThumbnailPath(it?.results?.get(0)?.key))
                        binding.trailerThumbnail.setOnClickListener { _ ->
                            it?.results?.get(0)?.key?.let { it1 -> openYoutubeLink(it1) }
                        }
                    }
                }

                launch {
                    viewModel.showExists.collect {
                        binding.addToWatchlist.isVisible = !it
                        binding.removeFromWatchlist.isVisible = it
                    }
                }

                launch {
                    viewModel.eventFlow.collect {

                        val value = when (it) {
                            is UiText.DynamicString -> {
                                it.value
                            }
                            is UiText.StringResource -> {
                                getString(it.id)
                            }
                        }

                        Snackbar.make(
                            requireView(),
                            value,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun openYoutubeLink(videoKey: String) {
        val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse(getYoutubeVideoAppPath(videoKey)))
        val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(getYoutubeVideoPath(videoKey)))
        try {
            this.startActivity(intentApp)
        } catch (ex: ActivityNotFoundException) {
            this.startActivity(intentBrowser)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}