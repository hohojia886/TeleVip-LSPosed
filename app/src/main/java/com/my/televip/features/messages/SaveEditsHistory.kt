package com.my.televip.features.messages

import android.text.method.ScrollingMovementMethod
import android.widget.ScrollView
import android.widget.TextView
import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.clients.ClientManager
import com.my.televip.configs.ConfigManager
import com.my.televip.database.MessageDatabase
import com.my.televip.base.BaseMethodHook
import com.my.televip.calendar.ConverterCalendar
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.ui.ThemeColors
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

    private var messageDatabase: MessageDatabase? = null

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                messageDatabase = MessageDatabase(Utils.getCurrentActivity())

                val chatClass = ClassLoad.getClass(ClassNames.CHAT_ACTIVITY)
                if (chatClass != null) {
                    val fillTypes: Array<Class<*>> = arrayOf(
                        ClassLoad.getClass(ClassNames.MESSAGE_OBJECT)!!,
                        ArrayList::class.java,
                        ArrayList::class.java,
                        ArrayList::class.java
                    )
                    val fillMerged = ArgsResolver.merge("fillMessageMenu", fillTypes, object : BaseMethodHook() {
                        override fun afterMethod(param: MethodHookParam) {
                            if (ConfigManager.saveEditsHistory?.isEnable == true) {
                                val chatActivity = ChatActivity(param.thisObject)
                                val messageObject = chatActivity.selectedObject
                                if (messageObject?.messageOwner != null) {
                                    val message = messageObject.messageOwner
                                    if (message.from_id != null && message.id > 0) {
                                        val userId = message.from_id.user_id
                                        val chatId = message.from_id.chat_id
                                        val channelId = message.from_id.channel_id
                                        var dialogId: Long = 0

                                        if (userId != 0L) {
                                            dialogId = userId
                                        } else if (chatId != 0L) {
                                            dialogId = chatId
                                        } else if (channelId != 0L) {
                                            dialogId = channelId
                                        }

                                        if (dialogId != 0L && messageDatabase?.getMessage(dialogId, message.id) != null) {
                                            @Suppress("UNCHECKED_CAST")
                                            val icons: ArrayList<Int>
                                            @Suppress("UNCHECKED_CAST")
                                            val items: ArrayList<CharSequence>
                                            @Suppress("UNCHECKED_CAST")
                                            val options: ArrayList<Int>

                                            if (ClientManager.`is`(ClientManager.Client.Telegraph)) {
                                                icons = param.args[2] as ArrayList<Int>
                                                items = param.args[3] as ArrayList<CharSequence>
                                                options = param.args[4] as ArrayList<Int>
                                            } else {
                                                icons = param.args[1] as ArrayList<Int>
                                                items = param.args[2] as ArrayList<CharSequence>
                                                options = param.args[3] as ArrayList<Int>
                                            }

                                            items.add(Translator.get(Keys.EditsHistory))
                                            options.add(8353847)
                                            if (!ClientManager.`is`(ClientManager.Client.Nagram)) {
                                                icons.add(SettingsIconResolver.getIconSettings())
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    })
                    if (fillMerged != null) {
                        XposedHelpers.findAndHookMethod(
                            chatClass,
                            Obfuscate.getMethodName("ChatActivity", "fillMessageMenu"),
                            *fillMerged
                        )
                    }

                    val processTypes: Array<Class<*>> = arrayOf(Int::class.javaPrimitiveType!!)
                    val processMerged = ArgsResolver.merge("processSelectedOption", processTypes, object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            if (ConfigManager.saveEditsHistory?.isEnable == true) {
                                val option = param.args[0] as Int
                                val chatActivity = ChatActivity(param.thisObject)

                                if (option == 8353847) {
                                    val messageObject = chatActivity.selectedObject
                                    if (messageObject?.messageOwner != null) {
                                        val message = messageObject.messageOwner
                                        if (message.get_Message() != null && message.from_id != null && message.id > 0 && message.messageText != null) {
                                            val dialogId = getDialogId(message.from_id)
                                            if (dialogId != 0L && messageDatabase?.searchMessage(dialogId, message.id) == true) {
                                                val alertDialog = AlertDialog(Utils.getCurrentActivity())
                                                alertDialog.setTitle(Translator.get(Keys.EditsHistory))

                                                val textView = TextView(Utils.getCurrentActivity())
                                                val db = messageDatabase ?: return
                                                val maxMsgCount = db.getMaxMessageCount(dialogId, message.id)
                                                val builder = StringBuilder()

                                                if (maxMsgCount > 1) {
                                                    for (i in 1..maxMsgCount) {
                                                        val msg = db.getMessage(dialogId, message.id, i)
                                                        if (msg != null) {
                                                            val messageDate = db.getMessageDate(dialogId, message.id, i)
                                                            if (messageDate != 0L) {
                                                                val date = ConverterCalendar.formatDate(messageDate)
                                                                builder.append(Translator.get(Keys.Message)).append(i).append(" ").append(date).append("\n")
                                                                builder.append(msg).append("\n")
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    val messageDate = db.getMessageDate(dialogId, message.id, 1)
                                                    if (messageDate != 0L) {
                                                        val date = ConverterCalendar.formatDate(messageDate)
                                                        builder.append(date).append("\n")
                                                    }
                                                    builder.append(db.getMessage(dialogId, message.id))
                                                }

                                                textView.text = builder.toString()
                                                textView.setPadding(32, 32, 32, 32)
                                                textView.textSize = 16f
                                                textView.setTextColor(ThemeColors.getTextColor())
                                                textView.movementMethod = ScrollingMovementMethod()
                                                textView.setTextIsSelectable(true)

                                                val scrollView = ScrollView(Utils.getCurrentActivity())
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
                    })
                    if (processMerged != null) {
                        XposedHelpers.findAndHookMethod(
                            chatClass,
                            Obfuscate.getMethodName("ChatActivity", "processSelectedOption"),
                            *processMerged
                        )
                    }
                }

                val msClass = ClassLoad.getClass(ClassNames.MESSAGES_STORAGE)
                if (msClass != null) {
                    val putTypes: Array<Class<*>> = arrayOf(
                        ClassLoad.getClass(ClassNames.TL_MESSAGES_MESSAGES)!!,
                        Long::class.javaPrimitiveType!!,
                        Int::class.javaPrimitiveType!!,
                        Int::class.javaPrimitiveType!!,
                        Boolean::class.javaPrimitiveType!!,
                        Int::class.javaPrimitiveType!!,
                        Long::class.javaPrimitiveType!!
                    )
                    val putMerged = ArgsResolver.merge("putMessages", putTypes, object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            if (ConfigManager.saveEditsHistory?.isEnable == true) {
                                var loadType = param.args[2] as Int
                                if (ClientManager.`is`(ClientManager.Client.Nagram)) {
                                    loadType = param.args[0] as Int
                                }

                                if (loadType == -2) {
                                    val messagesStorageObject = param.thisObject
                                    var messagesObject = param.args[0]
                                    if (ClientManager.`is`(ClientManager.Client.Nagram)) {
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
                                                String.format(
                                                    Locale.US,
                                                    "SELECT data FROM messages_v2 WHERE mid = %d AND uid = %d",
                                                    id,
                                                    MessageObject.getDialogId(message)
                                                ),
                                                arrayOf()
                                            )

                                            if (cursor.next()) {
                                                val data = cursor.byteBufferValue(0)
                                                if (data.nativeByteBuffer != null) {
                                                    val oldMessage = TLRPC.Message.TLdeserialize(
                                                        data,
                                                        data.readInt32(false),
                                                        false
                                                    )
                                                    oldMessage.readAttachPath(data, userConfig.clientUserId)
                                                    data.reuse()
                                                    if (oldMessage.get_Message() != null && message.get_Message() != null) {
                                                        if (oldMessage.from_id != null && oldMessage.get_Message() != message.get_Message()) {
                                                            val dialogId = getDialogId(message.from_id)
                                                            if (dialogId != 0L) {
                                                                messageDatabase?.addMessage(
                                                                    dialogId,
                                                                    oldMessage.id,
                                                                    Utils.getFieldAsString(oldMessage.get_Message())
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    })
                    if (putMerged != null) {
                        XposedHelpers.findAndHookMethod(
                            msClass,
                            Obfuscate.getMethodName("MessagesStorage", "putMessages"),
                            *putMerged
                        )
                    }
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
        var dialogId: Long = 0

        if (userId != 0L) {
            dialogId = userId
        } else if (chatId != 0L) {
            dialogId = chatId
        } else if (channelId != 0L) {
            dialogId = channelId
        }
        return dialogId
    }
}
