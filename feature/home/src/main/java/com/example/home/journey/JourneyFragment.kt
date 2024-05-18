package com.example.home.journey


import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.net.Uri
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.home.R
import com.example.home.adapter.PlaceAdapter
import com.example.home.databinding.FragmentJourneyBinding
import com.example.home.utils.CalculateDistance
import com.example.home.utils.CreateDrawableMarker
import com.example.home.utils.CustomBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.inter.entity.planner.JourneyEntity
import com.inter.entity.planner.PlaceEntity
import com.inter.mylocation.BackgroundLocationService
import com.inter.mylocation.LocationRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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


    var serviceIntent: Intent? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJourneyBinding.inflate(inflater, container, false)
        arguments?.apply {
            (getString("journey_id"))?.apply {
                journeyId = this
            }
        }

        gridPlace = binding.root.findViewById(R.id.gvPlaceImg)
        bottomSheet = binding.root.findViewById(R.id.bottomSheet)

        adapter = PlaceAdapter(object : PlaceAdapter.OnItemSelectOptionListener {
            override fun onDeletePlace(position: Int) {

                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                builder.setTitle("You are about to delete this place")
                    .setMessage("It can not be recovered.")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->

                        if (adapter.listPlaces.isNotEmpty()) {
                            adapter.listPlaces.apply {
                                val place = this.get(position)
                                viewModel.deletePlaceAndItsImage(place)
                                dialog.dismiss() // Close the dialog
                            }
                        }


                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                        // Handle Cancel button click
                        dialog.dismiss() // Close the dialog
                    })

                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()

            }

            override fun onNavigatePlace(position: Int) {
                val place = adapter.listPlaces.get(position)
                place?.apply {
                    val latitude = this.lat
                    val longitude = this.lon
                    val gmmIntentUri =
                        Uri.parse("geo:${latitude},${longitude}?z=17&q=${latitude},${longitude}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    startActivity(mapIntent)
                }

            }

            override fun onSharePlace(position: Int) {


            }

            override fun onSelectPlace(position: Int) {
                //  TODO("Not yet implemented")
            }

        })
        gridPlace.adapter = adapter
        mapView = binding.map
        mapController = mapView.controller



        return binding.root

    }

    fun addMarkerOnMap(place: PlaceEntity, geoPoint: GeoPoint, order: Int) {
        val newMarker = CreateDrawableMarker.CreateDrawableFromLayout(requireContext(), order)


        Marker(mapView).apply {
            this.icon = newMarker
            this.position = geoPoint
            mapView.overlays.add(this)


            this.setOnMarkerClickListener(object : Marker.OnMarkerClickListener {
                override fun onMarkerClick(marker: Marker?, mapView: MapView?): Boolean {
                    expandCollapseSheet()
                    selectItem = place
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                        delay(100)
                        gridPlace.smoothScrollToPosition(order)
                    }
                    return true
                }

            })

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
        val bottomSheetFragment =
            CustomBottomSheetDialog(object : CustomBottomSheetDialog.OnSelectCameraGalary {
                override fun onSelectCamera() {

                    val action = JourneyFragmentDirections.actJourneyCamera(journeyId)
                    findNavController().navigate(action)
                }

                override fun onSelectGalary() {
                    TODO("Not yet implemented")
                }
            })

        bottomSheetFragment.show(
            requireActivity().supportFragmentManager, bottomSheetFragment.tag
        )

    }

    fun expandCollapseSheet() {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                journeyId?.apply {
                    viewModel.getJourney(journeyId)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.syncJourney.observe(viewLifecycleOwner, object : Observer<Boolean> {
                override fun onChanged(value: Boolean) {
                    if (true == value) {
                        binding.ivStartStop.setImageResource(R.drawable.ic_stop)
                    } else {
                        binding.ivStartStop.setImageResource(R.drawable.ic_record)
                    }
                }

            })
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.journey.observe(requireActivity(), object : Observer<JourneyEntity> {
                    override fun onChanged(value: JourneyEntity) {
                        adapter.submitNewList(value.listPlaces)

                        if (value.listPlaces.isNotEmpty()) {
                            val firstPoint = value.listPlaces.first()
                            val lastPoint = value.listPlaces.last()
                            val dayInterVal =
                                Math.abs(lastPoint.timestamp - firstPoint.timestamp) / (1000 * 60 * 60)
                            binding.tvEstTime.text = dayInterVal.toString() + "hours"
                            binding.tvTotalCheckPoints.text = value.listPlaces.size.toString()
                            mapController.animateTo(
                                GeoPoint(
                                    firstPoint.lat, firstPoint.lon
                                )
                            )

                            val listGeo = value.listPlaces.map {
                                GeoPoint(it.lat, it.lon)
                            }

                            value.listPlaces.forEachIndexed { indx, place ->
                                addMarkerOnMap(place, GeoPoint(place.lat, place.lon), indx)
                                Log.d("ADDDDD", "" + indx + " : " + place.lat + "/" + place.lon)
                            }
                            AddPolyline(listGeo)

                            val listCoordinate = value.listPlaces.map {
                                Pair(it.lat, it.lon)
                            }

                            val totaldistance = CalculateDistance.GetDistance(listCoordinate)
                            binding.tvEstDistance.text = totaldistance + "kms"
                        }
                    }
                })
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            LocationRepository.myLocation.observe(viewLifecycleOwner, object : Observer<Location> {
                override fun onChanged(value: Location) {
//                    viewModel.

//                    binding.btnForeGround.text = "" + value.latitude + ":" + value.longitude
                }

            })
        }



        binding.llBackup.setOnClickListener {
            viewModel.uploadJourneyToServer()
        }

        binding.llStartStop.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                if (!viewModel.isTracking())
                    viewModel.setDataForbindService()
                else {
                    viewModel.removeDatabindService()
                }
            }

        })

        binding.ivCurrentLocation.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                if (!viewModel.journey.value?.listPlaces.isNullOrEmpty()) {
                    viewModel.journey.value?.listPlaces?.first()?.apply {
                        mapController.animateTo(
                            GeoPoint(
                                this.lat, this.lon
                            )
                        )
                    }

                }


