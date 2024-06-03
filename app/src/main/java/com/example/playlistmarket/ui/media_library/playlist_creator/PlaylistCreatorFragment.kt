package com.example.playlistmarket.ui.media_library.playlist_creator

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.FragmentPlaylistCreatorBinding
import com.example.playlistmarket.domain.media_library.models.Playlist
import com.example.playlistmarket.ui.BindingFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

@Suppress("UNREACHABLE_CODE")
class PlaylistCreatorFragment : BindingFragment<FragmentPlaylistCreatorBinding>() {

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private var playlistTitle: String = ""
    private var playlistDescription: String = ""
    private var coverImageUri: Uri? = null
    private val viewModel by viewModel<PlaylistCreatorViewModel>()
    private var playlistId: Int? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistCreatorBinding {
        return FragmentPlaylistCreatorBinding.inflate(inflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.let { playlistId = it.getInt(PLAYLIST_ID) }

        viewModel.getPlaylistFromLibrary(playlistId)
        viewModel.observeCurrentPlaylist()
            .observe(viewLifecycleOwner) { playlist -> updateUI(playlist) }

        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            checkDialog()
        }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                checkDialog()
            }
        })


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.playlistTitleEditText.text?.isNotEmpty() == true) {
                    binding.createPlaylistButton.apply {
                        background =
                            resources.getDrawable(R.drawable.rectangle_blue, resources.newTheme())

                    }
                } else {
                    binding.createPlaylistButton.apply {
                        background =
                            resources.getDrawable(R.drawable.rectangle_gray, resources.newTheme())
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                playlistTitle = p0.toString()
                binding.createPlaylistButton.isEnabled = playlistTitle.isNotEmpty()
            }
        }


        binding.playlistTitleEditText.let {
            it.requestFocus()
            it.addTextChangedListener(simpleTextWatcher)
        }

        binding.playlistDescriptionEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                playlistDescription = p0.toString()
            }
        })



        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    coverImageUri = uri
                    binding.coverPlace.setImageURI(uri)
                    binding.coverPlace.scaleType = ImageView.ScaleType.CENTER_CROP
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        binding.coverPlace.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createPlaylistButton.setOnClickListener {
            coverImageUri?.let { saveImageToPrivateStorage(it) }
            viewModel.createPlaylist(playlistTitle, playlistDescription, coverImageUri)
            findNavController().navigateUp()
            Toast.makeText(requireContext(), "Плейлист $playlistTitle создан", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "PlaylistMaker"
        )
        if (!filePath.exists()) filePath.mkdirs()
        val file = File(filePath, playlistTitle)
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun checkDialog() {
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { _, _ -> }
            .setPositiveButton("Завершить") { _, _ ->
                findNavController().navigateUp()
            }
        if (coverImageUri != null || playlistTitle.isNotEmpty() || playlistDescription.isNotEmpty()) {
            confirmDialog.show()
        } else findNavController().navigateUp()
    }



    private fun updateUI(playlist: Playlist) {
        binding.header.text = getString(R.string.edit)
        binding.createPlaylistButton.text = getString(R.string.save)
        binding.playlistTitleEditText.setText(playlist.title)
        binding.playlistDescriptionEditText.setText(playlist.description)

        if (playlist.cover != null) {
            coverImageUri = playlist.cover.toUri()

            Glide
                .with(requireContext())
                .load(playlist.cover)
                .placeholder(R.drawable.add_image_button)
                .centerCrop()
                .into(binding.coverPlace)
        }
        binding.createPlaylistButton.setOnClickListener {
            coverImageUri?.let { saveImageToPrivateStorage(it) }
            viewModel.updatePlaylist(id = playlistId!!, name = playlistTitle, description = playlistDescription, cover = coverImageUri)
            Toast.makeText(requireContext(), "Плейлист [ $playlistTitle ] сохранён", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    companion object {
        private const val PLAYLIST_ID = "PLAYLIST_ID"
        fun createBundle(playlistId: Int) = Bundle().apply {
            putInt(PLAYLIST_ID, playlistId)
        }
    }
}