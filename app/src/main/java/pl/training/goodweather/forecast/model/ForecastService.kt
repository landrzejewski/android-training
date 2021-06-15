package pl.training.goodweather.forecast.model

import pl.training.goodweather.forecast.adapter.provider.ForecastApi
import pl.training.goodweather.forecast.adapter.provider.RetrofitForecastProvider
import pl.training.goodweather.forecast.port.provider.ForecastProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ForecastService {

    private val forecastApi = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ForecastApi::class.java)
    private val forecastProvider: ForecastProvider = RetrofitForecastProvider(forecastApi)

    suspend fun getForecast(city: String) = forecastProvider.getForecast(city)

}