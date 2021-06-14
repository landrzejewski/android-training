package pl.training.goodweather.forecast.viewmodel

data class DayForecastViewModel(
    val icon: String,
    val description: String,
    val temperature: String,
    val pressure: String,
    val date: String
)