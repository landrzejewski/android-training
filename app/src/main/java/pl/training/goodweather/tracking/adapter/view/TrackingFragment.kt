package pl.training.goodweather.tracking.adapter.view

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.activityViewModels
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import pl.training.goodweather.GoodWeatherApplication.Companion.applicationGraph
import pl.training.goodweather.R
import pl.training.goodweather.common.logging.Logger
import pl.training.goodweather.databinding.FragmentTrackingBinding
import pl.training.goodweather.tracking.model.ActivityPoint
import javax.inject.Inject

class TrackingFragment : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks {

    private val viewModel: TrackingViewModel by activityViewModels()
    @Inject
    lateinit var logger: Logger
    private lateinit var binding: FragmentTrackingBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleApiClient: GoogleApiClient
    private val mapPermissionRequestCode = 999
    private val locationPermissionRequestCode = 998
    private val defaultZoom = 16F
    private val locationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            locationResult?.let { result ->
                centerCameraOnLocation(result.lastLocation)
                updateStats()
                drawRoute(viewModel.onLocation(result.lastLocation))
            }
        }

    }
    private var map: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        applicationGraph.inject(this)
        binding = FragmentTrackingBinding.inflate(inflater)
        createGoogleApiClient()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mapFragment = childFragmentManager.findFragmentById(R.id.tracking_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        if (checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) != PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(ACCESS_FINE_LOCATION), mapPermissionRequestCode)
        } else {
            configureMap()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions.contains(ACCESS_FINE_LOCATION) && grantResults[0] != PERMISSION_GRANTED) {
           if (requestCode == mapPermissionRequestCode) {
               requestPermissions(arrayOf(ACCESS_FINE_LOCATION), mapPermissionRequestCode)
           }
           if (requestCode == locationPermissionRequestCode) {
               startLocationUpdates()
           }
        }
    }

    @SuppressWarnings("MissingPermission")
    private fun configureMap() {
        map?.let {
            it.isMyLocationEnabled = true
            it.uiSettings.isCompassEnabled = false
            it.uiSettings.isMyLocationButtonEnabled = false
        }
        googleApiClient.connect()
    }

    private fun createGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(requireContext())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener { logger.log("Connection to Google Play failed") }
            .addApi(LocationServices.API)
            .build()
    }

    override fun onConnected(bundle: Bundle?) {
        startLocationUpdates()
    }

    override fun onConnectionSuspended(p0: Int) {
        logger.log("Connection to Google Play is suspended")
    }

    private fun startLocationUpdates() {
        if (checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            requestPermissions(arrayOf(ACCESS_FINE_LOCATION), locationPermissionRequestCode)
        } else {
            viewModel.start()
            val locationRequest = LocationRequest()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 1000
            locationRequest.fastestInterval = 1000
            locationRequest.smallestDisplacement = 1F
            LocationServices.getFusedLocationProviderClient(requireContext())
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        LocationServices.getFusedLocationProviderClient(requireContext())
            .removeLocationUpdates(locationCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapFragment.onDestroy()
    }

    private fun centerCameraOnLocation(location: Location) {
        val position = LatLng(location.latitude, location.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, defaultZoom)
        map?.animateCamera(cameraUpdate)
    }

    private fun updateStats() {
        with (binding) {
            trackingDuration.text = viewModel.currentDuration
            trackingSpeed.text = viewModel.currentSpeed
            trackingPace.text = viewModel.currentPace
        }
    }

    private fun drawRoute(points: Pair<ActivityPoint?, ActivityPoint>) {
       points.first?.let {
           val options = PolylineOptions()
           options.color(R.color.black)
           options.width(10F)
           options.add(LatLng(it.latitude, it.longitude))
           options.add(LatLng(points.second.latitude, points.second.longitude))
           map?.addPolyline(options)
       }
    }

}