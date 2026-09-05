package com.my.televip.virtuals.ui.Components

import android.widget.FrameLayout
import com.my.televip.application.AndroidUtilities

object LayoutHelper {

    const val MATCH_PARENT = -1
    const val WRAP_CONTENT = -2

    private fun getSize(size: Float): Int {
        return if (size < 0) size.toInt() else AndroidUtilities.dp(size)
    }

    @JvmStatic
    fun createFrame(width: Int, height: Float, gravity: Int, leftMargin: Float, topMargin: Float, rightMargin: Float, bottomMargin: Float): FrameLayout.LayoutParams {
        val layoutParams = FrameLayout.LayoutParams(getSize(width.toFloat()), getSize(height), gravity)
        layoutParams.setMargins(
            AndroidUtilities.dp(leftMargin),
            AndroidUtilities.dp(topMargin),
            AndroidUtilities.dp(rightMargin),
            AndroidUtilities.dp(bottomMargin)
        )
        return layoutParams
    }
}
