package com.my.televip.virtuals.messenger

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.tgnet.TLRPC
import de.robv.android.xposed.XposedHelpers

class MessageObject(@JvmField val messageObject: Any?) {

    fun getMessageOwner(): TLRPC.Message {
        return TLRPC.Message(
            XposedHelpers.getObjectField(
                messageObject,
                AutomationResolver.resolve("MessageObject", "messageOwner", AutomationResolver.ResolverType.Field)
            )
        )
    }

    fun getDialogId(): Long {
        return XposedHelpers.callMethod(
            messageObject,
            AutomationResolver.resolve("MessageObject", "getDialogId", AutomationResolver.ResolverType.Method)
        ) as Long
    }

    fun isVoice(): Boolean {
        return XposedHelpers.callMethod(
            messageObject,
            AutomationResolver.resolve("MessageObject", "isVoice", AutomationResolver.ResolverType.Method)
        ) as Boolean
    }

    companion object {
        @JvmStatic
        fun getDialogId(message: TLRPC.Message): Long {
            return XposedHelpers.callStaticMethod(
                ClassLoad.getClass(ClassNames.MESSAGE_OBJECT),
                AutomationResolver.resolve("MessageObject", "getDialogId", AutomationResolver.ResolverType.Method),
                message.message
            ) as Long
        }
    }
}
