package com.my.televip.virtuals.tgnet

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.utils.Utils
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.Proxy

class RequestDelegate(@JvmField val requestDelegateObj: Any?) {

    fun run(response: Any?, error: Any?) {
        XposedHelpers.callMethod(
            requestDelegateObj,
            AutomationResolver.resolve("RequestDelegate", "run", AutomationResolver.ResolverType.Method),
            response,
            error
        )
    }

    fun interface Callback {
        fun run(response: Any?, error: Any?)
    }

    companion object {
        @JvmStatic
        fun run(lambda: Callback): Any? {
            val requestDelegateClass = ClassLoad.getClass(ClassNames.REQUEST_DELEGATE)
            if (requestDelegateClass != null) {
                return Proxy.newProxyInstance(
                    Utils.classLoader,
                    arrayOf(requestDelegateClass)
                ) { _, method, args ->
                    if (method.parameterCount == 2 && method.parameterTypes[0] == ClassLoad.getClass(ClassNames.TL_OBJECT)) {
                        lambda.run(args[0], args[1])
                    }
                    null
                }
            }
            return null
        }
    }
}
