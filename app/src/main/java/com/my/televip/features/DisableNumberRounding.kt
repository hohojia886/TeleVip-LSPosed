package com.my.televip.features

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver

object DisableNumberRounding {
    @JvmField var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val localeClass = ClassLoad.getClass(ClassNames.LOCALE_CONTROLLER)
                if (localeClass != null) {
                    HMethod.hookMethod(
                        localeClass,
                        AutomationResolver.resolve("LocaleController", "formatShortNumber", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("formatShortNumber", arrayOf<Class<*>>(Int::class.javaPrimitiveType!!, IntArray::class.java)),
                            object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    if (ConfigManager.disableNumberRounding.isEnable) {
                                        val rounded = param.args[1] as? IntArray
                                        val number = param.args[0] as Int
                                        if (rounded != null && rounded.isNotEmpty()) {
                                            rounded[0] = number
                                        }
                                        param.result = number.toString()
                                    }
                                }
                            }
                        )
                    )
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }
}
