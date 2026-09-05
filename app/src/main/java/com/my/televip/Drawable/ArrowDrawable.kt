package com.my.televip.Drawable

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.view.View.LAYOUT_DIRECTION_RTL
import com.my.televip.virtuals.Theme

class ArrowDrawable : Drawable() {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Theme.getArrowDrawableColor()
    }
    private val path: Path = Path().apply {
        moveTo(20f, 11f)
        lineTo(7.8f, 11f)
        lineTo(13.4f, 5.4f)
        lineTo(12f, 4f)
        lineTo(4f, 12f)
        lineTo(12f, 20f)
        lineTo(13.4f, 18.6f)
        lineTo(7.8f, 13f)
        lineTo(20f, 13f)
        lineTo(20f, 11f)
        close()
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

        canvas.drawPath(path, paint)
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
