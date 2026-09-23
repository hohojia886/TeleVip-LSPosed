package com.my.televip.virtuals.messenger

import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

class DispatchQueue(@JvmField val dispatchQueue: Any?) {

    fun postRunnable(runnable: Runnable?) {
        val target = dispatchQueue ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("DispatchQueue", "postRunnable"), runnable)
    }
}
