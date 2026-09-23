package com.my.televip.virtuals.messenger

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.tgnet.TLRPC
import de.robv.android.xposed.XposedHelpers

class MessageObject(@JvmField val messageObject: Any?) {

    val messageOwner: TLRPC.Message
        get() {
            val ownerObj = XposedHelpers.getObjectField(messageObject, Obfuscate.getFieldName("MessageObject", "messageOwner"))
            return TLRPC.Message(ownerObj)
        }

    val dialogId: Long
        get() = XposedHelpers.callMethod(messageObject, Obfuscate.getMethodName("MessageObject", "getDialogId")) as? Long ?: 0L

    val isVoice: Boolean
        get() = XposedHelpers.callMethod(messageObject, Obfuscate.getMethodName("MessageObject", "isVoice")) as? Boolean ?: false

    companion object {
        @JvmStatic
        fun getDialogId(message: TLRPC.Message): Long {
            val msgClass = ClassLoad.getClass(ClassNames.MESSAGE_OBJECT) ?: return 0L
            return XposedHelpers.callStaticMethod(
                msgClass,
                Obfuscate.getMethodName("MessageObject", "getDialogId"),
                message.get_Message()
            ) as? Long ?: 0L
        }
    }
}
