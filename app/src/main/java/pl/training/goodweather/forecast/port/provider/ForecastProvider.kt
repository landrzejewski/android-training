package pl.training.goodweather.forecast.port.provider

import io.reactivex.rxjava3.core.Maybe
import pl.training.goodweather.forecast.model.DayForecast
import java.util.*

interface ForecastProvider {

   fun getForecast(city: String, numberOfDays: Int = 14): Maybe<List<DayForecast>>

}