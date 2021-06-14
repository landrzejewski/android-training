package pl.training.goodweather.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.training.goodweather.forecast.model.DayForecast
import pl.training.goodweather.forecast.model.FakeForecastProvider
import pl.training.goodweather.forecast.model.ForecastProvider
import java.text.SimpleDateFormat
import java.util.*

class ForecastViewModel : ViewModel() {

    private val forecastProvider: ForecastProvider = FakeForecastProvider()
    private val forecast = MutableLiveData<List<DayForecastViewModel>>()

    val currentForecast: LiveData<List<DayForecastViewModel>> = forecast

    fun refreshForecast(city: String) {
        viewModelScope.launch {
            forecast.value = forecastProvider.getForecast(city).map(::toViewModel)
        }
    }

    private fun toViewModel(dayForecast: DayForecast) = with(dayForecast) {
        DayForecastViewModel(icon, description, formatTemperature(temperature), formatPressure(pressure), formatDate(date))
    }

    private fun formatTemperature(value: Double) = "${value.toInt()}°"

    private fun formatPressure(value: Double) = "${value.toInt()} hPa"

    private fun formatDate(date: Date, format: String = "dd/MM") = SimpleDateFormat(format, Locale.getDefault()).format(date)

}