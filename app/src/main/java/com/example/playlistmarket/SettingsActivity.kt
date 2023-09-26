package com.example.playlistmarket

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import java.lang.IllegalStateException

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        val buttonShareApp = findViewById<LinearLayout>(R.id.buttonShareApp)
        val buttonSupport = findViewById<LinearLayout>(R.id.buttonSupport)
        val buttonUserAgreement = findViewById<LinearLayout>(R.id.buttonUserAgreement)
        val switchDayNight = findViewById<SwitchMaterial>(R.id.switcherDayNight)

        buttonBack.setOnClickListener {
            finish()
        }

        buttonShareApp.setOnClickListener {
            val shareText = getString(R.string.link_to_course)
            val sendIntent = Intent()
            sendIntent.apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            startActivity(sendIntent)
        }

        buttonSupport.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            val subjectMessage = getString(R.string.message_to_developers_playlistmaker_app)
            val textMessage = getString(R.string.thank_message_to_developers)
            supportIntent.apply {
                setData(Uri.parse("mailto:"))
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
                putExtra(Intent.EXTRA_SUBJECT, subjectMessage)
                putExtra(Intent.EXTRA_TEXT, textMessage)
            }
            try {
                startActivity(supportIntent)
            } catch (e: Exception){
                val errorMessage = getString(R.string.error_message_email)
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                throw IllegalStateException(errorMessage)
            }
        }

        buttonUserAgreement.setOnClickListener {
            val url = Uri.parse(getString(R.string.link_to_user_agreement))
            val userAgreementIntent = Intent(Intent.ACTION_VIEW, url)
            startActivity(userAgreementIntent)
        }

        switchDayNight.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}