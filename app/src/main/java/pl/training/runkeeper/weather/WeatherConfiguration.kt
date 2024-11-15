package pl.training.runkeeper.weather

import pl.training.runkeeper.weather.adapters.provider.FakeForecastProvider
import pl.training.runkeeper.weather.domain.ForecastService

object WeatherConfiguration {

    val forecastService by lazy { ForecastService(FakeForecastProvider()) }

}