package com.my.televip.virtuals.messenger

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.tgnet.TLRPC
import de.robv.android.xposed.XposedHelpers

class UserConfig(@JvmField val userConfig: Any?) {

    val currentUser: TLRPC.User
        get() = TLRPC.User(XposedHelpers.callMethod(userConfig, Obfuscate.getMethodName("UserConfig", "getCurrentUser")))

    val clientUserId: Long
        get() = XposedHelpers.callMethod(userConfig, Obfuscate.getMethodName("UserConfig", "getClientUserId")) as? Long ?: 0L

    companion object {
        @JvmStatic
        fun getSelectedAccount(): Int {
            val userConfigClass = ClassLoad.getClass(ClassNames.USER_CONFIG) ?: return 0
            return XposedHelpers.getStaticIntField(userConfigClass, Obfuscate.getFieldName("UserConfig", "selectedAccount"))
        }
    }
}
