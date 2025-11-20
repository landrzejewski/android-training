package pl.training.runkeeper.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL

fun enableSafeArea(view: View) {
    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        insets
    }
}

@SuppressLint("DiscouragedApi")
fun ImageView.loadDrawable(name: String) {
    try {
        val id = resources.getIdentifier(name, "drawable", context.opPackageName)
        val drawable = AppCompatResources.getDrawable(context, id)
        setImageDrawable(drawable)
    } catch (exception: Exception) {
        Log.d("###", "Resource not found: $name")
    }
}

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun linearManagerWithScreenOrientation(context: Context): LayoutManager {
    val orientation = if (context.resources.configuration.orientation == SCREEN_ORIENTATION_PORTRAIT)
        HORIZONTAL else VERTICAL
    return LinearLayoutManager(context, orientation, false)
}
