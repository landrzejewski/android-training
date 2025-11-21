package pl.training.runkeeper.weather

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import pl.training.runkeeper.common.AppIdInterceptor
import pl.training.runkeeper.weather.adapters.provider.FakeForecastProvider
import pl.training.runkeeper.weather.adapters.provider.openweather.OpenWeatherApi
import pl.training.runkeeper.weather.adapters.provider.openweather.OpenWeatherForecastProviderAdapter
import pl.training.runkeeper.weather.adapters.provider.openweather.OpenWeatherForecastProviderMapper
import pl.training.runkeeper.weather.domain.ForecastService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherConfiguration {

    // https://api.openweathermap.org/data/2.5/forecast/daily?units=metric&appid=b933866e6489f58987b2898c89f542b8&q=warsaw

    val fakeForecastService by lazy { ForecastService(FakeForecastProvider()) }

    private val httpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient().newBuilder()
            .addInterceptor(AppIdInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val openWeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(OpenWeatherApi::class.java)
    }

    private val openWeatherForecastProviderMapper by lazy { OpenWeatherForecastProviderMapper() }

    private val openWeatherForecastProviderAdapter by lazy {
        OpenWeatherForecastProviderAdapter(openWeatherApi, openWeatherForecastProviderMapper)
    }

    val forecastService by lazy { ForecastService(openWeatherForecastProviderAdapter) }

}