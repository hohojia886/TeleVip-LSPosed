package com.my.televip.features

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.messenger.NotificationCenter

object FixTLError {
    @JvmField var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val launchActivityClass = ClassLoad.getClass(ClassNames.LAUNCH_ACTIVITY)
                if (launchActivityClass != null) {
                    HMethod.hookMethod(
                        launchActivityClass,
                        AutomationResolver.resolve("LaunchActivity", "didReceivedNotification", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("didReceivedNotification", arrayOf<Class<*>>(Int::class.javaPrimitiveType!!, Int::class.javaPrimitiveType!!, Array<Any>::class.java)),
                            object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    val id = param.args[0] as Int
                                    if (id == NotificationCenter.getTlSchemeParseException() && ConfigManager.fixTLError.isEnable) {
                                        param.result = null
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
