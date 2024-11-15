package pl.training.runkeeper.weather.domain

class ForecastService(private val forecastProvider: ForecastProvider) {

    suspend fun getForecast(city: String) =
        try {
            forecastProvider.getForecast(city).take(5)
        } catch (exception: Exception) {
            throw RefreshForecastFailedException()
        }

}