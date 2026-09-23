package com.my.televip.dex

import com.my.televip.Clients.ClientManager
import com.my.televip.logging.Logger
import dalvik.system.InMemoryDexClassLoader
import java.nio.ByteBuffer

object DexInjector {

    @JvmField
    var classLoader: ClassLoader? = null

    @JvmStatic
    fun injectDex(classLoader: ClassLoader?) {
        if (ClientManager.`is`(ClientManager.Client.Nekogram) ||
            ClientManager.`is`(ClientManager.Client.Cherrygram)
        ) return
        try {
            val dexBytes = DexHolder.DEX_BYTES
            val buffer = ByteBuffer.wrap(dexBytes)

            DexInjector.classLoader = InMemoryDexClassLoader(
                buffer,
                classLoader
            )
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }
}
