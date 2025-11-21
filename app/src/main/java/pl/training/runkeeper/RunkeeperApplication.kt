package pl.training.runkeeper

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.training.runkeeper.common.AppIdInterceptor
import pl.training.runkeeper.common.store.SharedPreferencesStore
import pl.training.runkeeper.common.store.Store
import pl.training.runkeeper.weather.adapters.persistence.RoomForecastRepositoryAdapter
import pl.training.runkeeper.weather.adapters.persistence.RoomForecastRepositoryMapper
import pl.training.runkeeper.weather.adapters.provider.FakeForecastProvider
import pl.training.runkeeper.weather.adapters.provider.openweather.OpenWeatherApi
import pl.training.runkeeper.weather.adapters.provider.openweather.OpenWeatherForecastProviderAdapter
import pl.training.runkeeper.weather.adapters.provider.openweather.OpenWeatherForecastProviderMapper
import pl.training.runkeeper.weather.domain.ForecastService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class RunkeeperApplication : Application() {

    lateinit var store: Store
        private set

    override fun onCreate() {
        super.onCreate()
        store = SharedPreferencesStore(applicationContext)
    }

    fun database(): RunkeeperDatabase = Room
        .databaseBuilder(this, RunkeeperDatabase::class.java, "runkeeper")
        .fallbackToDestructiveMigration(false)
        .setQueryCallback(
            { sqlQuery, bindArgs -> Log.d("RoomQuery", "SQL: $sqlQuery BindArgs: $bindArgs") },
            Executors.newSingleThreadExecutor()
        )
        .build()

    fun forecastRepository() = RoomForecastRepositoryAdapter(
        database().forecastDao(),
        RoomForecastRepositoryMapper()
    )

    // val fakeForecastService by lazy { ForecastService(FakeForecastProvider(), forecastRepository()) }

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

    val forecastService by lazy { ForecastService(openWeatherForecastProviderAdapter, forecastRepository()) }

}