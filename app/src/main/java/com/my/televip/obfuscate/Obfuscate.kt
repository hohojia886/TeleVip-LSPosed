package com.my.televip.obfuscate

object Obfuscate {

    @JvmStatic
    fun getClassName(className: String): String {
        val data = ObfuscationManager.current() ?: return className
        return data.resolveClass(className)
    }

    @JvmStatic
    fun getMethodName(className: String, methodName: String): String {
        val data = ObfuscationManager.current()
            ?: return methodName.replace("storyEntitiesAllowed2", "storyEntitiesAllowed")
        return data.resolveMethod(className, methodName)
    }

    @JvmStatic
    fun getFieldName(className: String, fieldName: String): String {
        val data = ObfuscationManager.current() ?: return fieldName
        return data.resolveField(className, fieldName)
    }
}
