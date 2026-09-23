package com.my.televip.virtuals.androidx

import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers
import java.util.ArrayList

class LongSparseArray(@JvmField val longSparseArray: Any?) {

    @Suppress("UNCHECKED_CAST")
    fun get(key: Long): ArrayList<Any>? {
        val target = longSparseArray ?: return null
        return XposedHelpers.callMethod(target, Obfuscate.getMethodName("LongSparseArray", "get"), key) as? ArrayList<Any>
    }
}
