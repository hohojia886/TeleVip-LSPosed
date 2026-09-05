package com.my.televip.virtuals.tgnet

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.utils.Utils
import de.robv.android.xposed.XposedHelpers

class TLRPC {

    class Peer(@JvmField val peer: Any?) {
        fun getUser_id(): Long {
            return XposedHelpers.getLongField(
                peer,
                AutomationResolver.resolve("TLRPC\$Peer", "user_id", AutomationResolver.ResolverType.Field)
            )
        }

        fun getChat_id(): Long {
            return XposedHelpers.getLongField(
                peer,
                AutomationResolver.resolve("TLRPC\$Peer", "chat_id", AutomationResolver.ResolverType.Field)
            )
        }

        fun getChannel_id(): Long {
            return XposedHelpers.getLongField(
                peer,
                AutomationResolver.resolve("TLRPC\$Peer", "channel_id", AutomationResolver.ResolverType.Field)
            )
        }
    }

    class User(@JvmField val user: Any?) {
        fun getPhone(): String? {
            return XposedHelpers.getObjectField(
                user,
                AutomationResolver.resolve("TLRPC\$User", "phone", AutomationResolver.ResolverType.Field)
            ) as? String
        }

        fun setPhone(phone: String?) {
            XposedHelpers.setObjectField(
                user,
                AutomationResolver.resolve("TLRPC\$User", "phone", AutomationResolver.ResolverType.Field),
                phone
            )
        }
    }

    class Message(@JvmField val message: Any?) {
        private var cachedId = 0

        fun getID(): Int {
            if (cachedId == 0) {
                cachedId = XposedHelpers.getIntField(
                    message,
                    AutomationResolver.resolve("TLRPC\$Message", "id", AutomationResolver.ResolverType.Field)
                )
            }
            return cachedId
        }

        fun getMessage(): String? {
            return XposedHelpers.getObjectField(
                message,
                AutomationResolver.resolve("TLRPC\$Message", "message", AutomationResolver.ResolverType.Field)
            ) as? String
        }

        fun getFrom_id(): Peer {
            return Peer(
                XposedHelpers.getObjectField(
                    message,
                    AutomationResolver.resolve("TLRPC\$Message", "from_id", AutomationResolver.ResolverType.Field)
                )
            )
        }

        fun getFlags(): Int {
            return XposedHelpers.getIntField(
                message,
                AutomationResolver.resolve("TLRPC\$Message", "flags", AutomationResolver.ResolverType.Field)
            )
        }

        fun setFlags(flags: Int) {
            XposedHelpers.setIntField(
                message,
                AutomationResolver.resolve("TLRPC\$Message", "flags", AutomationResolver.ResolverType.Field),
                flags
            )
        }

        fun getTtl(): Int {
            return XposedHelpers.getIntField(
                message,
                AutomationResolver.resolve("TLRPC\$Message", "ttl", AutomationResolver.ResolverType.Field)
            )
        }

        fun setTtl(ttl: Any?) {
            XposedHelpers.setObjectField(
                message,
                AutomationResolver.resolve("TLRPC\$Message", "ttl", AutomationResolver.ResolverType.Field),
                ttl
            )
        }

        fun readAttachPath(stream: NativeByteBuffer, currentUserId: Long) {
            XposedHelpers.callMethod(
                message,
                AutomationResolver.resolve("TLRPC\$Message", "readAttachPath", AutomationResolver.ResolverType.Method),
                stream.nativeByteBuffer,
                currentUserId
            )
        }

        companion object {
            @JvmStatic
            fun TLdeserialize(stream: NativeByteBuffer, constructor: Int, exception: Boolean): Message {
                return Message(
                    XposedHelpers.callStaticMethod(
                        ClassLoad.getClass(ClassNames.TL_MESSAGE),
                        AutomationResolver.resolve("TLRPC\$Message", "TLdeserialize", AutomationResolver.ResolverType.Method),
                        stream.nativeByteBuffer,
                        constructor,
                        exception
                    )
                )
            }
        }
    }

    class InputPeer(@JvmField val inputPeer: Any?) {
        fun getUser_id(): Long {
            return XposedHelpers.getLongField(
                inputPeer,
                AutomationResolver.resolve("TLRPC\$InputPeer", "user_id", AutomationResolver.ResolverType.Field)
            )
        }

        fun getChat_id(): Long {
            return XposedHelpers.getLongField(
                inputPeer,
                AutomationResolver.resolve("TLRPC\$InputPeer", "chat_id", AutomationResolver.ResolverType.Field)
            )
        }

        fun getChannel_id(): Long {
            return XposedHelpers.getLongField(
                inputPeer,
                AutomationResolver.resolve("TLRPC\$InputPeer", "channel_id", AutomationResolver.ResolverType.Field)
            )
        }
    }

