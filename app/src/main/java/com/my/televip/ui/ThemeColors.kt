package com.my.televip.ui

import android.graphics.Color
import com.my.televip.virtuals.ActionBar.Theme

object ThemeColors {

    @JvmStatic
    fun getBackgroundGrayColor(): Int {
        return if (Theme.isLight()) Color.rgb(241, 241, 243) else Color.rgb(21, 30, 39)
    }

    @JvmStatic
    fun getBackgroundWhiteOrBlueColor(): Int {
        return if (Theme.isLight()) Color.WHITE else Color.rgb(29, 39, 51)
    }

    @JvmStatic
    fun getToolBarColor(): Int {
        return if (Theme.isLight()) Color.WHITE else Color.rgb(36, 45, 57)
    }

    @JvmStatic
    fun getToolBarRippleColor(): Int {
        return if (Theme.isLight()) 0x20000000 else 0x20FFFFFF.toInt()
    }

    @JvmStatic
    fun getTextToolBarColor(): Int {
        return if (Theme.isLight()) Color.BLACK else Color.WHITE
    }

    @JvmStatic
    fun getTextColor(): Int {
        return if (Theme.isLight()) Color.BLACK else Color.WHITE
    }

    @JvmStatic
    fun getTextBlueColor(): Int {
        return if (Theme.isLight()) Color.rgb(100, 164, 221) else Color.rgb(112, 184, 221)
    }

    @JvmStatic
    fun getTextGrayColor(): Int {
        return if (Theme.isLight()) Color.rgb(128, 128, 128) else Color.rgb(103, 115, 128)
    }

    @JvmStatic
    fun getArrowDrawableColor(): Int {
        return if (Theme.isLight()) Color.BLACK else Color.WHITE
    }
}
