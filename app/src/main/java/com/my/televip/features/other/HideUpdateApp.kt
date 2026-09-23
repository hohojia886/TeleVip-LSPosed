package com.my.televip.features.other

import android.app.Activity
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.application.ApplicationLoaderHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers

object HideUpdateApp {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true

                val preferences = ApplicationLoaderHook.getApplicationContext()
                    .getSharedPreferences("mainconfig", Activity.MODE_PRIVATE)
                preferences.edit().remove("appUpdate").apply()
                preferences.edit().remove("appUpdateCheckTime").apply()
                preferences.edit().remove("appUpdateBuild").apply()

                val sharedConfigClass = ClassLoad.getClass(ClassNames.SHARED_CONFIG)
                if (sharedConfigClass != null) {
                    val types: Array<Class<*>> = arrayOf(ClassLoad.getClass(ClassNames.TL_HELP_APP_UPDATE)!!)
                    val merged = ArgsResolver.merge(
                        "setNewAppVersionAvailable",
                        types,
                        object : XC_MethodReplacement() {
                            override fun replaceHookedMethod(param: MethodHookParam): Any {
                                return false
                            }
                        }
                    )
                    if (merged != null) {
                        XposedHelpers.findAndHookMethod(
                            sharedConfigClass,
                            Obfuscate.getMethodName("SharedConfig", "setNewAppVersionAvailable"),
                            *merged
                        )
                    }

                    HMethod.hookMethod(
                        sharedConfigClass,
                        Obfuscate.getMethodName("SharedConfig", "isAppUpdateAvailable"),
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
