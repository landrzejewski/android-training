package pl.training.goodweather.forecast

import dagger.Module
import dagger.Provides
import pl.training.goodweather.forecast.adapter.provider.ForecastApi
import pl.training.goodweather.forecast.adapter.provider.RetrofitForecastProvider
import pl.training.goodweather.forecast.model.ForecastService
import pl.training.goodweather.forecast.port.provider.ForecastProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ForecastModule {

    @Singleton
    @Provides
    fun forecastApi(): ForecastApi = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ForecastApi::class.java)

    @Singleton
    @Provides
    fun forecastProvider(forecastApi: ForecastApi): ForecastProvider = RetrofitForecastProvider(forecastApi)

    @Singleton
    @Provides
    fun forecastService(forecastProvider: ForecastProvider) = ForecastService(forecastProvider)

}