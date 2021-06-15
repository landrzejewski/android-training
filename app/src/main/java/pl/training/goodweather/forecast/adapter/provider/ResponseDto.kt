package pl.training.goodweather.forecast.adapter.provider

import com.google.gson.annotations.SerializedName

data class ResponseDto(@SerializedName("list") val forecast: List<DayForecastDto>)