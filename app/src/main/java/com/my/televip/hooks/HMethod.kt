package com.my.televip.hooks

import com.my.televip.clients.ClientManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.Obfuscate
import java.lang.reflect.Method
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

object HMethod {

    @JvmStatic
    fun hookMethod(cls: Class<*>?, name: String, vararg args: Any) {
        try {
            if (cls != null) {
                XposedHelpers.findAndHookMethod(cls, name, *args)
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }

    @JvmStatic
    fun hookMethod(cls: Class<*>?, className: String, names: Array<String>, vararg args: Any) {
        try {
            if (cls != null) {
                for (name in names) {
                    if (ClientManager.`is`(ClientManager.Client.Nagram) && name == "formatPmEditedDate") continue
                    XposedHelpers.findAndHookMethod(cls, Obfuscate.getMethodName(className, name), *args)
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }

    @JvmStatic
    fun hookMethod(method: Method?, callback: BaseMethodHook) {
        try {
            if (method != null) {
                XposedBridge.hookMethod(method, callback)
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }
}
