package com.my.televip.features.ghostMode

import com.my.televip.callback.IntCallback
import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.clients.ClientManager
import com.my.televip.configs.ConfigManager
import com.my.televip.application.AndroidUtilities
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.SQLite.SQLiteCursor
import com.my.televip.virtuals.messenger.MessagesController
import com.my.televip.virtuals.messenger.MessagesStorage
import com.my.televip.virtuals.messenger.Utilities
import com.my.televip.virtuals.tgnet.ConnectionsManager
import com.my.televip.virtuals.tgnet.RequestDelegate
import com.my.televip.virtuals.tgnet.TLRPC
import de.robv.android.xposed.XposedHelpers

object HideSeen {

    @JvmField
    var TLChannels_readHistory: Any? = null

    @JvmField
    var TLMessages_readHistory: Any? = null

    @JvmField
    var isReadMessages: Boolean = false

    private var tlMessagesReadHistoryClass: Class<*>? = null
    private var tlChannelsReadHistoryClass: Class<*>? = null
    private var tlMessagesReadDiscussionClass: Class<*>? = null
    private var tlMessagesReadEncryptedHistoryClass: Class<*>? = null
    private var tlMessagesReadMessageContentsClass: Class<*>? = null
    private var tlChannelsReadMessageContentsClass: Class<*>? = null

