package pl.training.runkeeper.weather

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import pl.training.runkeeper.common.AppIdInterceptor
import pl.training.runkeeper.weather.adapters.provider.FakeForecastProvider
import pl.training.runkeeper.weather.adapters.provider.openweather.OpenWeatherApi
import pl.training.runkeeper.weather.adapters.provider.openweather.OpenWeatherProviderAdapter
import pl.training.runkeeper.weather.adapters.provider.openweather.OpenWeatherProviderMapper
import pl.training.runkeeper.weather.domain.ForecastService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherConfiguration {

    private val fakeForecastProvider by lazy { FakeForecastProvider() }

    private val httpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = BASIC
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

    private val openWeatherMapper by lazy { OpenWeatherProviderMapper() }

    private val openWeatherProviderAdapter by lazy {
        OpenWeatherProviderAdapter(openWeatherApi, openWeatherMapper)
    }

    val forecastService by lazy { ForecastService(openWeatherProviderAdapter) }

}