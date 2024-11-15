package pl.training.runkeeper.weather.adapters.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pl.training.runkeeper.weather.WeatherConfiguration

class ForecastViewModel {

    private val forecastService = WeatherConfiguration.forecastService
    private val forecastData = MutableLiveData<List<DayForecastViewModel>>()

    val forecast: LiveData<List<DayForecastViewModel>> = forecastData

}