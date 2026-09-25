package com.my.televip.features.media

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.messenger.MessageObject

object VoiceToMusicHook {

    @JvmField
    var isEnable: Boolean = false

    private var stop = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true

                val msgClass = ClassLoad.getClass(ClassNames.MESSAGE_OBJECT)
                if (msgClass != null) {
                    HMethod.hookMethod(
                        msgClass,
                        Obfuscate.getMethodName("MessageObject", "isVoice"),
                        object : BaseMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (!stop) param.result = false
                            }

                            override fun afterMethod(param: MethodHookParam) {
                                stop = false
                            }
                        }
                    )

                    HMethod.hookMethod(
                        msgClass,
                        Obfuscate.getMethodName("MessageObject", "isMusic"),
                        object : BaseMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                val messageObject = MessageObject(param.thisObject)
                                stop = true
                                if (messageObject.isVoice) param.result = true
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
