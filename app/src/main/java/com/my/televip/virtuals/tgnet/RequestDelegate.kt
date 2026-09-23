package com.my.televip.virtuals.tgnet

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.utils.Utils
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.Proxy

class RequestDelegate(@JvmField val requestDelegate: Any?) {

    fun run(response: Any?, error: Any?) {
        val target = requestDelegate ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("RequestDelegate", "run"), response, error)
    }

    fun interface Callback {
        fun run(response: Any?, error: Any?)
    }

    companion object {
        @JvmStatic
        fun run(lambda: Callback): Any? {
            val delegateClass = ClassLoad.getClass(ClassNames.REQUEST_DELEGATE) ?: return null
            return Proxy.newProxyInstance(
                Utils.classLoader,
                arrayOf(delegateClass)
            ) { _, method, args ->
                if (method.parameterCount == 2 && method.parameterTypes[0] == ClassLoad.getClass(ClassNames.TL_OBJECT)) {
                    lambda.run(args?.getOrNull(0), args?.getOrNull(1))
                }
                null
            }
        }
    }
}
