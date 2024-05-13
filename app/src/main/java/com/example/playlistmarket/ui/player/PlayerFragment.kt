package com.example.playlistmarket.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.FragmentPlayerBinding
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.presentation.player.PlayerViewModel
import com.example.playlistmarket.ui.BindingFragment
import com.example.playlistmarket.ui.player.Model.PlayerScreenState
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : BindingFragment<FragmentPlayerBinding>() {

    private val playerViewModel by viewModel<PlayerViewModel>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerViewModel.observeTrack().observe(viewLifecycleOwner) {
            bind(it)
        }

        playerViewModel.observePlayerScreenState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlayerScreenState.Paused -> {
                    binding.playButton.setImageResource(R.drawable.play_button)
                    binding.timeFragmentTextview.text = state.progress
                    playerViewModel.stopUpdatePlayerState()
                }

                is PlayerScreenState.Default -> {
                }

                is PlayerScreenState.Prepared -> {
                    binding.playButton.isEnabled = true
                    binding.playButton.setImageResource(R.drawable.play_button)
                    binding.timeFragmentTextview.text = state.progress
                    playerViewModel.stopUpdatePlayerState()
                }

                is PlayerScreenState.Playing -> {
                    binding.playButton.setImageResource(R.drawable.pause_button)
                    binding.timeFragmentTextview.text = state.progress

                }
            }
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.playButton.setOnClickListener {
            playerViewModel.playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pausePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        playerViewModel.resetPlayer()
    }

    private fun bind(track: Track) {
        binding.apply {
            trackName.text = track.trackName
            artistName.text = track.artistName
            durationValueTextView.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            if (track.collectionName.isNotEmpty()) {
                binding.albumValueTextView.text = track.collectionName
            } else {
                binding.albumValueTextView.text = "-"
            }
            yearValueTextView.text =
                SimpleDateFormat("yyyy", Locale.getDefault()).format(track.releaseDate)
            genreValueTextView.text = track.primaryGenreName
            countryValueTextView.text = track.country
            getCoverArtwork(track)
        }
    }

    private fun getCoverArtwork(track: Track) {
        Glide
            .with(this)
            .load(track.artworkUrl512())
            .placeholder(R.drawable.placeholder_album_in_player)
            .centerCrop()
            .transform(
                RoundedCorners(
                    binding.albumImageView.context.resources.getDimensionPixelOffset(
                        R.dimen._8dp
                    )
                )
            )
            .into(binding.albumImageView)
    }
}
