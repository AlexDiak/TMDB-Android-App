package com.things.tothemovies.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.things.tothemovies.databinding.FragmentSearchBinding
import com.things.tothemovies.utils.UiText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    companion object {
        private const val ITEMS_PER_ROW = 2
    }

    private val binding get() = _binding!!
    private var _binding: FragmentSearchBinding? = null

    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {
        val adapter = ResultsAdapter()
        setupRecyclerView(adapter)
        observeViewStateUpdates(adapter)

        setupSearchViewListener()

        binding.watchlistMode.setOnClickListener {
            viewModel.setWatchlistMode((it as Chip).isChecked)
        }
    }

    private fun setupRecyclerView(searchAdapter: ResultsAdapter) {
        binding.resultsRecyclerView.apply {
            adapter = searchAdapter
            layoutManager = GridLayoutManager(requireContext(), ITEMS_PER_ROW)
        }
        searchAdapter.onClick = { type, id ->
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToDetailsFragment(
                    type,
                    id
                )
            )
        }
    }

    private fun observeViewStateUpdates(searchAdapter: ResultsAdapter) {

        viewModel.results.observe(viewLifecycleOwner) {
            searchAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    searchAdapter.loadStateFlow.collectLatest {
                        binding.progressBar.isVisible = it.refresh is LoadState.Loading
                    }
                }

                launch {
                    viewModel.watchListModeState.collect {
                        binding.watchlistMode.isChecked = it
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

    private fun setupSearchViewListener() {
        binding.searchMulti.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    binding.searchMulti.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.getSearchResults(newText.orEmpty())
                    return true
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}