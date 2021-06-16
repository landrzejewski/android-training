package pl.training.goodweather.forecast.model

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import pl.training.goodweather.common.UserPreferences
import pl.training.goodweather.forecast.port.persistence.ForecastRepository
import pl.training.goodweather.forecast.port.provider.ForecastProvider

class ForecastService(private val forecastProvider: ForecastProvider,
                      private val forecastRepository: ForecastRepository,
                      userPreferences: UserPreferences) {

    private var numberOfDays: Int
    private var cacheEnabled = true
    private val disposables = CompositeDisposable()

    init {
        val numberOfDaysKey = "forecast_number_of_days"
        val cacheEnabledKey = "forecast_cache_enabled"
        numberOfDays = userPreferences.get(numberOfDaysKey, "7").toInt()
        userPreferences.preferences
            .filter { it.first == numberOfDaysKey }
            .subscribe { numberOfDays = it.second.toInt() }
            .addTo(disposables)
        userPreferences.preferences
            .filter { it.first ==  cacheEnabledKey}
            .subscribe { cacheEnabled = it.second.toBoolean() }
            .addTo(disposables)
    }

    fun getForecast(city: String): Observable<List<DayForecast>> {
        val cachedForecast = forecastRepository.getAll(city)
            .toObservable()
        val forecast = forecastProvider.getForecast(city, numberOfDays)
            .flatMap {
                forecastRepository.deleteAll()
                    .andThen(forecastRepository.save(city, it))
                    .andThen(Maybe.just(it))
            }
            .toObservable()
        return if (cacheEnabled)  Observable.concat(cachedForecast, forecast) else forecast
    }

}