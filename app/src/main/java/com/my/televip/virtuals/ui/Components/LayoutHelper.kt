package com.my.televip.virtuals.ui.Components

import android.widget.FrameLayout
import com.my.televip.application.AndroidUtilities.dp

object LayoutHelper {

    const val MATCH_PARENT = -1
    const val WRAP_CONTENT = -2

    private fun getSize(size: Float): Int {
        return if (size < 0) size.toInt() else dp(size)
    }

    @JvmStatic
    fun createFrame(
        width: Int,
        height: Float,
        gravity: Int,
        leftMargin: Float,
        topMargin: Float,
        rightMargin: Float,
        bottomMargin: Float
    ): FrameLayout.LayoutParams {
        val layoutParams = FrameLayout.LayoutParams(getSize(width.toFloat()), getSize(height), gravity)
        layoutParams.setMargins(dp(leftMargin), dp(topMargin), dp(rightMargin), dp(bottomMargin))
        return layoutParams
    }
}
