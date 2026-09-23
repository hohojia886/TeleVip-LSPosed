package com.my.televip.virtuals.messenger

import android.content.SharedPreferences
import android.util.SparseArray
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.androidx.LongSparseArray
import com.my.televip.virtuals.tgnet.TLRPC
import de.robv.android.xposed.XposedHelpers

class MessagesController(@JvmField val messagesController: Any?) {

    fun processNewDifferenceParams(seq: Int, pts: Int, date: Int, pts_count: Int) {
        val target = messagesController ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("MessagesController", "processNewDifferenceParams"), seq, pts, date, pts_count)
    }

    fun processNewDifferenceParams(pts: Int, date: Int, pts_count: Int) {
        val target = messagesController ?: return
        XposedHelpers.callMethod(target, "processNewDifferenceParams", pts, date, pts_count)
    }

    fun removePromoDialog() {
        val target = messagesController ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("MessagesController", "removePromoDialog"))
    }

    @Suppress("UNCHECKED_CAST")
    val dialogMessagesByIds: SparseArray<Any>
        get() {
            val target = messagesController ?: return SparseArray()
            return XposedHelpers.getObjectField(target, Obfuscate.getFieldName("MessagesController", "dialogMessagesByIds")) as? SparseArray<Any> ?: SparseArray()
        }

    val dialogMessage: LongSparseArray
        get() {
            val target = messagesController ?: return LongSparseArray(null)
            val field = XposedHelpers.getObjectField(target, Obfuscate.getFieldName("MessagesController", "dialogMessage"))
            return LongSparseArray(field)
        }

    val messagesStorage: MessagesStorage
        get() {
            val target = messagesController ?: return MessagesStorage(null)
            val storageObj = XposedHelpers.callMethod(target, Obfuscate.getMethodName("MessagesController", "getMessagesStorage"))
            return MessagesStorage(storageObj)
        }

    companion object {
        @JvmStatic
        fun getInputChannel(peer: TLRPC.InputPeer): Any? {
            val mcClass = ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER) ?: return null
            return XposedHelpers.callStaticMethod(mcClass, Obfuscate.getMethodName("MessagesController", "getInputChannel"), peer.inputPeer)
        }

        @JvmStatic
        fun getInputChannel(id: Long): Any? {
            val mcClass = ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER) ?: return null
            return XposedHelpers.callStaticMethod(mcClass, Obfuscate.getMethodName("MessagesController", "getInputChannel"), id)
        }

        @JvmStatic
        fun getGlobalMainSettings(): SharedPreferences? {
            val mcClass = ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER) ?: return null
            return XposedHelpers.callStaticMethod(mcClass, Obfuscate.getMethodName("MessagesController", "getGlobalMainSettings")) as? SharedPreferences
        }

        @JvmStatic
        fun getInstance(num: Int): MessagesController {
            val mcClass = ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER)
            val instance = XposedHelpers.callStaticMethod(mcClass, Obfuscate.getMethodName("MessagesController", "getInstance"), num)
            return MessagesController(instance)
        }

        @JvmStatic
        fun getMessagesController(): MessagesController {
            return getInstance(UserConfig.getSelectedAccount())
        }
    }
}
