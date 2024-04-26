package com.inter.planner.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inter.planner.databinding.CustomBottomSheetBinding

class CustomBottomSheet : BottomSheetDialogFragment() {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = CustomBottomSheetBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up BottomSheetBehavior
        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)

        bottomSheetBehavior.peekHeight = 200

        bottomSheetBehavior.isHideable = false

        // You can customize the content of the bottom sheet here
    }
}