    fun initClasses() {
        if (ClientManager.isTgnetObfuscated() && tlMessagesReadHistoryClass == null) {
            tlMessagesReadHistoryClass = ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_MESSAGES_READ_HISTORY))
            tlChannelsReadHistoryClass = ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_CHANNELS_READ_HISTORY))
            tlMessagesReadDiscussionClass = ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_MESSAGES_READ_DISCUSSION))
            tlMessagesReadEncryptedHistoryClass = ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_MESSAGES_READ_ENCRYPTED_HISTORY))
            tlMessagesReadMessageContentsClass = ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_MESSAGES_READ_MESSAGE_CONTENTS))
            tlChannelsReadMessageContentsClass = ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_CHANNELS_READ_MESSAGE_CONTENTS))
        }
    }

    @JvmStatic
    fun sendFakeReadResponse(onCompleteOrig: Any?) {
        if (onCompleteOrig == null) return
        try {
            val fakeRes = TLRPC.TL_messages_affectedMessages()
            fakeRes.setPts(-1)
            fakeRes.setPtsCount(0)
            val onComplete = RequestDelegate(onCompleteOrig)
            Utilities.getStageQueue().postRunnable {
                try {
                    if (onComplete.requestDelegate != null) {
                        onComplete.run(fakeRes.tL_messages_affectedMessages, null)
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
    fun isTLMessagesReadHistoryRequest(objectParam: Any): Boolean {
        return if (!ClientManager.isTgnetObfuscated()) {
            objectParam.javaClass.name.contains("TL_messages_readHistory")
        } else {
            tlMessagesReadHistoryClass?.isInstance(objectParam) == true || objectParam.javaClass == tlMessagesReadHistoryClass
        }
    }

    @JvmStatic
    fun isTLChannelsReadHistoryRequest(objectParam: Any): Boolean {
        return if (!ClientManager.isTgnetObfuscated()) {
            objectParam.javaClass.name.contains("TL_channels_readHistory")
        } else {
            tlChannelsReadHistoryClass?.isInstance(objectParam) == true || objectParam.javaClass == tlChannelsReadHistoryClass
        }
    }

    @JvmStatic
    fun isReadMessageRequest(objectParam: Any): Boolean {
        val privateHide = ConfigManager.hideSeenPrivateChat?.isEnable == true
        val channelHide = ConfigManager.hideSeenChannel?.isEnable == true

        val readHistory: Boolean
        val readDiscussion: Boolean
        val encryptedHistory: Boolean
        val readMessageContents: Boolean
        val channelReadMessageContents: Boolean
        val channelReadHistory: Boolean

        if (!ClientManager.isTgnetObfuscated()) {
            val className = objectParam.javaClass.name

            readHistory = className.contains("TL_messages_readHistory")
            readDiscussion = className.contains("TL_messages_readDiscussion")
            encryptedHistory = className.contains("TL_messages_readEncryptedHistory")
            readMessageContents = className.contains("TL_messages_readMessageContents")
            channelReadMessageContents = className.contains("TL_channels_readMessageContents")
            channelReadHistory = className.contains("TL_channels_readHistory")
        } else {
            val objectClass = objectParam.javaClass

            readHistory = tlMessagesReadHistoryClass?.isInstance(objectParam) == true || objectClass == tlMessagesReadHistoryClass
            readDiscussion = tlMessagesReadDiscussionClass?.isInstance(objectParam) == true || objectClass == tlMessagesReadDiscussionClass
            encryptedHistory = tlMessagesReadEncryptedHistoryClass?.isInstance(objectParam) == true || objectClass == tlMessagesReadEncryptedHistoryClass
            readMessageContents = tlMessagesReadMessageContentsClass?.isInstance(objectParam) == true || objectClass == tlMessagesReadMessageContentsClass
            channelReadMessageContents = tlChannelsReadMessageContentsClass?.isInstance(objectParam) == true || objectClass == tlChannelsReadMessageContentsClass
            channelReadHistory = tlChannelsReadHistoryClass?.isInstance(objectParam) == true || objectClass == tlChannelsReadHistoryClass
        }

        if (!(readHistory || readDiscussion ||
                    (privateHide && encryptedHistory) ||
                    (privateHide && readMessageContents) ||
                    (channelHide && channelReadMessageContents) ||
                    (channelHide && channelReadHistory))
        ) {
            return false
        }

        if (privateHide && channelHide) {
            return true
        }

        if (readHistory || readDiscussion) {
            val objectName: String = if (!ClientManager.isTgnetObfuscated()) {
                objectParam.javaClass.simpleName
            } else {
                if (readHistory) "TLRPC\$TL_messages_readHistory" else "TLRPC\$TL_messages_readDiscussion"
            }
            val inputPeer = TLRPC.InputPeer(
                XposedHelpers.getObjectField(objectParam, Obfuscate.getFieldName(objectName, "peer"))
            )

            val isChannelOrGroup = inputPeer.channel_id > 0 || inputPeer.chat_id > 0

            if (privateHide && !channelHide) {
                return !isChannelOrGroup
            }

            if (channelHide && !privateHide) {
                return isChannelOrGroup
            }

            return false
        }

        return true
    }

    @JvmStatic
    fun saveReadHistory(objectParam: Any) {
        if (TLChannels_readHistory == null && isTLChannelsReadHistoryRequest(objectParam)) {
            TLChannels_readHistory = objectParam
        } else if (TLMessages_readHistory == null && isTLMessagesReadHistoryRequest(objectParam)) {
            TLMessages_readHistory = objectParam
        }
    }

    @JvmStatic
    fun handleReadAfterSend(objectParam: Any) {
        try {
            if (ConfigManager.hideSeen?.isEnable == true && ConfigManager.markReadAfterSend?.isEnable == true) {
                val peer = extractPeerFromSendObject(objectParam)

                if (peer?.inputPeer != null) {
                    val dialogId = getDialogId(peer)
                    val messagesStorage = MessagesStorage.getMessagesStorage()
                    messagesStorage.storageQueue.postRunnable {
                        getDialogMaxMessageId(messagesStorage, dialogId) { param -> markReadOnServer(param, peer) }
                    }
                }
            }
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    @JvmStatic
    fun getDialogMaxMessageId(messagesStorage: MessagesStorage, dialog_id: Long, callback: IntCallback) {
        messagesStorage.storageQueue.postRunnable {
            var cursor: SQLiteCursor? = null
            val max = IntArray(1)
            try {
                cursor = messagesStorage.database.queryFinalized(
                    "SELECT MAX(mid) FROM messages_v2 WHERE uid = $dialog_id",
                    arrayOf()
                )
                if (cursor.next()) {
                    max[0] = cursor.intValue(0)
                }
            } catch (e: Throwable) {
                Logger.e(e)
            } finally {
                cursor?.dispose()
            }
            AndroidUtilities.runOnUIThread { callback.run(max[0]) }
        }
    }

    @JvmStatic
    fun markReadOnServer(messageId: Int, peer: TLRPC.InputPeer) {
        try {
            val inputPeerObj = peer.inputPeer ?: return
            val req: Any?
            val inputPeerChannel: Boolean = if (ClientManager.isTgnetObfuscated()) {
                inputPeerObj.javaClass.name == Obfuscate.getClassName(ClassNames.TL_INPUT_PEER_CHANNEL)
            } else {
                inputPeerObj.javaClass.name.contains("TL_inputPeerChannel")
            }

            if (inputPeerChannel) {
                val request: TLRPC.TL_channels_readHistory = if (!ClientManager.`is`(ClientManager.Client.Nagram)) {
                    TLRPC.TL_channels_readHistory().apply {
                        setChannel(MessagesController.getInputChannel(peer))
                    }
                } else {
                    TLRPC.TL_channels_readHistory(TLChannels_readHistory).apply {
                        setChannel(MessagesController.getInputChannel(getDialogId(peer)))
                    }
                }
                request.setMax_id(messageId)
                req = request.tL_channels_readHistory
            } else {
                val request: TLRPC.TL_messages_readHistory = if (!ClientManager.`is`(ClientManager.Client.Nagram)) {
                    TLRPC.TL_messages_readHistory()
                } else {
                    TLRPC.TL_messages_readHistory(TLMessages_readHistory)
                }
                request.setPeer(peer)
                request.setMax_id(messageId)
                req = request.tL_messages_readHistory
            }

            isReadMessages = true
            ConnectionsManager.getConnectionsManager().sendRequest(req, RequestDelegate.Callback { response, error ->
                if (error == null) {
                    val affectedClass = ClassLoad.getClass(ClassNames.TL_MESSAGES_AFFECTED)
                    if (affectedClass != null && affectedClass.isInstance(response)) {
                        val res = TLRPC.TL_messages_affectedMessages(response)
                        if (!ClientManager.`is`(ClientManager.Client.Nagram)) {
                            MessagesController.getMessagesController().processNewDifferenceParams(-1, res.pts, -1, res.ptsCount)
                        } else {
                            MessagesController.getMessagesController().processNewDifferenceParams(res.pts, -1, res.ptsCount)
                        }
                    }
                }
            })
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    private fun extractPeerFromSendObject(objectParam: Any): TLRPC.InputPeer? {
        if (!ClientManager.isTgnetObfuscated()) {
            val className = objectParam.javaClass.name
            if (className.contains("TL_messages_sendMessage") ||
                className.contains("TL_messages_sendMedia") ||
                className.contains("TL_messages_sendReaction") ||
                className.contains("TL_messages_sendPaidReaction") ||
                className.contains("TL_messages_sendMultiMedia")
            ) {
                val peer = getPeer(objectParam) ?: return null
                return TLRPC.InputPeer(peer)
            }
        } else {
            val objectClass = objectParam.javaClass
            if (objectClass == ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_MESSAGES_SEND_MESSAGE)) ||
                objectClass == ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_MESSAGES_SEND_MEDIA)) ||
                objectClass == ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_MESSAGES_SEND_REACTION)) ||
                objectClass == ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_MESSAGES_SEND_PAID_REACTION)) ||
                objectClass == ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_MESSAGES_SEND_MULTI_MEDIA))
            ) {
                val peer = getPeer(objectParam) ?: return null
                return TLRPC.InputPeer(peer)
            }
        }
        return null
    }

    private fun getPeer(msg: Any): Any? {
        val objectClass = msg.javaClass
        return if (ClientManager.isTgnetObfuscated()) {
            var msgName: String? = null
            if (objectClass == ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_MESSAGES_SEND_MESSAGE)))
                msgName = "TLRPC\$TL_messages_sendMessage"
            if (objectClass == ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_MESSAGES_SEND_MEDIA)))
                msgName = "TLRPC\$TL_messages_sendMedia"
            if (objectClass == ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_MESSAGES_SEND_REACTION)))
                msgName = "TLRPC\$TL_messages_sendReaction"
            if (objectClass == ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_MESSAGES_SEND_PAID_REACTION)))
                msgName = "TLRPC\$TL_messages_sendPaidReaction"
            if (objectClass == ClassLoad.getClass(Obfuscate.getClassName(ClassNames.TL_MESSAGES_SEND_MULTI_MEDIA)))
                msgName = "TLRPC\$TL_messages_sendMultiMedia"
            XposedHelpers.getObjectField(msg, Obfuscate.getFieldName(msgName ?: "", "peer"))
        } else {
            XposedHelpers.getObjectField(msg, "peer")
        }
    }

    @JvmStatic
    fun getDialogId(peer: TLRPC.InputPeer): Long {
        return when {
            peer.chat_id != 0L -> -peer.chat_id
            peer.channel_id != 0L -> -peer.channel_id
            else -> peer.user_id
        }
    }
}
