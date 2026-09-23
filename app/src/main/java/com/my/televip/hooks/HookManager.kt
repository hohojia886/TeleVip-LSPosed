package com.my.televip.hooks

import com.my.televip.Class.ClassLoad
import com.my.televip.base.BaseMethodHook
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedBridge
import java.lang.reflect.Method
import java.lang.reflect.Modifier

object HookManager {

    @JvmStatic
    fun registerHooks(hookClass: Class<*>?) {
        if (hookClass == null) return

        val methods = hookClass.declaredMethods
        for (method in methods) {
            if (method.isAnnotationPresent(HookTarget::class.java)) {
                if (!Modifier.isStatic(method.modifiers)) {
                    Logger.e(IllegalStateException("HookTarget methods must be static: " + method.name))
                    continue
                }

                val annotation = method.getAnnotation(HookTarget::class.java)
                if (annotation != null) {
                    applyHook(annotation.clazz, annotation.method, method)
                }
            }
        }
    }

    private fun applyHook(className: String, methodName: String, hookMethod: Method) {
        try {
            val targetClass = ClassLoad.getClass(className)
            if (targetClass == null) {
                Logger.e(ClassNotFoundException("Hook target class not found: $className"))
                return
            }

            val resolvedMethodName = Obfuscate.getMethodName(className, methodName)

            var hooked = false
            for (target in targetClass.declaredMethods) {
                if (target.name == resolvedMethodName) {
                    XposedBridge.hookMethod(target, object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            try {
                                hookMethod.isAccessible = true
                                hookMethod.invoke(null, param)
                            } catch (t: Throwable) {
                                Logger.e(RuntimeException("Error executing hook: " + hookMethod.name, t))
                            }
                        }
                    })
                    hooked = true
                }
            }

            if (!hooked) {
                Logger.w("No matching method found for hook: $className#$methodName")
            }

        } catch (t: Throwable) {
            Logger.e(RuntimeException("Failed to register hook for $className#$methodName", t))
        }
    }
}
