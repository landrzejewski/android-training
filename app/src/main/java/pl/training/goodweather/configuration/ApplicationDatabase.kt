package pl.training.goodweather.configuration

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.training.goodweather.forecast.adapter.persistence.CityEntity
import pl.training.goodweather.forecast.adapter.persistence.DayForecastEntity
import pl.training.goodweather.forecast.adapter.persistence.ForecastDao

@Database(entities = [CityEntity::class, DayForecastEntity::class], version = 1, exportSchema = false)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun forecastDao(): ForecastDao

}