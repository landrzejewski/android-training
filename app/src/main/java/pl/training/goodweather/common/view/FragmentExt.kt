package pl.training.goodweather.common.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.setProperty(key: String, value: String, context: Context = requireContext()) = context.getSharedPreferences(context.packageName, AppCompatActivity.MODE_PRIVATE)
    .edit()
    .putString(key, value)
    .apply()

fun Fragment.getProperty(key: String, defaultValue: String = "", context: Context = requireContext()) = context.getSharedPreferences(context.packageName, AppCompatActivity.MODE_PRIVATE)
    .getString(key, defaultValue)