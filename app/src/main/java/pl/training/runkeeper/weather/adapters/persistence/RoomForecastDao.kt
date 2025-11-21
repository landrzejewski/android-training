package pl.training.runkeeper.weather.adapters.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import pl.training.runkeeper.weather.adapters.persistence.entity.CityEntity
import pl.training.runkeeper.weather.adapters.persistence.entity.DayForecastEntity
import pl.training.runkeeper.weather.adapters.persistence.entity.ForecastAggregate

@Dao
interface RoomForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(cityEntity: CityEntity)

    @Insert
    suspend fun save(forecast: List<DayForecastEntity>)

    @Delete
    suspend fun delete(dayForecastEntity: DayForecastEntity)

    @Query("delete from CityEntity")
    suspend fun deleteCity()

    @Query("delete from DayForecastEntity")
    suspend fun deleteForecast()

    @Transaction
    @Query("select * from CityEntity where name = :city")
    suspend fun findByCity(city: String): ForecastAggregate?

    @Transaction
    suspend fun replace(city: CityEntity, forecast: List<DayForecastEntity>) {
        deleteCity()
        deleteForecast()
        save(city)
        save(forecast)
    }

}