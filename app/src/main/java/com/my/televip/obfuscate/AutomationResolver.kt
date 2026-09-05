package com.my.televip.obfuscate

import com.my.televip.obfuscate.struct.CustomResolverOverlay
import com.my.televip.obfuscate.struct.ResolverRegistry
import de.robv.android.xposed.XC_MethodHook

object AutomationResolver {

    @JvmField
    var resolverRegistry: ResolverRegistry = ResolverRegistry(ResolverRegistry.getResolverClass())

    @JvmStatic
    fun resolve(className: String): String {
        if (CustomResolverOverlay.hasClassOverride(className)) {
            return CustomResolverOverlay.getClassOverride(className) ?: className
        }
        if (resolverRegistry.hasClass(className)) {
            return resolverRegistry.resolveClass(className) ?: className
        }
        return className
    }

    @JvmStatic
    @JvmSuppressWildcards
    fun resolveObject(name: String, classes: Array<Class<*>>): Array<Class<*>> {
        if (resolverRegistry.hasParameter(name)) {
            val res = resolverRegistry.resolveParameter(name)
            if (res != null) return res
        }
        return classes
    }

    @JvmStatic
    fun resolve(className: String, name: String, type: ResolverType): String {
        var resolvedName = name
        if (type == ResolverType.Field) {
            if (CustomResolverOverlay.hasFieldOverride(className, resolvedName)) {
                return CustomResolverOverlay.getFieldOverride(className, resolvedName) ?: resolvedName
            }
            if (resolverRegistry.hasField(className, resolvedName)) {
                return resolverRegistry.resolveField(className, resolvedName) ?: resolvedName
            }
        }
        val nameMethod = resolverRegistry.resolveMethodName(className, resolvedName)
        if (type == ResolverType.Method) {
            if (CustomResolverOverlay.hasMethodOverride(className, nameMethod)) {
                return CustomResolverOverlay.getMethodOverride(className, nameMethod) ?: nameMethod
            }
            if (resolverRegistry.hasMethod(className, nameMethod)) {
                return resolverRegistry.resolveMethod(className, nameMethod) ?: nameMethod
            }
        }

        resolvedName = resolvedName.replace("storyEntitiesAllowed2", "storyEntitiesAllowed")
        resolvedName = resolvedName.replace("hasStories2", "hasStories")

        return resolvedName
    }

    @JvmStatic
    fun merge(classes: Array<out Any>?, hook: XC_MethodHook): Array<Any> {
        if (classes != null) {
            val result = arrayOfNulls<Any>(classes.size + 1)
            System.arraycopy(classes, 0, result, 0, classes.size)
            result[classes.size] = hook
            @Suppress("UNCHECKED_CAST")
            return result as Array<Any>
        }
        return arrayOf(hook)
    }

    enum class ResolverType {
        Field,
        Method
    }
}
