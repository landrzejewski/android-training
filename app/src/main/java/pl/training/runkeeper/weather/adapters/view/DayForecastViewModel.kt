package pl.training.runkeeper.weather.adapters.view

data class DayForecastViewModel(
    val date: String,
    val temperature: String,
    val pressure: String,
    val description: String,
    val iconName: String
)