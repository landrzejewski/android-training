package pl.training.goodweather.forecast.port.provider

import pl.training.goodweather.forecast.model.DayForecast

interface ForecastProvider {

   suspend fun getForecast(city: String): List<DayForecast>

}