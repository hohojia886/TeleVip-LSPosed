package com.my.televip.virtuals.SQLite

import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.tgnet.NativeByteBuffer
import de.robv.android.xposed.XposedHelpers

class SQLitePreparedStatement(@JvmField val sqLitePreparedStatement: Any?) {

    fun requery() {
        val target = sqLitePreparedStatement ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("SQLitePreparedStatement", "requery"))
    }

    fun bindByteBuffer(index: Int, data: NativeByteBuffer) {
        val target = sqLitePreparedStatement ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("SQLitePreparedStatement", "bindByteBuffer"), index, data.nativeByteBuffer)
    }

    fun bindLong(index: Int, value: Long) {
        val target = sqLitePreparedStatement ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("SQLitePreparedStatement", "bindLong"), index, value)
    }

    fun bindInteger(index: Int, value: Int) {
        val target = sqLitePreparedStatement ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("SQLitePreparedStatement", "bindInteger"), index, value)
    }

    fun step() {
        val target = sqLitePreparedStatement ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("SQLitePreparedStatement", "step"))
    }
}
