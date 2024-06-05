package com.example.playlistmarket.ui.playlist

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.FragmentPlaylistBinding
import com.example.playlistmarket.domain.media_library.models.Playlist
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.presentation.playlist.PlaylistViewModel
import com.example.playlistmarket.ui.BindingFragment
import com.example.playlistmarket.ui.media_library.playlist_creator.PlaylistCreatorFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : BindingFragment<FragmentPlaylistBinding>() {

    private val playlistViewModel by viewModel<PlaylistViewModel>()
    private var playlistId: Int = -1

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehaviorMore: BottomSheetBehavior<LinearLayout>
    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private lateinit var trackAdapter: PlaylistTrackAdapter
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        playlistId = arguments?.getInt(PLAYLIST_ID) ?: -1

        trackAdapter = PlaylistTrackAdapter(
            onItemClick = { track ->
                openPlayer(track)

            },
            onLongItemClick = { track ->
                removeTrackDialog(track)
            }
        )
        binding.bottomSheetRecycler.adapter = trackAdapter
        playlistViewModel.getPlaylist(playlistId)
        playlistViewModel.observeContentExist().observe(viewLifecycleOwner) { hasContent ->
            if (hasContent) {
                binding.bottomSheetRecycler.isVisible = true
                binding.emptyListMessage.isVisible = false
            } else {
                binding.bottomSheetRecycler.isVisible = false
                binding.emptyListMessage.isVisible = true
            }
        }
        playlistViewModel.observeCurrentPlaylist()
            .observe(viewLifecycleOwner) { playlist -> bindPlaylist(playlist) }
        playlistViewModel.observePlaylistInfo()
            .observe(viewLifecycleOwner) { info -> bindPlaylistInfo(info) }
        playlistViewModel.observeCurrentPlaylistTracks().observe(viewLifecycleOwner) {
            trackAdapter.setTracks(it)
        }
        playlistViewModel.observeEmptyShareMessage().observe(viewLifecycleOwner) { playlistEmpty ->
            if (playlistEmpty == true) {
                showEmptyPlaylistMessage()
            }
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout)
            .apply { state = BottomSheetBehavior.STATE_COLLAPSED }

        binding.emptySpace.doOnLayout {
            bottomSheetBehavior.setPeekHeight(
                binding.playlistCoordinatorLayout.height - binding.playlistConstraintLayout.height,
                false
            )
        }

        bottomSheetBehaviorMore = BottomSheetBehavior.from(binding.bottomSheetLayoutMore)
            .apply { state = BottomSheetBehavior.STATE_HIDDEN }

        bottomSheetBehaviorMore.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.isVisible = false
                    else -> binding.overlay.isVisible = true
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1
            }
        })

        binding.buttonBack.setOnClickListener { findNavController().navigateUp() }
        binding.moreButton.setOnClickListener {
            bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.shareButton.setOnClickListener { playlistViewModel.sharePlaylist(playlistId) }
        binding.share.setOnClickListener { playlistViewModel.sharePlaylist(playlistId) }
        binding.edit.setOnClickListener {
            val bundle = PlaylistCreatorFragment.createBundle(playlistId)
            findNavController().navigate(
                R.id.action_playlistFragment_to_playlistCreatorFragment,
                bundle
            )
        }
        binding.delete.setOnClickListener { removePlaylistDialog() }

        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigateUp()
        }
    }

    private fun removeTrackDialog(track: Track) {
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_track))
            .setMessage(getString(R.string.do_you_want_delete_track))
            .setNeutralButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                playlistViewModel.removeTrackFromPlaylist(track, playlistId)
            }
        val dialog = confirmDialog.show()
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_day_night_color))
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_day_night_color))
    }

    private fun bindPlaylist(playlist: Playlist) {
        binding.playlistTitle.text = playlist.title
        binding.includePlaylistPlayer.playlistTitle.text = playlist.title
        binding.playlistDescription.text = playlist.description
        Glide.with(requireContext())
            .load(playlist.cover).placeholder(R.drawable.placeholder)
            .into(binding.playlistImage)
        Glide.with(requireContext())
            .load(playlist.cover).placeholder(R.drawable.placeholder)
            .into(binding.includePlaylistPlayer.playlistImage)
    }

    private fun bindPlaylistInfo(info: Pair<String, String>) {
        val text = "${info.first} • ${info.second}"
        binding.playlistInfo.text = text
        binding.includePlaylistPlayer.tracksCount.text = info.second

    }

    private fun openPlayer(track: Track) {
        playlistViewModel.provideTrack(track)
        findNavController().navigate(R.id.action_playlistFragment_to_playerFragment)

    }

    private fun showEmptyPlaylistMessage() {
        bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_HIDDEN
        Toast.makeText(
            requireContext(),
            getString(R.string.playlist_doesnt_have_tracks), Toast.LENGTH_SHORT
        ).show()
    }

    private fun removePlaylistDialog() {
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_playlist)
            .setMessage(getString(R.string.do_you_want_delete_playlist))
            .setNeutralButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ ->
                playlistViewModel.removePlaylistFromLibrary(playlistId)
                findNavController().navigateUp()
            }
        val dialog = confirmDialog.show()
        dialog.window
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_day_night_color))
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_day_night_color))
    }

    override fun onResume() {
        super.onResume()
        bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_HIDDEN
    }

    companion object {
        private const val PLAYLIST_ID = "PLAYLIST_ID"

        fun createBundle(playlistId: Int): Bundle {
            return Bundle().apply {
                putInt(PLAYLIST_ID, playlistId)
            }
        }
    }
}