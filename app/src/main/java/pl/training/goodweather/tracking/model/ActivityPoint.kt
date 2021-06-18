package pl.training.runkeeper.tracking.models

import java.util.*

data class ActivityPoint(var id: Int?, val activityId: String, val timestamp: Date, val distance: Float,
    val speed: Float, val pace: Double, val duration: Long, val longitude: Double, val latitude: Double)