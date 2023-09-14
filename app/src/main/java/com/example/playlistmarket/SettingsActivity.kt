package com.example.playlistmarket

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()
        }
        val buttonShareApp = findViewById<LinearLayout>(R.id.buttonShareApp)
        buttonShareApp.setOnClickListener{
            val url = Uri.parse("https://practicum.yandex.ru/android-developer")
            val sendIntent = Intent(Intent.ACTION_SEND, url)
            startActivity(sendIntent)
        }
        val buttonSupport = findViewById<LinearLayout>(R.id.buttonSupport)
        buttonSupport.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            val subjectMessage = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val textMessage = "Спасибо разработчикам и разработчицам за крутое приложение!"
            supportIntent.setData(Uri.parse("mailto:"))
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("gudsanta66@gmail.com"))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, subjectMessage)
            supportIntent.putExtra(Intent.EXTRA_TEXT, textMessage)
            startActivity(supportIntent)
        }

        val buttonUserAgreement = findViewById<LinearLayout>(R.id.buttonUserAgreement)
        buttonUserAgreement.setOnClickListener{
            val buttonUserAgreement = findViewById<LinearLayout>(R.id.buttonUserAgreement)
            buttonUserAgreement.setOnClickListener{
                val url = Uri.parse("https://yandex.ru/legal/practicum_offer/")
                val userAgreementIntent = Intent(Intent.ACTION_VIEW, url)
                startActivity(userAgreementIntent)
            }
        }
    }
}