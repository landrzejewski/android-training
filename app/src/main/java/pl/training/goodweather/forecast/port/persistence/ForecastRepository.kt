package pl.training.goodweather.forecast.port.persistence

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import pl.training.goodweather.forecast.model.DayForecast

interface ForecastRepository {

    fun save(city: String, forecast: List<DayForecast>): Completable

    fun getAll(city: String): Maybe<List<DayForecast>>

    fun deleteAll(): Completable

}