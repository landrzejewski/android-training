package pl.training.runkeeper.weather.domain

class ForecastService(private val forecastProvider: ForecastProvider) {

    suspend fun getForecast(city: String) = forecastProvider.getForecast(city)
        .take(5)

}