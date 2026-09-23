package com.my.televip.virtuals.SQLite

import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.tgnet.NativeByteBuffer
import de.robv.android.xposed.XposedHelpers

class SQLiteCursor(@JvmField val sqLiteCursor: Any?) {

    fun next(): Boolean {
        val target = sqLiteCursor ?: return false
        return XposedHelpers.callMethod(target, Obfuscate.getMethodName("SQLiteCursor", "next")) as? Boolean ?: false
    }

    fun intValue(columnIndex: Int): Int {
        val target = sqLiteCursor ?: return 0
        return XposedHelpers.callMethod(target, Obfuscate.getMethodName("SQLiteCursor", "intValue"), columnIndex) as? Int ?: 0
    }

    fun longValue(columnIndex: Int): Long {
        val target = sqLiteCursor ?: return 0L
        return XposedHelpers.callMethod(target, Obfuscate.getMethodName("SQLiteCursor", "longValue"), columnIndex) as? Long ?: 0L
    }

    fun dispose() {
        val target = sqLiteCursor ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("SQLiteCursor", "dispose"))
    }

    fun byteBufferValue(columnIndex: Int): NativeByteBuffer {
        val target = sqLiteCursor ?: return NativeByteBuffer(null)
        val buffer = XposedHelpers.callMethod(target, Obfuscate.getMethodName("SQLiteCursor", "byteBufferValue"), columnIndex)
        return NativeByteBuffer(buffer)
    }
}
