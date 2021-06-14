package pl.training.goodweather.forecast.model

import java.util.*

class FakeForecastProvider : ForecastProvider {

    override suspend fun getForecast(city: String) = listOf(
        DayForecast("ic_sun", "Clear sky", 18.0, 1024.0, Date()),
        DayForecast("ic_sun", "Clear sky", 18.0, 1024.0, Date()),
        DayForecast("ic_sun", "Clear sky", 18.0, 1024.0, Date()),
        DayForecast("ic_sun", "Clear sky", 18.0, 1024.0, Date()),
        DayForecast("ic_sun", "Clear sky", 18.0, 1024.0, Date()),
        DayForecast("ic_sun", "Clear sky", 18.0, 1024.0, Date()),
        DayForecast("ic_sun", "Clear sky", 18.0, 1024.0, Date())
    )

}