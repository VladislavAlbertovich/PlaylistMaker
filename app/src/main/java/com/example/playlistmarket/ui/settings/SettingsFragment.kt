package com.example.playlistmarket.ui.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.playlistmarket.App
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.FragmentSettingsBinding
import com.example.playlistmarket.presentation.settings.SettingsViewModel
import com.example.playlistmarket.ui.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: BindingFragment<FragmentSettingsBinding>() {

    private val viewModel by viewModel<SettingsViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeLiveData().observe(viewLifecycleOwner) {
            binding.themeSwitch.isChecked = it
        }

        binding.buttonShareApp.setOnClickListener {
            val shareText = getString(R.string.link_to_course)
            val sendIntent = Intent()
            sendIntent.apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            safeStartActivity(sendIntent)
        }

        binding.buttonSupport.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            val subjectMessage = getString(R.string.message_to_developers_playlistmaker_app)
            val textMessage = getString(R.string.thank_message_to_developers)
            supportIntent.apply {
                setData(Uri.parse("mailto:"))
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
                putExtra(Intent.EXTRA_SUBJECT, subjectMessage)
                putExtra(Intent.EXTRA_TEXT, textMessage)
            }
            safeStartActivity(supportIntent)
        }

        binding.buttonUserAgreement.setOnClickListener {
            val url = Uri.parse(getString(R.string.link_to_user_agreement))
            val userAgreementIntent = Intent(Intent.ACTION_VIEW, url)
            safeStartActivity(userAgreementIntent)
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateLiveData(isChecked)
            (requireActivity().application as App).switchTheme(isChecked)
        }
    }

    private fun safeStartActivity(intent: Intent) {
        try {
            startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            val errorMessage = getString(R.string.error_app_message)
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}