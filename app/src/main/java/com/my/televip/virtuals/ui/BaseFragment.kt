package com.my.televip.virtuals.ui

import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.messenger.UserConfig
import de.robv.android.xposed.XposedHelpers

class BaseFragment(@JvmField val baseFragment: Any?) {
    fun getUserConfig(): UserConfig {
        return UserConfig(
            XposedHelpers.callMethod(
                baseFragment,
                AutomationResolver.resolve("BaseFragment", "getUserConfig", AutomationResolver.ResolverType.Method)
            )
        )
    }
}
