package com.my.televip.virtuals

import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

open class ChatMessageCellDefault protected constructor(protected val instance: Any?) {

    fun getTimeTextWidth(): Int {
        return XposedHelpers.getIntField(
            instance,
            AutomationResolver.resolve("ChatMessageCell", "timeTextWidth", AutomationResolver.ResolverType.Field)
        )
    }

    fun getTimeWidth(): Int {
        return XposedHelpers.getIntField(
            instance,
            AutomationResolver.resolve("ChatMessageCell", "timeWidth", AutomationResolver.ResolverType.Field)
        )
    }

    fun setTimeTextWidth(width: Int) {
        try {
            XposedHelpers.setIntField(
                instance,
                AutomationResolver.resolve("ChatMessageCell", "timeTextWidth", AutomationResolver.ResolverType.Field),
                width
            )
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    fun setTimeWidth(width: Int) {
        try {
            XposedHelpers.setIntField(
                instance,
                AutomationResolver.resolve("ChatMessageCell", "timeWidth", AutomationResolver.ResolverType.Field),
                width
            )
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }
}
