package pl.training.runkeeper.common.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import pl.training.runkeeper.R
import pl.training.runkeeper.databinding.ViewCustomButtonBinding
import kotlin.math.roundToInt

class CustomButton(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet) {

    private val binding = ViewCustomButtonBinding.inflate(LayoutInflater.from(context), this, true)
    private val animators = mutableListOf<ValueAnimator>()
    private var isDisabled = false

    private val icon: Drawable
    private val label: String

    init {
        val settings = context.obtainStyledAttributes(attributeSet, R.styleable.CustomButton)
        icon = settings.getDrawable(R.styleable.CustomButton_button_image)!!
        label = settings.getString(R.styleable.CustomButton_button_label)!!
        settings.recycle()
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        val padding = dpToPx(16)
        setPadding(padding, 0, padding, padding)
        binding.button.icon = icon
        binding.button.text = label
        binding.button.setOnClickListener { disable() }
    }

    fun disable() {
        if (!isDisabled) {
            isDisabled = true
            binding.button.apply {
                icon = null
                text = ""
                isClickable = false
                isFocusable = false
                shrinkButton()
            }
            binding.progressBar.isVisible = true
        }
    }

    private fun shrinkButton() {
        val button = binding.button
        val width = button.measuredWidth
        val height = button.measuredHeight
        val widthAnimator = ValueAnimator.ofInt(width, height).apply {
            duration = 800
            addUpdateListener {
                button.updateLayoutParams { this.width = it.animatedValue as Int }
            }
        }

        val alphaAnimator = ObjectAnimator.ofFloat(binding.button, "alpha", 1F, 0.8f).apply {
            duration = 300
        }

        val animatorsList = listOf(widthAnimator, alphaAnimator)
        animators.addAll(animatorsList)
        animatorsList.forEach { it.start() }
    }

    fun enable() {
        if (isDisabled) {
            isDisabled = false
            binding.button.apply {
                extend()
                icon = this@CustomButton.icon
                text = label
                isClickable = true
                isFocusable = true
            }
            binding.progressBar.isVisible = false
            reverseShrink()
        }
    }

    private fun reverseShrink() {
        animators.forEach {
            it.reverse()
            if (animators.indexOf(it) == animators.lastIndex) {
                it.doOnEnd { animators.clear() }
            }
        }
    }

    fun setOnClickListener(listener: () -> Unit) {
        binding.button.setOnClickListener {
            disable()
            listener()
        }
    }

    private fun dpToPx(dp: Int) = (resources.displayMetrics.density * dp).roundToInt()

}