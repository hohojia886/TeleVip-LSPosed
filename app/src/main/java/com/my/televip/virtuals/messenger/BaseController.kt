package com.my.televip.virtuals.messenger

import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

class BaseController(@JvmField val baseController: Any?) {

    val userConfig: UserConfig
        get() = UserConfig(XposedHelpers.callMethod(baseController, Obfuscate.getMethodName("BaseController", "getUserConfig")))
}
