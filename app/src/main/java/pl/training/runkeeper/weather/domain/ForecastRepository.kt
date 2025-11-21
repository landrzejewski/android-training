package pl.training.runkeeper.weather.domain

interface ForecastRepository {

    suspend fun findByCity(city: String): List<DayForecast>

    suspend fun replace(city: String, forecast: List<DayForecast>)

}