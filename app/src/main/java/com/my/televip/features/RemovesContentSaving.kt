package com.my.televip.features

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.ClientChecker
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver

object RemovesContentSaving {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true

                val messagesControllerClass = ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER)
                val tlrpcChatClass = ClassLoad.getClass(ClassNames.TLRPC_CHAT)
                if (messagesControllerClass != null && tlrpcChatClass != null) {
                    HMethod.hookMethod(
                        messagesControllerClass,
                        AutomationResolver.resolve("MessagesController", "isChatNoForwards", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("isChatNoForwards", arrayOf(tlrpcChatClass)),
                            object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    if (ConfigManager.removesContentSaving.isEnable) {
                                        param.result = false
                                    }
                                }
                            }
                        )
                    )
                }

                val chatActivityClass = ClassLoad.getClass(ClassNames.CHAT_ACTIVITY)
                if (chatActivityClass != null && !ClientChecker.check(ClientChecker.ClientType.NagramXF)) {
                    HMethod.hookMethod(
                        chatActivityClass,
                        AutomationResolver.resolve("ChatActivity", "hasSelectedNoforwardsMessage", AutomationResolver.ResolverType.Method),
                        object : AbstractMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (ConfigManager.removesContentSaving.isEnable) {
                                    param.result = false
                                }
                            }
                        }
                    )
                }

                val messageObjectClass = ClassLoad.getClass(ClassNames.MESSAGE_OBJECT)
                if (messageObjectClass != null) {
                    HMethod.hookMethod(
                        messageObjectClass,
                        AutomationResolver.resolve("MessageObject", "canForwardMessage", AutomationResolver.ResolverType.Method),
                        object : AbstractMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (ConfigManager.removesContentSaving.isEnable) {
                                    param.result = true
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
