package com.my.televip.Clients

import com.my.televip.obfuscate.struct.ClassInfo
import com.my.televip.obfuscate.struct.FieldInfo
import com.my.televip.obfuscate.struct.MethodInfo

class NagramX {
    companion object {
        private val classList = ArrayList<ClassInfo>()
        private val fieldList = ArrayList<FieldInfo>()
        private val methodList = ArrayList<MethodInfo>()

        @JvmStatic
        fun loadParameter() {}
    }

    object ClassResolver {
        @JvmStatic
        fun resolve(name: String): String? = classList.firstOrNull { it.original == name }?.resolved

        @JvmStatic
        fun has(name: String): Boolean = classList.any { it.original == name }
    }

    object FieldResolver {
        @JvmStatic
        fun resolve(className: String, name: String): String? =
            fieldList.firstOrNull { it.className == className && it.original == name }?.resolved

        @JvmStatic
        fun has(className: String, name: String): Boolean =
            fieldList.any { it.className == className && it.original == name }
    }

    object MethodResolver {
        @JvmStatic
        fun resolve(className: String, name: String): String? =
            methodList.firstOrNull { it.className == className && it.original == name }?.resolved

        @JvmStatic
        fun has(className: String, name: String): Boolean =
            methodList.any { it.className == className && it.original == name }
    }

    object ParameterResolver {
        private val objectList = HashMap<String, Array<Class<*>>>()

        @JvmStatic
        fun register(name: String, classes: Array<Class<*>>) {
            objectList[name] = classes
        }

        @JvmStatic
        fun resolve(name: String): Array<Class<*>>? = objectList[name]

        @JvmStatic
        fun has(name: String): Boolean = objectList.containsKey(name)
    }
}
