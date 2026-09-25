package com.my.televip.features.messages

import android.util.SparseArray
import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.clients.ClientManager
import com.my.televip.configs.ConfigManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.messages.MessageStorage
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.androidx.LongSparseArray
import com.my.televip.virtuals.messenger.MessageObject
import com.my.televip.virtuals.messenger.MessagesController
import com.my.televip.virtuals.messenger.MessagesStorage
import com.my.televip.virtuals.messenger.NotificationCenter
import com.my.televip.virtuals.tgnet.TLRPC
import de.robv.android.xposed.XposedHelpers
import java.util.ArrayList

object ShowDeletedMessages {

    const val FLAG_DELETED: Int = 1 shl 31

    private var isDeleteMessage = false

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun markMessagesDeletedForController(
        messagesStorage: MessagesStorage,
        dialogId: Long,
        delMsg: ArrayList<Int>
    ) {
        MessageStorage.markMessagesDeleted(messagesStorage, dialogId, delMsg)
    }

    private fun processDeletedMessage(
        messagesController: MessagesController,
        item: Any,
        updateDeleteChannelMessages: Boolean,
        updateDeleteMessages: Boolean
    ) {
        if (updateDeleteChannelMessages) {
            val channelMessages = TLRPC.TL_updateDeleteChannelMessages(item)
            val dialogMessage: LongSparseArray = messagesController.dialogMessage
            val dialogMessages = dialogMessage.get(-channelMessages.channelID)

            if (dialogMessages != null) {
                for (msgObj in dialogMessages) {
                    val owner = MessageObject(msgObj).messageOwner
                    if (channelMessages.messages.contains(owner.id)) {
                        owner.flags = owner.flags or FLAG_DELETED
                    }
                }
            }
            markMessagesDeletedForController(
                messagesController.messagesStorage,
                -channelMessages.channelID,
                channelMessages.messages
            )
        }

        if (updateDeleteMessages) {
            val messages = TLRPC.TL_updateDeleteMessages(item).messages
            val dialogMessages: SparseArray<Any> = messagesController.dialogMessagesByIds
            for (id in messages) {
                val msgObj = dialogMessages.get(id)
                if (msgObj == null) {
                    break
                } else {
                    val owner = MessageObject(msgObj).messageOwner
                    owner.flags = owner.flags or FLAG_DELETED
                }
            }
            markMessagesDeletedForController(messagesController.messagesStorage, 0, messages)
        }
    }

