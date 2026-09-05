package com.my.televip.Class

import com.my.televip.ClientChecker
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.utils.Utils
import de.robv.android.xposed.XposedHelpers
import java.util.concurrent.ConcurrentHashMap

object ClassLoad {

    private val cache = ConcurrentHashMap<String, Class<*>>()

    @JvmStatic
    fun getClass(name: String): Class<*>? {
        val resolved = AutomationResolver.resolve(name)
        if (cache.containsKey(resolved)) {
            return cache[resolved]
        }
        return try {
            val cls = XposedHelpers.findClassIfExists(resolved, Utils.classLoader)
            if (cls != null) {
                cache[resolved] = cls
            } else {
                if ((ClientChecker.check(ClientChecker.ClientType.Nagram) || ClientChecker.check(ClientChecker.ClientType.Momogram)) && name == ClassNames.DRAWABLE) {
                    return null
                }
                Logger.w("Not found $name, $resolved ${Utils.issue}")
            }
            cls
        } catch (e: Throwable) {
            Logger.e(e)
            null
        }
    }

    @JvmStatic
    fun getClass(name: String, classLoader: ClassLoader): Class<*>? {
        val resolved = AutomationResolver.resolve(name)
        if (cache.containsKey(resolved)) {
            return cache[resolved]
        }
        return try {
            val cls = XposedHelpers.findClassIfExists(resolved, classLoader)
            if (cls != null) {
                cache[resolved] = cls
            } else {
                Logger.w("Not found Class $name, $resolved ${Utils.issue}")
            }
            cls
        } catch (e: Throwable) {
            Logger.e(e)
            null
        }
    }
}
