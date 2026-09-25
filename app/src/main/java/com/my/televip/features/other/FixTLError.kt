package com.my.televip.features.other

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.configs.ConfigManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.messenger.NotificationCenter
import de.robv.android.xposed.XposedHelpers

object FixTLError {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val launchClass = ClassLoad.getClass(ClassNames.LAUNCH_ACTIVITY)
                if (launchClass != null) {
                    val types: Array<Class<*>> = arrayOf(
                        Int::class.javaPrimitiveType!!,
                        Int::class.javaPrimitiveType!!,
                        Array<Any>::class.java
                    )
                    val merged = ArgsResolver.merge("didReceivedNotification", types, object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            val id = param.args[0] as Int
                            if (id == NotificationCenter.getTlSchemeParseException() && ConfigManager.fixTLError?.isEnable == true) {
                                param.result = null
                            }
                        }
                    })
                    if (merged != null) {
                        XposedHelpers.findAndHookMethod(
                            launchClass,
                            Obfuscate.getMethodName("LaunchActivity", "didReceivedNotification"),
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
