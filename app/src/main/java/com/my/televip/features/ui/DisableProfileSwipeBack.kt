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

object DisableProfileSwipeBack {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val profileClass = ClassLoad.getClass(ClassNames.PROFILE_ACTIVITY)
                if (profileClass != null) {
                    val types: Array<Class<*>> = arrayOf(MotionEvent::class.java)
                    val merged = ArgsResolver.merge("isSwipeBackEnabled", types, object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            if (ConfigManager.disableProfileSwipeBack?.isEnable == true) {
                                param.result = false
                            }
                        }
                    })
                    if (merged != null) {
                        XposedHelpers.findAndHookMethod(
                            profileClass,
                            Obfuscate.getMethodName("ProfileActivity", "isSwipeBackEnabled"),
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
