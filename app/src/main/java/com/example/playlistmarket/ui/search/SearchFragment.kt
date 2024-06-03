package com.example.playlistmarket.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.FragmentSearchBinding
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.presentation.search.SearchViewModel
import com.example.playlistmarket.ui.BindingFragment
import com.example.playlistmarket.ui.search.models.SearchState
import com.example.playlistmarket.utils.debounce
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyTrackAdapter: TrackAdapter
    private val searchViewModel by viewModel<SearchViewModel>()

    private var lastTrackRequest: String = ""
    private var simpleTextWatcher: TextWatcher? = null
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce =
            debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
                openPlayer(track)
            }

        searchViewModel.observeTracksHistory().observe(viewLifecycleOwner) { historyTracks ->
            historyTrackAdapter.updateTracks(historyTracks)
            if (historyTracks.isNotEmpty()) {
                binding.searchHistoryViewgroup.visibility = View.VISIBLE
                binding.placeholderImage.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.placeholderText.visibility = View.GONE
                binding.updateButton.visibility = View.GONE
            } else {
                binding.searchHistoryViewgroup.visibility = View.GONE
            }
            binding.clearButtonImageView.setOnClickListener {
                binding.searchInputEditText.setText("")
                trackAdapter.updateTracks(ArrayList())
                hideKeyboard()
                if (historyTracks.isNotEmpty()) {
                    binding.searchHistoryViewgroup.visibility = View.VISIBLE
                }
            }
        }

        binding.clearHistoryButton.setOnClickListener {
            binding.searchHistoryViewgroup.visibility = View.GONE
            lifecycleScope.launch { searchViewModel.clearHistory() }
        }

        searchViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        historyTrackAdapter = TrackAdapter() {
            onTrackClickDebounce(it)
        }
        trackAdapter = TrackAdapter() {
            onTrackClickDebounce(it)
            lifecycleScope.launch { searchViewModel.updateHistoryAdapterTracks(it) }
        }

        simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.trackListRecyclerView.visibility = View.GONE
                if (binding.searchInputEditText.text?.isNotEmpty() == true) {
                    searchViewModel.searchDebounce(s?.toString() ?: "")
                    binding.progressBar.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                lastTrackRequest = s.toString()
                clearButtonAndSearchHistoryGroupVisibility(s)
            }
        }

        binding.trackListRecyclerView.adapter = trackAdapter
        binding.historyRecyclerView.adapter = historyTrackAdapter

        binding.searchInputEditText.let {
            it.requestFocus()
            it.addTextChangedListener(simpleTextWatcher)
            it.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    lastTrackRequest = binding.searchInputEditText.text.toString()
                    if (lastTrackRequest.isNotEmpty()) {
                        searchViewModel.searchDebounce(binding.searchInputEditText.text.toString())
                    }
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }
        }

        binding.updateButton.setOnClickListener {
            searchViewModel.search(lastTrackRequest)
            if (binding.searchInputEditText.text?.isNotEmpty() == true) {
                binding.clearButtonImageView.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        searchViewModel.updateState()
    }
    override fun onDestroyView() {
        simpleTextWatcher?.let { binding.searchInputEditText.removeTextChangedListener(it) }
        super.onDestroyView()
    }

    private fun clearButtonAndSearchHistoryGroupVisibility(s: CharSequence?) {
        if (s.isNullOrEmpty()) {
            binding.clearButtonImageView.visibility = View.GONE
            binding.placeholderImage.visibility = View.GONE
            binding.placeholderText.visibility = View.GONE
            binding.placeholderAdditionalMessage.visibility = View.GONE
            binding.updateButton.visibility = View.GONE
            searchViewModel.observeTracksHistory().observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {
                    binding.searchHistoryViewgroup.visibility = View.VISIBLE
                }
            }

        } else {
            binding.clearButtonImageView.visibility = View.VISIBLE
            binding.searchHistoryViewgroup.visibility = View.GONE
        }
    }

    private fun openPlayer(track: Track) {
        searchViewModel.provideTrack(track)
        findNavController().navigate(R.id.action_searchFragment_to_playerFragment)
    }

    private fun hideKeyboard() {
        val view: View? = requireActivity().currentFocus
        if (view != null) {
            val inputMethodManager =
                requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.trackListRecyclerView.visibility = View.GONE
        binding.placeholderText.visibility = View.GONE
        binding.placeholderAdditionalMessage.visibility = View.GONE
        binding.placeholderImage.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
    }

    private fun showContent(tracks: List<Track>) {
        binding.progressBar.visibility = View.GONE
        if (lastTrackRequest.isNotEmpty()) {
            trackAdapter.updateTracks(tracks)
            binding.trackListRecyclerView.visibility = View.VISIBLE
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showError(errorMessage: String, additionalMessage: String) {
        binding.progressBar.visibility = View.GONE
        binding.placeholderText.visibility = View.VISIBLE
        binding.placeholderImage.visibility = View.VISIBLE
        trackAdapter.updateTracks(ArrayList())
        binding.placeholderText.text = errorMessage
        binding.placeholderAdditionalMessage.visibility = View.VISIBLE
        binding.placeholderAdditionalMessage.text = additionalMessage
        binding.placeholderImage.setImageDrawable(requireActivity().getDrawable(R.drawable.connection_problems))
        binding.updateButton.visibility = View.VISIBLE
        binding.searchHistoryViewgroup.visibility = View.GONE
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showNothingFoundMessage(emptyMessage: String) {
        binding.progressBar.visibility = View.GONE
        binding.placeholderText.visibility = View.VISIBLE
        binding.placeholderImage.visibility = View.VISIBLE
        trackAdapter.updateTracks(ArrayList())
        binding.placeholderText.text = emptyMessage
        binding.placeholderImage.setImageDrawable(requireActivity().getDrawable(R.drawable.nothing_was_found))
        binding.searchHistoryViewgroup.visibility = View.GONE
    }

    private fun render(state: SearchState) {
        when {
            state.isLoading -> showLoading()
            state.additionalMessage.isNotEmpty() -> showError(
                state.errorMessage,
                state.additionalMessage
            )

            state.tracks.isNotEmpty() -> showContent(state.tracks)
            else -> showNothingFoundMessage(state.errorMessage)
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}