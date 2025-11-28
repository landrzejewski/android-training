package pl.training.runkeeper.weather.adapters.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.training.runkeeper.R
import pl.training.runkeeper.common.view.ViewState
import pl.training.runkeeper.common.view.ViewState.Failure
import pl.training.runkeeper.common.view.ViewState.Initial
import pl.training.runkeeper.common.view.ViewState.Processing
import pl.training.runkeeper.common.view.ViewState.Success
import pl.training.runkeeper.common.formatDate
import pl.training.runkeeper.common.formatPressure
import pl.training.runkeeper.common.formatTemperature
import pl.training.runkeeper.common.store.Store
import pl.training.runkeeper.weather.domain.DayForecast
import pl.training.runkeeper.weather.domain.ForecastService
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val forecastService: ForecastService,
    private val store: Store
) : ViewModel() {

    private val state = MutableLiveData<ViewState>(Initial)

    val viewState: LiveData<ViewState> = state
    var selectedDayForecast: DayForecastViewModel? = null

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