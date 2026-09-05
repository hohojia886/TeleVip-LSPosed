package com.my.televip.virtuals.messenger

import android.content.SharedPreferences
import android.util.SparseArray
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.androidx.LongSparseArray
import com.my.televip.virtuals.tgnet.TLRPC
import de.robv.android.xposed.XposedHelpers

class MessagesController(@JvmField val messagesController: Any?) {

    fun processNewDifferenceParams(seq: Int, pts: Int, date: Int, pts_count: Int) {
        XposedHelpers.callMethod(
            messagesController,
            AutomationResolver.resolve("MessagesController", "processNewDifferenceParams", AutomationResolver.ResolverType.Method),
            seq, pts, date, pts_count
        )
    }

    fun processNewDifferenceParams(pts: Int, date: Int, pts_count: Int) {
        XposedHelpers.callMethod(messagesController, "processNewDifferenceParams", pts, date, pts_count)
    }

    fun removePromoDialog() {
        XposedHelpers.callMethod(
            messagesController,
            AutomationResolver.resolve("MessagesController", "removePromoDialog", AutomationResolver.ResolverType.Method)
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun getDialogMessagesByIds(): SparseArray<Any> {
        return XposedHelpers.getObjectField(
            messagesController,
            AutomationResolver.resolve("MessagesController", "dialogMessagesByIds", AutomationResolver.ResolverType.Field)
        ) as SparseArray<Any>
    }

    fun getDialogMessage(): LongSparseArray {
        return LongSparseArray(
            XposedHelpers.getObjectField(
                messagesController,
                AutomationResolver.resolve("MessagesController", "dialogMessage", AutomationResolver.ResolverType.Field)
            )
        )
    }

    fun getMessagesStorage(): MessagesStorage {
        return MessagesStorage(
            XposedHelpers.callMethod(
                messagesController,
                AutomationResolver.resolve("MessagesController", "getMessagesStorage", AutomationResolver.ResolverType.Method)
            )
        )
    }

    companion object {
        @JvmStatic
        fun getInputChannel(peer: TLRPC.InputPeer): Any {
            return XposedHelpers.callStaticMethod(
                ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER),
                AutomationResolver.resolve("MessagesController", "getInputChannel", AutomationResolver.ResolverType.Method),
                peer.inputPeer
            )
        }

        @JvmStatic
        fun getInputChannel(id: Long): Any {
            return XposedHelpers.callStaticMethod(
                ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER),
                AutomationResolver.resolve("MessagesController", "getInputChannel", AutomationResolver.ResolverType.Method),
                id
            )
        }

        @JvmStatic
        fun getGlobalMainSettings(): SharedPreferences? {
            return XposedHelpers.callStaticMethod(
                ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER),
                AutomationResolver.resolve("MessagesController", "getGlobalMainSettings", AutomationResolver.ResolverType.Method)
            ) as? SharedPreferences
        }

        @JvmStatic
        fun getInstance(num: Int): MessagesController {
            return MessagesController(
                XposedHelpers.callStaticMethod(
                    ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER),
                    AutomationResolver.resolve("MessagesController", "getInstance", AutomationResolver.ResolverType.Method),
                    num
                )
            )
        }
    }
}
