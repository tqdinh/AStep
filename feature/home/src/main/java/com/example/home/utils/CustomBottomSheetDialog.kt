package com.example.home.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.home.databinding.CustomBottomSheetDialogBinding

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CustomBottomSheetDialog(val onSelectCameraGalary: OnSelectCameraGalary) :
    BottomSheetDialogFragment() {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = CustomBottomSheetDialogBinding.inflate(inflater, container, false)
        binding.llCamera.setOnClickListener {
            onSelectCameraGalary.onSelectCamera()
            this.dismiss()
        }

        binding.llGalary.setOnClickListener {
            onSelectCameraGalary.onSelectGalary()
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up BottomSheetBehavior
        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)

        bottomSheetBehavior.peekHeight = 200

        bottomSheetBehavior.isHideable = false

    }


    interface OnSelectCameraGalary {
        fun onSelectCamera()
        fun onSelectGalary()
    }
}