package pl.training.goodweather.common

import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.setProperty(key: String, value: String) = getSharedPreferences(packageName, AppCompatActivity.MODE_PRIVATE)
    .edit()
    .putString(key, value)
    .apply()

fun AppCompatActivity.getProperty(key: String, defaultValue: String = "") = getSharedPreferences(packageName, AppCompatActivity.MODE_PRIVATE)
    .getString(key, defaultValue)