package com.my.televip.virtuals.SQLite

import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.tgnet.NativeByteBuffer
import de.robv.android.xposed.XposedHelpers

class SQLiteCursor(@JvmField val sqLiteCursor: Any?) {

    fun next(): Boolean {
        return XposedHelpers.callMethod(
            sqLiteCursor,
            AutomationResolver.resolve("SQLiteCursor", "next", AutomationResolver.ResolverType.Method)
        ) as Boolean
    }

    fun byteBufferValue(columnIndex: Int): NativeByteBuffer {
        return NativeByteBuffer(
            XposedHelpers.callMethod(
                sqLiteCursor,
                AutomationResolver.resolve("SQLiteCursor", "byteBufferValue", AutomationResolver.ResolverType.Method),
                columnIndex
            )
        )
    }

    fun intValue(columnIndex: Int): Int {
        return XposedHelpers.callMethod(
            sqLiteCursor,
            AutomationResolver.resolve("SQLiteCursor", "intValue", AutomationResolver.ResolverType.Method),
            columnIndex
        ) as Int
    }

    fun longValue(columnIndex: Int): Long {
        return XposedHelpers.callMethod(
            sqLiteCursor,
            AutomationResolver.resolve("SQLiteCursor", "longValue", AutomationResolver.ResolverType.Method),
            columnIndex
        ) as Long
    }

    fun dispose() {
        XposedHelpers.callMethod(
            sqLiteCursor,
            AutomationResolver.resolve("SQLiteCursor", "dispose", AutomationResolver.ResolverType.Method)
        )
    }
}
