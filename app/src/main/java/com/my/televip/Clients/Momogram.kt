package com.my.televip.Clients

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import java.util.ArrayList

object Momogram {

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
}
