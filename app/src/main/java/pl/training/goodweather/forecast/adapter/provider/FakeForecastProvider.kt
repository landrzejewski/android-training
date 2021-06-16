package pl.training.goodweather.forecast.adapter.provider

import io.reactivex.rxjava3.core.Maybe
import pl.training.goodweather.forecast.model.DayForecast
import pl.training.goodweather.forecast.port.provider.ForecastProvider
import java.util.*

class FakeForecastProvider : ForecastProvider {

    override fun getForecast(city: String): Maybe<List<DayForecast>> = Maybe.just(
        listOf(
            DayForecast("ic_sun", "Clear sky", 18.0, 1024.0, Date()),
            DayForecast("ic_sun", "Clear sky", 18.0, 1024.0, Date()),
            DayForecast("ic_sun", "Clear sky", 18.0, 1024.0, Date()),
            DayForecast("ic_sun", "Clear sky", 18.0, 1024.0, Date()),
            DayForecast("ic_sun", "Clear sky", 18.0, 1024.0, Date()),
            DayForecast("ic_sun", "Clear sky", 18.0, 1024.0, Date()),
            DayForecast("ic_sun", "Clear sky", 18.0, 1024.0, Date())
        )
    )

}