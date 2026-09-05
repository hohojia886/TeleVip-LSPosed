package com.my.televip.virtuals.messenger

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers
import java.util.Locale

class LocaleController {
    @JvmField
    val localeController: Any? = XposedHelpers.callStaticMethod(
        ClassLoad.getClass(ClassNames.LOCALE_CONTROLLER),
        AutomationResolver.resolve("LocaleController", "getInstance", AutomationResolver.ResolverType.Method)
    )

    fun getCurrentLocale(): Locale? {
        return XposedHelpers.getObjectField(
            localeController,
            AutomationResolver.resolve("LocaleController", "currentLocale", AutomationResolver.ResolverType.Field)
        ) as? Locale
    }

    companion object {
        @JvmStatic
        fun isRTL(): Boolean {
            return XposedHelpers.getStaticBooleanField(
                ClassLoad.getClass(ClassNames.LOCALE_CONTROLLER),
                AutomationResolver.resolve("LocaleController", "isRTL", AutomationResolver.ResolverType.Field)
            )
        }
    }
}
