package pl.training.runkeeper.weather

import pl.training.runkeeper.weather.adapters.provider.FakeForecastProvider
import pl.training.runkeeper.weather.domain.ForecastService

object WeatherConfiguration {

    private val forecastService = ForecastService(FakeForecastProvider())

    fun getForecastService() = forecastService

}