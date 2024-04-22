package com.inter.planner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.inter.planner.databinding.FragmentPlannerMainBinding

class PlannerMainFragment : Fragment() {

    lateinit var _binding: FragmentPlannerMainBinding
    val binding get() = _binding

    private lateinit var viewModel: PlannerMainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlannerMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlannerMainViewModel::class.java)

    }

}