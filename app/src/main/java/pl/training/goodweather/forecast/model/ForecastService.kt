package pl.training.goodweather.forecast.model

import pl.training.goodweather.forecast.port.persistence.ForecastRepository
import pl.training.goodweather.forecast.port.provider.ForecastProvider

class ForecastService(private val forecastProvider: ForecastProvider, private val forecastRepository: ForecastRepository) {

    suspend fun getForecast(city: String): List<DayForecast> {
        val forecast = forecastProvider.getForecast(city)
        forecastRepository.deleteAll()
        forecastRepository.save(city, forecast)
        return forecast
    }

    suspend fun getCachedForecast(city: String) = forecastRepository.getAll(city)

}