package com.my.televip.obfuscate.struct

import com.my.televip.clients.ClientManager

class ResolverRegistry(private val clazz: Class<*>?) {

    private var finalParameter: Class<*>? = null

    init {
        try {
            if (clazz != null) {
                var parameterResolverClass: Class<*>? = null
                for (inner in clazz.declaredClasses) {
                    if (inner.simpleName == "ParameterResolver") {
                        parameterResolverClass = inner
                    }
                }
                finalParameter = parameterResolverClass
            }
        } catch (ignored: Throwable) {
        }
    }

    fun hasParameter(name: String): Boolean {
        return try {
            val paramClass = finalParameter ?: return false
            val method = paramClass.getMethod("has", String::class.java)
            (method.invoke(null, name) as? Boolean) ?: false
        } catch (e: Throwable) {
            false
        }
    }

    fun resolveParameter(name: String): Array<Class<*>>? {
        return try {
            val paramClass = finalParameter ?: return null
            val method = paramClass.getMethod("resolve", String::class.java)
            @Suppress("UNCHECKED_CAST")
            method.invoke(null, name) as? Array<Class<*>>
        } catch (e: Throwable) {
            null
        }
    }

    fun loadParameter() {
        try {
            clazz?.getMethod("loadParameter")?.invoke(null)
        } catch (ignored: Throwable) {
        }
    }

    companion object {
        @JvmStatic
        fun getResolverClass(): Class<*>? {
            val clientType = ClientManager.getCurrent() ?: return null
            return clientType.resolverClass
        }
    }
}
