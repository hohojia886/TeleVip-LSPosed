package com.my.televip.features

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.messenger.MessageObject

object VoiceToMusicHook {
    @JvmField var isEnable: Boolean = false
    private var stop: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true

                val msgObjClass = ClassLoad.getClass(ClassNames.MESSAGE_OBJECT)
                if (msgObjClass != null) {
                    HMethod.hookMethod(
                        msgObjClass,
                        AutomationResolver.resolve("MessageObject", "isVoice", AutomationResolver.ResolverType.Method),
                        object : AbstractMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (!stop) param.result = false
                            }

                            override fun afterMethod(param: MethodHookParam) {
                                stop = false
                            }
                        }
                    )
                    HMethod.hookMethod(
                        msgObjClass,
                        AutomationResolver.resolve("MessageObject", "isMusic", AutomationResolver.ResolverType.Method),
                        object : AbstractMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                val messageObject = MessageObject(param.thisObject)
                                stop = true
                                if (messageObject.isVoice()) param.result = true
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
