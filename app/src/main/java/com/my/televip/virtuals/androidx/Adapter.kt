package com.my.televip.virtuals.androidx

import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

class Adapter(@JvmField val adapter: Any?) {

    fun notifyDataSetChanged() {
        val target = adapter ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("Adapter", "notifyDataSetChanged"))
    }
}
