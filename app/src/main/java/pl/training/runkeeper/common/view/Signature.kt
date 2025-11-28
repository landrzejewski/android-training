package pl.training.runkeeper.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.View

class Signature(context: Context, attributeSet: AttributeSet): View(context){

    private val paint: Paint = run {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.color = Color.DKGRAY
        paint.strokeWidth = 10.0F
        paint
    }
    private val path = Path()

    init {
        isFocusable = true
        isFocusableInTouchMode = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when(event.action) {
            ACTION_DOWN -> path.moveTo(x, y)
            ACTION_MOVE -> path.lineTo(x, y)
        }
        invalidate()
        return true
    }

}