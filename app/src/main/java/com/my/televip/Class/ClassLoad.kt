package com.my.televip.Class

import com.my.televip.Clients.ClientManager
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.utils.Utils
import de.robv.android.xposed.XposedHelpers
import java.util.HashMap
import java.util.WeakHashMap

object ClassLoad {

    private val cache: MutableMap<ClassLoader, MutableMap<String, Class<*>>> = WeakHashMap()

    @JvmStatic
    fun getClass(name: String): Class<*>? {
        return getClass(name, Utils.classLoader, true)
    }

    @JvmStatic
    @JvmOverloads
    fun getClass(name: String, classLoader: ClassLoader?, log: Boolean = true): Class<*>? {
        if (classLoader == null) return null
        val resolved = Obfuscate.getClassName(name)

        val loaderCache = cache.getOrPut(classLoader) { HashMap() }

        if (loaderCache.containsKey(resolved)) {
            return loaderCache[resolved]
        }

        try {
            val cls = XposedHelpers.findClassIfExists(resolved, classLoader)

            if (cls != null) {
                loaderCache[resolved] = cls
            } else {
                if (log) {
                    if ((ClientManager.`is`(ClientManager.Client.Nagram) ||
                                ClientManager.`is`(ClientManager.Client.Momogram) ||
                                ClientManager.`is`(ClientManager.Client.Nekogram)) &&
                        name == ClassNames.DRAWABLE
                    ) {
                        return null
                    }

                    if (name == resolved) {
                        Logger.w("Not found $name ${Utils.issue}")
                    } else {
                        Logger.w("Not found $name, $resolved ${Utils.issue}")
                    }
                }
            }

            return cls
        } catch (e: Throwable) {
            Logger.e(e)
            return null
        }
    }
}
