package com.my.televip.virtuals

import android.graphics.Color
import android.text.TextPaint
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

object Theme {

    @JvmStatic
    fun getTextPaint(): TextPaint? {
        return XposedHelpers.getStaticObjectField(
            ClassLoad.getClass(ClassNames.THEME),
            AutomationResolver.resolve("Theme", "chat_timePaint", AutomationResolver.ResolverType.Field)
        ) as? TextPaint
    }

    @JvmStatic
    fun isLight(): Boolean {
        val isDark = XposedHelpers.callStaticMethod(
            ClassLoad.getClass(ClassNames.THEME),
            AutomationResolver.resolve("Theme", "isCurrentThemeDark", AutomationResolver.ResolverType.Method)
        ) as? Boolean ?: false
        return !isDark
    }

    @JvmStatic
    fun getBackgroundGrayColor(): Int {
        return if (isLight()) Color.rgb(241, 241, 243) else Color.rgb(21, 30, 39)
    }

    @JvmStatic
    fun getBackgroundWhiteOrBlueColor(): Int {
        return if (isLight()) Color.WHITE else Color.rgb(29, 39, 51)
    }

    @JvmStatic
    fun getToolBarColor(): Int {
        return if (isLight()) Color.WHITE else Color.rgb(36, 45, 57)
    }

    @JvmStatic
    fun getToolBarRippleColor(): Int {
        return if (isLight()) 0x20000000 else 0x20FFFFFF.toInt()
    }

    @JvmStatic
    fun getTextToolBarColor(): Int {
        return if (isLight()) Color.BLACK else Color.WHITE
    }

    @JvmStatic
    fun getTextColor(): Int {
        return if (isLight()) Color.BLACK else Color.WHITE
    }

    @JvmStatic
    fun getTextBlueColor(): Int {
        return if (isLight()) Color.rgb(100, 164, 221) else Color.rgb(112, 184, 221)
    }

    @JvmStatic
    fun getTextGrayColor(): Int {
        return if (isLight()) Color.rgb(128, 128, 128) else Color.rgb(103, 115, 128)
    }

    @JvmStatic
    fun getArrowDrawableColor(): Int {
        return if (isLight()) Color.BLACK else Color.WHITE
    }
}
