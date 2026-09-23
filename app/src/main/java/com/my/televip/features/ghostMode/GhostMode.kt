package com.my.televip.features.ghostMode

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Clients.ClientManager
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

object GhostMode {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val connClass = ClassLoad.getClass(ClassNames.CONNECTIONS_MANAGER)
                if (connClass != null && ConfigManager.isGhostMode()) {
                    val methodName = Obfuscate.getMethodName("ConnectionsManager", "sendRequestInternal")
                    val argTypes: Array<Class<*>> = arrayOf(
                        ClassLoad.getClass(ClassNames.TL_OBJECT)!!,
                        ClassLoad.getClass(ClassNames.REQUEST_DELEGATE)!!,
                        ClassLoad.getClass(ClassNames.REQUEST_DELEGATE_TIMESTAMP)!!,
                        ClassLoad.getClass(ClassNames.QUICK_ACK_DELEGATE)!!,
                        ClassLoad.getClass(ClassNames.WRITE_TO_SOCKET_DELEGATE)!!,
                        Int::class.javaPrimitiveType!!,
                        Int::class.javaPrimitiveType!!,
                        Int::class.javaPrimitiveType!!,
                        Boolean::class.javaPrimitiveType!!,
                        Int::class.javaPrimitiveType!!
                    )
                    val hook = object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            try {
                                if (HideSeen.isReadMessages) {
                                    HideSeen.isReadMessages = false
                                } else if (ConfigManager.isGhostMode()) {
                                    val objectParam = param.args[0] ?: return

                                    if (ClientManager.`is`(ClientManager.Client.Nagram)) {
                                        HideSeen.saveReadHistory(objectParam)
                                    }
                                    if (ConfigManager.hideOnline?.isEnable == true) {
                                        if (isOnlineRequest(objectParam)) {
                                            XposedHelpers.setBooleanField(
                                                objectParam,
                                                Obfuscate.getFieldName("TL_account\$updateStatus", "offline"),
                                                true
                                            )
                                        }
                                    }

                                    if (ConfigManager.hideSeen?.isEnable == true && HideSeen.isReadMessageRequest(objectParam)) {
                                        HideSeen.sendFakeReadResponse(param.args[1])
                                        param.result = null
                                        return
                                    }

                                    if (ConfigManager.hideTyping?.isEnable == true && HideTyping.isTypingRequest(objectParam)) {
                                        param.result = null
                                        return
                                    }

                                    if (ConfigManager.hideStoryView?.isEnable == true && HideStoryRead.isReadStoriesRequest(objectParam)) {
                                        param.result = null
                                        return
                                    }

                                    HideSeen.handleReadAfterSend(objectParam)
                                }
                            } catch (e: Throwable) {
                                Logger.e(e)
                            }
                        }
                    }
                    val merged = ArgsResolver.merge("sendRequestInternal", argTypes, hook)
                    if (merged != null) {
                        XposedHelpers.findAndHookMethod(connClass, methodName, *merged)
                    }
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }

    @JvmStatic
    fun isOnlineRequest(objectParam: Any): Boolean {
        return if (!ClientManager.isTgnetObfuscated()) {
            objectParam.javaClass.name.contains("TL_account\$updateStatus")
        } else {
            objectParam.javaClass == ClassLoad.getClass(ClassNames.TL_ACCOUNT_UPDATE_STATUS)
        }
    }
}
