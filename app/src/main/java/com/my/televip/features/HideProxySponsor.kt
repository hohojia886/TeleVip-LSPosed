package com.my.televip.features

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.application.AndroidUtilities
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.messenger.MessagesController

object HideProxySponsor {
    @JvmField var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val messagesControllerClass = ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER)
                if (messagesControllerClass != null) {
                    HMethod.hookMethod(
                        messagesControllerClass,
                        AutomationResolver.resolve("MessagesController", "checkPromoInfoInternal", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("checkPromoInfoInternal", arrayOf<Class<*>>(Boolean::class.javaPrimitiveType!!)),
                            object : AbstractMethodHook() {
                                override fun afterMethod(param: MethodHookParam) {
                                    if (ConfigManager.hideProxySponsor.isEnable) {
                                        val messagesController = MessagesController(param.thisObject)
                                        AndroidUtilities.runOnUIThread { messagesController.removePromoDialog() }
                                        removePromoDialog()
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

    @JvmStatic
    fun removePromoDialog() {
        val globalSettings = MessagesController.getGlobalMainSettings() ?: return
        globalSettings.edit().remove("proxy_dialog").remove("proxyDialogAddress").remove("nextPromoInfoCheckTime").apply()
    }
}
