package pl.training.goodweather.forecast.adapter.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import pl.training.goodweather.GoodWeatherApplication.Companion.applicationGraph
import pl.training.goodweather.common.UserPreferences
import pl.training.goodweather.common.formatDate
import pl.training.goodweather.common.formatPressure
import pl.training.goodweather.common.formatTemperature
import pl.training.goodweather.common.logging.Logger
import pl.training.goodweather.forecast.model.DayForecast
import pl.training.goodweather.forecast.model.ForecastService
import javax.inject.Inject

class ForecastViewModel : ViewModel() {

    @Inject
    lateinit var forecastService: ForecastService
    @Inject
    lateinit var logger: Logger
    @Inject
    lateinit var userPreferences: UserPreferences

    private val cityKey = "cityName"
    private val defaultCity = "Warsaw"
    private val forecast = MutableLiveData<List<DayForecastViewModel>>()
    private val disposables = CompositeDisposable()

    val currentForecast: LiveData<List<DayForecastViewModel>> = forecast

    init {
        applicationGraph.inject(this)
        refreshForecast()
    }

    fun refreshForecast(city: String = userPreferences.get(cityKey, defaultCity)) {
        forecastService.getForecast(city)
            .subscribe({ onForecast(city, it) }, { logger.log(it.toString()) })
            .addTo(disposables)
    }

    private fun onForecast(city: String, forecast: List<DayForecast>) {
        userPreferences.set(cityKey, city)
        this.forecast.postValue(forecast.map(::toViewModel))
    }

    private fun toViewModel(dayForecast: DayForecast) = with(dayForecast) {
        DayForecastViewModel(icon, description, formatTemperature(temperature), formatPressure(pressure), formatDate(date))
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}