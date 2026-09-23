package com.my.televip.features.ui

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.Obfuscate

object HideProxySponsor {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val mcClass = ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER)
                if (mcClass != null) {
                    HMethod.hookMethod(
                        mcClass,
                        Obfuscate.getMethodName("MessagesController", "getProxySponsorChannel"),
                        object : BaseMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (ConfigManager.hideProxySponsor?.isEnable == true) {
                                    param.result = null
                                }
                            }
                        }
                    )
                }
            }
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }
}
