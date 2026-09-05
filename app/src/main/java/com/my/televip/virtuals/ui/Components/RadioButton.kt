package com.my.televip.virtuals.ui.Components

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.Keep
import androidx.core.graphics.ColorUtils
import com.my.televip.application.AndroidUtilities
import kotlin.math.max
import kotlin.math.min

class RadioButton(context: Context) : View(context) {

    private var checkedColor = 0
    private var color = 0

    private var progress = 0f
    private var checkAnimator: ObjectAnimator? = null

    private var attachedToWindow = false
    private var isChecked = false
    private var size = AndroidUtilities.dp(16f)

    private var iconColor = 0
    private var icon: Drawable? = null

    init {
        if (paint == null) {
            paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                strokeWidth = AndroidUtilities.dp(2f).toFloat()
                style = Paint.Style.STROKE
            }
            checkedPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            eraser = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = 0
                xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            }
        }
    }

    @Keep
    fun setProgress(value: Float) {
        if (progress == value) return
        progress = value
        invalidate()
    }

    @Keep
    fun getProgress(): Float = progress

    fun setSize(value: Int) {
        if (size == value) return
        size = value
    }

    fun setIcon(drawable: Drawable?) {
        iconColor = 0
        icon = drawable
        invalidate()
    }

    fun getColor(): Int = color

    fun setColor(color1: Int, color2: Int) {
        color = color1
        checkedColor = color2
        invalidate()
    }

    override fun setBackgroundColor(color1: Int) {
        color = color1
        invalidate()
    }

    fun setCheckedColor(color2: Int) {
        checkedColor = color2
        invalidate()
    }

    private fun cancelCheckAnimator() {
        checkAnimator?.cancel()
    }

    private fun animateToCheckedState(newCheckedState: Boolean) {
        checkAnimator = ObjectAnimator.ofFloat(this, "progress", if (newCheckedState) 1f else 0f).apply {
            duration = 200
            start()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        attachedToWindow = true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        attachedToWindow = false
    }

    fun setChecked(checked: Boolean, animated: Boolean) {
        if (checked == isChecked) return
        isChecked = checked

        if (attachedToWindow && animated) {
            animateToCheckedState(checked)
        } else {
            cancelCheckAnimator()
            setProgress(if (checked) 1.0f else 0.0f)
        }
    }

    fun isChecked(): Boolean = isChecked

    override fun onDraw(canvas: Canvas) {
        val circleProgress: Float
        val p = paint ?: return
        val cp = checkedPaint ?: return
        val e = eraser ?: return

        if (progress <= 0.5f) {
            p.color = color
            cp.color = color
            circleProgress = progress / 0.5f
        } else {
            circleProgress = 2.0f - progress / 0.5f
            val r1 = Color.red(color)
            val rD = ((Color.red(checkedColor) - r1) * (1.0f - circleProgress)).toInt()
            val g1 = Color.green(color)
            val gD = ((Color.green(checkedColor) - g1) * (1.0f - circleProgress)).toInt()
            val b1 = Color.blue(color)
            val bD = ((Color.blue(checkedColor) - b1) * (1.0f - circleProgress)).toInt()
            val c = Color.rgb(r1 + rD, g1 + gD, b1 + bD)
            p.color = c
            cp.color = c
        }
        canvas.saveLayerAlpha(0f, 0f, width.toFloat(), height.toFloat(), 0xFF, Canvas.ALL_SAVE_FLAG)

        val rad = size / 2f - (1f + circleProgress) * AndroidUtilities.density
        canvas.drawCircle(measuredWidth / 2f, measuredHeight / 2f, rad, p)
        if (icon == null) {
            if (progress <= 0.5f) {
                canvas.drawCircle(measuredWidth / 2f, measuredHeight / 2f, rad - AndroidUtilities.dp(1f), cp)
                canvas.drawCircle(measuredWidth / 2f, measuredHeight / 2f, (rad - AndroidUtilities.dp(1f)) * (1.0f - circleProgress), e)
            } else {
                canvas.drawCircle(measuredWidth / 2f, measuredHeight / 2f, size / 4f + (rad - AndroidUtilities.dp(1f) - size / 4f) * circleProgress, cp)
            }
        }
        canvas.restore()

        val ic = icon
        if (ic != null) {
            val finalIconColor = ColorUtils.blendARGB(color, checkedColor, clamp(progress, 1f, 0f))
            if (iconColor != finalIconColor) {
                iconColor = finalIconColor
                ic.colorFilter = PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_IN)
            }
            ic.setBounds(
                (width / 2f - ic.intrinsicWidth / 2f).toInt(),
                (height / 2f - ic.intrinsicHeight / 2f).toInt(),
                (width / 2f + ic.intrinsicWidth / 2f).toInt(),
                (height / 2f + ic.intrinsicHeight / 2f).toInt()
            )
            ic.draw(canvas)
        }
    }

    private fun clamp(value: Float, maxValue: Float, minValue: Float): Float {
        if (java.lang.Float.isNaN(value)) return minValue
        if (java.lang.Float.isInfinite(value)) return maxValue
        return max(min(value, maxValue), minValue)
    }

    companion object {
        private var paint: Paint? = null
        private var eraser: Paint? = null
        private var checkedPaint: Paint? = null
    }
}
