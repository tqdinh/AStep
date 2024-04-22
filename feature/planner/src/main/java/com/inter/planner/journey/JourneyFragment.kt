package com.inter.planner.journey

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.inter.mylocation.LocationRepository
import com.inter.planner.databinding.FragmentJourneyBinding
import com.inter.planner.entity.JourneyEntity
import com.inter.planner.utils.CreateDrawableMarker.CreateDrawableFromLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


@AndroidEntryPoint
class JourneyFragment : Fragment() {


    lateinit var _binding: FragmentJourneyBinding
    val binding get() = _binding

    lateinit var journeyId: String
    val viewModel: JourneyViewModel by viewModels()
    lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJourneyBinding.inflate(inflater, container, false)
        //inflater.inflate(R.layout., container, false)
        arguments?.apply {
            (getString("journey_id"))?.apply {
                journeyId = this
                viewModel.getJourney(this)
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }

            })


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED)
            {
                viewModel.journey.observe(viewLifecycleOwner, object : Observer<JourneyEntity> {
                    override fun onChanged(value: JourneyEntity) {

                        val listGeo = value.listPlaces.map {
                            GeoPoint(it.lat, it.lon)
                        }
                        listGeo.forEachIndexed { indx, geo ->
                            addMarkerOnMap(geo, indx)
                        }
                        AddPolyline(listGeo)


                    }

                })
            }
        }



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



        return binding.root

    }


    fun addMarkerOnMap(geoPoint: GeoPoint, order: Int) {

        val newMarker = CreateDrawableFromLayout(requireActivity(), order)

        Marker(mapView)?.apply {
            this.icon = newMarker
            this.position = geoPoint


            mapView.overlays.add(this)
            // binding.coorDetail.visibility = View.VISIBLE
        }

    }

    fun AddPolyline(p: List<GeoPoint?>?): Polyline? {
        val polyline = Polyline(mapView)
        polyline.setPoints(p)
        polyline.setColor(Color.BLUE)
        polyline.setWidth(3f)
        mapView.getOverlays().add(polyline)
        return polyline
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        // TODO: Use the ViewModel
    }

}