package pl.training.goodweather.common.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import pl.training.goodweather.R

class ImageBox(context: Context, attributes: AttributeSet) :  LinearLayout(context, attributes) {

    private val image: ImageView
    private val label: TextView

    init {
        inflate(context, R.layout.view_image_box, this)
        val settings = context.obtainStyledAttributes(attributes, R.styleable.ImageBox)
        image = findViewById(R.id.boxImageView)
        label = findViewById(R.id.labelTextView)
        image.setImageDrawable(settings.getDrawable(R.styleable.ImageBox_image))
        label.text = settings.getString(R.styleable.ImageBox_label)
        settings.recycle()
    }

    fun setOnClickListener(handler: () -> Unit) {
        image.setOnClickListener { handler() }
    }

}