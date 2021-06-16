package pl.training.goodweather.common.view

import android.widget.ImageView
import androidx.core.content.ContextCompat

fun ImageView.setDrawable(name: String) {
    val drawableId = resources.getIdentifier(name, "drawable", context.packageName)
    setImageDrawable(ContextCompat.getDrawable(context, drawableId))
}