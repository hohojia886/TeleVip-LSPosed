package com.my.televip.features

import android.view.MotionEvent
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver

object DisableChannelSwipeBack {
    @JvmField var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val chatActivityClass = ClassLoad.getClass(ClassNames.CHAT_ACTIVITY)
                if (chatActivityClass != null) {
                    HMethod.hookMethod(
                        chatActivityClass,
                        AutomationResolver.resolve("ChatActivity", "isSwipeBackEnabled", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("isSwipeBackEnabled", arrayOf<Class<*>>(MotionEvent::class.java)),
                            object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    if (ConfigManager.disableChannelSwipeBack.isEnable) {
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
