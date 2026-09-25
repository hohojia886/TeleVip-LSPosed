package com.my.televip.features.ui

import android.view.MotionEvent
import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.configs.ConfigManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

object DisableChannelSwipeBack {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val chatClass = ClassLoad.getClass(ClassNames.CHAT_ACTIVITY)
                if (chatClass != null) {
                    val types: Array<Class<*>> = arrayOf(MotionEvent::class.java)
                    val merged = ArgsResolver.merge("isSwipeBackEnabled", types, object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            if (ConfigManager.disableChannelSwipeBack?.isEnable == true) {
                                param.result = false
                            }
                        }
                    })
                    if (merged != null) {
                        XposedHelpers.findAndHookMethod(
                            chatClass,
                            Obfuscate.getMethodName("ChatActivity", "isSwipeBackEnabled"),
                            *merged
                        )
                    }
                }
            }
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }
}
