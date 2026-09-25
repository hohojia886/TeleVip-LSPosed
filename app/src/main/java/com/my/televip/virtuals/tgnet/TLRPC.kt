package com.my.televip.virtuals.tgnet

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers
import java.util.ArrayList

class TLRPC {

    class Peer(@JvmField val peer: Any?) {
        val user_id: Long
            get() = XposedHelpers.getLongField(peer, Obfuscate.getFieldName("TLRPC\$Peer", "user_id"))

        val chat_id: Long
            get() = XposedHelpers.getLongField(peer, Obfuscate.getFieldName("TLRPC\$Peer", "chat_id"))

        val channel_id: Long
            get() = XposedHelpers.getLongField(peer, Obfuscate.getFieldName("TLRPC\$Peer", "channel_id"))
    }

    class User(@JvmField var user: Any?) {
        var phone: String?
            get() = XposedHelpers.getObjectField(user, Obfuscate.getFieldName("TLRPC\$User", "phone")) as? String
            set(value) {
                XposedHelpers.setObjectField(user, Obfuscate.getFieldName("TLRPC\$User", "phone"), value)
            }
    }

    class Message {
        private val message: Any?
        private var idInternal: Int = 0

        constructor(message: Any?) {
            this.message = message
        }

        constructor() {
            this.message = XposedHelpers.newInstance(ClassLoad.getClass(ClassNames.MESSAGE))
        }

        val id: Int
            get() {
                if (idInternal == 0) {
                    idInternal = XposedHelpers.getIntField(message, Obfuscate.getFieldName("TLRPC\$Message", "id"))
                }
                return idInternal
            }

        val messageText: String?
            get() = XposedHelpers.getObjectField(message, Obfuscate.getFieldName("TLRPC\$Message", "message")) as? String

        fun getMessage(): String? = messageText

        val from_id: Peer
            get() = Peer(XposedHelpers.getObjectField(message, Obfuscate.getFieldName("TLRPC\$Message", "from_id")))

        var flags: Int
            get() = XposedHelpers.getIntField(message, Obfuscate.getFieldName("TLRPC\$Message", "flags"))
            set(value) {
                XposedHelpers.setIntField(message, Obfuscate.getFieldName("TLRPC\$Message", "flags"), value)
            }

        val ttl: Int
            get() = XposedHelpers.getIntField(message, Obfuscate.getFieldName("TLRPC\$Message", "ttl"))

        fun get_Message(): Any? = message

        fun setId(id: Int) {
            XposedHelpers.setIntField(message, Obfuscate.getFieldName("TLRPC\$Message", "id"), id)
            this.idInternal = id
        }

        fun setMessage(msg: String?) {
            XposedHelpers.setObjectField(message, Obfuscate.getFieldName("TLRPC\$Message", "message"), msg)
        }

        fun setTtl(ttl: Any?) {
            XposedHelpers.setObjectField(message, Obfuscate.getFieldName("TLRPC\$Message", "ttl"), ttl)
        }

        fun readAttachPath(stream: NativeByteBuffer, currentUserId: Long) {
            XposedHelpers.callMethod(
                message,
                Obfuscate.getMethodName("TLRPC\$Message", "readAttachPath"),
                stream.nativeByteBuffer,
                currentUserId
            )
        }

        companion object {
            @JvmStatic
            fun TLdeserialize(stream: NativeByteBuffer, constructor: Int, exception: Boolean): Message {
                val msgClass = ClassLoad.getClass(ClassNames.MESSAGE)
                val deserialized = XposedHelpers.callStaticMethod(
                    msgClass,
                    Obfuscate.getMethodName("TLRPC\$Message", "TLdeserialize"),
                    stream.nativeByteBuffer,
                    constructor,
                    exception
                )
                return Message(deserialized)
            }
        }
    }

    class InputPeer(@JvmField val inputPeer: Any?) {
        val user_id: Long
            get() = XposedHelpers.getLongField(inputPeer, Obfuscate.getFieldName("TLRPC\$InputPeer", "user_id"))

        val chat_id: Long
            get() = XposedHelpers.getLongField(inputPeer, Obfuscate.getFieldName("TLRPC\$InputPeer", "chat_id"))

