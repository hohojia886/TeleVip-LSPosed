package com.my.televip.features.media

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.ui.SecretMediaViewer
import de.robv.android.xposed.XposedHelpers

object PreventMedia {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true

                val chatClass = ClassLoad.getClass(ClassNames.CHAT_ACTIVITY)
                if (chatClass != null) {
                    val readTypes = arrayOf(
                        ClassLoad.getClass(ClassNames.MESSAGE_OBJECT)!!,
                        Boolean::class.javaPrimitiveType!!
                    )
                    val readMerged = ArgsResolver.merge("sendSecretMessageRead", readTypes, object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            if (ConfigManager.preventMedia?.isEnable == true) param.result = null
                        }
                    })
                    if (readMerged != null) {
                        XposedHelpers.findAndHookMethod(
                            chatClass,
                            Obfuscate.getMethodName("ChatActivity", "sendSecretMessageRead"),
                            *readMerged
                        )
                    }

                    val deleteTypes = arrayOf<Class<*>>(ClassLoad.getClass(ClassNames.MESSAGE_OBJECT)!!)
                    val deleteMerged = ArgsResolver.merge("sendSecretMediaDelete", deleteTypes, object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            if (ConfigManager.preventMedia?.isEnable == true) param.result = null
                        }
                    })
                    if (deleteMerged != null) {
                        XposedHelpers.findAndHookMethod(
                            chatClass,
                            Obfuscate.getMethodName("ChatActivity", "sendSecretMediaDelete"),
                            *deleteMerged
                        )
                    }
                }

                val secretClass = ClassLoad.getClass(ClassNames.SECRET_MEDIA_VIEWER)
                if (secretClass != null) {
                    SecretMediaViewer.openMedia()
                    val closeTypes: Array<Class<*>> = arrayOf(
                        Boolean::class.javaPrimitiveType!!,
                        Boolean::class.javaPrimitiveType!!
                    )
                    val closeMerged = ArgsResolver.merge("closePhoto", closeTypes, object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            if (ConfigManager.preventMedia?.isEnable == true) {
                                val thisObject = param.thisObject
                                XposedHelpers.setObjectField(
                                    thisObject,
                                    Obfuscate.getFieldName("SecretMediaViewer", "onClose"),
                                    null
                                )
                            }
                        }
                    })
                    if (closeMerged != null) {
                        XposedHelpers.findAndHookMethod(
                            secretClass,
                            Obfuscate.getMethodName("SecretMediaViewer", "closePhoto"),
                            *closeMerged
                        )
                    }
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }
}
