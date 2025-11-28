package pl.training.runkeeper.common.view

import android.animation.AnimatorSet
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style.FILL_AND_STROKE
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import pl.training.runkeeper.R

class Box(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val color: Int
    private val size: Float
    private val radiusProperty = "radius"
    private val propertyRadius = PropertyValuesHolder.ofFloat(radiusProperty, 0F, 150F)
    private val rotationProperty = "rotation"
    private val propertyRotation = PropertyValuesHolder.ofFloat(rotationProperty, 0F, 360F)

    private var alpha = 256
    private var radius = 0F
    private var angle = 0F

    init {
        val settings = context.obtainStyledAttributes(attributeSet, R.styleable.Box)
        color = settings.getColor(R.styleable.Box_color, Color.DKGRAY)
        size = settings.getDimension(R.styleable.Box_size, 300.0F)
        settings.recycle()
        animateBox()
    }

    private fun animateBox() {
        val propertiesAnimator = ValueAnimator().apply {
            setValues(propertyRadius, propertyRotation)
            duration = 1_000
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                radius = it.getAnimatedValue(radiusProperty) as Float
                angle = it.getAnimatedValue(rotationProperty) as Float
                invalidate()
            }
        }

        val fadeAnimator = ValueAnimator.ofInt(255, 0).apply {
            duration = 700
            addUpdateListener {
                alpha = it.animatedValue as Int
                invalidate()
            }
            startDelay = 100
        }

        val animators = AnimatorSet()
        animators.playTogether(propertiesAnimator, fadeAnimator)
        animators.start()
    }

    private fun getPaint(): Paint {
        val paint = Paint()
        paint.style = FILL_AND_STROKE
        paint.color = color
        paint.strokeWidth = 10.0f
        paint.alpha = alpha
        return paint
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val halfSize = size / 2.0f
        val centerY = height / 2.0F
        val centerX = width / 2.0F
        val leftTopY = centerY - halfSize
        val leftTopX = centerX - halfSize
        val rightBottomY = centerY + halfSize
        val rightBottomX = centerX + halfSize
        canvas.rotate(angle, centerX, centerY)
        canvas.drawRoundRect(leftTopX, leftTopY, rightBottomX, rightBottomY, radius, radius, getPaint())
    }

}