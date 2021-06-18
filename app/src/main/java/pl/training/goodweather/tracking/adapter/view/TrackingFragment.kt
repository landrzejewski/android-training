package pl.training.goodweather.tracking.adapter.view

import android.Manifest
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.activityViewModels
import com.google.android.gms.common.api.GoogleApiClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import pl.training.goodweather.R
import pl.training.runkeeper.tracking.models.ActivityPoint
import javax.inject.Inject

class TrackingFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tracking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

}

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
        initViews()
        bindViews()
    }

    private fun initViews() {
        mapFragment = childFragmentManager.findFragmentById(R.id.tracking_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun bindViews() {

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PermissionChecker.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), mapPermissionRequestCode)
        } else {
            configureMap()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] != PermissionChecker.PERMISSION_GRANTED) {
           if (requestCode == mapPermissionRequestCode) {
               requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), mapPermissionRequestCode)
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
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PermissionChecker.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionRequestCode)
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