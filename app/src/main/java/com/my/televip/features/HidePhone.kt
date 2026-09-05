package com.my.televip.features

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.messenger.UserConfig

object HidePhone {
    @JvmField var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val userConfigClass = ClassLoad.getClass(ClassNames.USER_CONFIG)
                if (userConfigClass != null) {
                    HMethod.hookMethod(
                        userConfigClass,
                        AutomationResolver.resolve("UserConfig", "getClientUserId", AutomationResolver.ResolverType.Method),
                        object : AbstractMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (ConfigManager.hidePhone.isEnable) {
                                    val userConfig = UserConfig(param.thisObject)
                                    val user = userConfig.getCurrentUser()
                                    if (user != null && user.user != null) {
                                        if (user.getPhone() != null) {
                                            user.setPhone(null)
                                        }
                                    }
                                }
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
