package com.my.televip.features.ghostMode

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.configs.ConfigManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.messenger.UserConfig

object HidePhone {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true

                if (ClassLoad.getClass(ClassNames.USER_CONFIG) != null) {
                    HMethod.hookMethod(
                        ClassLoad.getClass(ClassNames.USER_CONFIG),
                        Obfuscate.getMethodName("UserConfig", "getClientUserId"),
                        object : BaseMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (ConfigManager.hidePhone?.isEnable == true) {
                                    val userConfig = UserConfig(param.thisObject)
                                    val currentUser = userConfig.currentUser
                                    if (currentUser.user != null) {
                                        if (currentUser.phone != null) {
                                            currentUser.phone = null
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
