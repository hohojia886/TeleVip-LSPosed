package com.my.televip.Drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.view.View.LAYOUT_DIRECTION_RTL

class GhostDrawable : Drawable() {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }
    private val bodyPath: Path = Path().apply {
        moveTo(12f, 1.83f)
        cubicTo(6.94f, 1.83f, 2.84f, 5.96f, 2.84f, 11.06f)
        lineTo(2.84f, 20.82f)
        cubicTo(2.84f, 21.37f, 3.4f, 21.71f, 3.88f, 21.47f)
        lineTo(6.2f, 20.3f)
        cubicTo(7.77f, 19.51f, 9.67f, 19.97f, 10.73f, 21.38f)
        cubicTo(11.44f, 22.34f, 12.83f, 22.44f, 13.68f, 21.58f)
        lineTo(14.05f, 21.21f)
        cubicTo(15.37f, 19.88f, 17.43f, 19.63f, 19.05f, 20.6f)
        lineTo(20.35f, 21.39f)
        cubicTo(20.7f, 21.59f, 21.15f, 21.35f, 21.15f, 20.92f)
        lineTo(21.15f, 11.06f)
        cubicTo(21.15f, 5.96f, 17.05f, 1.83f, 12f, 1.83f)
        close()
    }
    private val eyeLeft: Path = Path().apply {
        addCircle(8.5f, 11f, 1.5f, Path.Direction.CW)
    }
    private val eyeRight: Path = Path().apply {
        addCircle(15.5f, 11f, 1.5f, Path.Direction.CW)
    }
    private val eyePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = -0x1000000
    }

    override fun draw(canvas: Canvas) {
        val scaleX = bounds.width() / 24f
        val scaleY = bounds.height() / 24f

        canvas.save()

        if (layoutDirection == LAYOUT_DIRECTION_RTL) {
            canvas.translate(bounds.width().toFloat(), 0f)
            canvas.scale(-scaleX, scaleY)
        } else {
            canvas.scale(scaleX, scaleY)
        }

        canvas.drawPath(bodyPath, paint)
        canvas.drawPath(eyeLeft, eyePaint)
        canvas.drawPath(eyeRight, eyePaint)

        canvas.restore()
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    @Deprecated("Deprecated in Java", ReplaceWith("PixelFormat.UNKNOWN", "android.graphics.PixelFormat"))
    override fun getOpacity(): Int {
        return if (paint.alpha == 255) PixelFormat.OPAQUE else PixelFormat.TRANSLUCENT
    }
}
