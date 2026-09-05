package com.my.televip.features

import android.app.Activity
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.application.ApplicationLoaderHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XC_MethodReplacement

object HideUpdateApp {
    @JvmField var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val preferences = ApplicationLoaderHook.getApplicationContext().getSharedPreferences("mainconfig", Activity.MODE_PRIVATE)
                preferences.edit().remove("appUpdate").apply()
                preferences.edit().remove("appUpdateCheckTime").apply()
                preferences.edit().remove("appUpdateBuild").apply()

                val sharedConfigClass = ClassLoad.getClass(ClassNames.SHARED_CONFIG)
                val tlHelpAppUpdateClass = ClassLoad.getClass(ClassNames.TL_HELP_APP_UPDATE)
                if (sharedConfigClass != null && tlHelpAppUpdateClass != null) {
                    HMethod.hookMethod(
                        sharedConfigClass,
                        AutomationResolver.resolve("SharedConfig", "setNewAppVersionAvailable", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("setNewAppVersionAvailable", arrayOf<Class<*>>(tlHelpAppUpdateClass)),
                            object : XC_MethodReplacement() {
                                override fun replaceHookedMethod(param: MethodHookParam): Any {
                                    return false
                                }
                            }
                        )
                    )

                    HMethod.hookMethod(
                        sharedConfigClass,
                        AutomationResolver.resolve("SharedConfig", "isAppUpdateAvailable", AutomationResolver.ResolverType.Method),
                        object : XC_MethodReplacement() {
                            override fun replaceHookedMethod(param: MethodHookParam): Any {
                                return false
                            }
                        }
                    )
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }
}
