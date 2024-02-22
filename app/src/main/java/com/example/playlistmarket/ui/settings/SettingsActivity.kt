package com.example.playlistmarket.ui.settings

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmarket.App
import com.example.playlistmarket.R
import com.example.playlistmarket.presentation.settings.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModel<SettingsViewModel>()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        val buttonShareApp = findViewById<LinearLayout>(R.id.buttonShareApp)
        val buttonSupport = findViewById<LinearLayout>(R.id.buttonSupport)
        val buttonUserAgreement = findViewById<LinearLayout>(R.id.buttonUserAgreement)
        val themeSwitch = findViewById<SwitchMaterial>(R.id.themeSwitch)
        viewModel.observeLiveData().observe(this){
            themeSwitch.isChecked = it
        }

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
            startActivitySafe(sendIntent)
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
            startActivitySafe(supportIntent)
        }

        buttonUserAgreement.setOnClickListener {
            val url = Uri.parse(getString(R.string.link_to_user_agreement))
            val userAgreementIntent = Intent(Intent.ACTION_VIEW, url)
            startActivitySafe(userAgreementIntent)
        }

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            (application as App).switchTheme(isChecked)
        }
    }

    private fun startActivitySafe(intent: Intent) {
        try {
            startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            val errorMessage = getString(R.string.error_app_message)
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}