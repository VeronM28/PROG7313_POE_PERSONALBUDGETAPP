package com.st10083866.prog7313_poe_personalbudgetapp.ui.budget


import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class PieChartView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var data: List<PieSlice> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (data.isEmpty()) return

        val total = data.sumOf { it.value.toDouble() }.toFloat()
        var startAngle = -90f
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val size = width.coerceAtMost(height).toFloat()
        val rect = RectF(0f, 0f, size, size)

        for (slice in data) {
            val sweep = (slice.value / total) * 360f
            paint.color = slice.color
            canvas.drawArc(rect, startAngle, sweep, true, paint)
            startAngle += sweep
        }
    }

    data class PieSlice(val value: Float, val color: Int)
}