//                val myPrevLocation = LocationRepository.myLocation?.value
//                myPrevLocation?.apply {
//                    mapController.animateTo(GeoPoint(this.latitude, this.longitude))
//                    mapController.setZoom(18)
//                }
            }
        })

        binding.ivTarget.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val myPrevLocation = LocationRepository.myLocation?.value
                myPrevLocation?.apply {
                    mapController.animateTo(GeoPoint(this.latitude, this.longitude))
                    mapController.setZoom(18)
                }
            }
        })

        binding.ivAddPlace.setOnClickListener {
            showBottomSheet()

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED)
            {
                serviceIntent = Intent(requireActivity(), BackgroundLocationService::class.java)
                requireActivity().startService(serviceIntent)
                requireActivity().bindService(
                    serviceIntent!!, viewModel.serviceConnection, Context.BIND_AUTO_CREATE
                )
            }


        }

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

        Configuration.getInstance().load(
            requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext())
        )


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

        mapView.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                if (binding.llFunction.visibility == View.VISIBLE) {
                    binding.llFunction.visibility = View.GONE
                } else {
                    if (binding.llFunction.visibility == View.GONE) binding.llFunction.visibility =
                        View.VISIBLE
                }
                return false
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }))
        mapController.setZoom(13)


    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
//        val manager =
//            requireContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
//        for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
//            if (serviceClass.getName() == service.service.className) {
//                return true
//            }
//        }
        return false
    }


    override fun onDestroy() {
        super.onDestroy()
        // if service not bind  -> stop
        if (false == viewModel.isTracking()) {
            requireActivity().stopService(serviceIntent)
        }
        requireActivity().unbindService(viewModel.serviceConnection)
    }

}