        val channel_id: Long
            get() = XposedHelpers.getLongField(inputPeer, Obfuscate.getFieldName("TLRPC\$InputPeer", "channel_id"))
    }

    class messages_Messages(val messages_Messages: Any?) {
        @Suppress("UNCHECKED_CAST")
        val messages: ArrayList<Any>
            get() = XposedHelpers.getObjectField(
                messages_Messages,
                Obfuscate.getFieldName("TLRPC\$messages_Messages", "messages")
            ) as? ArrayList<Any> ?: ArrayList()
    }

    class TL_updateDeleteChannelMessages(val instance: Any?) {
        val channelID: Long
            get() = XposedHelpers.getLongField(
                instance,
                Obfuscate.getFieldName("TL_update\$TL_updateDeleteChannelMessages", "channel_id")
            )

        @Suppress("UNCHECKED_CAST")
        val messages: ArrayList<Int>
            get() = XposedHelpers.getObjectField(
                instance,
                Obfuscate.getFieldName("TL_update\$TL_updateDeleteChannelMessages", "messages")
            ) as? ArrayList<Int> ?: ArrayList()
    }

    class TL_updateDeleteMessages(val instance: Any?) {
        @Suppress("UNCHECKED_CAST")
        val messages: ArrayList<Int>
            get() = XposedHelpers.getObjectField(
                instance,
                Obfuscate.getFieldName("TL_update\$TL_updateDeleteMessages", "messages")
            ) as? ArrayList<Int> ?: ArrayList()
    }

    class TL_messages_affectedMessages {
        @JvmField
        val instance: Any?

        constructor() {
            instance = XposedHelpers.newInstance(ClassLoad.getClass(ClassNames.TL_MESSAGES_AFFECTED))
        }

        constructor(instance: Any?) {
            this.instance = instance
        }

        val pts: Int
            get() = XposedHelpers.getIntField(instance, Obfuscate.getFieldName("TLRPC\$TL_messages_affectedMessages", "pts"))

        val ptsCount: Int
            get() = XposedHelpers.getIntField(instance, Obfuscate.getFieldName("TLRPC\$TL_messages_affectedMessages", "pts_count"))

        fun setPts(pts: Int) {
            XposedHelpers.setIntField(instance, Obfuscate.getFieldName("TLRPC\$TL_messages_affectedMessages", "pts"), pts)
        }

        fun setPtsCount(pts_count: Int) {
            XposedHelpers.setIntField(instance, Obfuscate.getFieldName("TLRPC\$TL_messages_affectedMessages", "pts_count"), pts_count)
        }

        val tL_messages_affectedMessages: Any?
            get() = instance
    }

    class TL_channels_readHistory {
        @JvmField
        val instance: Any?

        constructor() {
            instance = XposedHelpers.newInstance(ClassLoad.getClass(ClassNames.TL_CHANNELS_READ_HISTORY))
        }

        constructor(instance: Any?) {
            this.instance = instance
        }

        fun setChannel(channel: Any?) {
            XposedHelpers.setObjectField(instance, Obfuscate.getFieldName("TLRPC\$TL_channels_readHistory", "channel"), channel)
        }

        fun setMax_id(max_id: Int) {
            XposedHelpers.setIntField(instance, Obfuscate.getFieldName("TLRPC\$TL_channels_readHistory", "max_id"), max_id)
        }

        val tL_channels_readHistory: Any?
            get() = instance
    }

    class TL_messages_readHistory {
        @JvmField
        val instance: Any?

        constructor() {
            instance = XposedHelpers.newInstance(ClassLoad.getClass(ClassNames.TL_MESSAGES_READ_HISTORY))
        }

        constructor(instance: Any?) {
            this.instance = instance
        }

        fun setPeer(peer: InputPeer) {
            XposedHelpers.setObjectField(instance, Obfuscate.getFieldName("TLRPC\$TL_messages_readHistory", "peer"), peer.inputPeer)
        }

        fun setMax_id(max_id: Int) {
            XposedHelpers.setIntField(instance, Obfuscate.getFieldName("TLRPC\$TL_messages_readHistory", "max_id"), max_id)
        }

        val tL_messages_readHistory: Any?
            get() = instance
    }
}
