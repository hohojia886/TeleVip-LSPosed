package com.my.televip.features

import android.content.Context
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
import com.my.televip.virtuals.messenger.BaseController
import com.my.televip.virtuals.messenger.MessageObject
import com.my.televip.virtuals.messenger.MessagesStorage
import com.my.televip.virtuals.tgnet.TLRPC
import com.my.televip.virtuals.ui.ChatActivity
import de.robv.android.xposed.XposedHelpers
import java.util.ArrayList
import java.util.Locale

object SaveEditsHistory {

    @JvmField
    var messageDatabase: MessageDatabase? = null

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init(context: Context) {
        try {
            if (!isEnable) {
                isEnable = true
                if (messageDatabase == null) {
                    messageDatabase = MessageDatabase(context)
                }

                val chatActivityClass = ClassLoad.getClass(ClassNames.CHAT_ACTIVITY)
                if (chatActivityClass != null) {
                    HMethod.hookMethod(
                        chatActivityClass,
                        AutomationResolver.resolve("ChatActivity", "fillSendMenu", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject(
                                "fillSendMenu",
                                arrayOf<Class<*>>(
                                    ArrayList::class.java,
                                    ArrayList::class.java,
                                    ArrayList::class.java
                                )
                            ),
                            object : AbstractMethodHook() {
                                override fun afterMethod(param: MethodHookParam) {
                                    if (ConfigManager.saveEditsHistory.isEnable) {
                                        val chatActivity = ChatActivity(param.thisObject)
                                        val selectedObj = chatActivity.getSelectedObject()
                                        if (selectedObj.messageObject != null) {
                                            val messageOwner = selectedObj.getMessageOwner()
                                            val fromId = messageOwner.getFrom_id()
                                            if (messageOwner.getID() > 0) {
                                                val dialogId = getDialogId(fromId)
                                                if (dialogId != 0L && messageDatabase?.getMessage(dialogId, messageOwner.getID()) != null) {
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
                                            val selectedObj = chatActivity.getSelectedObject()
                                            if (selectedObj.messageObject != null) {
                                                val messageOwner = selectedObj.getMessageOwner()
                                                if (messageOwner.getMessage() != null && messageOwner.getID() > 0) {
                                                    val dialogId = getDialogId(messageOwner.getFrom_id())
                                                    val db = messageDatabase
                                                    if (dialogId != 0L && db != null && db.searchMessage(dialogId, messageOwner.getID())) {
                                                        val alertDialog = AlertDialog(context)
                                                        alertDialog.setTitle(Translator.get(Keys.EditsHistory))

                                                        val textView = TextView(context)
                                                        val maxMsgCount = db.getMaxMessageCount(dialogId, messageOwner.getID())
                                                        val builder = StringBuilder()

                                                        for (a in 0 until maxMsgCount) {
                                                            val msgText = db.getMessage(dialogId, messageOwner.getID(), a)
                                                            val msgDate = db.getMessageDate(dialogId, messageOwner.getID(), a)
                                                            if (msgText != null) {
                                                                builder.append(ConverterCalendar.formatDate(msgDate))
                                                                    .append(":\n")
                                                                    .append(msgText)
                                                                    .append("\n\n")
                                                            }
                                                        }

                                                        textView.text = builder.toString()
                                                        textView.setPadding(30, 30, 30, 30)

                                                        alertDialog.setView(textView)
                                                        alertDialog.setPositiveButton(Translator.get(Keys.Cancel), null)
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
                val msgMessagesClass = ClassLoad.getClass(ClassNames.TL_MESSAGES_MESSAGES)
                if (messagesStorageClass != null && msgMessagesClass != null) {
                    HMethod.hookMethod(
                        messagesStorageClass,
                        AutomationResolver.resolve("MessagesStorage", "updateMessageStateAndHistory", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject(
                                "updateMessageStateAndHistory",
                                arrayOf<Class<*>>(
                                    Long::class.javaPrimitiveType!!,
                                    ArrayList::class.java,
                                    ArrayList::class.java,
                                    Int::class.javaPrimitiveType!!,
                                    Int::class.javaPrimitiveType!!,
                                    msgMessagesClass
                                )
                            ),
                            object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    if (ConfigManager.saveEditsHistory.isEnable) {
                                        val messagesStorageObject = param.thisObject
                                        if (messagesStorageObject != null) {
                                            var messagesObject = param.args[2]

                                            if (ClientChecker.check(ClientChecker.ClientType.Nagram)) {
                                                messagesObject = param.args[5]
                                            }

                                            if (messagesObject != null) {
                                                val messagesStorage = MessagesStorage(messagesStorageObject)
                                                val messages = TLRPC.messages_Messages(messagesObject)
                                                val count = messages.getMessages().size
                                                val baseController = BaseController(messagesStorageObject)
                                                val userConfig = baseController.getUserConfig()
                                                val sqLiteDatabase = messagesStorage.getDatabase()

                                                for (a in 0 until count) {
                                                    val message = TLRPC.Message(messages.getMessages()[a])
                                                    val id = message.getID()
                                                    val cursor = sqLiteDatabase.queryFinalized(
                                                        String.format(Locale.US, "SELECT data FROM messages_v2 WHERE mid = %d AND uid = %d", id, MessageObject.getDialogId(message)),
                                                        emptyArray<Any>()
                                                    )

                                                    if (cursor.next()) {
                                                        val data = cursor.byteBufferValue(0)
                                                        if (data.nativeByteBuffer != null) {
                                                            try {
                                                                val oldMessage = TLRPC.Message.TLdeserialize(data, data.readInt32(false), false)
                                                                oldMessage.readAttachPath(data, userConfig.getClientUserId())
                                                                val oldText = oldMessage.getMessage()
                                                                val newText = message.getMessage()
                                                                if (oldText != null && newText != null) {
                                                                    if (oldText != newText) {
                                                                        val dialogId = getDialogId(message.getFrom_id())
                                                                        if (dialogId != 0L) {
                                                                            messageDatabase?.addMessage(dialogId, oldMessage.getID(), oldText)
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
        val userId = peer.getUser_id()
        val chatId = peer.getChat_id()
        val channelId = peer.getChannel_id()
        return when {
            userId != 0L -> userId
            chatId != 0L -> chatId
            channelId != 0L -> channelId
            else -> 0L
        }
    }
}
