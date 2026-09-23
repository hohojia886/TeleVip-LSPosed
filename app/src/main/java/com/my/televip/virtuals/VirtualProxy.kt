package com.my.televip.virtuals

import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

object VirtualProxy {

    @Suppress("UNCHECKED_CAST")
    fun <T> create(interfaceClass: Class<T>, target: Any?): T? {
        if (target == null) return null

        return Proxy.newProxyInstance(
            interfaceClass.classLoader,
            arrayOf(interfaceClass),
            VirtualInvocationHandler(target)
        ) as T
    }

    private class VirtualInvocationHandler(private val target: Any) : InvocationHandler {
        override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
            val virtualField = method.getAnnotation(VirtualField::class.java)
            if (virtualField != null) {
                val fieldName = if (virtualField.className.isNotEmpty()) {
                    Obfuscate.getFieldName(virtualField.className, virtualField.name)
                } else {
                    virtualField.name
                }
                return XposedHelpers.getObjectField(target, fieldName)
            }

            val virtualMethod = method.getAnnotation(VirtualMethod::class.java)
            if (virtualMethod != null) {
                val methodName = if (virtualMethod.className.isNotEmpty()) {
                    Obfuscate.getMethodName(virtualMethod.className, virtualMethod.name)
                } else {
                    virtualMethod.name
                }
                val callArgs = args ?: emptyArray()
                return XposedHelpers.callMethod(target, methodName, *callArgs)
            }

            if (method.name == "toString") return target.toString()
            if (method.name == "hashCode") return target.hashCode()
            if (method.name == "equals") return target == args?.getOrNull(0)

            throw UnsupportedOperationException("Method ${method.name} is missing @VirtualField or @VirtualMethod annotation")
        }
    }
}
