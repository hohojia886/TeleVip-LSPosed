package com.my.televip.virtuals.SQLite

import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

class SQLiteDatabase(@JvmField val sqLiteDatabase: Any?) {

    fun queryFinalized(s: String?, objects: Array<Any?>): SQLiteCursor {
        val target = sqLiteDatabase ?: return SQLiteCursor(null)
        val result = XposedHelpers.callMethod(target, Obfuscate.getMethodName("SQLiteDatabase", "queryFinalized"), s, objects)
        return SQLiteCursor(result)
    }

    fun executeFast(s: String?): SQLitePreparedStatement {
        val target = sqLiteDatabase ?: return SQLitePreparedStatement(null)
        val result = XposedHelpers.callMethod(target, Obfuscate.getMethodName("SQLiteDatabase", "executeFast"), s)
        return SQLitePreparedStatement(result)
    }
}
