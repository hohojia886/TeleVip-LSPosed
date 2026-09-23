package com.my.televip.virtuals.messenger

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers
import java.util.Locale

class LocaleController(@JvmField val localeController: Any? = null) {

    val currentLocale: Locale?
        get() {
            val target = localeController ?: getInstance()?.localeController ?: return null
            return XposedHelpers.getObjectField(target, Obfuscate.getFieldName("LocaleController", "currentLocale")) as? Locale
        }

    companion object {
        @JvmStatic
        fun getInstance(): LocaleController? {
            val lcClass = ClassLoad.getClass(ClassNames.LOCALE_CONTROLLER) ?: return null
            val instance = XposedHelpers.callStaticMethod(lcClass, Obfuscate.getMethodName("LocaleController", "getInstance"))
            return LocaleController(instance)
        }

        @JvmStatic
        fun isRTL(): Boolean {
            val lcClass = ClassLoad.getClass(ClassNames.LOCALE_CONTROLLER) ?: return false
            return XposedHelpers.callStaticMethod(lcClass, Obfuscate.getMethodName("LocaleController", "isRTL")) as? Boolean ?: false
        }
    }
}
