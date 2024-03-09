package com.example.playlistmarket.ui.media_library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmarket.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmarket.presentation.media_library.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoriteTracksFragment: Fragment() {

    private lateinit var binding: FragmentFavoriteTracksBinding
    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel(){
        parametersOf("")
    }
    companion object{

        fun newInstance(): FavoriteTracksFragment {

            return FavoriteTracksFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteTracksViewModel.observeFavoriteTracks().observe(viewLifecycleOwner){
            if (it.isNullOrEmpty()){
                binding.placeholderImage.visibility = View.VISIBLE
                binding.placeholderText.visibility = View.VISIBLE
            }
        }
    }
}