    @JvmStatic
    fun initProcessUpdateArray() {
        try {
            val mcClass = ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER)
            if (mcClass != null) {
                val argTypes: Array<Class<*>> = arrayOf(
                    ArrayList::class.java,
                    ArrayList::class.java,
                    ArrayList::class.java,
                    Boolean::class.javaPrimitiveType!!,
                    Int::class.javaPrimitiveType!!
                )
                val merged = ArgsResolver.merge("processUpdateArray", argTypes, object : BaseMethodHook() {
                    override fun beforeMethod(param: MethodHookParam) {
                        try {
                            @Suppress("UNCHECKED_CAST")
                            val updates = param.args[0] as? ArrayList<Any> ?: return
                            val messagesController = MessagesController(param.thisObject)
                            if (updates.isEmpty()) return

                            val result = ArrayList<Any>()
                            for (update in updates) {
                                val name = update.javaClass.name
                                val updateDeleteChannelMessages: Boolean
                                val updateDeleteMessages: Boolean

                                if (!ClientManager.isTgnetObfuscated()) {
                                    updateDeleteChannelMessages = name.contains("TL_updateDeleteChannelMessages")
                                    updateDeleteMessages = name.contains("TL_updateDeleteMessages")
                                } else {
                                    updateDeleteChannelMessages = update.javaClass == ClassLoad.getClass(ClassNames.TL_UPDATE_DELETE_CHANNEL_MESSAGES)
                                    updateDeleteMessages = update.javaClass == ClassLoad.getClass(ClassNames.TL_UPDATE_DELETE_MESSAGES)
                                }

                                if (updateDeleteChannelMessages || updateDeleteMessages) {
                                    processDeletedMessage(
                                        messagesController,
                                        update,
                                        updateDeleteChannelMessages,
                                        updateDeleteMessages
                                    )
                                    continue
                                }
                                result.add(update)
                            }
                            param.args[0] = result
                        } catch (e: Throwable) {
                            Logger.e(e)
                        }
                    }
                })
                if (merged != null) {
                    XposedHelpers.findAndHookMethod(
                        mcClass,
                        Obfuscate.getMethodName("MessagesController", "processUpdateArray"),
                        *merged
                    )
                }
            }
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true

                val msClass = ClassLoad.getClass(ClassNames.MESSAGES_STORAGE)
                if (msClass != null) {
                    val markTypes: Array<Class<*>> = arrayOf(
                        Long::class.javaPrimitiveType!!,
                        ArrayList::class.java,
                        Boolean::class.javaPrimitiveType!!,
                        Boolean::class.javaPrimitiveType!!,
                        Int::class.javaPrimitiveType!!,
                        Int::class.javaPrimitiveType!!
                    )
                    val markMerged = ArgsResolver.merge("markMessagesAsDeleted", markTypes, object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            if (!isDeleteMessage) {
                                param.result = null
                            }
                        }
                    })
                    if (markMerged != null) {
                        XposedHelpers.findAndHookMethod(
                            msClass,
                            Obfuscate.getMethodName("MessagesStorage", "markMessagesAsDeleted"),
                            *markMerged
                        )
                    }
                }
            }

            val ncClass = ClassLoad.getClass(ClassNames.NOTIFICATIONS_CONTROLLER)
            if (ncClass != null) {
                val ncTypes: Array<Class<*>> = arrayOf(
                    ClassLoad.getClass(ClassNames.LONG_SPARES_ARRAY)!!,
                    Boolean::class.javaPrimitiveType!!
                )
                val ncMerged = ArgsResolver.merge("removeDeletedMessagesFromNotifications", ncTypes, object : BaseMethodHook() {
                    override fun beforeMethod(param: MethodHookParam) {
                        if (ConfigManager.showDeletedMessages?.isEnable == true) {
                            param.result = null
                        }
                    }
                })
                if (ncMerged != null) {
                    XposedHelpers.findAndHookMethod(
                        ncClass,
                        Obfuscate.getMethodName("NotificationsController", "removeDeletedMessagesFromNotifications"),
                        *ncMerged
                    )
                }
            }

            val deleteTypes: Array<Class<*>> = arrayOf(
                ArrayList::class.java,
                ArrayList::class.java,
                ClassLoad.getClass(ClassNames.TLRPC_ENCRYPTED_CHAT)!!,
                Long::class.javaPrimitiveType!!,
                Boolean::class.javaPrimitiveType!!,
                Int::class.javaPrimitiveType!!,
                Boolean::class.javaPrimitiveType!!,
                Long::class.javaPrimitiveType!!,
                ClassLoad.getClass(ClassNames.TL_OBJECT)!!,
                Int::class.javaPrimitiveType!!,
                Boolean::class.javaPrimitiveType!!,
                Int::class.javaPrimitiveType!!
            )
            val deleteMerged = ArgsResolver.merge("deleteMessages", deleteTypes, object : BaseMethodHook() {
                override fun beforeMethod(param: MethodHookParam) {
                    isDeleteMessage = true
                }
            })
            val mcClass = ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER)
            if (mcClass != null && deleteMerged != null) {
                XposedHelpers.findAndHookMethod(
                    mcClass,
                    Obfuscate.getMethodName("MessagesController", "deleteMessages"),
                    *deleteMerged
                )
            }

            val notifCenterClass = ClassLoad.getClass(ClassNames.NOTIFICATION_CENTER)
            if (notifCenterClass != null) {
                val postTypes: Array<Class<*>> = arrayOf(
                    Int::class.javaPrimitiveType!!,
                    Array<Any>::class.java
                )
                val postMerged = ArgsResolver.merge("postNotificationName", postTypes, object : BaseMethodHook() {
                    override fun beforeMethod(param: MethodHookParam) {
                        if (!isDeleteMessage) {
                            val id = param.args[0] as Int
                            if (id == NotificationCenter.getMessagesDeleted()) {
                                param.result = null
                            }
                        }
                    }

                    override fun afterMethod(param: MethodHookParam) {
                        isDeleteMessage = false
                    }
                })
                if (postMerged != null) {
                    XposedHelpers.findAndHookMethod(
                        notifCenterClass,
                        Obfuscate.getMethodName("NotificationCenter", "postNotificationName"),
                        *postMerged
                    )
                }
            }

            initProcessUpdateArray()
        } catch (e: Throwable) {
            Logger.e(e)
        }

        if (ConfigManager.showDeletedMessages?.isEnable == true && !MessageTimeModifier.loaded) {
            MessageTimeModifier.init()
        }
    }
}
