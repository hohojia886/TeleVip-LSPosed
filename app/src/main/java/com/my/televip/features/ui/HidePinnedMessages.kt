package com.my.televip.features.ui

import android.view.View
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.ui.ChatActivity

object HidePinnedMessages {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val chatClass = ClassLoad.getClass(ClassNames.CHAT_ACTIVITY)
                if (chatClass != null) {
                    HMethod.hookMethod(
                        chatClass,
                        Obfuscate.getMethodName("ChatActivity", "createPinnedMessageView"),
                        object : BaseMethodHook() {
                            override fun afterMethod(param: MethodHookParam) {
                                if (ConfigManager.hidePinnedMessages?.isEnable == true) {
                                    val button = ChatActivity(param.thisObject).pinnedMessageView
                                    if (button != null && button.visibility != View.GONE) {
                                        button.visibility = View.GONE
                                    }
                                }
                            }
                        }
                    )

                    HMethod.hookMethod(
                        chatClass,
                        Obfuscate.getMethodName("ChatActivity", "updatePinnedMessageView"),
                        Boolean::class.javaPrimitiveType!!,
                        Int::class.javaPrimitiveType!!,
                        object : BaseMethodHook() {
                            override fun afterMethod(param: MethodHookParam) {
                                if (ConfigManager.hidePinnedMessages?.isEnable == true) {
                                    val button = ChatActivity(param.thisObject).pinnedMessageView
                                    if (button != null && button.visibility != View.GONE) {
                                        button.visibility = View.GONE
                                    }
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