    class messages_Messages(@JvmField val messages_Messages: Any?) {
        @Suppress("UNCHECKED_CAST")
        fun getMessages(): ArrayList<Any> {
            return XposedHelpers.getObjectField(
                messages_Messages,
                AutomationResolver.resolve("TLRPC\$messages_Messages", "messages", AutomationResolver.ResolverType.Field)
            ) as ArrayList<Any>
        }
    }

    class TL_updateDeleteChannelMessages(@JvmField val instance: Any?) {
        fun getChannelID(): Long {
            return try {
                XposedHelpers.getLongField(
                    instance,
                    AutomationResolver.resolve("TL_update\$TL_updateDeleteChannelMessages", "channel_id", AutomationResolver.ResolverType.Field)
                )
            } catch (e: Throwable) {
                Logger.e(e)
                Long.MIN_VALUE
            }
        }

        fun getMessages(): ArrayList<Int>? {
            return try {
                Utils.castList(
                    XposedHelpers.getObjectField(
                        instance,
                        AutomationResolver.resolve("TL_update\$TL_updateDeleteChannelMessages", "messages", AutomationResolver.ResolverType.Field)
                    ),
                    Int::class.javaObjectType
                )
            } catch (e: Throwable) {
                Logger.e(e)
                null
            }
        }
    }

    class TL_updateDeleteMessages(@JvmField val instance: Any?) {
        fun getMessages(): ArrayList<Int>? {
            return try {
                Utils.castList(
                    XposedHelpers.getObjectField(
                        instance,
                        AutomationResolver.resolve("TL_update\$TL_updateDeleteMessages", "messages", AutomationResolver.ResolverType.Field)
                    ),
                    Int::class.javaObjectType
                )
            } catch (e: Throwable) {
                Logger.e(e)
                null
            }
        }
    }

    class TL_messages_affectedMessages {
        @JvmField val instance: Any?

        constructor() {
            instance = XposedHelpers.newInstance(ClassLoad.getClass(ClassNames.TL_MESSAGES_AFFECTED))
        }

        constructor(instance: Any?) {
            this.instance = instance
        }

        fun getPts(): Int {
            return XposedHelpers.getIntField(
                instance,
                AutomationResolver.resolve("TLRPC\$TL_messages_affectedMessages", "pts", AutomationResolver.ResolverType.Field)
            )
        }

        fun getPtsCount(): Int {
            return XposedHelpers.getIntField(
                instance,
                AutomationResolver.resolve("TLRPC\$TL_messages_affectedMessages", "pts_count", AutomationResolver.ResolverType.Field)
            )
        }

        fun setPts(pts: Int) {
            XposedHelpers.setIntField(
                instance,
                AutomationResolver.resolve("TLRPC\$TL_messages_affectedMessages", "pts", AutomationResolver.ResolverType.Field),
                pts
            )
        }

        fun setPtsCount(pts_count: Int) {
            XposedHelpers.setIntField(
                instance,
                AutomationResolver.resolve("TLRPC\$TL_messages_affectedMessages", "pts_count", AutomationResolver.ResolverType.Field),
                pts_count
            )
        }

        fun getTL_messages_affectedMessages(): Any? = instance
    }

    class TL_channels_readHistory {
        @JvmField val instance: Any?

        constructor() {
            instance = XposedHelpers.newInstance(ClassLoad.getClass(ClassNames.TL_CHANNELS_READ_HISTORY))
        }

        constructor(instance: Any?) {
            this.instance = instance
        }

        fun setChannel(channel: Any?) {
            XposedHelpers.setObjectField(
                instance,
                AutomationResolver.resolve("TLRPC\$TL_channels_readHistory", "channel", AutomationResolver.ResolverType.Field),
                channel
            )
        }

        fun setMax_id(max_id: Int) {
            XposedHelpers.setIntField(
                instance,
                AutomationResolver.resolve("TLRPC\$TL_channels_readHistory", "max_id", AutomationResolver.ResolverType.Field),
                max_id
            )
        }

        fun getTL_channels_readHistory(): Any? = instance
    }

    class TL_messages_readHistory {
        @JvmField val instance: Any?

        constructor() {
            instance = XposedHelpers.newInstance(ClassLoad.getClass(ClassNames.TL_MESSAGES_READ_HISTORY))
        }

        constructor(instance: Any?) {
            this.instance = instance
        }

        fun setPeer(peer: InputPeer) {
            XposedHelpers.setObjectField(
                instance,
                AutomationResolver.resolve("TLRPC\$TL_messages_readHistory", "peer", AutomationResolver.ResolverType.Field),
                peer.inputPeer
            )
        }

        fun setMax_id(max_id: Int) {
            XposedHelpers.setIntField(
                instance,
                AutomationResolver.resolve("TLRPC\$TL_messages_readHistory", "max_id", AutomationResolver.ResolverType.Field),
                max_id
            )
        }

        fun getTL_messages_readHistory(): Any? = instance
    }
}
