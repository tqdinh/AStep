package com.example.home.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.firstosproject.reviewImage.ImageReviewViewmodel
import com.example.home.databinding.FragmentImageReviewBinding
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [ReviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class ImageReviewFragment(
    val fullPath: String,
    val onCancel: (path: String) -> Unit = { path ->

    },
    val onSave: (path: String) -> Unit = { path ->
    }
) :
    DialogFragment() {

    val viewModel: ImageReviewViewmodel by viewModels()
    lateinit var _binding: FragmentImageReviewBinding
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(STYLE_NO_FRAME, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserver()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.setCancelable(false)
            dialog.window!!.setLayout(width, height)
        }
    }

    fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            Glide.with(requireActivity()).asBitmap()
                .load(fullPath)
                .fitCenter()
                .into(binding.ivResultPhoto)

            binding.btSave.isEnabled = true
            binding.btCancel.isEnabled = true
        }
    }

    fun setupView() {
        binding.btSave.setOnClickListener({
            dismiss()
            onSave.invoke(fullPath)
        })
        binding.btCancel.setOnClickListener({
            dismiss()
        })
    }
}