package com.my.televip.virtuals.messenger

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

object NotificationCenter {
    private var messagesDeleted = -1
    private var tlSchemeParseException = -1

    @JvmStatic
    fun getMessagesDeleted(): Int {
        if (messagesDeleted == -1) {
            messagesDeleted = XposedHelpers.getStaticIntField(
                ClassLoad.getClass(ClassNames.NOTIFICATION_CENTER),
                AutomationResolver.resolve("NotificationCenter", "messagesDeleted", AutomationResolver.ResolverType.Field)
            )
        }
        return messagesDeleted
    }

    @JvmStatic
    fun getTlSchemeParseException(): Int {
        if (tlSchemeParseException == -1) {
            tlSchemeParseException = XposedHelpers.getStaticIntField(
                ClassLoad.getClass(ClassNames.NOTIFICATION_CENTER),
                AutomationResolver.resolve("NotificationCenter", "tlSchemeParseException", AutomationResolver.ResolverType.Field)
            )
        }
        return tlSchemeParseException
    }
}
