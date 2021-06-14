package pl.training.goodweather.forecast.model.api

import pl.training.goodweather.forecast.model.DayForecast
import pl.training.goodweather.forecast.model.ForecastProvider
import java.util.*

class RetrofitForecastProvider(private val forecastApi: ForecastApi) : ForecastProvider {

    private val icons = mapOf("01d" to "ic_sun", "02d" to "ic_cloud_sun", "03d" to "ic_cloud", "04d" to "ic_cloud",
        "09d" to "ic_cloud_rain", "10d" to "ic_cloud_sun_rain", "11d" to "ic_bolt", "13d" to "ic_snowflake", "50d" to "ic_wind")

    override suspend fun getForecast(city: String): List<DayForecast> = forecastApi.getForecast(city).forecast.map {
        val weather = it.weather.first()
        val icon = icons[weather.icon] ?: "ic_launcher_foreground"
        DayForecast(icon, weather.description, it.temperature.day, it.pressure, Date(it.date * 1_000))
    }

}