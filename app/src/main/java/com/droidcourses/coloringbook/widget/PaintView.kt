package com.droidcourses.coloringbook.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.droidcourses.coloringbook.PicsHolder
import com.droidcourses.coloringbook.util.floodFill


class PaintView : View {
    var bitmap: Bitmap? = null
    var selectedPicIndex = 0
    var selectedColor = Color.BLACK

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)


        val srcBitmap = BitmapFactory.decodeResource(resources, PicsHolder.pics[selectedPicIndex])
        bitmap = Bitmap.createScaledBitmap(srcBitmap, w, h, false)
        bitmap?.let { bitmap ->
            if (x < 0 || y < 0 || x >= bitmap.width || y >= bitmap.height)
                return

            for (i in 0 until bitmap.getWidth()) {
                for (j in 0 until bitmap.getHeight()) {
                    val alpha = 255 - brightness(bitmap.getPixel(i, j))
                    if (alpha < 200) {
                        bitmap.setPixel(i, j, Color.WHITE)
                    } else {
                        bitmap.setPixel(i, j, Color.BLACK)
                    }
                }
            }
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bitmap?.let { canvas.drawBitmap(it, 0f, 0f, null) }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x?.toInt()
        val y = event?.y?.toInt()
        if (x != null && y != null) {
            paint(x, y)
        }
        return true
    }

    private fun paint(x: Int, y: Int) {
        val targetColor = bitmap?.getPixel(x, y)
        if (targetColor != Color.BLACK) {
            runCatching {
                bitmap?.let {
                    if (targetColor != null) {
                        floodFill(it, Point(x, y), targetColor, selectedColor)
                    }
                }
            }
                .onSuccess {
                    invalidate()
                }
        }

    }

    private fun brightness(value: Int) = 0xff and (value shr 16)
}