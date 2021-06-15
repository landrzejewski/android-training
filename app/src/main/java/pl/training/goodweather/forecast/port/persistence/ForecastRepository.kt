package pl.training.goodweather.forecast.port.persistence

import pl.training.goodweather.forecast.model.DayForecast

interface ForecastRepository {

    suspend fun save(city: String, forecast: List<DayForecast>)

    suspend fun getAll(city: String): List<DayForecast>

    suspend fun deleteAll()

}