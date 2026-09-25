package com.my.televip.clients

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import java.util.ArrayList

object Telegraph {

    object ParameterResolver {
        private val objectList: MutableMap<String, Array<Class<*>?>?> = HashMap()

        @JvmStatic
        fun register(name: String, classes: Array<Class<*>?>?) {
            objectList[name] = classes
        }

        @JvmStatic
        fun resolve(name: String): Array<Class<*>?>? {
            return objectList[name]
        }

        @JvmStatic
        fun has(name: String): Boolean {
            return objectList[name] != null
        }
    }

    @JvmStatic
    fun loadParameter() {
        ParameterResolver.register(
            "fillMessageMenu",
            arrayOf(
                ClassLoad.getClass(ClassNames.MESSAGE_OBJECT),
                ClassLoad.getClass(ClassNames.MESSAGE_OBJECT),
                ArrayList::class.java,
                ArrayList::class.java,
                ArrayList::class.java
            )
        )
    }

    @JvmStatic
    fun removeAd() {
        try {
            val connectionsManager = ClassLoad.getClass(ClassNames.CONNECTIONS_MANAGER)
            if (connectionsManager != null) {
                HMethod.hookMethod(
                    connectionsManager,
                    "native_expireFile",
                    Long::class.javaPrimitiveType!!,
                    object : XC_MethodReplacement() {
                        override fun replaceHookedMethod(param: MethodHookParam): Any {
                            return false
                        }
                    }
                )
                HMethod.hookMethod(
                    connectionsManager,
                    "native_daysFile",
                    Long::class.javaPrimitiveType!!,
                    object : XC_MethodReplacement() {
                        override fun replaceHookedMethod(param: MethodHookParam): Any {
                            return 999
                        }
                    }
                )
                HMethod.hookMethod(
                    connectionsManager,
                    "native_checkLicense",
                    Long::class.javaPrimitiveType!!,
                    object : XC_MethodReplacement() {
                        override fun replaceHookedMethod(param: MethodHookParam): Any {
                            return true
                        }
                    }
                )
                HMethod.hookMethod(
                    connectionsManager,
                    "native_removeInstance",
                    Int::class.javaPrimitiveType!!,
                    Boolean::class.javaPrimitiveType!!,
                    object : XC_MethodReplacement() {
                        override fun replaceHookedMethod(param: MethodHookParam): Any {
                            return true
                        }
                    }
                )
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }
}
