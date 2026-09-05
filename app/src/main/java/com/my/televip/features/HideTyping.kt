package com.my.televip.features

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.ClientChecker
import com.my.televip.obfuscate.AutomationResolver

object HideTyping {
    @JvmStatic
    fun isTypingRequest(obj: Any): Boolean {
        return if (!ClientChecker.isTgnetObfuscated()) {
            val className = obj.javaClass.name
            className.contains("TL_messages_setTyping") || className.contains("TL_messages_setEncryptedTyping")
        } else {
            val objectClass = obj.javaClass
            objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_MESSAGES_SET_TYPING)) ||
                    objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_MESSAGES_SET_ENCRYPTED_TYPING))
        }
    }
}
