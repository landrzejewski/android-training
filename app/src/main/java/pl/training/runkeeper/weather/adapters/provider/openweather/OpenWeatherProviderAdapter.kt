package pl.training.runkeeper.weather.adapters.provider.openweather

import pl.training.runkeeper.weather.domain.DayForecast
import pl.training.runkeeper.weather.domain.ForecastProvider

class OpenWeatherProviderAdapter(
    private val openWeatherApi: OpenWeatherApi,
    private val mapper: OpenWeatherProviderMapper
) : ForecastProvider {

    override suspend fun getForecast(city: String): List<DayForecast> {
        return openWeatherApi.getForecast(city)
            .forecast
            .map(mapper::toModel)
    }

}