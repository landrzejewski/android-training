package pl.training.runkeeper.weather

import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import pl.training.runkeeper.Development
import pl.training.runkeeper.Production
import pl.training.runkeeper.RunkeeperDatabase
import pl.training.runkeeper.weather.adapters.persistence.RoomForecastRepositoryAdapter
import pl.training.runkeeper.weather.adapters.persistence.RoomForecastRepositoryMapper
import pl.training.runkeeper.weather.adapters.provider.FakeForecastProvider
import pl.training.runkeeper.weather.adapters.provider.openweather.OpenWeatherApi
import pl.training.runkeeper.weather.adapters.provider.openweather.OpenWeatherForecastProviderAdapter
import pl.training.runkeeper.weather.adapters.provider.openweather.OpenWeatherForecastProviderMapper
import pl.training.runkeeper.weather.domain.ForecastProvider
import pl.training.runkeeper.weather.domain.ForecastRepository
import pl.training.runkeeper.weather.domain.ForecastService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class WeatherModule {

    @Development
    @Singleton
    @Provides
    fun fakeForecastProvider(): ForecastProvider = FakeForecastProvider()

    @Singleton
    @Provides
    fun openWeatherForecastProviderMapper(): OpenWeatherForecastProviderMapper =
        OpenWeatherForecastProviderMapper()

    @Singleton
    @Provides
    fun openWeatherApi(httpClient: OkHttpClient): OpenWeatherApi = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(OpenWeatherApi::class.java)

    @Production
    @Singleton
    @Provides
    fun openWeatherForecastProviderAdapter(api: OpenWeatherApi, mapper: OpenWeatherForecastProviderMapper): ForecastProvider =
        OpenWeatherForecastProviderAdapter(api, mapper)

    @Singleton
    @Provides
    fun roomForecastRepositoryMapper(): RoomForecastRepositoryMapper = RoomForecastRepositoryMapper()

    @Singleton
    @Provides
    fun roomForecastRepositoryAdapter(database: RunkeeperDatabase, mapper: RoomForecastRepositoryMapper): ForecastRepository =
        RoomForecastRepositoryAdapter(database.forecastDao(), mapper)

    @Singleton
    @Provides
    fun forecastService(@Production forecastProvider: ForecastProvider, forecastRepository: ForecastRepository): ForecastService =
        ForecastService(forecastProvider, forecastRepository)

}
