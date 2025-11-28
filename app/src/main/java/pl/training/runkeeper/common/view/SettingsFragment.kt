package pl.training.runkeeper.common.view

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import pl.training.runkeeper.R

class SettingsFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        preferences.registerOnSharedPreferenceChangeListener(this)

        val value = preferences.getString("forecast_number_of_days", "not set")
        Log.d("###", "Forecast number of days $value")
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val changedKey = key ?: ""
        val value = when (changedKey) {
            "forecast_number_of_days" -> sharedPreferences?.getString(changedKey, "")
            "forecast_cache_enabled" -> sharedPreferences?.getBoolean(changedKey, false)
            else -> "Unknow"
        }
        Log.d("###", "Settings update: $changedKey = $value")
    }

}