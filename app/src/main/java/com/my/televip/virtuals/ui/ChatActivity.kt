package com.my.televip.virtuals.ui

import android.view.View
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.ActionBar.ActionBarMenuItem
import com.my.televip.virtuals.messenger.MessageObject
import de.robv.android.xposed.XposedHelpers

class ChatActivity(@JvmField val chatActivity: Any?) {

    fun getSelectedObject(): MessageObject {
        return MessageObject(
            XposedHelpers.getObjectField(
                chatActivity,
                AutomationResolver.resolve("ChatActivity", "selectedObject", AutomationResolver.ResolverType.Field)
            )
        )
    }

    fun getHeaderItem(): ActionBarMenuItem {
        return ActionBarMenuItem(
            XposedHelpers.getObjectField(
                chatActivity,
                AutomationResolver.resolve("ChatActivity", "headerItem", AutomationResolver.ResolverType.Field)
            )
        )
    }

    fun getPinnedMessageView(): View? {
        return XposedHelpers.getObjectField(
            chatActivity,
            AutomationResolver.resolve("ChatActivity", "pinnedMessageView", AutomationResolver.ResolverType.Field)
        ) as? View
    }

    fun scrollToMessageId(id: Int, fromMessageId: Int, select: Boolean, loadIndex: Int, forceScroll: Boolean, forcePinnedMessageId: Int) {
        XposedHelpers.callMethod(
            chatActivity,
            AutomationResolver.resolve("ChatActivity", "scrollToMessageId", AutomationResolver.ResolverType.Method),
            id, fromMessageId, select, loadIndex, forceScroll, forcePinnedMessageId
        )
    }
}
