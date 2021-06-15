package pl.training.goodweather.forecast.adapter.provider

import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApi {

    @GET("forecast/daily?cnt=14&units=metric&APPID=b933866e6489f58987b2898c89f542b8")
    suspend fun getForecast(@Query("q") city: String): ResponseDto

}