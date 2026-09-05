package com.my.televip.virtuals.ui

import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.ActionBar.ActionBarMenuItem
import de.robv.android.xposed.XposedHelpers

class ProfileActivity(@JvmField val profileActivity: Any?) {

    fun getChatId(): Long {
        return XposedHelpers.getLongField(
            profileActivity,
            AutomationResolver.resolve("ProfileActivity", "chatId", AutomationResolver.ResolverType.Field)
        )
    }

    fun getUserId(): Long {
        return XposedHelpers.getLongField(
            profileActivity,
            AutomationResolver.resolve("ProfileActivity", "userId", AutomationResolver.ResolverType.Field)
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun getNameTextView(): Array<Any>? {
        return XposedHelpers.getObjectField(
            profileActivity,
            AutomationResolver.resolve("ProfileActivity", "nameTextView", AutomationResolver.ResolverType.Field)
        ) as? Array<Any>
    }

    @Suppress("UNCHECKED_CAST")
    fun getOnlineTextView(): Array<Any>? {
        return XposedHelpers.getObjectField(
            profileActivity,
            AutomationResolver.resolve("ProfileActivity", "onlineTextView", AutomationResolver.ResolverType.Field)
        ) as? Array<Any>
    }

    fun getOtherItem(): ActionBarMenuItem {
        return ActionBarMenuItem(
            XposedHelpers.getObjectField(
                profileActivity,
                AutomationResolver.resolve("ProfileActivity", "otherItem", AutomationResolver.ResolverType.Field)
            )
        )
    }

    fun getBaseFragment(): BaseFragment {
        return BaseFragment(profileActivity)
    }
}
