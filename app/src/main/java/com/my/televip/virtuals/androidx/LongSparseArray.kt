package com.my.televip.virtuals.androidx

import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.utils.Utils
import de.robv.android.xposed.XposedHelpers

class LongSparseArray(@JvmField val longSparseArray: Any?) {
    fun get(id: Long): ArrayList<Any>? {
        val raw = XposedHelpers.callMethod(
            longSparseArray,
            AutomationResolver.resolve("LongSparseArray", "get", AutomationResolver.ResolverType.Method),
            id
        )
        return Utils.castList(raw, Any::class.java)
    }
}
