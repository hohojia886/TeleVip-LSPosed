package com.my.televip.virtuals

import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

class OfficialChatMessageCell(instance: Any?) : ChatMessageCellDefault(instance) {

    fun getCurrentTimeString(): CharSequence? {
        return XposedHelpers.getObjectField(
            this.instance,
            AutomationResolver.resolve("ChatMessageCell", "currentTimeString", AutomationResolver.ResolverType.Field)
        ) as? CharSequence
    }

    fun setCurrentTimeString(currentTimeString: CharSequence?) {
        try {
            XposedHelpers.setObjectField(
                this.instance,
                AutomationResolver.resolve("ChatMessageCell", "currentTimeString", AutomationResolver.ResolverType.Field),
                currentTimeString
            )
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }
}
