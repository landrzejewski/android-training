package pl.training.runkeeper.weather.adapters.provider.openweather

import pl.training.runkeeper.weather.domain.ForecastProvider

class OpenWeatherForecastProviderAdapter(
    private val api: OpenWeatherApi,
    private val mapper: OpenWeatherForecastProviderMapper
) : ForecastProvider {

    override suspend fun getForecast(city: String) = api.getForecast(city).forecast
            .map(mapper::toModel)

}