package com.my.televip.virtuals.SQLite

import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.tgnet.NativeByteBuffer
import de.robv.android.xposed.XposedHelpers

class SQLitePreparedStatement(@JvmField val sqLitePreparedStatement: Any?) {

    fun bindByteBuffer(index: Int, buffer: NativeByteBuffer) {
        XposedHelpers.callMethod(
            sqLitePreparedStatement,
            AutomationResolver.resolve("SQLitePreparedStatement", "bindByteBuffer", AutomationResolver.ResolverType.Method),
            index,
            buffer.nativeByteBuffer
        )
    }

    fun bindLong(index: Int, value: Long) {
        XposedHelpers.callMethod(
            sqLitePreparedStatement,
            AutomationResolver.resolve("SQLitePreparedStatement", "bindLong", AutomationResolver.ResolverType.Method),
            index,
            value
        )
    }

    fun bindInteger(index: Int, value: Int) {
        XposedHelpers.callMethod(
            sqLitePreparedStatement,
            AutomationResolver.resolve("SQLitePreparedStatement", "bindInteger", AutomationResolver.ResolverType.Method),
            index,
            value
        )
    }

    fun step(): Int {
        return XposedHelpers.callMethod(
            sqLitePreparedStatement,
            AutomationResolver.resolve("SQLitePreparedStatement", "step", AutomationResolver.ResolverType.Method)
        ) as Int
    }

    fun requery() {
        XposedHelpers.callMethod(
            sqLitePreparedStatement,
            AutomationResolver.resolve("SQLitePreparedStatement", "requery", AutomationResolver.ResolverType.Method)
        )
    }

    fun dispose() {
        XposedHelpers.callMethod(
            sqLitePreparedStatement,
            AutomationResolver.resolve("SQLitePreparedStatement", "dispose", AutomationResolver.ResolverType.Method)
        )
    }
}
