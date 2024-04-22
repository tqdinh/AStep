package com.inter.map


import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.inter.map.databinding.FragmentNewPlanBinding
import com.inter.mylocation.ForegroundLocation
import com.inter.mylocation.LocationRepository
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class NewPlanFragment : Fragment() {

    lateinit var _binding: FragmentNewPlanBinding
    val binding get() = _binding

    lateinit var mapView: MapView
    var currentMarker: Marker? = null

    val viewModel: NewPlanViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPlanBinding.inflate(inflater, container, false)

        // very important
        Configuration.getInstance()
            .load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))
        mapView = binding.map

        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)

        val myLocationOverlay =
            MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), mapView)
        myLocationOverlay.enableMyLocation()
        mapView.overlays.add(myLocationOverlay)
        val location = myLocationOverlay.myLocation
        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(location)


        val mapController = mapView.controller
        mapController.setZoom(13)


        binding.ivCurrentLocation.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val myPrevLocation = LocationRepository.myLocation?.value
                myPrevLocation?.apply {
                    mapController.animateTo(GeoPoint(this.latitude, this.longitude))
                    mapController.setZoom(18)
                }
            }

        })

        mapView.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                if (null == currentMarker) {
                    currentMarker = Marker(mapView)?.apply {
                        this.position = p
                        mapView.overlays.add(this)
                        // binding.coorDetail.visibility = View.VISIBLE
                    }
                } else {
                    //  binding.coorDetail.visibility = View.GONE
                    currentMarker?.remove(mapView)
                    currentMarker = null
                }








                return false
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }

        }))

        binding.map.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                return false
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }

        }
        ))


        viewLifecycleOwner.lifecycleScope.launch {
            LocationRepository.myLocation.observe(viewLifecycleOwner, object : Observer<Location> {
                override fun onChanged(value: Location) {
                    mapController.animateTo(GeoPoint(value.latitude, value.longitude))
                    mapController.setZoom(18)
                }

            })
        }

        return binding.root


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val service = Intent(requireActivity(), ForegroundLocation::class.java)
        requireActivity().startService(service)
    }

}

