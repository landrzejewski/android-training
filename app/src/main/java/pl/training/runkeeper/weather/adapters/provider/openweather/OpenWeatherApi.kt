package pl.training.runkeeper.weather.adapters.provider.openweather

import pl.training.runkeeper.weather.adapters.provider.openweather.dto.ResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("forecast/daily?units=metric")
    suspend fun getForecast(@Query("q") city: String): ResponseDto

}