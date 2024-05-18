package com.example.home

import android.app.ProgressDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
import com.inter.entity.planner.ApiResult
import com.inter.entity.planner.JourneyEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : Fragment(), EventAdapter.OnItemClickListener {


    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding

    val viewModel: HomeViewModel by viewModels()
    val adapterActiveEvent = EventAdapter(this)
    private lateinit var onScrollListener: RecyclerViewScrollListener
    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).setSupportActionBar(binding.homeToolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        progressDialog = ProgressDialog(context)

        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)

        return binding.root
    }

    fun setupView() {


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


        //setup bottom sheet for places
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
        setupListener()

        viewModel.getJourney()
//        viewModel.migrate()
        //viewModel.getPlaceOfJourney("9cdca200-1216-4edf-b391-ff6be51f0c54")

    }

    fun setupListener() {

        binding.ivCreatePlan.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val builder = AlertDialog.Builder(requireActivity())
                val editText = EditText(requireActivity())
                editText.inputType = InputType.TYPE_CLASS_TEXT
                builder.setTitle("Input your journey")
                builder.setView(editText)
                builder.setMessage("Please input your journey name")
                builder.setPositiveButton("OK") { dialog, which ->
                    createJourney(editText.text.toString())
                    dialog.cancel()
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    // Do something when Cancel button is clicked
                    dialog.cancel()
                }
                builder.setCancelable(false) // Prevent dialog from being dismissed when touching outside of it or pressing back button
                val dialog = builder.create()
                dialog.show()
            }

        })


    }

    override fun onItemClick(item: com.inter.entity.planner.JourneyEntity) {
        val action = HomeFragmentDirections.actFrJourney(item.id)
        findNavController().navigate(action)

    }

    override fun onItemLongClick(item: JourneyEntity) {

        val builder = AlertDialog.Builder(requireActivity())
        val editText = EditText(requireActivity())
        editText.inputType = InputType.TYPE_CLASS_TEXT
        builder.setTitle("${item.title}")
        builder.setView(editText)
        builder.setMessage("Please input the above text ")
        builder.setPositiveButton("OK") { dialog, which ->

            val tmp = editText.text.toString()
            if (tmp.equals(item.title)) {
                deleteJourney(item.id)
            } else {
                Toast.makeText(requireActivity(), "Text note match ", Toast.LENGTH_SHORT).show()
            }
            dialog.cancel()

        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            // Do something when Cancel button is clicked
            dialog.cancel()
        }
        builder.setCancelable(false) // Prevent dialog from being dismissed when touching outside of it or pressing back button
        val dialog = builder.create()
        dialog.show()

    }


    fun deleteJourney(journeyId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deleteJourney(journeyId)
                .collect()
                {
                    when (it) {
                        is ApiResult.Success -> {

                        }

                        is ApiResult.Error<*> -> {

                        }

                        is ApiResult.Loading -> {
                            if (true == it.data)
                                withContext(Dispatchers.Main)
                                {
                                    progressDialog.show()
                                }
                            else {
                                withContext(Dispatchers.Main)
                                { progressDialog.hide() }
                            }

                        }

                        else -> {

                        }
                    }
                }
        }
    }

    fun createJourney(journeyName: String) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewModel.createJourney(journeyName, "nopp").collect()
            {
                when (it) {
                    is ApiResult.Success -> {

                    }

                    is ApiResult.Error<*> -> {

                    }

                    is ApiResult.Loading -> {
                        if (true == it.data)
                            withContext(Dispatchers.Main)
                            {
                                progressDialog.show()
                            }
                        else {
                            withContext(Dispatchers.Main)
                            { progressDialog.hide() }
                        }

                    }

                    else -> {

                    }
                }
            }
        }

    }


}