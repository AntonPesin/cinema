package com.example.android_coursework_lvl1.additionalPages

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.databinding.ImagedialogLayoutBinding

class ImageDialogFragment : DialogFragment() {

    private var _binding: ImagedialogLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), R.style.FullScreenDialog)
        dialog.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        return dialog
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ImagedialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUrl = arguments?.getString("imageUrl")
        Glide.with(requireContext())
            .load(imageUrl)
            .into(binding.fullscreenPhoto)

        binding.fullscreenPhoto.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        fun newInstance(imageUrl: String?): ImageDialogFragment {
            val args = Bundle()
            args.putString("imageUrl", imageUrl)
            val fragment = ImageDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }





}