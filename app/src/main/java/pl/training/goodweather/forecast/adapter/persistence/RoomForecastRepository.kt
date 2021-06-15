package pl.training.goodweather.forecast.adapter.persistence

import pl.training.goodweather.forecast.model.DayForecast
import pl.training.goodweather.forecast.port.persistence.ForecastRepository
import java.util.*

class RoomForecastRepository(private val forecastDao: ForecastDao) : ForecastRepository {

    override suspend fun save(city: String, forecast: List<DayForecast>) {
        forecastDao.save(CityEntity(1L, city))
        forecastDao.save(forecast.map { toRoomModel(1L, it)})
    }

    override suspend fun getAll(city: String) = forecastDao.findAll(city).forecast.map(::toDomainModel)

    override suspend fun deleteAll() {
       forecastDao.deleteAll()
    }

    private fun toRoomModel(cityId: Long, dayForecast: DayForecast) = DayForecastEntity(null,
        dayForecast.icon, dayForecast.description, dayForecast.temperature,
        dayForecast.pressure, dayForecast.date.time, cityId)

    private fun toDomainModel(dayForecastEntity: DayForecastEntity) = with(dayForecastEntity) {
        DayForecast(icon, description, temperature, pressure, Date(date))
    }

}