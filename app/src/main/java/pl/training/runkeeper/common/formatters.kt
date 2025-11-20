package pl.training.runkeeper.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTemperature(value: Double) = "${value.toInt()}°"

fun formatPressure(value: Double) = "${value.toInt()} hPa"

fun formatDate(date: Date, format: String = "dd/MM") = SimpleDateFormat(format, Locale.getDefault())
    .format(date)