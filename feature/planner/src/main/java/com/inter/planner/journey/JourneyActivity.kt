package com.inter.planner.journey

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AbsListView
import android.widget.GridView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.inter.entity.planner.JourneyEntity
import com.inter.entity.planner.PlaceEntity
import com.inter.mylocation.LocationRepository
import com.inter.planner.adapter.PlaceAdapter
import com.inter.planner.databinding.ActivityJourneyBinding
import com.inter.planner.utils.CreateDrawableMarker
import com.inter.planner.utils.CustomBottomSheet
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
class JourneyActivity : AppCompatActivity() {
    lateinit var binding: ActivityJourneyBinding
    val viewModel: JourneyViewModel by viewModels()

    lateinit var journeyId: String
    lateinit var mapView: MapView
    lateinit var mapController: IMapController

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    var selectItem: PlaceEntity? = null

    lateinit var adapter: PlaceAdapter
    lateinit var gridPlace: GridView

    var isOnTop: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityJourneyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        gridPlace = binding.bottomSheet.gvPlaceImg
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

        intent.extras?.apply {
            (getString("journey_id"))?.apply {
                journeyId = this
                viewModel.getJourney(this)
            }
        }

        val windowManager =
            getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val widthPixels = displayMetrics.widthPixels
        val heightPixels = displayMetrics.heightPixels

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        Log.d("BOTTOM_SHEET", "STATE_HIDDEN")
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {

//                        selectItem?.listImage?.apply {
//                            if (this.isNotEmpty()) {
//                                val imageview = binding.bottomSheet.ivPlaces
//                                val path = this.first().path
//                                Glide.with(this@JourneyActivity)
//                                    .load(path)
//                                    .apply(RequestOptions().centerCrop())
//                                    .transition(DrawableTransitionOptions.withCrossFade())
//                                    .into(imageview)
//                            }
//                        }


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



        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED)
            {
                viewModel.journey.observe(
                    this@JourneyActivity,
                    object : Observer<JourneyEntity> {
                        override fun onChanged(value: JourneyEntity) {


                            if (value.listPlaces.isNotEmpty()) {
                                adapter.submitNewList(value.listPlaces)

                                val firstPoint = value.listPlaces.first()
                                val lastPoint = value.listPlaces.last()

                                val dayInterVal =
                                    Math.abs(lastPoint.timestamp - firstPoint.timestamp) / (1000 * 60 * 60)
                                binding.tvEstTime.text = dayInterVal.toString() + "hours"
                                binding.tvTotalCheckPoints.text = value.listPlaces.size.toString()

                                mapController.animateTo(GeoPoint(firstPoint.lat, firstPoint.lon))
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
                this@JourneyActivity,
                PreferenceManager.getDefaultSharedPreferences(this@JourneyActivity)
            )
        mapView = binding.map
        mapController = mapView.controller

        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)

        val myLocationOverlay =
            MyLocationNewOverlay(GpsMyLocationProvider(this@JourneyActivity), mapView)
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


    }


    fun addMarkerOnMap(place: PlaceEntity, geoPoint: GeoPoint, order: Int) {

        val newMarker = CreateDrawableMarker.CreateDrawableFromLayout(this@JourneyActivity, order)

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
        bottomSheetFragment.show(this.supportFragmentManager, bottomSheetFragment.tag)
    }

    private fun expandCollapseSheet() {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        }
    }

}