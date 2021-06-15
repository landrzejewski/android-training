package pl.training.goodweather.forecast.adapter.persistence;

import androidx.room.Embedded;
import androidx.room.Relation;

data class ForecastEntity(@Embedded val city: CityEntity,
                          @Relation(parentColumn = "id", entityColumn = "cityId") val forecast:List<DayForecastEntity>)