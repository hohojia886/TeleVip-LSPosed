package com.my.televip.virtuals.messenger

import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

class BaseController(@JvmField val baseController: Any?) {
    fun getUserConfig(): UserConfig {
        return UserConfig(
            XposedHelpers.callMethod(
                baseController,
                AutomationResolver.resolve("BaseController", "getUserConfig", AutomationResolver.ResolverType.Method)
            )
        )
    }
}
