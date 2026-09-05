package com.my.televip.messages

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Process
import android.text.TextUtils
import com.my.televip.application.ApplicationLoaderHook
import com.my.televip.features.ShowDeletedMessages
import com.my.televip.logging.Logger
import com.my.televip.virtuals.messenger.MessagesStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

object MessageStorage {

    private val storageThreadLooper: Looper by lazy {
        makeLooper("Storage")
    }

    @JvmStatic
    val storage: Handler by lazy {
        Handler(storageThreadLooper)
    }

    val coroutineScope = CoroutineScope(Dispatchers.IO)

    @JvmStatic
    fun getStorageFile(): File {
        val dir = File(
            ApplicationLoaderHook.getApplicationContext().filesDir.parentFile,
            "TeleVip"
        )
        if (!dir.exists() && !dir.mkdir()) {
            Logger.w("Cannot create ${dir.absolutePath}")
        }
        return dir
    }

    @JvmStatic
    fun makeLooper(str: String): Looper {
        val handlerThread = HandlerThread("TeleVip - $str", Process.THREAD_PRIORITY_DISPLAY)
        handlerThread.start()
        return handlerThread.looper
    }

    @JvmStatic
    fun markMessagesDeleted(messagesStorage: MessagesStorage, dialogId: Long, delMsg: ArrayList<Int>) {
        coroutineScope.launch {
            try {
                val db = messagesStorage.getDatabase()
                for (i in 0..1) {
                    val table = if (i == 0) "messages_v2" else "messages_topics"
                    val query = ("SELECT data,mid,uid FROM " + table + " WHERE "
                            + (if (dialogId == 0L) "is_channel" else "uid") + " = " + dialogId + " AND mid IN (" + TextUtils.join(",", delMsg) + ");")
                    val update = "UPDATE $table SET data = ? WHERE uid = ? AND mid = ?"

                    val cursor = db.queryFinalized(query, emptyArray<Any>())
                    val state = db.executeFast(update)

                    try {
                        while (cursor.next()) {
                            val data = cursor.byteBufferValue(0)
                            val mid = cursor.intValue(1)
                            val lastDialogId = cursor.longValue(2)

                            try {
                                data.position(4)
                                var flags = data.readInt32(true)
                                flags = flags or ShowDeletedMessages.FLAG_DELETED
                                data.position(4)
                                data.writeInt32(flags)
                                data.position(0)

                                state.requery()
                                state.bindByteBuffer(1, data)
                                state.bindLong(2, lastDialogId)
                                state.bindInteger(3, mid)
                                state.step()
                            } finally {
                                data.reuse()
                            }
                        }
                    } finally {
                        cursor.dispose()
                        state.step()
                    }
                }
            } catch (e: Throwable) {
                Logger.e(e)
            }
        }
    }
}
