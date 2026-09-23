package com.my.televip.virtuals.messenger

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.SQLite.SQLiteDatabase
import de.robv.android.xposed.XposedHelpers

class MessagesStorage(@JvmField val messagesStorage: Any?) {

    val database: SQLiteDatabase
        get() {
            val dbObj = XposedHelpers.callMethod(messagesStorage, Obfuscate.getMethodName("MessagesStorage", "getDatabase"))
            return SQLiteDatabase(dbObj)
        }

    val storageQueue: DispatchQueue
        get() {
            val queueObj = XposedHelpers.callMethod(messagesStorage, Obfuscate.getMethodName("MessagesStorage", "getStorageQueue"))
            return DispatchQueue(queueObj)
        }

    companion object {
        @JvmStatic
        fun getInstance(num: Int): MessagesStorage {
            val msClass = ClassLoad.getClass(ClassNames.MESSAGES_STORAGE)
            val instance = XposedHelpers.callStaticMethod(msClass, Obfuscate.getMethodName("MessagesStorage", "getInstance"), num)
            return MessagesStorage(instance)
        }

        @JvmStatic
        fun getMessagesStorage(): MessagesStorage {
            return getInstance(UserConfig.getSelectedAccount())
        }
    }
}
