package com.example.playlistmarket

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {


    private var inputText: Editable? = null
    private lateinit var inputSearchEditText: TextInputEditText
    private lateinit var placeHolderMessage: TextView
    private lateinit var placeHolderAdditionalMessage: TextView
    private lateinit var placeHolderImage: ImageView
    private lateinit var updateButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var searchHistoryViewGroup: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var searchHistory: SearchHistory
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyTrackAdapter: TrackAdapter
    private lateinit var progressBar: ProgressBar

    private val retrofit =
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

    private val iTunesSearchService = retrofit.create(ITunesSearchApi::class.java)
    private var tracks = ArrayList<Track>()
    private var lastTrackRequest = ""
    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search() }

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputSearchEditText = findViewById<TextInputEditText>(R.id.searchInputEditText)
        placeHolderMessage = findViewById<TextView>(R.id.placeholder_text)
        placeHolderAdditionalMessage = findViewById<TextView>(R.id.placeholder_additional_message)
        placeHolderImage = findViewById<ImageView>(R.id.placeholder_image)
        updateButton = findViewById<Button>(R.id.update_button)
        recyclerView = findViewById<RecyclerView>(R.id.trackListRecyclerView)
        searchHistoryViewGroup = findViewById<LinearLayout>(R.id.search_history_viewgroup)
        historyRecyclerView = findViewById<RecyclerView>(R.id.history_recycler_view)
        progressBar = findViewById(R.id.progressBar)

        val clearButton = findViewById<ImageView>(R.id.clearButtonImageView)
        val buttonBack = findViewById<ImageView>(R.id.buttonBack)
        val clearHistoryButton = findViewById<Button>(R.id.clear_history_button)

        sharedPreferences =
            getSharedPreferences(TRACKS_HISTORY_SHARED_PREFERENCES_KEY, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)


        historyTrackAdapter = TrackAdapter() {
            if (clickDebounce()) {
                openPlayerActivity(it)
            }
        }
        historyTrackAdapter.updateTracks(getTracksFromHistory())
        trackAdapter = TrackAdapter() {
            if (clickDebounce()) {
                searchHistory.addTrackToSearchHistory(it)
                historyTrackAdapter.updateTracks(getTracksFromHistory())
                openPlayerActivity(it)
            }
        }

        val listener = OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == TRACKS_KEY) {
                val jsonTracks = sharedPreferences.getString(TRACKS_KEY, null)
                if (jsonTracks != null) {
                    val historyTracks = searchHistory.createTracksFromJson(jsonTracks)
                    historyTrackAdapter.updateTracks(historyTracks)
                } else {
                    historyTrackAdapter.updateTracks(ArrayList())
                }
            }
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setSearchHistoryViewGroupVisibility(s)
                recyclerView.visibility = View.GONE
                if (inputSearchEditText.text?.isNotEmpty() == true) {
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                clearButton.visibility = clearButtonVisibility(s)
                inputText = s
                setSearchHistoryViewGroupVisibility(s)
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        recyclerView.adapter = trackAdapter
        historyRecyclerView.adapter = historyTrackAdapter

        buttonBack.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputSearchEditText.setText("")
            tracks.clear()
            trackAdapter.updateTracks(tracks)
            hideKeyboard()
            placeHolderImage.visibility = View.GONE
            placeHolderMessage.visibility = View.GONE
            placeHolderAdditionalMessage.visibility = View.GONE
        }

        clearHistoryButton.setOnClickListener {
            searchHistoryViewGroup.visibility = View.GONE
            searchHistory.clearSharedPreferences()
            trackAdapter.notifyDataSetChanged()
        }

        inputSearchEditText.let {
            it.requestFocus()
            if (it.hasFocus() && getTracksFromHistory().size > 0) {
                searchHistoryViewGroup.visibility = View.VISIBLE
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
                    if (inputSearchEditText.text?.isNotEmpty() == true) {
                        search()
                    }
                    true
                } else {
                    false
                }
            }
            it.setOnFocusChangeListener { _, hasFocus ->
                searchHistoryViewGroup.visibility =
                    if (hasFocus && it.text.isNullOrEmpty() && getTracksFromHistory().size > 0) View.VISIBLE else View.GONE
            }
        }

        updateButton.setOnClickListener {
            search()
            if (lastTrackRequest.isNotEmpty()) {
                clearButton.visibility = View.VISIBLE
            }
        }
    }

    private fun getTracksFromHistory() = searchHistory.getTracksFromSharedPreferences()

    private fun setSearchHistoryViewGroupVisibility(s: CharSequence?) {
        searchHistoryViewGroup.visibility =
            if (inputSearchEditText.hasFocus() && s?.isEmpty() == true && getTracksFromHistory().size > 0) View.VISIBLE else View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USER_INPUT, inputSearchEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputSearchEditText.setText(savedInstanceState.getString(USER_INPUT))
    }

    private fun openPlayerActivity(track: Track) {
        val intentPlayerActivity = Intent(this, PlayerActivity::class.java)
        intentPlayerActivity.putExtra(TRACK, Gson().toJson(track))
        startActivity(intentPlayerActivity)
    }

    private fun search() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE
        placeHolderMessage.visibility = View.GONE
        placeHolderAdditionalMessage.visibility = View.GONE
        placeHolderImage.visibility = View.GONE
        updateButton.visibility = View.GONE
        lastTrackRequest = inputSearchEditText.text.toString()
        hideKeyboard()

        iTunesSearchService
            .getTracks(inputSearchEditText.text.toString())
            .enqueue(object : Callback<ITunesSearchResponse> {
                override fun onResponse(
                    call: Call<ITunesSearchResponse>,
                    response: Response<ITunesSearchResponse>
                ) {
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.tracks?.isNotEmpty() == true) {
                                tracks.clear()
                                tracks.addAll(response.body()?.tracks!!)
                                trackAdapter.updateTracks(tracks)
                            } else {
                                showMessage(getString(R.string.nothing_found), "")
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ITunesSearchResponse>, t: Throwable) {
                    recyclerView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    showMessage(
                        getString(R.string.connection_problem),
                        getString(R.string.check_internet)
                    )
                    updateButton.visibility = View.VISIBLE
                }
            })
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            placeHolderMessage.visibility = View.VISIBLE
            placeHolderImage.visibility = View.VISIBLE
            tracks.clear()
            trackAdapter.updateTracks(tracks)
            placeHolderMessage.text = text
            if (additionalMessage.isNotEmpty()) {
                placeHolderAdditionalMessage.visibility = View.VISIBLE
                placeHolderAdditionalMessage.text = additionalMessage
                placeHolderImage.visibility = View.VISIBLE
                placeHolderImage.setImageDrawable(getDrawable(R.drawable.connection_problems))
            }
        } else {
            placeHolderMessage.visibility = View.GONE
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun hideKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        var current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val USER_INPUT = "USER_INPUT"
        const val TRACK = "TRACK"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}

