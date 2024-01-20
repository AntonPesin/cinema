package com.example.android_coursework_lvl1.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.databinding.ImagedialogBinding

class ImageDialogFragment : DialogFragment() {

    private var _binding: ImagedialogBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val IMAGE_URL_KEY = "imageUrl"
        fun newInstance(imageUrl: String?): ImageDialogFragment {
            val args = Bundle()
            args.putString("imageUrl", imageUrl)
            val fragment = ImageDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), R.style.FullScreenDialog)
        dialog.window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ImagedialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUrl = arguments?.getString(IMAGE_URL_KEY)
        Glide.with(requireContext())
            .load(imageUrl)
            .into(binding.fullscreenPhoto)

        binding.fullscreenPhoto.setOnClickListener {
            dismiss()
        }
    }



}