package com.my.televip.features

import android.view.MotionEvent
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver

object DisableProfileSwipeBack {
    @JvmField var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val profileActivityClass = ClassLoad.getClass(ClassNames.PROFILE_ACTIVITY)
                if (profileActivityClass != null) {
                    HMethod.hookMethod(
                        profileActivityClass,
                        AutomationResolver.resolve("ProfileActivity", "isSwipeBackEnabled", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("isSwipeBackEnabled", arrayOf<Class<*>>(MotionEvent::class.java)),
                            object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    if (ConfigManager.disableProfileSwipeBack.isEnable) {
                                        param.result = false
                                    }
                                }
                            }
                        )
                    )
                }
            }
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }
}
