package pl.training.goodweather.forecast.adapter.persistence

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import pl.training.goodweather.forecast.model.DayForecast
import pl.training.goodweather.forecast.port.persistence.ForecastRepository
import java.util.*


class RoomForecastRepository(private val forecastDao: ForecastDao) : ForecastRepository {

    override fun save(city: String, forecast: List<DayForecast>): Completable  = Completable
        .concat(listOf(forecastDao.save(CityEntity(1L, city)), forecastDao.save(forecast.map { toRoomModel(1L, it)})))
        .subscribeOn(Schedulers.io())

    override fun getAll(city: String): Maybe<List<DayForecast>> = forecastDao.findAll(city)
        .map { it.forecast.map { dayForecastEntity ->  toDomainModel(dayForecastEntity) } }
        .subscribeOn(Schedulers.io())

    override fun deleteAll() = forecastDao.deleteAll()
        .subscribeOn(Schedulers.io())

    private fun toRoomModel(cityId: Long, dayForecast: DayForecast) = DayForecastEntity(null,
        dayForecast.icon, dayForecast.description, dayForecast.temperature,
        dayForecast.pressure, dayForecast.date.time, cityId)

    private fun toDomainModel(dayForecastEntity: DayForecastEntity) = with(dayForecastEntity) {
        DayForecast(icon, description, temperature, pressure, Date(date))
    }

}