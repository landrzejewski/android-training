package pl.training.runkeeper.weather.adapters.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.training.runkeeper.R
import pl.training.runkeeper.RunkeeperApplication
import pl.training.runkeeper.common.ViewState
import pl.training.runkeeper.common.ViewState.Failure
import pl.training.runkeeper.common.ViewState.Initial
import pl.training.runkeeper.common.ViewState.Processing
import pl.training.runkeeper.common.ViewState.Success
import pl.training.runkeeper.common.formatDate
import pl.training.runkeeper.common.formatPressure
import pl.training.runkeeper.common.formatTemperature
import pl.training.runkeeper.weather.domain.DayForecast

class ForecastViewModel(application: Application) : AndroidViewModel(application) {

    private val forecastService = (application as RunkeeperApplication).forecastService
    private val store = (application as RunkeeperApplication).store
    private val state = MutableLiveData<ViewState>(Initial)

    val viewState: LiveData<ViewState> = state

    fun refreshForecast(city: String) {
        execute(
            task = { forecastService.getForecast(city) },
            onSuccess = {
                store.set(CITY_KEY, city)
                updateState(city, it)
            },
            onFailure = { state.postValue(Failure(R.string.forecast_refresh_failed)) }
        )
    }

    fun refreshForecastFromCache() {
        val city = store.get(CITY_KEY, DEFAULT_CITY)
        execute(
            task = { forecastService.getCachedForecast(city) },
            onSuccess = { updateState(city, it) }
        )
    }

    private fun updateState(city: String, forecast: List<DayForecast>) {
        if (forecast.isNotEmpty()) {
            state.postValue(Success(ViewData(city, forecast.map(::toViewModel))))
        }
    }

    private fun toViewModel(dayForecast: DayForecast) = with(dayForecast) {
        DayForecastViewModel(
            formatDate(date),
            formatTemperature(temperature),
            formatPressure(pressure),
            description,
            iconName
        )
    }

    private fun <T> execute(
        task: suspend () -> T,
        onSuccess: (T) -> Unit,
        onFailure: (Throwable) -> Unit = {}
    ) {
        state.postValue(Processing)
        viewModelScope.launch {
            try {
                val result = task()
                onSuccess(result)
            } catch (e: Throwable) {
                onFailure(e)
            }
        }
    }

    data class ViewData(
        val city: String,
        val forecast: List<DayForecastViewModel>
    )

    private companion object {

        const val CITY_KEY = "city"
        const val DEFAULT_CITY = ""

    }


}