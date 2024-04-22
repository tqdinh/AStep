package com.inter.planner.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inter.planner.databinding.FragmentPlannerBinding
import com.inter.planner.entity.JourneyEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlannerFragment : Fragment(), EventAdapter.OnItemClickListener {


    private lateinit var _binding: FragmentPlannerBinding
    private val binding get() = _binding

    val viewModel: PlannerViewModel by viewModels()
    val adapterActiveEvent = EventAdapter(this)
    private lateinit var onScrollListener: RecyclerViewScrollListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlannerBinding.inflate(inflater, container, false)


        return binding.root
    }

    fun setupView() {

        try {

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.listJourney.observe(
                        viewLifecycleOwner,
                        object : Observer<List<JourneyEntity>> {
                            override fun onChanged(listJourney: List<JourneyEntity>) {
                                adapterActiveEvent.submitList(listJourney)

                            }

                        })
                }
            }
        } catch (e: Exception) {
            e.toString()
        }

        val linearlayout = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rcvJourney.apply {
            adapter = adapterActiveEvent
            itemAnimator = null
            layoutManager = linearlayout
            onScrollListener = object : RecyclerViewScrollListener(linearlayout) {
                override fun onLoadMore(
                    page: Int,
                    totalItemsCount: Int,
                    view: RecyclerView?
                ) {

                }
            }
            addOnScrollListener(onScrollListener)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        viewModel.getJourney()


        //viewModel.getPlaceOfJourney("9cdca200-1216-4edf-b391-ff6be51f0c54")

    }

    override fun onItemClick(item: JourneyEntity) {
        val action = PlannerFragmentDirections.actFrJourney(item.id)
        findNavController().navigate(action)

    }


}