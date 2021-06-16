package pl.training.goodweather.forecast.adapter.persistence

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

@Dao
interface ForecastDao {

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun save(city: CityEntity): Completable

   @Insert
   fun save(forecast: List<DayForecastEntity>): Completable

   @Transaction
   @Query("select * from CityEntity where name = :city")
   fun findAll(city: String): Maybe<ForecastEntity>

   @Query("delete from DayForecastEntity")
   fun deleteAll(): Completable

}