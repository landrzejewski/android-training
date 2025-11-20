package pl.training.runkeeper.weather.adapters.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.training.runkeeper.common.formatDate
import pl.training.runkeeper.common.formatPressure
import pl.training.runkeeper.common.formatTemperature
import pl.training.runkeeper.weather.WeatherConfiguration
import pl.training.runkeeper.weather.domain.DayForecast
import pl.training.runkeeper.weather.domain.ForecastService

class ForecastViewModel(private val forecastService: ForecastService = WeatherConfiguration.forecastService) :
    ViewModel() {

    private val forecastData = MutableLiveData<List<DayForecastViewModel>>()

    val forecast: LiveData<List<DayForecastViewModel>> = forecastData

    fun refreshForecast(city: String) {
        viewModelScope.launch {
            val data = forecastService.getForecast(city).map(::toViewModel)
            forecastData.postValue(data)
        }

    }

    private fun toViewModel(dayForecast: DayForecast) = with(dayForecast) {
        DayForecastViewModel(formatDate(date),
            formatTemperature(temperature),
            formatPressure(pressure),
            description,
            iconName
        )
    }

}