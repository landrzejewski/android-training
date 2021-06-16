package pl.training.goodweather.forecast.adapter.provider

import io.reactivex.rxjava3.core.Maybe
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApi {

    @GET("forecast/daily?units=metric&APPID=b933866e6489f58987b2898c89f542b8")
    fun getForecast(@Query("q") city: String, @Query("cnt") numberOfDays: Int): Maybe<ResponseDto>

}