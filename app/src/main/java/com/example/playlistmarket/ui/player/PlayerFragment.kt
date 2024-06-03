package com.example.playlistmarket.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.FragmentPlayerBinding
import com.example.playlistmarket.domain.media_library.models.Playlist
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.presentation.player.PlayerViewModel
import com.example.playlistmarket.presentation.player.TrackAddingState
import com.example.playlistmarket.ui.BindingFragment
import com.example.playlistmarket.ui.player.Model.PlayerScreenState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : BindingFragment<FragmentPlayerBinding>() {

    private val playerViewModel by viewModel<PlayerViewModel>()
    private var playlistAdapter: PlayerPlaylistAdapter? = null
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout)
            .apply { state = BottomSheetBehavior.STATE_HIDDEN }

        bottomSheetBehavior.addBottomSheetCallback(
            object: BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.visibility = View.GONE
                        }
                        else -> {
                            binding.overlay.visibility = View.VISIBLE
                            playerViewModel.getPlaylistsFromLibrary()
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.overlay.alpha = slideOffset + 1
                }
            })
        playerViewModel.observeTrack().observe(viewLifecycleOwner) { track ->
            bind(track)
            playlistAdapter = PlayerPlaylistAdapter{playerViewModel.addTrackToPlaylist(track, it)}
            binding.bottomSheetRecycler.adapter = playlistAdapter

        }

        playerViewModel.observeIsFavorite().observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
                binding.likeButton.setImageResource(R.drawable.pressed_like_button)
            } else {
                binding.likeButton.setImageResource(R.drawable.unpressed_like_button)
            }
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

        playerViewModel.observePlaylists().observe(viewLifecycleOwner) {
            updatePlaylists(it)
        }

        playerViewModel.observeTrackAddingStatus().observe(viewLifecycleOwner) { status ->
            when (status) {
                is TrackAddingState.Done -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    showToast(status.playlist, false)
                }
                is TrackAddingState.NotDone -> {
                    showToast(status.playlist, true)
                }
                is TrackAddingState.Ready -> {}
            }
        }




        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.playButton.setOnClickListener {
            playerViewModel.playbackControl()
        }

        binding.likeButton.setOnClickListener {
            lifecycleScope.launch { playerViewModel.onFavoriteClicked() }
        }

        binding.addToPlaylistButton.setOnClickListener { bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED }
        binding.bottomSheetButton.setOnClickListener { findNavController().navigate(R.id.action_playerFragment_to_playlistCreatorFragment) }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                } else findNavController().navigateUp()
            }
        })

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

    private fun updatePlaylists(itemList: List<Playlist>) { playlistAdapter?.setItems(itemList) }

    private fun showToast(playlist: Playlist, exists: Boolean) {
        if (exists) Toast.makeText(requireContext(),
            getString(R.string.the_track_has_already_been_added_to_the_playlist, playlist.title), Toast.LENGTH_SHORT).show()
        else Toast.makeText(requireContext(),
            getString(R.string.added_to_playlist, playlist.title), Toast.LENGTH_SHORT).show()
    }
}
