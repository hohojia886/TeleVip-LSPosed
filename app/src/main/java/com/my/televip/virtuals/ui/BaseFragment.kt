package com.my.televip.virtuals.ui

import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.messenger.UserConfig
import de.robv.android.xposed.XposedHelpers

class BaseFragment(@JvmField val fragment: Any?) {

    val userConfig: UserConfig
        get() = UserConfig(XposedHelpers.callMethod(fragment, Obfuscate.getMethodName("BaseFragment", "getUserConfig")))
}
