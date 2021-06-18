package pl.training.goodweather.tracking.adapter.view

import android.location.Location
import androidx.lifecycle.ViewModel
import pl.training.goodweather.common.formatPace
import pl.training.goodweather.common.formatSpeed
import pl.training.goodweather.common.formatTime
import pl.training.goodweather.tracking.model.ActivityPoint
import java.util.*

class TrackingViewModel : ViewModel() {

    private var startTime = 0L
    private var activityId = ""
    private var cumulativeDistance = 0F
    private var lastLocation: Location? = null
    private var lastPoint: ActivityPoint? = null

    var currentSpeed = ""
    var currentPace = ""
    var currentDuration = ""

    fun start() {
        startTime = System.currentTimeMillis()
        activityId = UUID.randomUUID().toString()
        cumulativeDistance = 0F
    }

    fun onLocation(location: Location) : Pair<ActivityPoint?, ActivityPoint> {
        val currentTime = System.currentTimeMillis()
        val duration = currentTime - startTime
        val speed = location.speed
        var distance = 0F
        lastLocation?.let { distance = location.distanceTo(it) }
        cumulativeDistance += distance
        var pace = 0.0
        if (cumulativeDistance > 0) {
            pace = (duration.toDouble() / (1_000 * 60) / (cumulativeDistance / 1_000))
        }
        val point = ActivityPoint(null, activityId, Date(currentTime), distance, speed, pace, duration, location.longitude, location.latitude)
        val result = Pair(lastPoint, point)
        lastPoint = point
        lastLocation = location

        currentSpeed = formatSpeed(point.speed)
        currentPace = formatPace(point.pace)
        currentDuration = formatTime(point.duration)
        return result
    }

}