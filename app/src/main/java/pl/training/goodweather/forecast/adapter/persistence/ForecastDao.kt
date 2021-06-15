package pl.training.goodweather.forecast.adapter.persistence

import androidx.room.*

@Dao
interface ForecastDao {

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun save(city: CityEntity)

   @Insert
   suspend fun save(forecast: List<DayForecastEntity>)

   @Transaction
   @Query("select * from CityEntity where name = :city")
   suspend fun findAll(city: String): ForecastEntity

   @Query("delete from DayForecastEntity")
   suspend fun deleteAll()

}