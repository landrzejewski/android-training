package pl.training.runkeeper.weather

import pl.training.runkeeper.weather.adapters.provider.FakeForecastProvider
import pl.training.runkeeper.weather.domain.ForecastService

object WeatherConfiguration {

    // https://api.openweathermap.org/data/2.5/forecast/daily?units=metric&appid=b933866e6489f58987b2898c89f542b8&q=warsaw

    val forecastService by lazy { ForecastService(FakeForecastProvider()) }

}