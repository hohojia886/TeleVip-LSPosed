package com.my.televip.virtuals.messenger

import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

class DispatchQueue(@JvmField val dispatchQueue: Any?) {
    fun postRunnable(runnable: Runnable) {
        XposedHelpers.callMethod(
            dispatchQueue,
            AutomationResolver.resolve("DispatchQueue", "postRunnable", AutomationResolver.ResolverType.Method),
            runnable
        )
    }
}
