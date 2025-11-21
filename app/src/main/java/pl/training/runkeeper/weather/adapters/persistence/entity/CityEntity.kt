package pl.training.runkeeper.weather.adapters.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityEntity(
    @PrimaryKey
    val name: String
)