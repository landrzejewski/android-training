package pl.training.goodweather.forecast.adapter.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DayForecastEntity(@PrimaryKey(autoGenerate = true) var id: Long?,
                             @ColumnInfo(name = "icon_name") val icon: String,
                             val description: String,
                             val temperature: Double,
                             val pressure: Double,
                             val date: Long,
                             val cityId: Long)