package com.my.televip.hooks

import com.my.televip.ClientChecker
import com.my.televip.base.AbstractMethodHook
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.Method

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
    fun hookConstructor(cls: Class<*>?, vararg args: Any) {
        try {
            if (cls != null) {
                XposedHelpers.findAndHookConstructor(cls, *args)
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
                    if (ClientChecker.check(ClientChecker.ClientType.Nagram) && name == "formatPmEditedDate") continue
                    XposedHelpers.findAndHookMethod(
                        cls,
                        AutomationResolver.resolve(className, name, AutomationResolver.ResolverType.Method),
                        *args
                    )
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }

    @JvmStatic
    fun hookMethod(method: Method?, callback: AbstractMethodHook) {
        try {
            if (method != null) {
                XposedBridge.hookMethod(method, callback)
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }
}
