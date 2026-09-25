package com.my.televip.virtuals.ActionBar

import android.text.TextPaint
import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

object Theme {

    @JvmStatic
    fun getTextPaint(): TextPaint? {
        val themeClass = ClassLoad.getClass(ClassNames.THEME) ?: return null
        return XposedHelpers.getStaticObjectField(
            themeClass,
            Obfuscate.getFieldName("Theme", "chat_timePaint")
        ) as? TextPaint
    }

    @JvmStatic
    fun isLight(): Boolean {
        val themeClass = ClassLoad.getClass(ClassNames.THEME) ?: return true
        val isDark = XposedHelpers.callStaticMethod(
            themeClass,
            Obfuscate.getMethodName("Theme", "isCurrentThemeDark")
        ) as? Boolean ?: false
        return !isDark
    }
}
