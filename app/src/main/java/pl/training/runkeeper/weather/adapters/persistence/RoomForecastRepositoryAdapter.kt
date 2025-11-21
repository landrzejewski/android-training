package pl.training.runkeeper.weather.adapters.persistence

import pl.training.runkeeper.weather.domain.DayForecast
import pl.training.runkeeper.weather.domain.ForecastRepository

class RoomForecastRepositoryAdapter(
    private val dao: RoomForecastDao,
    private val mapper: RoomForecastRepositoryMapper
) : ForecastRepository {

    override suspend fun findByCity(city: String) = dao.findByCity(city)
        ?.forecast?.map(mapper::toModel) ?: emptyList()

    override suspend fun replace(city: String, forecast: List<DayForecast>) {
        val cityEntity = mapper.toEntity(city)
        val forecastEntities = forecast.map { mapper.toEntity(city, it) }
        dao.replace(cityEntity, forecastEntities)
    }

}