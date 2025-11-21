package pl.training.runkeeper.weather.domain

import android.util.Log

class ForecastService(private val forecastProvider: ForecastProvider) {

    suspend fun getForecast(city: String) = try {
        forecastProvider.getForecast(city)
            .take(5)

    } catch (exception: RuntimeException) {
        Log.d(ForecastService::class.java.canonicalName, "Refresh forecast failed: ${exception.message}")
        throw RefreshForecastFailedException()
    }

}