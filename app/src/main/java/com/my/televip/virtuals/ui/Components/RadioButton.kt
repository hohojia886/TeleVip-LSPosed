package com.my.televip.virtuals.ui.Components

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.view.View
import com.my.televip.application.AndroidUtilities.dp
import de.robv.android.xposed.XposedBridge

class RadioButton(context: Context) : View(context) {

    private var bitmap: Bitmap? = null
    private var bitmapCanvas: Canvas? = null
    private var color1 = 0
    private var color2 = 0
    private var size = dp(16f)
    private var progress = 0f
    private var checkAnimator: ObjectAnimator? = null
    private var isChecked = false

    init {
        if (paint == null) {
            paint = Paint(Paint.ANTI_ALIAS_FLAG)
            eraser = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = 0
                xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            }
            checkPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        }
    }

    fun setProgress(value: Float) {
        if (progress == value) return
        progress = value
        invalidate()
    }

    fun getProgress(): Float = progress

    fun setSize(value: Int) {
        if (size == value) return
        size = value
    }

    fun setColor(color1: Int, color2: Int) {
        this.color1 = color1
        this.color2 = color2
        invalidate()
    }

    fun setChecked(checked: Boolean, animated: Boolean) {
        if (checked == isChecked) return
        isChecked = checked

        checkAnimator?.cancel()

        if (animated) {
            val animator = ObjectAnimator.ofFloat(this, "progress", if (checked) 1f else 0f)
            animator.duration = 200
            checkAnimator = animator
            animator.start()
        } else {
            setProgress(if (checked) 1f else 0f)
        }
    }

    fun isChecked(): Boolean = isChecked

    override fun onDraw(canvas: Canvas) {
        if (bitmap == null) {
            try {
                val bmp = Bitmap.createBitmap(dp(20f), dp(20f), Bitmap.Config.ARGB_8888)
                bitmap = bmp
                bitmapCanvas = Canvas(bmp)
            } catch (t: Throwable) {
                XposedBridge.log(t)
            }
        }

        val circleRadius: Float = if (progress <= 0.5f) {
            paint?.color = color1
            checkPaint?.color = color1
            progress / 0.5f
        } else {
            val p = (progress - 0.5f) / 0.5f
            paint?.color = Color.rgb(
                Color.red(color1) + ((Color.red(color2) - Color.red(color1)) * p).toInt(),
                Color.green(color1) + ((Color.green(color2) - Color.green(color1)) * p).toInt(),
                Color.blue(color1) + ((Color.blue(color2) - Color.blue(color1)) * p).toInt()
            )
            checkPaint?.color = paint?.color ?: 0
            1f - p
        }

        val currentBitmap = bitmap
        val currentCanvas = bitmapCanvas
        if (currentBitmap != null && currentCanvas != null) {
            currentBitmap.eraseColor(0)
            val rad = size / 2f - dp(1f)
            val cx = dp(10f).toFloat()
            val cy = dp(10f).toFloat()

            paint?.let { currentCanvas.drawCircle(cx, cy, rad, it) }
            eraser?.let { currentCanvas.drawCircle(cx, cy, rad - dp(1f), it) }

            if (progress > 0) {
                checkPaint?.let { currentCanvas.drawCircle(cx, cy, (rad - dp(1f)) * circleRadius, it) }
            }

            canvas.drawBitmap(currentBitmap, 0f, 0f, null)
        }
    }

    companion object {
        private var paint: Paint? = null
        private var eraser: Paint? = null
        private var checkPaint: Paint? = null
    }
}
