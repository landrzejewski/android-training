package pl.training.runkeeper.weather.adapters.provider.openweather.dto

import com.google.gson.annotations.SerializedName

data class ResponseDto(
    @SerializedName("list") val forecast: List<DayForecastDto>
)