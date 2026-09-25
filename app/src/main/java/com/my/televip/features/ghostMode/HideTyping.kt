package com.my.televip.features.ghostMode

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.clients.ClientManager
import com.my.televip.obfuscate.Obfuscate

object HideTyping {

    @JvmStatic
    fun isTypingRequest(objectParam: Any): Boolean {
        return if (!ClientManager.isTgnetObfuscated()) {
            val className = objectParam.javaClass.name
            className.contains("TL_messages_setTyping") ||
                    className.contains("TL_messages_setEncryptedTyping")
        } else {
            val objectClass = objectParam.javaClass
            objectClass == ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_MESSAGES_SET_TYPING)) ||
                    objectClass == ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_MESSAGES_SET_ENCRYPTED_TYPING))
        }
    }
}
