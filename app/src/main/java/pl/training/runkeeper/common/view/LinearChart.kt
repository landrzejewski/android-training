package pl.training.runkeeper.common.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.PropertyValuesHolder.ofFloat
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class LinearChart(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val paint: Paint = run {
        val paint = Paint()
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = Color.WHITE
        paint.strokeWidth = 5.0F
        paint
    }
    private val axisPaint: Paint = run {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.color = Color.DKGRAY
        paint.strokeWidth = 12.0F
        paint
    }
    private val xProperty = "x"
    private val yProperty = "y"
    private var animatedPoints = mutableListOf<Pair<Point, Point>>()
    private var maxX = 0F
    private var maxY = 0F

    fun draw(points: List<Point>) {
        maxX = max(points.map { it.x })
        maxY = max(points.map { it.y })
        val pairs = points.zipWithNext()
        animatedPoints = pairs.map { Point(0F, 0F) to Point(0F, 0F) }.toMutableList()
        runAnimation(pairs.mapIndexed(::createAnimator))
    }

    private fun runAnimation(animators: List<Animator>) {
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(*animators.toTypedArray())
        animatorSet.startDelay = 1_000
        animatorSet.start()
    }

    private fun createAnimator(index: Int, points: Pair<Point, Point>) = ValueAnimator().apply {
        setValues(
            ofFloat(xProperty, points.first.x, points.second.x),
            ofFloat(yProperty, points.first.y, points.second.y)
        )
        duration = 1_000
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
            animatedPoints[index] = points.first to Point(
                it.getAnimatedValue(xProperty) as Float,
                it.getAnimatedValue(yProperty) as Float
            )
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(0f, height.toFloat())
        canvas.scale(1F, -1F)
        animatedPoints.forEach { drawLine(canvas, it.first, it.second) }
        canvas.drawLine(0F, 0F, 0F, scaleY(maxY), axisPaint)
        canvas.drawLine(0F, 0F, scaleX(maxX), 0F, axisPaint)
    }

    private fun drawLine(canvas: Canvas, start: Point, end: Point) {
        val path = Path()
        path.moveTo(scaleX(start.x), scaleY(start.y))
        path.lineTo(scaleX(end.x), scaleY(end.y))
        canvas.drawPath(path, paint)
    }

    private fun max(values: List<Float>) = values.maxOrNull() ?: 0F

    private fun scaleX(value: Float) = (value * width) / maxX

    private fun scaleY(value: Float) = (value * height) / maxY

}

data class Point(val x: Float, val y: Float)