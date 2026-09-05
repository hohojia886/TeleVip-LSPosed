package com.my.televip.features

import android.view.View
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.ui.ChatActivity

object HidePinnedMessages {
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
                        AutomationResolver.resolve("ChatActivity", "createPinnedMessageView", AutomationResolver.ResolverType.Method),
                        object : AbstractMethodHook() {
                            override fun afterMethod(param: MethodHookParam) {
                                if (ConfigManager.hidePinnedMessages.isEnable) {
                                    val button = ChatActivity(param.thisObject).getPinnedMessageView()
                                    if (button != null && button.visibility != View.GONE) {
                                        button.visibility = View.GONE
                                    }
                                }
                            }
                        }
                    )
                    HMethod.hookMethod(
                        chatActivityClass,
                        AutomationResolver.resolve("ChatActivity", "updatePinnedMessageView", AutomationResolver.ResolverType.Method),
                        Boolean::class.javaPrimitiveType!!,
                        Int::class.javaPrimitiveType!!,
                        object : AbstractMethodHook() {
                            override fun afterMethod(param: MethodHookParam) {
                                if (ConfigManager.hidePinnedMessages.isEnable) {
                                    val button = ChatActivity(param.thisObject).getPinnedMessageView()
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
