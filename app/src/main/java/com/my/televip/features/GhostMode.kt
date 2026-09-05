package com.my.televip.features

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.ClientChecker
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import java.util.concurrent.atomic.AtomicBoolean

object GhostMode {

    @JvmStatic
    val isEnable: AtomicBoolean = AtomicBoolean(false)

    @JvmStatic
    fun init() {
        try {
            if (!isEnable.getAndSet(true)) {
                val connManagerClass = ClassLoad.getClass(ClassNames.CONNECTIONS_MANAGER)
                if (connManagerClass != null && ConfigManager.isGhostMode()) {
                    val methodName = AutomationResolver.resolve("ConnectionsManager", "sendRequestInternal", AutomationResolver.ResolverType.Method)
                    val tlObj = ClassLoad.getClass(ClassNames.TL_OBJECT)
                    val reqDel = ClassLoad.getClass(ClassNames.REQUEST_DELEGATE)
                    val reqDelTs = ClassLoad.getClass(ClassNames.REQUEST_DELEGATE_TIMESTAMP)
                    val quickAck = ClassLoad.getClass(ClassNames.QUICK_ACK_DELEGATE)
                    val writeSocket = ClassLoad.getClass(ClassNames.WRITE_TO_SOCKET_DELEGATE)
                    if (tlObj != null && reqDel != null && reqDelTs != null && quickAck != null && writeSocket != null) {
                        val paramTypes = AutomationResolver.resolveObject(
                            "sendRequestInternal",
                            arrayOf(
                                tlObj,
                                reqDel,
                                reqDelTs,
                                quickAck,
                                writeSocket,
                                Int::class.javaPrimitiveType!!,
                                Int::class.javaPrimitiveType!!,
                                Int::class.javaPrimitiveType!!,
                                Boolean::class.javaPrimitiveType!!,
                                Int::class.javaPrimitiveType!!
                            )
                        )

                        HMethod.hookMethod(
                            connManagerClass,
                            methodName,
                            *AutomationResolver.merge(paramTypes, object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    try {
                                        if (HideSeen.isReadMessages.get()) {
                                            HideSeen.isReadMessages.set(false)
                                        } else if (ConfigManager.isGhostMode()) {
                                            val reqObject = param.args[0] ?: return
                                            if (ClientChecker.check(ClientChecker.ClientType.Nagram)) {
                                                HideSeen.saveReadHistory(reqObject)
                                            }
                                            if (ConfigManager.hideOnline.isEnable) {
                                                if (isOnlineRequest(reqObject)) {
                                                    XposedHelpers.setBooleanField(
                                                        reqObject,
                                                        AutomationResolver.resolve("TL_account\$updateStatus", "offline", AutomationResolver.ResolverType.Field),
                                                        true
                                                    )
                                                }
                                            }

                                            if (ConfigManager.hideSeen.isEnable && HideSeen.isReadMessageRequest(reqObject)) {
                                                HideSeen.sendFakeReadResponse(param.args[1])
                                                param.result = null
                                                return
                                            }

                                            if (ConfigManager.hideTyping.isEnable && HideTyping.isTypingRequest(reqObject)) {
                                                param.result = null
                                                return
                                            }

                                            if (ConfigManager.hideStoryView.isEnable && HideStoryRead.isReadStoriesRequest(reqObject)) {
                                                param.result = null
                                                return
                                            }

                                            HideSeen.handleReadAfterSend(reqObject)
                                        }
                                    } catch (e: Throwable) {
                                        XposedBridge.log(e)
                                    }
                                }
                            })
                        )
                    }
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }

    @JvmStatic
    fun isOnlineRequest(obj: Any): Boolean {
        return if (!ClientChecker.isTgnetObfuscated()) {
            obj.javaClass.name.contains("TL_account\$updateStatus")
        } else {
            obj.javaClass.name == AutomationResolver.resolve(ClassNames.TL_ACCOUNT_UPDATE_STATUS)
        }
    }
}
