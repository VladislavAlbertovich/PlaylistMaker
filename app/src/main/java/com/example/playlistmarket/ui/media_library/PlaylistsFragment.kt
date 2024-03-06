package com.example.playlistmarket.ui.media_library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmarket.databinding.FragmentPlaylistsBinding
import com.example.playlistmarket.presentation.media_library.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistsFragment: Fragment() {

    private val playlistsViewModel: PlaylistsViewModel by viewModel{
        parametersOf("")
    }
    private lateinit var binding: FragmentPlaylistsBinding
    companion object{
        fun newInstance(): PlaylistsFragment{
            return PlaylistsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistsViewModel.observeLiveData().observe(viewLifecycleOwner){
            if(it.isNullOrEmpty()){
                binding.trackListRecyclerView.visibility = View.GONE
                binding.placeholderImage.visibility = View.VISIBLE
                binding.placeholderText.visibility = View.VISIBLE
            }
        }
    }
}