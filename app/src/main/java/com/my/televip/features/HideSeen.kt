package com.my.televip.features

import com.my.televip.Callback.IntCallback
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.ClientChecker
import com.my.televip.Configs.ConfigManager
import com.my.televip.application.AndroidUtilities
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.messenger.MessagesController
import com.my.televip.virtuals.messenger.MessagesStorage
import com.my.televip.virtuals.messenger.UserConfig
import com.my.televip.virtuals.messenger.Utilities
import com.my.televip.virtuals.tgnet.ConnectionsManager
import com.my.televip.virtuals.tgnet.RequestDelegate
import com.my.televip.virtuals.tgnet.TLRPC
import de.robv.android.xposed.XposedHelpers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

object HideSeen {

    @JvmField
    var TLChannels_readHistory: Any? = null

    @JvmField
    var TLMessages_readHistory: Any? = null

    @JvmField
    val isReadMessages: AtomicBoolean = AtomicBoolean(false)

    private val bgScope = CoroutineScope(Dispatchers.IO)

    @JvmStatic
    fun sendFakeReadResponse(onCompleteOrig: Any?) {
        try {
            val fakeRes = TLRPC.TL_messages_affectedMessages()
            fakeRes.setPts(-1)
            fakeRes.setPtsCount(0)
            val onComplete = RequestDelegate(onCompleteOrig)
            Utilities.getStageQueue().postRunnable {
                try {
                    if (onComplete.requestDelegateObj != null) {
                        onComplete.run(fakeRes.getTL_messages_affectedMessages(), null)
                    }
                } catch (e: Throwable) {
                    Logger.e(e)
                }
            }
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    @JvmStatic
    fun isTLMessagesReadHistoryRequest(obj: Any): Boolean {
        return if (!ClientChecker.isTgnetObfuscated()) {
            obj.javaClass.name.contains("TL_messages_readHistory")
        } else {
            obj.javaClass.name == AutomationResolver.resolve(ClassNames.TL_MESSAGES_READ_HISTORY)
        }
    }

    @JvmStatic
    fun isTLChannelsReadHistoryRequest(obj: Any): Boolean {
        return if (!ClientChecker.isTgnetObfuscated()) {
            obj.javaClass.name.contains("TL_channels_readHistory")
        } else {
            obj.javaClass.name == AutomationResolver.resolve(ClassNames.TL_CHANNELS_READ_HISTORY)
        }
    }

    @JvmStatic
    fun isReadMessageRequest(obj: Any): Boolean {
        val privateHide = ConfigManager.hideSeenPrivateChat.isEnable
        val channelHide = ConfigManager.hideSeenChannel.isEnable

        val readHistory: Boolean
        val readDiscussion: Boolean
        val encryptedHistory: Boolean
        val readMessageContents: Boolean
        val channelReadMessageContents: Boolean
        val channelReadHistory: Boolean

        if (!ClientChecker.isTgnetObfuscated()) {
            val className = obj.javaClass.name
            readHistory = className.contains("TL_messages_readHistory")
            readDiscussion = className.contains("TL_messages_readDiscussion")
            encryptedHistory = className.contains("TL_messages_readEncryptedHistory")
            readMessageContents = className.contains("TL_messages_readMessageContents")
            channelReadMessageContents = className.contains("TL_channels_readMessageContents")
            channelReadHistory = className.contains("TL_channels_readHistory")
        } else {
            val objectClass = obj.javaClass
            readHistory = objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_MESSAGES_READ_HISTORY))
            readDiscussion = objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_MESSAGES_READ_DISCUSSION))
            encryptedHistory = objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_MESSAGES_READ_ENCRYPTED_HISTORY))
            readMessageContents = objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_MESSAGES_READ_MESSAGE_CONTENTS))
            channelReadMessageContents = objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_CHANNELS_READ_MESSAGE_CONTENTS))
            channelReadHistory = objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_CHANNELS_READ_HISTORY))
        }

        if (!(readHistory || readDiscussion ||
                    (privateHide && encryptedHistory) ||
                    (privateHide && readMessageContents) ||
                    (channelHide && channelReadMessageContents) ||
                    (channelHide && channelReadHistory))) {
            return false
        }

        if (privateHide && channelHide) {
            return true
        }

        if (readHistory || readDiscussion) {
            val objectName = if (!ClientChecker.isTgnetObfuscated()) {
                obj.javaClass.simpleName
            } else {
                if (readHistory) "TLRPC\$TL_messages_readHistory" else "TLRPC\$TL_messages_readDiscussion"
            }
            val peerObj = XposedHelpers.getObjectField(obj, AutomationResolver.resolve(objectName, "peer", AutomationResolver.ResolverType.Field))
            val inputPeer = TLRPC.InputPeer(peerObj)
            val isChannelOrGroup = inputPeer.getChannel_id() > 0 || inputPeer.getChat_id() > 0

            if (privateHide && !channelHide) return !isChannelOrGroup
            if (channelHide && !privateHide) return isChannelOrGroup
            return false
        }

        return true
    }

    @JvmStatic
    fun saveReadHistory(obj: Any) {
        if (TLChannels_readHistory == null && isTLChannelsReadHistoryRequest(obj)) {
            TLChannels_readHistory = obj
        } else if (TLMessages_readHistory == null && isTLMessagesReadHistoryRequest(obj)) {
            TLMessages_readHistory = obj
        }
    }

    @JvmStatic
    fun handleReadAfterSend(obj: Any) {
        try {
            if (ConfigManager.hideSeen.isEnable && ConfigManager.markReadAfterSend.isEnable) {
                val peer = extractPeerFromSendObject(obj)
                if (peer != null && peer.inputPeer != null) {
                    val dialogId = getDialogId(peer)
                    val messagesStorage = getMessagesStorage()
                    bgScope.launch {
                        getDialogMaxMessageId(messagesStorage, dialogId) { param ->
                            markReadOnServer(param, peer)
                        }
                    }
                }
            }
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    @JvmStatic
    fun getDialogMaxMessageId(messagesStorage: MessagesStorage, dialog_id: Long, callback: IntCallback) {
        bgScope.launch {
            var maxVal = 0
            try {
                val cursor = messagesStorage.getDatabase().queryFinalized("SELECT MAX(mid) FROM messages_v2 WHERE uid = $dialog_id", emptyArray<Any>())
                if (cursor.next()) {
                    maxVal = cursor.intValue(0)
                }
                cursor.dispose()
            } catch (e: Throwable) {
                Logger.e(e)
            }
            AndroidUtilities.runOnUIThread { callback.run(maxVal) }
        }
    }

    @JvmStatic
    fun markReadOnServer(messageId: Int, peer: TLRPC.InputPeer) {
        try {
            val req: Any?
            val ip = peer.inputPeer
            val inputPeerChannel = if (ip != null) {
                if (ClientChecker.isTgnetObfuscated()) {
                    ip.javaClass.name == AutomationResolver.resolve(ClassNames.TL_INPUT_PEER_CHANNEL)
                } else {
                    ip.javaClass.name.contains("TL_inputPeerChannel")
                }
            } else false

            if (inputPeerChannel) {
                val request: TLRPC.TL_channels_readHistory = if (!ClientChecker.check(ClientChecker.ClientType.Nagram)) {
                    TLRPC.TL_channels_readHistory().apply {
                        setChannel(MessagesController.getInputChannel(peer))
                    }
                } else {
                    TLRPC.TL_channels_readHistory(TLChannels_readHistory).apply {
                        setChannel(MessagesController.getInputChannel(getDialogId(peer)))
                    }
                }
                request.setMax_id(messageId)
                req = request.getTL_channels_readHistory()
            } else {
                val request: TLRPC.TL_messages_readHistory = if (!ClientChecker.check(ClientChecker.ClientType.Nagram)) {
                    TLRPC.TL_messages_readHistory()
                } else {
                    TLRPC.TL_messages_readHistory(TLMessages_readHistory)
                }
                request.setPeer(peer)
                request.setMax_id(messageId)
                req = request.getTL_messages_readHistory()
            }

            isReadMessages.set(true)
            getConnectionsManager().sendRequest(req, RequestDelegate.run { response, error ->
                if (error == null) {
                    val affectedClass = ClassLoad.getClass(ClassNames.TL_MESSAGES_AFFECTED)
                    if (affectedClass != null && affectedClass.isInstance(response)) {
                        val res = TLRPC.TL_messages_affectedMessages(response)
                        if (!ClientChecker.check(ClientChecker.ClientType.Nagram)) {
                            getMessagesController().processNewDifferenceParams(-1, res.getPts(), -1, res.getPtsCount())
                        } else {
                            getMessagesController().processNewDifferenceParams(res.getPts(), -1, res.getPtsCount())
                        }
                    }
                }
            })
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    private fun extractPeerFromSendObject(obj: Any): TLRPC.InputPeer? {
        if (!ClientChecker.isTgnetObfuscated()) {
            val className = obj.javaClass.name
            if (className.contains("TL_messages_sendMessage") ||
                className.contains("TL_messages_sendMedia") ||
                className.contains("TL_messages_sendReaction") ||
                className.contains("TL_messages_sendPaidReaction") ||
                className.contains("TL_messages_sendMultiMedia")) {
                return TLRPC.InputPeer(getPeer(obj))
            }
        } else {
            val objectClass = obj.javaClass
            if (objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_MESSAGES_SEND_MESSAGE)) ||
                objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_MESSAGES_SEND_MEDIA)) ||
                objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_MESSAGES_SEND_REACTION)) ||
                objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_MESSAGES_SEND_PAID_REACTION)) ||
                objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_MESSAGES_SEND_MULTI_MEDIA))) {
                return TLRPC.InputPeer(getPeer(obj))
            }
        }
        return null
    }

    private fun getPeer(msg: Any): Any? {
        val objectClass = msg.javaClass
        var msgName: String? = null
        return if (ClientChecker.isTgnetObfuscated()) {
            if (objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_MESSAGES_SEND_MESSAGE)))
                msgName = "TLRPC\$TL_messages_sendMessage"
            if (objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_MESSAGES_SEND_MEDIA)))
                msgName = "TLRPC\$TL_messages_sendMedia"
            if (objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_MESSAGES_SEND_REACTION)))
                msgName = "TLRPC\$TL_messages_sendReaction"
            if (objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_MESSAGES_SEND_PAID_REACTION)))
                msgName = "TLRPC\$TL_messages_sendPaidReaction"
            if (objectClass == ClassLoad.getClass(AutomationResolver.resolve(ClassNames.TL_MESSAGES_SEND_MULTI_MEDIA)))
                msgName = "TLRPC\$TL_messages_sendMultiMedia"
            if (msgName != null) {
                XposedHelpers.getObjectField(msg, AutomationResolver.resolve(msgName, "peer", AutomationResolver.ResolverType.Field))
            } else {
                null
            }
        } else {
            XposedHelpers.getObjectField(msg, "peer")
        }
    }

    @JvmStatic
    fun getDialogId(peer: TLRPC.InputPeer): Long {
        return when {
            peer.getChat_id() != 0L -> -peer.getChat_id()
            peer.getChannel_id() != 0L -> -peer.getChannel_id()
            else -> peer.getUser_id()
        }
    }

    @JvmStatic
    fun getMessagesStorage(): MessagesStorage {
        return MessagesStorage.getInstance(UserConfig.getSelectedAccount())
    }

    @JvmStatic
    fun getConnectionsManager(): ConnectionsManager {
        return ConnectionsManager.getInstance(UserConfig.getSelectedAccount())
    }

    @JvmStatic
    fun getMessagesController(): MessagesController {
        return MessagesController.getInstance(UserConfig.getSelectedAccount())
    }
}
