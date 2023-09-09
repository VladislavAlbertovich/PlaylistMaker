package com.example.playlistmarket

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class  MainActivity : AppCompatActivity(){
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSearch = findViewById<Button>(R.id.buttonSearch)
        val buttonLibrary = findViewById<Button>(R.id.buttonLibrary)
        val buttonSettings = findViewById<Button>(R.id.buttonSettings)

        buttonSearch.setOnClickListener{
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        buttonLibrary.setOnClickListener{
            val libraryIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(libraryIntent)
        }

        buttonSettings.setOnClickListener{
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

    }


}