package com.my.televip.virtuals.ui.Cells

import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.messenger.MessageObject
import de.robv.android.xposed.XposedHelpers

class ChatMessageCell(@JvmField val chatMessageCell: Any?) {

    val currentTimeString: CharSequence?
        get() = XposedHelpers.getObjectField(chatMessageCell, Obfuscate.getFieldName("ChatMessageCell", "currentTimeString")) as? CharSequence

    fun setCurrentTimeString(time: CharSequence?) {
        XposedHelpers.setObjectField(chatMessageCell, Obfuscate.getFieldName("ChatMessageCell", "currentTimeString"), time)
    }

    var timeTextWidth: Int
        get() = XposedHelpers.getIntField(chatMessageCell, Obfuscate.getFieldName("ChatMessageCell", "timeTextWidth"))
        set(value) {
            XposedHelpers.setIntField(chatMessageCell, Obfuscate.getFieldName("ChatMessageCell", "timeTextWidth"), value)
        }

    var timeWidth: Int
        get() = XposedHelpers.getIntField(chatMessageCell, Obfuscate.getFieldName("ChatMessageCell", "timeWidth"))
        set(value) {
            XposedHelpers.setIntField(chatMessageCell, Obfuscate.getFieldName("ChatMessageCell", "timeWidth"), value)
        }

    val messageObject: MessageObject
        get() = MessageObject(XposedHelpers.callMethod(chatMessageCell, Obfuscate.getMethodName("ChatMessageCell", "getMessageObject")))
}
