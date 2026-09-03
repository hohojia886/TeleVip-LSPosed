package com.my.televip.features

import android.util.SparseArray
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.ClientChecker
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod;
import com.my.televip.logging.Logger
import com.my.televip.messages.MessageStorage
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.utils.Utils
import com.my.televip.virtuals.androidx.LongSparseArray
import com.my.televip.virtuals.messenger.MessageObject
import com.my.televip.virtuals.messenger.MessagesController
import com.my.televip.virtuals.messenger.MessagesStorage
import com.my.televip.virtuals.messenger.NotificationCenter
import com.my.televip.virtuals.tgnet.TLRPC
import com.my.televip.virtuals.ui.Cells.ChatMessageCell
import java.lang.reflect.Method
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

object ShowDeletedMessages {

    const val FLAG_DELETED: Int = 1 shl 31

    @JvmField
    val isDeleteMessage: AtomicBoolean = AtomicBoolean(false)

    @JvmField
    val isEnable: AtomicBoolean = AtomicBoolean(false)

    @JvmStatic
    fun markMessagesDeletedForController(messagesStorage: MessagesStorage, dialogId: Long, delMsg: ArrayList<Int>) {
        MessageStorage.markMessagesDeleted(messagesStorage, dialogId, delMsg)
    }

    @JvmStatic
    fun init() {
        try {
            val messagesControllerClass = ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER)
            if (messagesControllerClass != null) {
                val messagesControllerMethods = messagesControllerClass.declaredMethods
                val methodNames = ArrayList<String>()

                for (method in messagesControllerMethods) {
                    if (method.parameterCount == 5 &&
                        method.parameterTypes[0] == ArrayList::class.java &&
                        method.parameterTypes[1] == ArrayList::class.java &&
                        method.parameterTypes[2] == ArrayList::class.java &&
                        method.parameterTypes[3] == Boolean::class.javaPrimitiveType &&
                        method.parameterTypes[4] == Int::class.javaPrimitiveType) {
                        methodNames.add(method.name)
                    }
                }

                if (methodNames.size != 1) {
                    Logger.w("Failed to hook processUpdateArray! Reason: " + (if (methodNames.isEmpty()) "No method found" else "Multiple methods found") + ", " + Utils.issue)
                } else {
                    val methodName = methodNames[0]

                    HMethod.hookMethod(messagesControllerClass, methodName, ArrayList::class.java, ArrayList::class.java, ArrayList::class.java, Boolean::class.javaPrimitiveType, Int::class.javaPrimitiveType, object : AbstractMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            try {
                                val messagesController = MessagesController(param.thisObject)
                                val updates = CopyOnWriteArrayList(Utils.castList(param.args[0], Any::class.java))
                                if (updates.isNotEmpty()) {
                                    val newUpdates = ArrayList<Any>()

                                    for (item in updates) {
                                        val updateDeleteChannelMessages: Boolean
                                        val updateDeleteMessages: Boolean
                                        if (!ClientChecker.isTgnetObfuscated()) {
                                            updateDeleteChannelMessages = item.javaClass.name.contains("TL_updateDeleteChannelMessages")
                                            updateDeleteMessages = item.javaClass.name.contains("TL_updateDeleteMessages")
                                        } else {
                                            updateDeleteChannelMessages = item.javaClass == ClassLoad.getClass(ClassNames.TL_UPDATE_DELETE_CHANNEL_MESSAGES)
                                            updateDeleteMessages = item.javaClass == ClassLoad.getClass(ClassNames.TL_UPDATE_DELETE_MESSAGES)
                                        }

                                        if (!updateDeleteChannelMessages && !updateDeleteMessages) {
                                            newUpdates.add(item)
                                        }

                                        if (updateDeleteChannelMessages) {
                                            val channelMessages = TLRPC.TL_updateDeleteChannelMessages(item)
                                            val dialogMessage: LongSparseArray = messagesController.dialogMessage
                                            val dialogMessages = Utils.castList(dialogMessage.get(-channelMessages.channelID), Any::class.java)
                                            if (dialogMessages != null) {
                                                for (msgObj in dialogMessages) {
                                                    val owner = MessageObject(msgObj).messageOwner
                                                    if (channelMessages.messages.contains(owner.id)) {
                                                        owner.flags = owner.flags or FLAG_DELETED
                                                    }
                                                }
                                            }
                                            markMessagesDeletedForController(messagesController.messagesStorage, -channelMessages.channelID, channelMessages.messages)
                                        }

                                        if (updateDeleteMessages) {
                                            val messages = TLRPC.TL_updateDeleteMessages(item).messages
                                            val dialogMessages: SparseArray<*> = messagesController.dialogMessagesByIds
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
                                    param.args[0] = newUpdates
                                }
                            } catch (throwable: Throwable) {
                                Logger.e(throwable)
                            }
                        }
                    })
                }
            }
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    @JvmStatic
    fun initProcessing() {
        try {
            if (!isEnable.getAndSet(true)) {
                HMethod.hookMethod(
                    ClassLoad.getClass(ClassNames.MESSAGES_STORAGE),
                    AutomationResolver.resolve("MessagesStorage", "markMessagesAsDeleted", AutomationResolver.ResolverType.Method),
                    *AutomationResolver.merge(
                        AutomationResolver.resolveObject("markMessagesAsDeleted", arrayOf(Long::class.javaPrimitiveType, ArrayList::class.java, Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)),
                        object : AbstractMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (!isDeleteMessage.get()) {
                                    param.result = null
                                }
                            }
                        }
                    )
                )
            }

            var removeDeletedMessagesFromNotifications: Method? = null
            val notifControllerClass = ClassLoad.getClass(ClassNames.NOTIFICATIONS_CONTROLLER)
            if (notifControllerClass != null) {
                for (method in notifControllerClass.declaredMethods) {
                    if (method.name == AutomationResolver.resolve("NotificationsController", "removeDeletedMessagesFromNotifications", AutomationResolver.ResolverType.Method)) {
                        removeDeletedMessagesFromNotifications = method
                        break
                    }
                }
            }

            if (removeDeletedMessagesFromNotifications == null) {
                Logger.w("Failed to hook removeDeletedMessagesFromNotifications! Reason: No method found, " + Utils.issue)
            } else {
                HMethod.hookMethod(removeDeletedMessagesFromNotifications, object : AbstractMethodHook() {
                    override fun beforeMethod(param: MethodHookParam) {
                        if (ConfigManager.showDeletedMessages.isEnable) {
                            param.result = null
                        }
                    }
                })
            }

            HMethod.hookMethod(
                ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER),
                AutomationResolver.resolve("MessagesController", "deleteMessages", AutomationResolver.ResolverType.Method),
                *AutomationResolver.merge(
                    AutomationResolver.resolveObject("deleteMessages", arrayOf(
                        ArrayList::class.java,
                        ArrayList::class.java,
                        ClassLoad.getClass(ClassNames.TLRPC_ENCRYPTED_CHAT),
                        Long::class.javaPrimitiveType,
                        Boolean::class.javaPrimitiveType,
                        Int::class.javaPrimitiveType,
                        Boolean::class.javaPrimitiveType,
                        Long::class.javaPrimitiveType,
                        ClassLoad.getClass(ClassNames.TL_OBJECT),
                        Int::class.javaPrimitiveType,
                        Boolean::class.javaPrimitiveType,
                        Int::class.javaPrimitiveType
                    )),
                    object : AbstractMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            isDeleteMessage.set(true)
                        }
                    }
                )
            )

            HMethod.hookMethod(
                ClassLoad.getClass(ClassNames.NOTIFICATION_CENTER),
                AutomationResolver.resolve("NotificationCenter", "postNotificationName", AutomationResolver.ResolverType.Method),
                *AutomationResolver.merge(
                    AutomationResolver.resolveObject("postNotificationName", arrayOf(Int::class.javaPrimitiveType, Array<Any>::class.java)),
                    object : AbstractMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            if (!isDeleteMessage.get()) {
                                val id = param.args[0] as Int
                                if (id == NotificationCenter.getMessagesDeleted()) {
                                    param.result = null
                                }
                            }
                        }

                        override fun afterMethod(param: MethodHookParam) {
                            isDeleteMessage.set(false)
                        }
                    }
                )
            )

            init()
        } catch (e: Throwable) {
            Logger.e(e)
        }

        if (ConfigManager.showDeletedMessages.isEnable && !ChatMessageCell.isEnable) {
            ChatMessageCell.init()
        }
    }
}
