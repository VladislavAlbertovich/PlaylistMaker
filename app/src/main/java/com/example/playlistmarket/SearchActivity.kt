package com.example.playlistmarket

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.google.android.material.textfield.TextInputEditText

class SearchActivity : AppCompatActivity() {

    private var inputText: Editable? = null
    private lateinit var inputSearchEditText: TextInputEditText
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        inputSearchEditText = findViewById<TextInputEditText>(R.id.searchInputEditText)
        val clearButton: ImageView = findViewById<ImageView>(R.id.clearButtonImageView)
        val buttonBack = findViewById<ImageView>(R.id.buttonBack)
        buttonBack.setOnClickListener{
            finish()
        }
        clearButton.setOnClickListener {
            inputSearchEditText.setText("")
            val view: View? = this.currentFocus
            if (view != null){
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }
        }

        val simpleTextWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                clearButton.visibility= clearButtonVisibility(s)
                inputText = s
            }
        }
        inputSearchEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("USER_INPUT",inputSearchEditText.text.toString())

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputSearchEditText.setText(savedInstanceState.getString("USER_INPUT"))
    }

}