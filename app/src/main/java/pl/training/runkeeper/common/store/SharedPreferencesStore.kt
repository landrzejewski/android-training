package pl.training.runkeeper.common.store

import android.content.Context
import androidx.core.content.edit

class SharedPreferencesStore(context: Context) : Store {

    private val preferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    override fun set(key: String, value: String) = preferences.edit {
        putString(key, value)
    }

    override fun get(key: String, defaultValue: String): String = preferences
        .getString(key, defaultValue) ?: defaultValue

}