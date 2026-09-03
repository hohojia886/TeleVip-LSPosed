package com.my.televip.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.my.televip.logging.Logger
import com.my.televip.messages.MessageStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class MessageDatabase(context: Context) : SQLiteOpenHelper(context, getDataBasePath(), null, 1) {

    companion object {
        private const val TABLE_MESSAGES = "messages"
        private const val COLUMN_ID = "id"
        private const val COLUMN_MSG_ID = "msg_id"
        private const val COLUMN_MESSAGE = "message"
        private const val COLUMN_MESSAGE_DATE = "message_date"
        private const val COLUMN_MSG_COUNT = "msg_count"

        private val dbScope = CoroutineScope(Dispatchers.IO)

        @JvmStatic
        fun getDataBasePath(): String {
            return File(MessageStorage.getStorageFile(), "saveMessages.db").absolutePath
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val tableCreate = ("CREATE TABLE " + TABLE_MESSAGES + " ("
                + COLUMN_ID + " LONG, "
                + COLUMN_MSG_ID + " INTEGER, "
                + COLUMN_MSG_COUNT + " INTEGER, "
                + COLUMN_MESSAGE + " TEXT, "
                + COLUMN_MESSAGE_DATE + " LONG "
                + ");")
        db.execSQL(tableCreate)
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_messages_id_msg ON $TABLE_MESSAGES($COLUMN_ID, $COLUMN_MSG_ID);")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MESSAGES")
        onCreate(db)
    }

    fun addMessage(id: Long, msgID: Int, message: String?) {
        if (message == null) return
        dbScope.launch {
            try {
                if (!searchMessage(id, msgID, message)) {
                    val values = ContentValues().apply {
                        put(COLUMN_ID, id)
                        put(COLUMN_MSG_ID, msgID)
                        put(COLUMN_MESSAGE, message)
                        val maxMsgCount = getMaxMessageCount(id, msgID)
                        put(COLUMN_MSG_COUNT, maxMsgCount + 1)
                        put(COLUMN_MESSAGE_DATE, System.currentTimeMillis())
                    }
                    writableDatabase.insert(TABLE_MESSAGES, null, values)
                }
            } catch (t: Throwable) {
                Logger.e(t)
            }
        }
    }

    fun searchMessage(id: Long, msgId: Int, message: String?): Boolean {
        if (message == null) return false
        return try {
            val query = ("SELECT * FROM $TABLE_MESSAGES WHERE "
                    + "$COLUMN_ID = ? AND $COLUMN_MSG_ID = ? AND $COLUMN_MESSAGE = ? LIMIT 1")
            readableDatabase.rawQuery(query, arrayOf(id.toString(), msgId.toString(), message)).use { cursor ->
                cursor.count > 0
            }
        } catch (t: Throwable) {
            Logger.e(t)
            false
        }
    }

    fun searchMessage(id: Long, msgId: Int): Boolean {
        return try {
            val query = ("SELECT * FROM $TABLE_MESSAGES WHERE "
                    + "$COLUMN_ID = ? AND $COLUMN_MSG_ID = ? LIMIT 1")
            readableDatabase.rawQuery(query, arrayOf(id.toString(), msgId.toString())).use { cursor ->
                cursor.count > 0
            }
        } catch (t: Throwable) {
            Logger.e(t)
            false
        }
    }

    fun getMessage(id: Long, msgId: Int): String? {
        return try {
            val query = ("SELECT $COLUMN_MESSAGE FROM $TABLE_MESSAGES WHERE "
                    + "$COLUMN_ID = ? AND $COLUMN_MSG_ID = ? LIMIT 1")
            readableDatabase.rawQuery(query, arrayOf(id.toString(), msgId.toString())).use { cursor ->
                if (cursor.moveToFirst()) cursor.getString(0) else null
            }
        } catch (t: Throwable) {
            Logger.e(t)
            null
        }
    }

    fun getMessage(id: Long, msgID: Int, msgCount: Int): String? {
        return try {
            val query = ("SELECT $COLUMN_MESSAGE FROM $TABLE_MESSAGES WHERE "
                    + "$COLUMN_ID = ? AND $COLUMN_MSG_ID = ? AND $COLUMN_MSG_COUNT = ? LIMIT 1")
            readableDatabase.rawQuery(query, arrayOf(id.toString(), msgID.toString(), msgCount.toString())).use { cursor ->
                if (cursor.moveToFirst()) cursor.getString(0) else null
            }
        } catch (t: Throwable) {
            Logger.e(t)
            null
        }
    }

    fun getMessageDate(id: Long, msgID: Int, msgCount: Int): Long {
        return try {
            val query = ("SELECT $COLUMN_MESSAGE_DATE FROM $TABLE_MESSAGES WHERE "
                    + "$COLUMN_ID = ? AND $COLUMN_MSG_ID = ? AND $COLUMN_MSG_COUNT = ? LIMIT 1")
            readableDatabase.rawQuery(query, arrayOf(id.toString(), msgID.toString(), msgCount.toString())).use { cursor ->
                if (cursor.moveToFirst()) cursor.getLong(0) else 0L
            }
        } catch (_: Throwable) {
            0L
        }
    }

    fun getMaxMessageCount(id: Long, msgID: Int): Int {
        return try {
            val query = ("SELECT MAX($COLUMN_MSG_COUNT) FROM $TABLE_MESSAGES WHERE "
                    + "$COLUMN_ID = ? AND $COLUMN_MSG_ID = ?")
            readableDatabase.rawQuery(query, arrayOf(id.toString(), msgID.toString())).use { cursor ->
                if (cursor.moveToFirst()) cursor.getInt(0) else 0
            }
        } catch (t: Throwable) {
            Logger.e(t)
            0
        }
    }
}
