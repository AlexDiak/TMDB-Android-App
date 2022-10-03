package com.things.tothemovies.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.things.tothemovies.databinding.FragmentDetailsBinding
import com.things.tothemovies.util.TYPE
import com.things.tothemovies.util.UiText
import com.things.tothemovies.util.setImage
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
                        binding.photo.setImage(it?.poster_path)
                        binding.genre.text = it?.genres?.getOrNull(0)?.name
                        binding.name.text = it?.name ?: it?.title
                        binding.summary.text = it?.overview

                        binding.addToWatchlist.setOnClickListener { _ ->
                            viewModel.addToWatchlist(
                                it?.id ?: -1,
                                (it?.name ?: it?.title).toString(),
                                it?.poster_path.toString(),
                                mediaType = arguments?.getString(TYPE) ?: ""
                            )
                        }
                    }
                }

                launch {
                    viewModel.isLoading.collect {
                        //binding.progressBar.isVisible = it
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}