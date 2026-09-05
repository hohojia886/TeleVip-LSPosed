package com.my.televip.virtuals.messenger

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.SQLite.SQLiteDatabase
import de.robv.android.xposed.XposedHelpers

class MessagesStorage(@JvmField val messagesStorage: Any?) {

    fun getDatabase(): SQLiteDatabase {
        return SQLiteDatabase(
            XposedHelpers.callMethod(
                messagesStorage,
                AutomationResolver.resolve("MessagesStorage", "getDatabase", AutomationResolver.ResolverType.Method)
            )
        )
    }

    fun getStorageQueue(): DispatchQueue {
        return DispatchQueue(
            XposedHelpers.callMethod(
                messagesStorage,
                AutomationResolver.resolve("MessagesStorage", "getStorageQueue", AutomationResolver.ResolverType.Method)
            )
        )
    }

    companion object {
        @JvmStatic
        fun getInstance(num: Int): MessagesStorage {
            return MessagesStorage(
                XposedHelpers.callStaticMethod(
                    ClassLoad.getClass(ClassNames.MESSAGES_STORAGE),
                    AutomationResolver.resolve("MessagesStorage", "getInstance", AutomationResolver.ResolverType.Method),
                    num
                )
            )
        }
    }
}
