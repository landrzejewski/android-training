package pl.training.goodweather.forecast

import dagger.Module
import dagger.Provides
import pl.training.goodweather.configuration.ApplicationDatabase
import pl.training.goodweather.forecast.adapter.persistence.ForecastDao
import pl.training.goodweather.forecast.adapter.persistence.RoomForecastRepository
import pl.training.goodweather.forecast.adapter.provider.FakeForecastProvider
import pl.training.goodweather.forecast.adapter.provider.ForecastApi
import pl.training.goodweather.forecast.adapter.provider.RetrofitForecastProvider
import pl.training.goodweather.forecast.model.ForecastService
import pl.training.goodweather.forecast.port.persistence.ForecastRepository
import pl.training.goodweather.forecast.port.provider.ForecastProvider
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class ForecastModule {

    @Singleton
    @Provides
    fun forecastApi(): ForecastApi = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(ForecastApi::class.java)

    @Named("default")
    @Singleton
    @Provides
    fun forecastProvider(forecastApi: ForecastApi): ForecastProvider = RetrofitForecastProvider(forecastApi)

    @Named("fake")
    @Singleton
    @Provides
    fun fakeForecastProvider(): ForecastProvider = FakeForecastProvider()

    @Singleton
    @Provides
    fun forecastDao(database: ApplicationDatabase): ForecastDao = database.forecastDao();

    @Singleton
    @Provides
    fun forecastRepository(forecastDao: ForecastDao): ForecastRepository = RoomForecastRepository(forecastDao)

    @Singleton
    @Provides
    fun forecastService(@Named("default") forecastProvider: ForecastProvider, forecastRepository: ForecastRepository) = ForecastService(forecastProvider, forecastRepository)

}