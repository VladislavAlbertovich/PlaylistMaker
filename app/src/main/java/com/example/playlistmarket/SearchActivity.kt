package com.example.playlistmarket

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
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

    private val retrofit =
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val iTunesSearchService = retrofit.create(ITunesSearchApi::class.java)
    private var tracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(tracks)
    private var lastTrackRequest = ""


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputSearchEditText = findViewById<TextInputEditText>(R.id.searchInputEditText)
        placeHolderMessage = findViewById<TextView>(R.id.placeholder_text)
        placeHolderAdditionalMessage = findViewById<TextView>(R.id.placeholder_additional_message)
        placeHolderImage = findViewById<ImageView>(R.id.placeholder_image)
        updateButton = findViewById<Button>(R.id.update_button)
        recyclerView = findViewById<RecyclerView>(R.id.trackListRecyclerView)

        val clearButton: ImageView = findViewById<ImageView>(R.id.clearButtonImageView)
        val buttonBack = findViewById<ImageView>(R.id.buttonBack)

        recyclerView.adapter = trackAdapter

        buttonBack.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputSearchEditText.setText("")
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            hideKeyboard()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                clearButton.visibility = clearButtonVisibility(s)
                inputText = s
            }
        }

        inputSearchEditText.addTextChangedListener(simpleTextWatcher)
        inputSearchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(inputSearchEditText.text.toString())
                true
            } else {
                false
            }
        }

        updateButton.setOnClickListener {
            search(lastTrackRequest)
            if (lastTrackRequest.isNotEmpty()) {
                clearButton.visibility = View.VISIBLE
            }
        }
    }

    private fun search(trackOrArtistName: String) {

        recyclerView.visibility = View.VISIBLE
        placeHolderMessage.visibility = View.GONE
        placeHolderAdditionalMessage.visibility = View.GONE
        placeHolderImage.visibility = View.GONE
        updateButton.visibility = View.GONE
        lastTrackRequest = trackOrArtistName
        hideKeyboard()

        iTunesSearchService
            .getTracks(trackOrArtistName)
            .enqueue(object : Callback<ITunesSearchResponse> {
                override fun onResponse(
                    call: Call<ITunesSearchResponse>,
                    response: Response<ITunesSearchResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.tracks?.isNotEmpty() == true) {
                                tracks.clear()
                                tracks.addAll(response.body()?.tracks!!)
                                trackAdapter.notifyDataSetChanged()
                            } else {
                                showMessage(getString(R.string.nothing_found), "")
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ITunesSearchResponse>, t: Throwable) {
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
            trackAdapter.notifyDataSetChanged()
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

    companion object {
        const val USER_INPUT = "USER_INPUT"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USER_INPUT, inputSearchEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputSearchEditText.setText(savedInstanceState.getString(USER_INPUT))
    }
}