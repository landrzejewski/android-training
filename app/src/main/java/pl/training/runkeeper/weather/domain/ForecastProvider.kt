package pl.training.runkeeper.weather.domain

interface ForecastProvider {

    suspend fun getForecast(city: String): List<DayForecast>

}