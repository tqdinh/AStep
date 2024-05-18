package com.inter.explore

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.inter.explore.databinding.FragmentExploreBinding
import com.inter.mylocation.BackgroundLocationService
import com.inter.mylocation.ForegroundLocation
import com.inter.mylocation.LocationRepository
import com.inter.notification.MyNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ExploreFragment : Fragment() {


    val viewModel: ExploreViewModel by viewModels()
    lateinit var _binding: FragmentExploreBinding

    val binding get() = _binding

    companion object {
        fun newInstance() = ExploreFragment()
    }

    lateinit var notification: MyNotification
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root = binding.root
        val appContext = requireContext().applicationContext

        binding.btnCopyJourney.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                 //   AppDatabase.copyJourney(requireActivity().application)


                }
            }

        })
        binding.btnCopyImage.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                 //   AppDatabase.getImportdata(requireActivity().application)
                }
            }

        })







        viewLifecycleOwner.lifecycleScope.launch {
            LocationRepository.myLocation.observe(viewLifecycleOwner, object : Observer<Location> {
                override fun onChanged(value: Location) {

                    binding.btnForeGround.text = "" + value.latitude + ":" + value.longitude
                }

            })
        }

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {

            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                notification = MyNotification(appContext)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
            }

        })

        binding.btnInsert.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                notification.createNotification()


            }

        })
        binding.btnForeGround.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
//                val service = Intent(requireActivity(), ForegroundLocation::class.java)
                val service = Intent(requireActivity(), BackgroundLocationService::class.java)
                requireActivity().startService(service)
                //LocationRepository.startLocationUpdates(requireContext().applicationContext)

            }

        })

        return root
//        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
        // viewModel = ViewModelProvider(this).get(ExploreViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        val applicationContext = requireActivity().applicationContext

    }

}