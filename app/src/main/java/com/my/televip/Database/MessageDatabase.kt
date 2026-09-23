package com.my.televip.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.my.televip.logging.Logger
import com.my.televip.messages.MessageStorage
import java.io.File
import java.util.Calendar

class MessageDatabase(context: Context?) : SQLiteOpenHelper(context, dataBasePath, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val tableCreate = ("CREATE TABLE " + TABLE_MESSAGES + " (" +
                COLUMN_ID + " LONG, " +
                COLUMN_MSG_ID + " INTEGER, " +
                COLUMN_MSG_COUNT + " INTEGER, " +
                COLUMN_MESSAGE + " TEXT, " +
                COLUMN_MESSAGE_DATE + " LONG " +
                ");")
        db.execSQL(tableCreate)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MESSAGES")
        onCreate(db)
    }

    fun addMessage(id: Long, msgID: Int, message: String?) {
        try {
            MessageStorage.storage.post {
                val database = writableDatabase
                if (!searchMessage(id, msgID, message)) {
                    val values = ContentValues().apply {
                        put(COLUMN_ID, id)
                        put(COLUMN_MSG_ID, msgID)
                        put(COLUMN_MESSAGE, message)
                        val maxMsgCount = getMaxMessageCount(id, msgID)
                        put(COLUMN_MSG_COUNT, maxMsgCount + 1)
                        put(COLUMN_MESSAGE_DATE, Calendar.getInstance().timeInMillis)
                    }
                    database.insert(TABLE_MESSAGES, null, values)
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }

    fun searchMessage(id: Long, msgId: Int, message: String?): Boolean {
        if (message == null) return false
        try {
            val database = readableDatabase
            val query = ("SELECT * FROM " + TABLE_MESSAGES +
                    " WHERE " + COLUMN_ID + " = ? AND " +
                    COLUMN_MSG_ID + " = ? AND " +
                    COLUMN_MESSAGE + " = ? LIMIT 1")
            var cursor: Cursor? = null
            return try {
                cursor = database.rawQuery(query, arrayOf(id.toString(), msgId.toString(), message))
                cursor.count > 0
            } finally {
                cursor?.close()
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
        return false
    }

    fun searchMessage(id: Long, msgId: Int): Boolean {
        try {
            val database = readableDatabase
            val query = ("SELECT * FROM " + TABLE_MESSAGES +
                    " WHERE " + COLUMN_ID + " = ? AND " +
                    COLUMN_MSG_ID + " = ? LIMIT 1")
            var cursor: Cursor? = null
            return try {
                cursor = database.rawQuery(query, arrayOf(id.toString(), msgId.toString()))
                cursor.count > 0
            } finally {
                cursor?.close()
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
        return false
    }

    fun getMessageEntity(id: Long, msgId: Int): MessageEntity? {
        try {
            val database = readableDatabase
            val query = ("SELECT * FROM " + TABLE_MESSAGES +
                    " WHERE " + COLUMN_ID + " = ? AND " + COLUMN_MSG_ID + " = ? LIMIT 1")
            var cursor: Cursor? = null
            return try {
                cursor = database.rawQuery(query, arrayOf(id.toString(), msgId.toString()))
                if (cursor.moveToFirst()) {
                    MessageEntity(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MSG_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MSG_COUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_DATE))
                    )
                } else {
                    null
                }
            } finally {
                cursor?.close()
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
        return null
    }

    fun getMessage(id: Long, msgId: Int): String? {
        val entity = getMessageEntity(id, msgId)
        return entity?.message
    }

    fun getMessageEntity(id: Long, msgID: Int, msgCount: Int): MessageEntity? {
        try {
            val database = readableDatabase
            val query = ("SELECT * FROM " + TABLE_MESSAGES +
                    " WHERE " + COLUMN_ID + " = ? AND " + COLUMN_MSG_ID + " = ? AND " + COLUMN_MSG_COUNT + " = ? LIMIT 1")
            var cursor: Cursor? = null
            return try {
                cursor = database.rawQuery(query, arrayOf(id.toString(), msgID.toString(), msgCount.toString()))
                if (cursor.moveToFirst()) {
                    MessageEntity(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MSG_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MSG_COUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_DATE))
                    )
                } else {
                    null
                }
            } finally {
                cursor?.close()
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
        return null
    }

    fun getMessage(id: Long, msgID: Int, msgCount: Int): String? {
        val entity = getMessageEntity(id, msgID, msgCount)
        return entity?.message
    }

    fun getMessageDate(id: Long, msgID: Int, msgCount: Int): Long {
        val entity = getMessageEntity(id, msgID, msgCount)
        return entity?.messageDate ?: 0L
    }

    fun getMaxMessageCount(id: Long, msgID: Int): Int {
        var maxCount = 0
        try {
            val database = readableDatabase
            val query = ("SELECT MAX(" + COLUMN_MSG_COUNT + ") FROM " + TABLE_MESSAGES +
                    " WHERE " + COLUMN_ID + " = ? AND " + COLUMN_MSG_ID + " = ?")
            var cursor: Cursor? = null
            try {
                cursor = database.rawQuery(query, arrayOf(id.toString(), msgID.toString()))
                if (cursor.moveToFirst()) {
                    maxCount = cursor.getInt(0)
                }
            } finally {
                cursor?.close()
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
        return maxCount
    }

    companion object {
        private const val TABLE_MESSAGES = "messages"
        private const val COLUMN_ID = "id"
        private const val COLUMN_MSG_ID = "msg_id"
        private const val COLUMN_MESSAGE = "message"
        private const val COLUMN_MESSAGE_DATE = "message_date"
        private const val COLUMN_MSG_COUNT = "msg_count"

        @JvmStatic
        val dataBasePath: String
            get() = File(MessageStorage.getStorageFile(), "saveMessages.db").absolutePath
    }
}
