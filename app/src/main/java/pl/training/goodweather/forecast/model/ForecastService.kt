package pl.training.goodweather.forecast.model

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import pl.training.goodweather.forecast.port.persistence.ForecastRepository
import pl.training.goodweather.forecast.port.provider.ForecastProvider


class ForecastService(private val forecastProvider: ForecastProvider, private val forecastRepository: ForecastRepository) {

    fun getForecast(city: String): Observable<List<DayForecast>> {
        val cachedForecast = forecastRepository.getAll(city)
            .toObservable()
        val forecast = forecastProvider.getForecast(city)
            .flatMap {
                forecastRepository.deleteAll()
                    .andThen(forecastRepository.save(city, it))
                    .andThen(Maybe.just(it))
            }
            .toObservable()
        return Observable.concat(cachedForecast, forecast)
    }


}