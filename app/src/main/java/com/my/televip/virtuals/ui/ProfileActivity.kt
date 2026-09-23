package com.my.televip.virtuals.ui

import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.ActionBar.ActionBarMenuItem
import de.robv.android.xposed.XposedHelpers

class ProfileActivity(@JvmField val profileActivity: Any?) {

    val userId: Long
        get() {
            val target = profileActivity ?: return 0L
            return XposedHelpers.getLongField(target, Obfuscate.getFieldName("ProfileActivity", "user_id"))
        }

    val chatId: Long
        get() {
            val target = profileActivity ?: return 0L
            return XposedHelpers.getLongField(target, Obfuscate.getFieldName("ProfileActivity", "chat_id"))
        }

    val nameTextView: Array<Any>?
        get() {
            val target = profileActivity ?: return null
            @Suppress("UNCHECKED_CAST")
            return XposedHelpers.getObjectField(target, Obfuscate.getFieldName("ProfileActivity", "nameTextView")) as? Array<Any>
        }

    val onlineTextView: Array<Any>?
        get() {
            val target = profileActivity ?: return null
            @Suppress("UNCHECKED_CAST")
            return XposedHelpers.getObjectField(target, Obfuscate.getFieldName("ProfileActivity", "onlineTextView")) as? Array<Any>
        }

    val otherItem: ActionBarMenuItem
        get() {
            val target = profileActivity ?: return ActionBarMenuItem(null)
            val result = XposedHelpers.getObjectField(target, Obfuscate.getFieldName("ProfileActivity", "otherItem"))
            return ActionBarMenuItem(result)
        }

    val baseFragment: BaseFragment?
        get() {
            val target = profileActivity ?: return null
            return BaseFragment(target)
        }
}
