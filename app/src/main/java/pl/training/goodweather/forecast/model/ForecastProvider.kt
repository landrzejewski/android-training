package pl.training.goodweather.forecast.model

interface ForecastProvider {

   suspend fun getForecast(city: String): List<DayForecast>

}