package com.my.televip.virtuals.ui

import android.widget.FrameLayout
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

class LaunchActivity(@JvmField val launchActivity: Any?) {
    @JvmField
    val frameLayout: FrameLayout = XposedHelpers.getObjectField(
        launchActivity,
        AutomationResolver.resolve("LaunchActivity", "frameLayout", AutomationResolver.ResolverType.Field)
    ) as FrameLayout
}
