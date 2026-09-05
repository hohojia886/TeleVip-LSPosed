package com.my.televip.virtuals.messenger

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.tgnet.TLRPC
import de.robv.android.xposed.XposedHelpers

class UserConfig(@JvmField val userConfig: Any?) {

    fun getClientUserId(): Long {
        return XposedHelpers.getLongField(
            userConfig,
            AutomationResolver.resolve("UserConfig", "clientUserId", AutomationResolver.ResolverType.Field)
        )
    }

    fun getCurrentUser(): TLRPC.User {
        return TLRPC.User(
            XposedHelpers.callMethod(
                userConfig,
                AutomationResolver.resolve("UserConfig", "getCurrentUser", AutomationResolver.ResolverType.Method)
            )
        )
    }

    companion object {
        @JvmStatic
        fun getSelectedAccount(): Int {
            val selectedAccountField = AutomationResolver.resolve("UserConfig", "selectedAccount", AutomationResolver.ResolverType.Field)
            return XposedHelpers.getStaticIntField(ClassLoad.getClass(ClassNames.USER_CONFIG), selectedAccountField)
        }
    }
}
