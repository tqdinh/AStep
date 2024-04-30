package com.example.home.journey

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AbsListView
import android.widget.GridView
import android.widget.ImageView

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.home.R
import com.example.home.adapter.PlaceAdapter

import com.example.home.databinding.FragmentJourneyBinding
import com.example.home.utils.CreateDrawableMarker
import com.example.home.utils.CustomBottomSheet

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.inter.entity.planner.JourneyEntity
import com.inter.entity.planner.PlaceEntity
import com.inter.mylocation.LocationRepository


import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


@AndroidEntryPoint
class JourneyFragment : Fragment() {

    lateinit var _binding: FragmentJourneyBinding
    val viewModel: JourneyViewModel by viewModels()
    val binding get() = _binding


    lateinit var journeyId: String
    lateinit var mapView: MapView
    lateinit var mapController: IMapController

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    lateinit var bottomSheet: ConstraintLayout
    var selectItem: PlaceEntity? = null

    lateinit var adapter: PlaceAdapter
    lateinit var gridPlace: GridView

    var isOnTop: Boolean = false

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

        gridPlace = binding.root.findViewById(R.id.gvPlaceImg)
        bottomSheet =binding.root.findViewById(R.id.bottomSheet)

        adapter = PlaceAdapter()
        gridPlace.adapter = adapter

        gridPlace.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {

            }

            override fun onScroll(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                isOnTop = false
                if (firstVisibleItem == 0) {
                    isOnTop = true
                }

            }

        })


        val windowManager =
            requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val widthPixels = displayMetrics.widthPixels
        val heightPixels = displayMetrics.heightPixels

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        Log.d("BOTTOM_SHEET", "STATE_HIDDEN")
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {

                        Log.d("BOTTOM_SHEET", "STATE_EXPANDED")
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        Log.d("BOTTOM_SHEET", "STATE_COLLAPSED")
                        bottomSheetBehavior.setPeekHeight(heightPixels / 4, true)
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        Log.d("BOTTOM_SHEET", "STATE_HIDDEN")
                        //bottomSheetBehavior.setPeekHeight(heightPixels / 4, true)
                    }

                    BottomSheetBehavior.STATE_SETTLING -> {
                        Log.d("BOTTOM_SHEET", "STATE_SETTLING")

                    }

                    BottomSheetBehavior.STATE_DRAGGING -> {
                        if (!isOnTop) {
                            (bottomSheetBehavior as BottomSheetBehavior<*>).state =
                                BottomSheetBehavior.STATE_EXPANDED
                        }


                    }

                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        Log.d("BOTTOM_SHEET", "STATE_SETTLING")

                    }
                }


            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })
//


        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED)
            {
                viewModel.journey.observe(
                    requireActivity(),
                    object : Observer<JourneyEntity> {
                        override fun onChanged(value: JourneyEntity) {


                            if (value.listPlaces.isNotEmpty()) {
                                adapter.submitNewList(value.listPlaces)

                                val firstPoint = value.listPlaces.first()
                                val lastPoint = value.listPlaces.last()

                                val dayInterVal =
                                    Math.abs(lastPoint.timestamp - firstPoint.timestamp) / (1000 * 60 * 60)
                                binding.tvEstTime.text = dayInterVal.toString() + "hours"
                                binding.tvTotalCheckPoints.text =
                                    value.listPlaces.size.toString()

                                mapController.animateTo(
                                    GeoPoint(
                                        firstPoint.lat,
                                        firstPoint.lon
                                    )
                                )
                            }


                            val listGeo = value.listPlaces.map {
                                GeoPoint(it.lat, it.lon)
                            }

                            value.listPlaces.forEachIndexed { indx, place ->
                                addMarkerOnMap(place, GeoPoint(place.lat, place.lon), indx)
                            }

                            AddPolyline(listGeo)


                        }

                    })
            }
        }

        Configuration.getInstance()
            .load(
                requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext())
            )
        mapView = binding.map
        mapController = mapView.controller

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


        binding.llBackup.setOnClickListener {
            viewModel.uploadJourneyToServer()
        }


        mapView.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                if (binding.llFunction.visibility == View.VISIBLE) {
                    binding.llFunction.visibility = View.GONE
                } else {
                    if (binding.llFunction.visibility == View.GONE)
                        binding.llFunction.visibility = View.VISIBLE
                }


                return false
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }

        }))



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

    fun addMarkerOnMap(place: PlaceEntity, geoPoint: GeoPoint, order: Int) {

        val newMarker = CreateDrawableMarker.CreateDrawableFromLayout(requireContext(), order)

        Marker(mapView)?.apply {
            this.icon = newMarker
            this.position = geoPoint
            mapView.overlays.add(this)
            this.setOnMarkerClickListener(object : Marker.OnMarkerClickListener {
                override fun onMarkerClick(marker: Marker?, mapView: MapView?): Boolean {
                    expandCollapseSheet()
                    selectItem = place
                    return true
                }

            })
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

    fun showBottomSheet() {
        val bottomSheetFragment = CustomBottomSheet()
        bottomSheetFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetFragment.tag
        )
    }

    fun expandCollapseSheet() {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        }
    }


}