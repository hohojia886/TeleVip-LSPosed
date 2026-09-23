package com.my.televip.obfuscate

import com.my.televip.obfuscate.struct.ResolverRegistry
import de.robv.android.xposed.XC_MethodHook

object ArgsResolver {

    @JvmField
    var resolverRegistry: ResolverRegistry = ResolverRegistry(ResolverRegistry.getResolverClass())

    @JvmStatic
    fun resolveObject(name: String, classes: Array<Class<*>>): Array<Class<*>> {
        if (resolverRegistry.hasParameter(name)) {
            val resolved = resolverRegistry.resolveParameter(name)
            if (resolved != null) {
                return resolved
            }
        }
        return classes
    }

    @JvmStatic
    fun merge(name: String, classes: Array<Class<*>>, hook: XC_MethodHook): Array<Any?>? {
        val c = resolveObject(name, classes)
        val result = arrayOfNulls<Any>(c.size + 1)
        System.arraycopy(c, 0, result, 0, c.size)
        result[c.size] = hook
        return result
    }
}
