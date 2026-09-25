package com.my.televip.virtuals.messenger

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

object NotificationCenter {

    private var messagesDeleted = -1
    private var tlSchemeParseException = -1

    @JvmStatic
    fun getMessagesDeleted(): Int {
        if (messagesDeleted == -1) {
            val ncClass = ClassLoad.getClass(ClassNames.NOTIFICATION_CENTER) ?: return -1
            messagesDeleted = XposedHelpers.getStaticIntField(ncClass, Obfuscate.getFieldName("NotificationCenter", "messagesDeleted"))
        }
        return messagesDeleted
    }

    @JvmStatic
    fun getTlSchemeParseException(): Int {
        if (tlSchemeParseException == -1) {
            val ncClass = ClassLoad.getClass(ClassNames.NOTIFICATION_CENTER) ?: return -1
            tlSchemeParseException = XposedHelpers.getStaticIntField(ncClass, Obfuscate.getFieldName("NotificationCenter", "tlSchemeParseException"))
        }
        return tlSchemeParseException
    }
}
