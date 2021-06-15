package pl.training.goodweather.forecast.adapter.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.training.goodweather.common.formatDate
import pl.training.goodweather.common.formatPressure
import pl.training.goodweather.common.formatTemperature
import pl.training.goodweather.forecast.model.DayForecast
import pl.training.goodweather.forecast.model.ForecastService

class ForecastViewModel : ViewModel() {

    private val forecastService = ForecastService()
    private val forecast = MutableLiveData<List<DayForecastViewModel>>()

    val currentForecast: LiveData<List<DayForecastViewModel>> = forecast

    fun refreshForecast(city: String) {
        viewModelScope.launch {
            forecast.value = forecastService.getForecast(city).map(::toViewModel)
        }
    }

    private fun toViewModel(dayForecast: DayForecast) = with(dayForecast) {
        DayForecastViewModel(icon, description, formatTemperature(temperature), formatPressure(pressure), formatDate(date))
    }

}