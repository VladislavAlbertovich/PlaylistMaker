package com.example.playlistmarket.ui.media_library.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.FragmentPlaylistsBinding
import com.example.playlistmarket.domain.media_library.models.Playlist
import com.example.playlistmarket.presentation.media_library.PlaylistsState
import com.example.playlistmarket.presentation.media_library.PlaylistsViewModel
import com.example.playlistmarket.ui.BindingFragment
import com.example.playlistmarket.ui.playlist.PlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : BindingFragment<FragmentPlaylistsBinding>() {

    private val viewModel by viewModel<PlaylistsViewModel>()

    private var adapter: PlaylistsAdapter? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance(): PlaylistsFragment {
            return PlaylistsFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PlaylistsAdapter { playlist -> onPlaylistClick(playlist) }
        binding.playlistRecyclerView.adapter = adapter
        binding.newPlaylistButton.setOnClickListener {
            requireParentFragment().findNavController()
                .navigate(R.id.action_mediaLibraryFragment_to_playlistCreatorFragment)
        }
        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistsState.Content -> showContent(it.data)
                is PlaylistsState.Empty -> showEmpty()
            }
        }
    }

    private fun showContent(playlistList: List<Playlist>) {
        binding.placeholderImage.visibility = View.GONE
        binding.placeholderText.visibility = View.GONE
        binding.playlistRecyclerView.visibility = View.VISIBLE
        updatePlaylists(playlistList)
    }

    private fun showEmpty() {
        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderText.visibility = View.VISIBLE
        binding.playlistRecyclerView.visibility = View.GONE
        updatePlaylists(listOf())
    }

    private fun updatePlaylists(playlistList: List<Playlist>) {
        adapter?.updatePlaylistsList(playlistList)
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateState()
    }

    private fun onPlaylistClick(playlist: Playlist) {
        val arguments = PlaylistFragment.createBundle(playlist.id)
        findNavController().navigate(R.id.action_mediaLibraryFragment_to_playlistFragment, arguments)
    }
}