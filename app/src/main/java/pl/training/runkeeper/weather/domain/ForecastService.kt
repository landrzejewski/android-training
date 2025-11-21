package pl.training.runkeeper.weather.domain

import android.util.Log

class ForecastService(
    private val forecastProvider: ForecastProvider,
    private val forecastRepository: ForecastRepository
) {

    suspend fun getForecast(city: String) = try {
        val forecast = forecastProvider.getForecast(city).take(5)
        forecastRepository.replace(city, forecast)
        forecast
    } catch (exception: RuntimeException) {
        Log.d(ForecastService::class.java.canonicalName, "Refresh forecast failed: ${exception.message}")
        throw RefreshForecastFailedException()
    }

    suspend fun getCachedForecast(city: String) = forecastRepository.findByCity(city)

}