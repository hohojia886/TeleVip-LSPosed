package com.my.televip.obfuscate.struct

import java.util.concurrent.ConcurrentHashMap

object CustomResolverOverlay {

    private val classMap = ConcurrentHashMap<String, String>()
    private val fieldMap = ConcurrentHashMap<String, String>()
    private val methodMap = ConcurrentHashMap<String, String>()

    @JvmStatic
    fun putClassOverride(originalName: String, resolvedName: String) {
        classMap[originalName] = resolvedName
    }

    @JvmStatic
    fun putFieldOverride(className: String, fieldName: String, resolvedFieldName: String) {
        fieldMap["$className#$fieldName"] = resolvedFieldName
    }

    @JvmStatic
    fun putMethodOverride(className: String, methodName: String, resolvedMethodName: String) {
        methodMap["$className#$methodName"] = resolvedMethodName
    }

    @JvmStatic
    fun hasClassOverride(originalName: String): Boolean {
        return classMap.containsKey(originalName)
    }

    @JvmStatic
    fun getClassOverride(originalName: String): String? {
        return classMap[originalName]
    }

    @JvmStatic
    fun hasFieldOverride(className: String, fieldName: String): Boolean {
        return fieldMap.containsKey("$className#$fieldName")
    }

    @JvmStatic
    fun getFieldOverride(className: String, fieldName: String): String? {
        return fieldMap["$className#$fieldName"]
    }

    @JvmStatic
    fun hasMethodOverride(className: String, methodName: String): Boolean {
        return methodMap.containsKey("$className#$methodName")
    }

    @JvmStatic
    fun getMethodOverride(className: String, methodName: String): String? {
        return methodMap["$className#$methodName"]
    }
}
