package pl.training.runkeeper.tracking.adapters.view

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import pl.training.runkeeper.R
import pl.training.runkeeper.databinding.FragmentTrackingBinding

class TrackingFragment : Fragment() {

    private lateinit var binding: FragmentTrackingBinding
    private lateinit var map: GoogleMap
    private lateinit var locationClient: FusedLocationProviderClient
    private val viewModel: TrackingViewModel by activityViewModels()
    private val locationRequest = LocationRequest.Builder(1_000)
        .setPriority(PRIORITY_HIGH_ACCURACY)
        .setMinUpdateDistanceMeters(5F)
        .build()
    private var routeColor = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentTrackingBinding.inflate(layoutInflater)
        locationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        routeColor = ContextCompat.getColor(requireContext(), R.color.main)
        initView()
    }

    private fun initView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(onMapReady)
        binding.startButton.setOnClickListener {
            locationClient.lastLocation.addOnSuccessListener {
                viewModel.startActivity(it.latitude to it.longitude)
            }
        }
        viewModel.stats.observe(viewLifecycleOwner, ::update)
    }

    private fun update(stats: TrackingStatsViewModel) {
        binding.durationText.text = stats.duration
        binding.speedText.text = stats.speed
        binding.paceText.text = stats.pace
    }

    private val onMapReady = OnMapReadyCallback {
        map = it
        checkPermissions.launch(ACCESS_FINE_LOCATION)
    }

    private val checkPermissions = registerForActivityResult(RequestPermission()) { isGranted ->
        if (isGranted) {
            configureMap()
            locationClient.requestLocationUpdates(locationRequest, onLocationUpdate, Looper.getMainLooper())
        } else {
            Toast.makeText(requireContext(), "Location access is required", Toast.LENGTH_LONG).show()
        }
    }

    private val onLocationUpdate = object : LocationCallback() {

        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            result.lastLocation?.let { location ->
                moveCamera(location)
                viewModel.getLastPosition()?.let { lastPosition ->
                    drawRoute(location)
                    val distanceChange = Location(null)
                        .also {
                            it.latitude = lastPosition.first
                            it.longitude = lastPosition.second
                        }
                        .distanceTo(location)
                    val position = location.latitude to location.longitude
                    viewModel.createActivityPoint(position, distanceChange, location.speed)
                }
            }
        }

    }

    private fun moveCamera(location: Location) {
        val position = LatLng(location.latitude, location.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, CAMERA_ZOOM)
        map.moveCamera(cameraUpdate)
    }

    private fun drawRoute(location: Location) {
        viewModel.getLastPosition()?.let {
            val options = PolylineOptions()
            options.width(ROUTE_WIDTH)
            options.color(routeColor)
            options.add(LatLng(location.latitude, location.longitude))
            options.add(LatLng(it.first, it.second))
            map.addPolyline(options)
        }
    }

    override fun onPause() {
        super.onPause()
        locationClient.removeLocationUpdates(onLocationUpdate)
    }

    private fun configureMap() {
        map.isMyLocationEnabled = true
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
    }

    private companion object {

        const val CAMERA_ZOOM = 16F
        const val ROUTE_WIDTH = 10F

    }


}