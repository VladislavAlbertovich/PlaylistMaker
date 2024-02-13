package com.example.playlistmarket.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.ActivitySearchBinding
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.presentation.search.SearchViewModel
import com.example.playlistmarket.ui.player.PlayerActivity
import com.example.playlistmarket.ui.search.models.SearchState

class SearchActivity : AppCompatActivity() {

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyTrackAdapter: TrackAdapter
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchViewModel: SearchViewModel

    private var lastTrackRequest: String = ""
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private var simpleTextWatcher: TextWatcher? = null
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {

        searchViewModel =
            ViewModelProvider(this, SearchViewModel.getViewModelFactory())[SearchViewModel::class.java]

        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchViewModel.observeTracksHistory().observe(this){
            if (it.size>0){
                binding.searchHistoryViewgroup.visibility = View.VISIBLE
            }
            historyTrackAdapter.updateTracks(it)
        }

        searchViewModel.observeState().observe(this){
            render(it)
        }

        historyTrackAdapter = TrackAdapter() {
            if (clickDebounce()) {
                openPlayerActivity(it)
            }
        }
        trackAdapter = TrackAdapter() {
            if (clickDebounce()) {
                searchViewModel.updateHistoryAdapterTracks(it)
                openPlayerActivity(it)
            }
        }

        simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setSearchHistoryViewGroupVisibility(s)
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
        simpleTextWatcher?.let { binding.searchInputEditText.addTextChangedListener(it) }

        binding.trackListRecyclerView.adapter = trackAdapter
        binding.historyRecyclerView.adapter = historyTrackAdapter

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.clearButtonImageView.setOnClickListener {
            binding.searchInputEditText.setText("")
            trackAdapter.updateTracks(ArrayList())
            hideKeyboard()
            binding.placeholderImage.visibility = View.GONE
            binding.placeholderText.visibility = View.GONE
            binding.placeholderAdditionalMessage.visibility = View.GONE
        }

        binding.clearHistoryButton.setOnClickListener {
            binding.searchHistoryViewgroup.visibility = View.GONE
            searchViewModel.clearHistory()
        }

        binding.searchInputEditText.let {
            it.requestFocus()
            if (it.hasFocus() && historyIsNotEmpty()) {
                binding.searchHistoryViewgroup.visibility = View.VISIBLE
            }
            it.postDelayed(object : Runnable {
                override fun run() {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
                }
            }, 1000)
            it.addTextChangedListener(simpleTextWatcher)
            it.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    lastTrackRequest = binding.searchInputEditText.text.toString()
                    if (lastTrackRequest.isNotEmpty()) {
                        searchViewModel.searchDebounce(binding.searchInputEditText.text.toString())
                    }
                    true
                } else {
                    false
                }
            }
            it.setOnFocusChangeListener { _, hasFocus ->
                setSearchHistoryViewGroupVisibility(it.text)

            }
        }

        binding.updateButton.setOnClickListener {
            searchViewModel.searchDebounce(lastTrackRequest)
            if (binding.searchInputEditText.text?.isNotEmpty() == true) {
                binding.clearButtonImageView.visibility = View.VISIBLE
            }
        }
    }

    private fun historyIsNotEmpty(): Boolean{
        var tracks = ArrayList<Track>()
        searchViewModel.observeTracksHistory().observe(this){
            tracks = it
        }
        return tracks.isNotEmpty()
    }
    override fun onDestroy() {
        super.onDestroy()
        simpleTextWatcher?.let { binding.searchInputEditText.removeTextChangedListener(it) }
        searchViewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USER_INPUT, binding.searchInputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.searchInputEditText.setText(savedInstanceState.getString(USER_INPUT))
    }

    private fun setSearchHistoryViewGroupVisibility(s: CharSequence?) {
        binding.searchHistoryViewgroup.visibility =
            if (binding.searchInputEditText.hasFocus() && s?.isEmpty() == true && historyIsNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun clearButtonAndSearchHistoryGroupVisibility(s: CharSequence?) {
        if (s.isNullOrEmpty()) {
            binding.clearButtonImageView.visibility = View.GONE
            setSearchHistoryViewGroupVisibility(s)
            binding.placeholderImage.visibility = View.GONE
            binding.placeholderText.visibility = View.GONE
            binding.placeholderAdditionalMessage.visibility = View.GONE

        } else {
            binding.clearButtonImageView.visibility = View.VISIBLE
            binding.searchHistoryViewgroup.visibility = View.GONE
        }
    }
    private fun openPlayerActivity(track: Track) {
        searchViewModel.provideTrack(track)
        val intentPlayerActivity = Intent(this, PlayerActivity::class.java)
        startActivity(intentPlayerActivity)
    }

    private fun hideKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }

    private fun clickDebounce(): Boolean {
        var current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun showLoading() {
        binding.progressBar.visibility= View.VISIBLE
        binding.trackListRecyclerView.visibility = View.GONE
        binding.placeholderText.visibility = View.GONE
        binding.placeholderAdditionalMessage.visibility = View.GONE
        binding.placeholderImage.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
        hideKeyboard()
    }

    private fun showContent(tracks: ArrayList<Track>) {
        if (lastTrackRequest.isNotEmpty()) {
            binding.progressBar.visibility = View.GONE
            trackAdapter.updateTracks(tracks)
            binding.trackListRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun showError(errorMessage: String, additionalMessage: String) {
        binding.progressBar.visibility = View.GONE
        binding.placeholderText.visibility = View.VISIBLE
        binding.placeholderImage.visibility = View.VISIBLE
        trackAdapter.updateTracks(ArrayList())
        binding.placeholderText.text = errorMessage
        binding.placeholderAdditionalMessage.visibility = View.VISIBLE
        binding.placeholderAdditionalMessage.text = additionalMessage
        binding.placeholderImage.setImageDrawable(getDrawable(R.drawable.connection_problems))
    }

    private fun showNothingFoundMessage(emptyMessage: String) {
        binding.progressBar.visibility = View.GONE
        binding.placeholderText.visibility = View.VISIBLE
        binding.placeholderImage.visibility = View.VISIBLE
        trackAdapter.updateTracks(ArrayList())
        binding.placeholderText.text = emptyMessage
        binding.placeholderImage.setImageDrawable(getDrawable(R.drawable.nothing_was_found))
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
        const val USER_INPUT = "USER_INPUT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}

