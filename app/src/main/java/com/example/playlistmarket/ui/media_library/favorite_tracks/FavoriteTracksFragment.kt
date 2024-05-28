package com.example.playlistmarket.ui.media_library.favorite_tracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.presentation.media_library.FavoriteTracksState
import com.example.playlistmarket.presentation.media_library.FavoriteTracksViewModel
import com.example.playlistmarket.ui.BindingFragment
import com.example.playlistmarket.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : BindingFragment<FragmentFavoriteTracksBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteTracksBinding {
        return FragmentFavoriteTracksBinding.inflate(inflater, container, false)
    }

    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()
    private lateinit var adapter: FavoriteTrackAdapter
    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce =
            debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
                openPlayer(track)
            }

        adapter = FavoriteTrackAdapter {
            openPlayer(it)
        }

        binding.trackListRecyclerView.adapter = adapter

        favoriteTracksViewModel.observeFavoriteTracksState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: FavoriteTracksState) {
        when (state) {
            is FavoriteTracksState.Content -> showContent(state.tracks)
            is FavoriteTracksState.Empty -> showEmptyMessage(state.message)
        }
    }

    private fun showContent(tracks: List<Track>) {
        adapter.updateTracks(tracks)
        binding.trackListRecyclerView.visibility = View.VISIBLE
        binding.placeholderText.visibility = View.GONE
        binding.placeholderImage.visibility = View.GONE
    }

    private fun showEmptyMessage(message: String) {
        binding.placeholderText.text = message
        binding.placeholderText.visibility = View.VISIBLE
        binding.placeholderImage.visibility = View.VISIBLE
        binding.trackListRecyclerView.visibility = View.GONE
    }

    private fun openPlayer(track: Track) {
        favoriteTracksViewModel.provideTrack(track)
        findNavController().navigate(R.id.action_mediaLibraryFragment_to_playerFragment)
    }

    override fun onResume() {
        super.onResume()
        favoriteTracksViewModel.fillData()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 100L
        fun newInstance(): FavoriteTracksFragment {
            return FavoriteTracksFragment()
        }
    }
}