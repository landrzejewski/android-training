package pl.training.runkeeper.weather.adapters.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.training.runkeeper.R
import pl.training.runkeeper.common.ViewState
import pl.training.runkeeper.common.ViewState.Failure
import pl.training.runkeeper.common.ViewState.Initial
import pl.training.runkeeper.common.ViewState.Processing
import pl.training.runkeeper.common.ViewState.Success
import pl.training.runkeeper.common.formatDate
import pl.training.runkeeper.common.formatPressure
import pl.training.runkeeper.common.formatTemperature
import pl.training.runkeeper.weather.WeatherConfiguration
import pl.training.runkeeper.weather.domain.DayForecast
import pl.training.runkeeper.weather.domain.RefreshForecastFailedException

class ForecastViewModel : ViewModel() {

    private val forecastService = WeatherConfiguration.forecastService
    private val state = MutableLiveData<ViewState>(Initial)

    val viewState: LiveData<ViewState> = state

    fun refreshForecast(city: String) {
        viewModelScope.launch {
            state.postValue(Processing)
            try {
                val data = forecastService.getForecast(city).map(::toViewModel)
                // delay(1_000)
                state.postValue(Success(data))
            } catch (exception: RefreshForecastFailedException) {
                state.postValue(Failure(R.string.forecast_refresh_failed))
            }
        }
    }

    private fun toViewModel(dayForecast: DayForecast) = with(dayForecast) {
        DayForecastViewModel(formatDate(date), formatTemperature(temperature), formatPressure(pressure), description, iconName)
    }

}