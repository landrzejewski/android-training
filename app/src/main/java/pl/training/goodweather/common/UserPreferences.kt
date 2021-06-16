package pl.training.goodweather.common

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.lang.ClassCastException

class UserPreferences(private val context: Context) : SharedPreferences.OnSharedPreferenceChangeListener {

    private val subject = PublishSubject.create<Pair<String, String>>()

    val preferences: Observable<Pair<String, String>> = subject

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        var value = ""
        try {
            value = get(key, "$key not found")
        } catch(e : ClassCastException) {
            value = get(key, false).toString()
        }
        subject.onNext(Pair(key, value))
    }

    fun set(key: String, value: String) = PreferenceManager.getDefaultSharedPreferences(context)
        .edit()
        .putString(key, value)
        .apply()

    fun get(key: String, defaultValue: String = "") = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(key, defaultValue) ?: ""

    fun get(key: String, defaultValue: Boolean = false) = PreferenceManager.getDefaultSharedPreferences(context)
        .getBoolean(key, defaultValue)

}