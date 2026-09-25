package com.my.televip.features.media

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.configs.ConfigManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.messenger.MessageObject
import com.my.televip.virtuals.tgnet.TLRPC
import com.my.televip.virtuals.ui.Cells.ChatMessageCell
import com.my.televip.virtuals.ui.PhotoViewer
import com.my.televip.virtuals.ui.SecretMediaViewer
import de.robv.android.xposed.XposedHelpers
import java.io.File

object SecretMediaSave {

    @JvmField
    var isEnable: Boolean = false

    @JvmField
    var id: Long = 0

    @JvmField
    var pathImage: File? = null

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val msgObjClass = ClassLoad.getClass(ClassNames.MESSAGE_OBJECT)
                if (msgObjClass != null) {
                    HMethod.hookMethod(
                        msgObjClass,
                        Obfuscate.getMethodName("MessageObject", "isSecret"),
                        object : BaseMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (ConfigManager.secretMediaSave?.isEnable == true) param.result = false
                            }
                        }
                    )
                }

                val delegateClass = ClassLoad.getClass(ClassNames.CHAT_MESSAGE_CELL_DELEGATE)
                if (delegateClass != null) {
                    val pressTypes: Array<Class<*>> = arrayOf(
                        ClassLoad.getClass(ClassNames.CHAT_MESSAGE_CELL)!!,
                        Float::class.javaPrimitiveType!!,
                        Float::class.javaPrimitiveType!!,
                        Boolean::class.javaPrimitiveType!!
                    )
                    val pressMerged = ArgsResolver.merge("didPressImage", pressTypes, object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            try {
                                if (ConfigManager.secretMediaSave?.isEnable == true && param.args[0] != null) {
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
                    })
                    if (pressMerged != null) {
                        XposedHelpers.findAndHookMethod(
                            delegateClass,
                            Obfuscate.getMethodName("ChatActivity\$ChatMessageCellDelegate", "didPressImage"),
                            *pressMerged
                        )
                    }
                }

                val loaderClass = ClassLoad.getClass(ClassNames.FILE_LOADER)
                if (loaderClass != null) {
                    val pathTypes: Array<Class<*>> = arrayOf(ClassLoad.getClass(ClassNames.MESSAGE)!!)
                    val pathMerged = ArgsResolver.merge("getPathToMessage", pathTypes, object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            try {
                                if (ConfigManager.secretMediaSave?.isEnable == true && param.args[0] != null && pathImage != null) {
                                    val message = TLRPC.Message(param.args[0])
                                    if (message.id.toLong() == id) {
                                        param.result = pathImage
                                    }
                                }
                            } catch (e: Throwable) {
                                Logger.e(e)
                            }
                        }
                    })
                    if (pathMerged != null) {
                        XposedHelpers.findAndHookMethod(
                            loaderClass,
                            Obfuscate.getMethodName("FileLoader", "getPathToMessage"),
                            *pathMerged
                        )
                    }
                }

                if (ConfigManager.secretMediaSave?.isEnable == true) SecretMediaViewer.openMedia()
            }
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    private fun bindPhotoViewerToActivity(cell: ChatMessageCell) {
        try {
            if (cell.chatMessageCell !is View) return
            val view = cell.chatMessageCell as View
            val context = view.context
            val activity = extractActivityFromContext(context) ?: return
            PhotoViewer.getInstance().setParentActivity(activity)
        } catch (ignored: Throwable) {
        }
    }

    private fun extractActivityFromContext(contextParam: Context?): Activity? {
        var context = contextParam
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        return null
    }
}
