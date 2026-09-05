package com.my.televip.dex

import android.content.Context
import com.my.televip.ClientChecker
import com.my.televip.logging.Logger
import com.my.televip.utils.Utils
import dalvik.system.InMemoryDexClassLoader
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.zip.ZipFile

object DexInjector {

    @JvmField
    var classLoader: ClassLoader? = null

    @JvmStatic
    fun injectDex(context: Context?, parentClassLoader: ClassLoader) {
        if (ClientChecker.check(ClientChecker.ClientType.Nekogram) || ClientChecker.check(ClientChecker.ClientType.Cherrygram)) return
        try {
            val dexBytes = loadDexBytes(context)
            if (dexBytes == null || dexBytes.isEmpty()) {
                Logger.w("Dex bytes empty, skipping dex injection")
                return
            }

            val buffer = ByteBuffer.wrap(dexBytes)
            classLoader = InMemoryDexClassLoader(buffer, parentClassLoader)
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    private fun loadDexBytes(context: Context?): ByteArray? {
        if (context != null) {
            try {
                context.assets.open("settings_adapter.dex").use { isStream ->
                    ByteArrayOutputStream().use { osStream ->
                        val buffer = ByteArray(8192)
                        var bytesRead: Int
                        while (isStream.read(buffer).also { bytesRead = it } != -1) {
                            osStream.write(buffer, 0, bytesRead)
                        }
                        return osStream.toByteArray()
                    }
                }
            } catch (_: Throwable) {
            }
        }

        val modulePath = Utils.modulePath
        if (modulePath != null) {
            try {
                ZipFile(modulePath).use { zipFile ->
                    val entry = zipFile.getEntry("assets/settings_adapter.dex")
                    if (entry != null) {
                        zipFile.getInputStream(entry).use { isStream ->
                            ByteArrayOutputStream().use { osStream ->
                                val buffer = ByteArray(8192)
                                var bytesRead: Int
                                while (isStream.read(buffer).also { bytesRead = it } != -1) {
                                    osStream.write(buffer, 0, bytesRead)
                                }
                                return osStream.toByteArray()
                            }
                        }
                    }
                }
            } catch (e: Throwable) {
                Logger.e(e)
            }
        }
        return null
    }
}
