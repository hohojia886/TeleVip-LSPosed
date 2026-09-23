package com.my.televip.virtuals.ui

import android.view.View
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.ActionBar.ActionBarMenuItem
import com.my.televip.virtuals.messenger.MessageObject
import de.robv.android.xposed.XposedHelpers

class ChatActivity(@JvmField val chatActivity: Any?) {

    val selectedObject: MessageObject?
        get() {
            val target = chatActivity ?: return null
            val obj = XposedHelpers.getObjectField(target, Obfuscate.getFieldName("ChatActivity", "selectedObject")) ?: return null
            return MessageObject(obj)
        }

    val headerItem: ActionBarMenuItem
        get() {
            val target = chatActivity ?: return ActionBarMenuItem(null)
            val obj = XposedHelpers.getObjectField(target, Obfuscate.getFieldName("ChatActivity", "headerItem"))
            return ActionBarMenuItem(obj)
        }

    val pinnedMessageView: View?
        get() {
            val target = chatActivity ?: return null
            return XposedHelpers.getObjectField(target, Obfuscate.getFieldName("ChatActivity", "pinnedMessageView")) as? View
        }

    fun scrollToMessageId(
        messageId: Int,
        num: Int,
        notify: Boolean,
        loadType: Int,
        open: Boolean,
        mode: Int
    ) {
        val target = chatActivity ?: return
        XposedHelpers.callMethod(
            target,
            Obfuscate.getMethodName("ChatActivity", "scrollToMessageId"),
            messageId,
            num,
            notify,
            loadType,
            open,
            mode
        )
    }
}
