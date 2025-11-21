package pl.training.runkeeper.weather.adapters.persistence.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ForecastAggregate(
    @Embedded
    val city: CityEntity,
    @Relation(parentColumn = "name", entityColumn = "city_name")
    val forecast: List<DayForecastEntity>
)