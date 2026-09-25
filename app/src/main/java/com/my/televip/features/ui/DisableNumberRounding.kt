package com.my.televip.features.ui

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.configs.ConfigManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

object DisableNumberRounding {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val lcClass = ClassLoad.getClass(ClassNames.LOCALE_CONTROLLER)
                if (lcClass != null) {
                    val types: Array<Class<*>> = arrayOf(
                        Int::class.javaPrimitiveType!!,
                        IntArray::class.java
                    )
                    val merged = ArgsResolver.merge("formatShortNumber", types, object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            if (ConfigManager.disableNumberRounding?.isEnable == true) {
                                val rounded = param.args[1] as? IntArray
                                val number = param.args[0] as Int
                                if (rounded != null && rounded.isNotEmpty()) {
                                    rounded[0] = number
                                }
                                param.result = number.toString()
                            }
                        }
                    })
                    if (merged != null) {
                        XposedHelpers.findAndHookMethod(
                            lcClass,
                            Obfuscate.getMethodName("LocaleController", "formatShortNumber"),
                            *merged
                        )
                    }
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }
}
