package com.my.televip.features

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.ui.SecretMediaViewer
import de.robv.android.xposed.XposedHelpers

object PreventMedia {
    @JvmField var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val chatActivityClass = ClassLoad.getClass(ClassNames.CHAT_ACTIVITY)
                val msgObjClass = ClassLoad.getClass(ClassNames.MESSAGE_OBJECT)
                if (chatActivityClass != null && msgObjClass != null) {
                    HMethod.hookMethod(
                        chatActivityClass,
                        AutomationResolver.resolve("ChatActivity", "sendSecretMessageRead", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("sendSecretMessageRead", arrayOf<Class<*>>(msgObjClass, Boolean::class.javaPrimitiveType!!)),
                            object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    if (ConfigManager.preventMedia.isEnable) param.result = null
                                }
                            }
                        )
                    )
                    HMethod.hookMethod(
                        chatActivityClass,
                        AutomationResolver.resolve("ChatActivity", "sendSecretMediaDelete", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("sendSecretMediaDelete", arrayOf<Class<*>>(msgObjClass)),
                            object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    if (ConfigManager.preventMedia.isEnable) param.result = null
                                }
                            }
                        )
                    )
                }

                val secretMediaViewerClass = ClassLoad.getClass(ClassNames.SECRET_MEDIA_VIEWER)
                if (secretMediaViewerClass != null) {
                    SecretMediaViewer.openMedia()
                    HMethod.hookMethod(
                        secretMediaViewerClass,
                        AutomationResolver.resolve("SecretMediaViewer", "closePhoto", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("closePhoto", arrayOf<Class<*>>(Boolean::class.javaPrimitiveType!!, Boolean::class.javaPrimitiveType!!)),
                            object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    if (ConfigManager.preventMedia.isEnable) {
                                        val thisObject = param.thisObject
                                        XposedHelpers.setObjectField(thisObject, AutomationResolver.resolve("SecretMediaViewer", "onClose", AutomationResolver.ResolverType.Field), null)
                                    }
                                }
                            }
                        )
                    )
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }
}
