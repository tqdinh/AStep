package com.example.home

import android.os.Bundle
import android.util.Log
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
import com.example.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), EventAdapter.OnItemClickListener {


    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding

    val viewModel: HomeViewModel by viewModels()
    val adapterActiveEvent = EventAdapter(this)
    private lateinit var onScrollListener: RecyclerViewScrollListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    fun setupView() {
        try {

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    viewModel.listnumber.observe(
                        viewLifecycleOwner,
                        object : Observer<List<Int>> {
                            override fun onChanged(listJourney: List<Int>) {
                                listJourney?.forEach {
                                    Log.d("LIST_PLACES", "" + it)
                                }

                            }

                        })
                }
            }


            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    adapterActiveEvent.submitList(emptyList())
                    viewModel.listJourney.observe(
                        viewLifecycleOwner,
                        object : Observer<List<com.inter.entity.planner.JourneyEntity>> {
                            override fun onChanged(listJourney: List<com.inter.entity.planner.JourneyEntity>) {
                                listJourney?.forEach {
                                    Log.d("LIST_PLACES", "Place:" + it.listPlaces.size)
                                }
                                adapterActiveEvent.submitList(listJourney)
                                adapterActiveEvent.notifyDataSetChanged()
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

    override fun onItemClick(item: com.inter.entity.planner.JourneyEntity) {
        val action = HomeFragmentDirections.actFrJourney(item.id)
        findNavController().navigate(action)

    }


}