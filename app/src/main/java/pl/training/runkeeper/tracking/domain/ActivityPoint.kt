package pl.training.runkeeper.tracking.domain

import java.util.UUID

data class ActivityPoint(
    val id: UUID = UUID.randomUUID(),
    val timestamp: Long,
    val distance: Float,
    val speed: Float,
    val pace: Float,
    val duration: Long,
    val position: Position
)

typealias Position = Pair<Double, Double>