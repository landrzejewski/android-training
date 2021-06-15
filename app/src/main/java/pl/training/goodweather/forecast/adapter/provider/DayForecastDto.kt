package pl.training.goodweather.forecast.adapter.provider

import com.google.gson.annotations.SerializedName

data class DayForecastDto(@SerializedName("weather") val weather: List<WeatherDto>,
                          @SerializedName("temp") val temperature: TemperatureDto,
                          val pressure: Double,
                          @SerializedName("dt") val date: Long)