package pl.training.runkeeper.tracking.domain

import pl.training.runkeeper.tracking.domain.ActivityType.RUNNING
import java.util.UUID

class Activity(private val type: ActivityType = RUNNING) {
    private val id: UUID = UUID.randomUUID()
    private val activityPoints = mutableListOf<ActivityPoint>()

    fun start(position: Position) {
        if (activityPoints.isNotEmpty()) {
            throw IllegalStateException()
        }
        addActivityPoint(position, 0F, 0F)
    }

    fun reset() {
        activityPoints.clear()
    }

    fun isStarted() = activityPoints.isNotEmpty()

    fun getLastActivityPoint() = activityPoints.lastOrNull()

    fun addActivityPoint(position: Position, distanceChange: Float, speed: Float) {
        val activityPoint = createActivityPoint(position, distanceChange, speed)
        activityPoints.add(activityPoint)
    }

    private fun createActivityPoint(position: Position, distanceChange: Float, speed: Float): ActivityPoint {
        val timestamp = System.currentTimeMillis()
        val distance = if (activityPoints.isNotEmpty()) activityPoints.last().distance + distanceChange else 0F
        val duration = if (activityPoints.isEmpty()) 0 else timestamp - activityPoints.first().timestamp
        val pace = if (distance > 0) (duration.toFloat() / (1_000 * 60)) / (distance / 1_000) else 0.0F
        return ActivityPoint(timestamp = timestamp, distance = distance, speed = speed, pace = pace, duration = duration, position = position)
    }

}