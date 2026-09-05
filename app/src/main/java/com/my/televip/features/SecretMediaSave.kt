package com.my.televip.features

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.messenger.MessageObject
import com.my.televip.virtuals.tgnet.TLRPC
import com.my.televip.virtuals.ui.Cells.ChatMessageCell
import com.my.televip.virtuals.ui.PhotoViewer
import com.my.televip.virtuals.ui.SecretMediaViewer
import java.io.File

object SecretMediaSave {
    @JvmField var isEnable: Boolean = false
    @JvmField var id: Long = 0L
    @JvmField var pathImage: File? = null

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val msgObjClass = ClassLoad.getClass(ClassNames.MESSAGE_OBJECT)
                if (msgObjClass != null) {
                    HMethod.hookMethod(
                        msgObjClass,
                        AutomationResolver.resolve("MessageObject", "isSecret", AutomationResolver.ResolverType.Method),
                        object : AbstractMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (ConfigManager.secretMediaSave.isEnable) param.result = false
                            }
                        }
                    )
                }

                val delegateClass = ClassLoad.getClass(ClassNames.CHAT_MESSAGE_CELL_DELEGATE)
                val chatMsgCellClass = ClassLoad.getClass(ClassNames.CHAT_MESSAGE_CELL)
                if (delegateClass != null && chatMsgCellClass != null) {
                    HMethod.hookMethod(
                        delegateClass,
                        AutomationResolver.resolve("ChatActivity\$ChatMessageCellDelegate", "didPressImage", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("didPressImage", arrayOf<Class<*>>(chatMsgCellClass, Float::class.javaPrimitiveType!!, Float::class.javaPrimitiveType!!, Boolean::class.javaPrimitiveType!!)),
                            object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    try {
                                        if (ConfigManager.secretMediaSave.isEnable && param.args[0] != null) {
                                            val messageCell = ChatMessageCell(param.args[0])
                                            if (messageCell.chatMessageCell != null) {
                                                val messageObject = messageCell.messageObject
                                                if (messageObject.messageObject != null) {
                                                    val message = messageObject.messageOwner
                                                    if (message.ttl > 0) message.setTtl(0)
                                                }
                                                bindPhotoViewerToActivity(messageCell)
                                            }
                                        }
                                    } catch (e: Throwable) {
                                        Logger.e(e)
                                    }
                                }
                            }
                        )
                    )
                }

                val fileLoaderClass = ClassLoad.getClass(ClassNames.FILE_LOADER)
                val tlMessageClass = ClassLoad.getClass(ClassNames.TL_MESSAGE)
                if (fileLoaderClass != null && tlMessageClass != null) {
                    HMethod.hookMethod(
                        fileLoaderClass,
                        AutomationResolver.resolve("FileLoader", "getPathToMessage", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("getPathToMessage", arrayOf<Class<*>>(tlMessageClass)),
                            object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    try {
                                        if (ConfigManager.secretMediaSave.isEnable && param.args[0] != null && pathImage != null) {
                                            val message = TLRPC.Message(param.args[0])
                                            if (message.id.toLong() == id) {
                                                param.result = pathImage
                                            }
                                        }
                                    } catch (e: Throwable) {
                                        Logger.e(e)
                                    }
                                }
                            }
                        )
                    )
                }

                if (ConfigManager.secretMediaSave.isEnable) SecretMediaViewer.openMedia()
            }
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    private fun bindPhotoViewerToActivity(cell: ChatMessageCell) {
        try {
            val cellView = cell.chatMessageCell as? View ?: return
            val activity = extractActivityFromContext(cellView.context) ?: return
            PhotoViewer.getInstance().setParentActivity(activity)
        } catch (_: Throwable) {}
    }

    private fun extractActivityFromContext(context: Context?): Activity? {
        var ctx = context
        while (ctx is ContextWrapper) {
            if (ctx is Activity) return ctx
            ctx = ctx.baseContext
        }
        return null
    }
}
