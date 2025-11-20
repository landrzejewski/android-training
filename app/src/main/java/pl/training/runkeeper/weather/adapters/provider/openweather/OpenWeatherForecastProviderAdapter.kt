package pl.training.runkeeper.weather.adapters.provider.openweather

import pl.training.runkeeper.weather.domain.DayForecast
import pl.training.runkeeper.weather.domain.ForecastProvider

class OpenWeatherForecastProviderAdapter(
    private val api: OpenWeatherApi,
    private val mapper: OpenWeatherForecastProviderMapper
) : ForecastProvider {

    override suspend fun getForecast(city: String) = try {
        api.getForecast(city)
            .forecast
            .map(mapper::toModel)
    } catch (_: Exception) {
        emptyList<DayForecast>()
    }

}