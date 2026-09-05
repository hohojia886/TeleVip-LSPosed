package com.my.televip.features

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.widget.ScrollView
import android.widget.TextView
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.ClientChecker
import com.my.televip.Configs.ConfigManager
import com.my.televip.Database.MessageDatabase
import com.my.televip.base.AbstractMethodHook
import com.my.televip.calendar.ConverterCalendar
import com.my.televip.hooks.HMethod
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.utils.Utils
import com.my.televip.virtuals.ActionBar.AlertDialog
import com.my.televip.virtuals.SettingsIconResolver
import com.my.televip.virtuals.Theme
import com.my.televip.virtuals.messenger.BaseController
import com.my.televip.virtuals.messenger.MessageObject
import com.my.televip.virtuals.messenger.MessagesStorage
import com.my.televip.virtuals.tgnet.TLRPC
import com.my.televip.virtuals.ui.ChatActivity
import java.util.Locale

object SaveEditsHistory {

    private var messageDatabase: MessageDatabase? = null

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init(context: Context) {
        try {
            if (!isEnable) {
                isEnable = true
                messageDatabase = MessageDatabase(context)

                val chatActivityClass = ClassLoad.getClass(ClassNames.CHAT_ACTIVITY)
                val msgObjClass = ClassLoad.getClass(ClassNames.MESSAGE_OBJECT)
                if (chatActivityClass != null && msgObjClass != null) {
                    HMethod.hookMethod(
                        chatActivityClass,
                        AutomationResolver.resolve("ChatActivity", "fillMessageMenu", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject(
                                "fillMessageMenu",
                                arrayOf<Class<*>>(
                                    msgObjClass,
                                    ArrayList::class.java,
                                    ArrayList::class.java,
                                    ArrayList::class.java
                                )
                            ),
                            object : AbstractMethodHook() {
                                override fun afterMethod(param: MethodHookParam) {
                                    if (ConfigManager.saveEditsHistory.isEnable) {
                                        val chatActivity = ChatActivity(param.thisObject)
                                        val selectedObj = chatActivity.selectedObject
                                        if (selectedObj != null) {
                                            val messageOwner = selectedObj.messageOwner
                                            if (messageOwner != null) {
                                                val fromId = messageOwner.from_id
                                                if (fromId != null && messageOwner.id > 0) {
                                                    val dialogId = getDialogId(fromId)
                                                    if (dialogId != 0L && messageDatabase?.getMessage(dialogId, messageOwner.id) != null) {
                                                        val icons: ArrayList<Int>
                                                        val items: ArrayList<CharSequence>
                                                        val options: ArrayList<Int>

                                                        if (ClientChecker.check(ClientChecker.ClientType.Telegraph)) {
                                                            icons = Utils.castList(param.args[2], Int::class.javaObjectType)
                                                            items = Utils.castList(param.args[3], CharSequence::class.java)
                                                            options = Utils.castList(param.args[4], Int::class.javaObjectType)
                                                        } else {
                                                            icons = Utils.castList(param.args[1], Int::class.javaObjectType)
                                                            items = Utils.castList(param.args[2], CharSequence::class.java)
                                                            options = Utils.castList(param.args[3], Int::class.javaObjectType)
                                                        }

                                                        items.add(Translator.get(Keys.EditsHistory))
                                                        options.add(8353847)
                                                        if (!ClientChecker.check(ClientChecker.ClientType.Nagram)) {
                                                            icons.add(SettingsIconResolver.getIconSettings())
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        )
                    )

                    HMethod.hookMethod(
                        chatActivityClass,
                        AutomationResolver.resolve("ChatActivity", "processSelectedOption", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("processSelectedOption", arrayOf<Class<*>>(Int::class.javaPrimitiveType!!)),
                            object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    if (ConfigManager.saveEditsHistory.isEnable) {
                                        val option = param.args[0] as Int
                                        if (option == 8353847) {
                                            val chatActivity = ChatActivity(param.thisObject)
                                            val selectedObj = chatActivity.selectedObject
                                            if (selectedObj != null) {
                                                val messageOwner = selectedObj.messageOwner
                                                if (messageOwner != null && messageOwner.getMessage() != null && messageOwner.from_id != null && messageOwner.id > 0) {
                                                    val dialogId = getDialogId(messageOwner.from_id)
                                                    val db = messageDatabase
                                                    if (dialogId != 0L && db != null && db.searchMessage(dialogId, messageOwner.id)) {
                                                        val alertDialog = AlertDialog(context)
                                                        alertDialog.setTitle(Translator.get(Keys.EditsHistory))

                                                        val textView = TextView(context)
                                                        val maxMsgCount = db.getMaxMessageCount(dialogId, messageOwner.id)
                                                        val builder = StringBuilder()

                                                        if (maxMsgCount > 1) {
                                                            for (i in 1..maxMsgCount) {
                                                                val msg = db.getMessage(dialogId, messageOwner.id, i)
                                                                if (msg != null) {
                                                                    val messageDate = db.getMessageDate(dialogId, messageOwner.id, i)
                                                                    if (messageDate != 0L) {
                                                                        val date = ConverterCalendar.formatDate(messageDate)
                                                                        builder.append(Translator.get(Keys.Message)).append(i).append(" ").append(date).append("\n")
                                                                        builder.append(msg).append("\n")
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            val messageDate = db.getMessageDate(dialogId, messageOwner.id, 1)
                                                            if (messageDate != 0L) {
                                                                val date = ConverterCalendar.formatDate(messageDate)
                                                                builder.append(date).append("\n")
                                                            }
                                                            builder.append(db.getMessage(dialogId, messageOwner.id))
                                                        }

                                                        textView.text = builder.toString()
                                                        textView.setPadding(32, 32, 32, 32)
                                                        textView.textSize = 16f
                                                        textView.setTextColor(Theme.getTextColor())
                                                        textView.movementMethod = ScrollingMovementMethod()
                                                        textView.setTextIsSelectable(true)

                                                        val scrollView = ScrollView(context)
                                                        scrollView.addView(textView)
                                                        alertDialog.setView(scrollView)
                                                        alertDialog.setPositiveButton(Translator.get(Keys.Done), null)
                                                        alertDialog.show()
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        )
                    )
                }

                val messagesStorageClass = ClassLoad.getClass(ClassNames.MESSAGES_STORAGE)
                val tlMessagesMessages = ClassLoad.getClass(ClassNames.TL_MESSAGES_MESSAGES)
                if (messagesStorageClass != null && tlMessagesMessages != null) {
                    HMethod.hookMethod(
                        messagesStorageClass,
                        AutomationResolver.resolve("MessagesStorage", "putMessages", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject(
                                "putMessages",
                                arrayOf<Class<*>>(
                                    tlMessagesMessages,
                                    Long::class.javaPrimitiveType!!,
                                    Int::class.javaPrimitiveType!!,
                                    Int::class.javaPrimitiveType!!,
                                    Boolean::class.javaPrimitiveType!!,
                                    Int::class.javaPrimitiveType!!,
                                    Long::class.javaPrimitiveType!!
                                )
                            ),
                            object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    if (ConfigManager.saveEditsHistory.isEnable) {
                                        var loadType = param.args[2] as Int
                                        if (ClientChecker.check(ClientChecker.ClientType.Nagram)) {
                                            loadType = param.args[0] as Int
                                        }

                                        if (loadType == -2) {
                                            val messagesStorageObject = param.thisObject
                                            var messagesObject = param.args[0]
                                            if (ClientChecker.check(ClientChecker.ClientType.Nagram)) {
                                                messagesObject = param.args[5]
                                            }

                                            if (messagesObject != null) {
                                                val messagesStorage = MessagesStorage(messagesStorageObject)
                                                val messages = TLRPC.messages_Messages(messagesObject)
                                                val count = messages.messages.size
                                                val baseController = BaseController(messagesStorageObject)
                                                val userConfig = baseController.userConfig
                                                val sqLiteDatabase = messagesStorage.database

                                                for (a in 0 until count) {
                                                    val message = TLRPC.Message(messages.messages[a])
                                                    val id = message.id
                                                    val cursor = sqLiteDatabase.queryFinalized(
                                                        String.format(Locale.US, "SELECT data FROM messages_v2 WHERE mid = %d AND uid = %d", id, MessageObject.getDialogId(message)),
                                                        arrayOf()
                                                    )

                                                    if (cursor.next()) {
                                                        val data = cursor.byteBufferValue(0)
                                                        if (data.nativeByteBuffer != null) {
                                                            try {
                                                                val oldMessage = TLRPC.Message.TLdeserialize(data, data.readInt32(false), false)
                                                                oldMessage.readAttachPath(data, userConfig.clientUserId)
                                                                val oldText = oldMessage.getMessage()
                                                                val newText = message.getMessage()
                                                                if (oldText != null && newText != null) {
                                                                    if (oldMessage.from_id != null && oldText != newText) {
                                                                        val dialogId = getDialogId(message.from_id)
                                                                        if (dialogId != 0L) {
                                                                            messageDatabase?.addMessage(dialogId, oldMessage.id, oldText)
                                                                        }
                                                                    }
                                                                }
                                                            } finally {
                                                                data.reuse()
                                                            }
                                                        }
                                                    }
                                                    cursor.dispose()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        )
                    )
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }

    private fun getDialogId(peer: TLRPC.Peer): Long {
        val userId = peer.user_id
        val chatId = peer.chat_id
        val channelId = peer.channel_id
        return when {
            userId != 0L -> userId
            chatId != 0L -> chatId
            channelId != 0L -> channelId
            else -> 0L
        }
    }
}
