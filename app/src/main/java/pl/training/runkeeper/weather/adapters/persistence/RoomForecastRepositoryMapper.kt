package pl.training.runkeeper.weather.adapters.persistence

import pl.training.runkeeper.weather.adapters.persistence.entity.CityEntity
import pl.training.runkeeper.weather.adapters.persistence.entity.DayForecastEntity
import pl.training.runkeeper.weather.domain.DayForecast
import java.util.Date

class RoomForecastRepositoryMapper {

    fun toEntity(city: String) = CityEntity(city)

    fun toEntity(city: String, dayForecast: DayForecast) = with(dayForecast) {
        DayForecastEntity(null, temperature, pressure, date.time, description, iconName, city)
    }

    fun toModel(dayForecastEntity: DayForecastEntity) = with(dayForecastEntity) {
        DayForecast(Date(date), temperature, pressure, description, iconName)
    }

}