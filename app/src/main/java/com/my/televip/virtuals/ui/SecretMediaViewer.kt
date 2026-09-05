package com.my.televip.virtuals.ui

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.features.SecretMediaSave
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.messenger.FileLoader
import com.my.televip.virtuals.messenger.MessageObject
import com.my.televip.virtuals.messenger.UserConfig

object SecretMediaViewer {

    private var isEnable = false

    @JvmStatic
    fun openMedia() {
        try {
            if (!isEnable) {
                isEnable = true

                val secretMediaViewerClass = ClassLoad.getClass(ClassNames.SECRET_MEDIA_VIEWER) ?: return

                val msgObjClass = ClassLoad.getClass(ClassNames.MESSAGE_OBJECT) ?: return
                val providerClass = ClassLoad.getClass(ClassNames.PHOTO_VIEWER_PROVIDER) ?: return

                HMethod.hookMethod(
                    secretMediaViewerClass,
                    AutomationResolver.resolve("SecretMediaViewer", "openMedia", AutomationResolver.ResolverType.Method),
                    *AutomationResolver.merge(
                        AutomationResolver.resolveObject("openMedia", arrayOf<Class<*>>(msgObjClass, providerClass, Runnable::class.java, Runnable::class.java)),
                        object : AbstractMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                val secretMediaSave = ConfigManager.secretMediaSave.isEnable
                                if (ConfigManager.preventMedia.isEnable && !secretMediaSave) {
                                    param.args[2] = null
                                    param.args[3] = null

                                    val messageObject = MessageObject(param.args[0])
                                    if (messageObject.messageObject != null) {
                                        val messageOwner = messageObject.getMessageOwner()
                                        if (messageOwner.message != null) {
                                            messageOwner.setTtl(0)
                                        }
                                    }
                                }

                                if (secretMediaSave && (param.args.size >= 2 || param.args[0] != null)) {
                                    val messageObject = MessageObject(param.args[0])
                                    val provider = PhotoViewer.PhotoViewerProvider(param.args[1])
                                    val obj = provider.getPlaceForPhoto(messageObject, null, 0, true, false)
                                    if (obj.getImageReceiver().imageReceiver != null) {
                                        val fileLoader = FileLoader.getInstance(UserConfig.getSelectedAccount())
                                        val image = fileLoader.getLocalFile(obj.getImageReceiver().getImageLocation())
                                        if (image != null) {
                                            SecretMediaSave.pathImage = image
                                            SecretMediaSave.id = messageObject.getMessageOwner().getID().toLong()
                                        }
                                        PhotoViewer.getInstance().openPhoto(messageObject, messageObject.getDialogId(), 0L, 0L, provider, false)
                                        param.result = null
                                    }
                                }
                            }
                        }
                    )
                )
            }
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }
}
