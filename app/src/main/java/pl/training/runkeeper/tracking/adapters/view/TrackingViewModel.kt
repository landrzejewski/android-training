package pl.training.runkeeper.tracking.adapters.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.training.runkeeper.common.formatPace
import pl.training.runkeeper.common.formatSpeed
import pl.training.runkeeper.common.formatTime
import pl.training.runkeeper.tracking.domain.Activity
import pl.training.runkeeper.tracking.domain.ActivityPoint
import pl.training.runkeeper.tracking.domain.TrackingService
import pl.training.runkeeper.tracking.domain.Position
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val trackingService: TrackingService
) : ViewModel() {

    private val activity = Activity()
    private val trackingStats = MutableLiveData<TrackingStatsViewModel>()

    val stats: LiveData<TrackingStatsViewModel> = trackingStats

    fun startActivity(position: Position) {
        activity.reset()
        activity.start(position)
    }

    fun createActivityPoint(position: Position, distanceChange: Float, speed: Float) {
        if (activity.isStarted()) {
            activity.addActivityPoint(position, distanceChange, speed)
            activity.getLastActivityPoint()?.let {
                trackingStats.postValue(toViewModel(it))
            }
        }
    }

    fun getLastPosition() = activity.getLastActivityPoint()?.position

    private fun toViewModel(activityPoint: ActivityPoint) = with(activityPoint) {
        TrackingStatsViewModel(formatTime(duration), formatSpeed(speed), formatPace(pace))
    }

}