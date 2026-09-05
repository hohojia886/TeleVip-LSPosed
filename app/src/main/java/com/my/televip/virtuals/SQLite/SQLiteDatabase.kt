package com.my.televip.virtuals.SQLite

import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

class SQLiteDatabase(@JvmField val sqLiteDatabase: Any?) {

    fun queryFinalized(sql: String, vararg args: Any): SQLiteCursor {
        return SQLiteCursor(
            XposedHelpers.callMethod(
                sqLiteDatabase,
                AutomationResolver.resolve("SQLiteDatabase", "queryFinalized", AutomationResolver.ResolverType.Method),
                sql,
                args
            )
        )
    }

    fun executeFast(sql: String): SQLitePreparedStatement {
        return SQLitePreparedStatement(
            XposedHelpers.callMethod(
                sqLiteDatabase,
                AutomationResolver.resolve("SQLiteDatabase", "executeFast", AutomationResolver.ResolverType.Method),
                sql
            )
        )
    }
}
