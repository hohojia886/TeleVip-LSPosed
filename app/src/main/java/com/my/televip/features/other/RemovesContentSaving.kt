package com.my.televip.features.other

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.clients.ClientManager
import com.my.televip.configs.ConfigManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

object RemovesContentSaving {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true

                val mcClass = ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER)
                if (mcClass != null) {
                    val types: Array<Class<*>> = arrayOf(ClassLoad.getClass(ClassNames.TLRPC_CHAT)!!)
                    val merged = ArgsResolver.merge("isChatNoForwards", types, object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            if (ConfigManager.removesContentSaving?.isEnable == true) {
                                param.result = false
                            }
                        }
                    })
                    if (merged != null) {
                        XposedHelpers.findAndHookMethod(
                            mcClass,
                            Obfuscate.getMethodName("MessagesController", "isChatNoForwards"),
                            *merged
                        )
                    }
                }

                val chatClass = ClassLoad.getClass(ClassNames.CHAT_ACTIVITY)
                if (chatClass != null && !ClientManager.`is`(ClientManager.Client.NagramXF)) {
                    HMethod.hookMethod(
                        chatClass,
                        Obfuscate.getMethodName("ChatActivity", "hasSelectedNoforwardsMessage"),
                        object : BaseMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (ConfigManager.removesContentSaving?.isEnable == true) {
                                    param.result = false
                                }
                            }
                        }
                    )
                }

                val msgClass = ClassLoad.getClass(ClassNames.MESSAGE_OBJECT)
                if (msgClass != null) {
                    HMethod.hookMethod(
                        msgClass,
                        Obfuscate.getMethodName("MessageObject", "canForwardMessage"),
                        object : BaseMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (ConfigManager.removesContentSaving?.isEnable == true) {
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
