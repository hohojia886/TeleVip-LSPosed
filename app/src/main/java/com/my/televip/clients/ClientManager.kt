package com.my.televip.clients

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.utils.Utils

object ClientManager {

    enum class Client(
        val pkg: String,
        val isTgnetObfuscated: Boolean = false,
        private val resolverClassName: String? = null
    ) {
        Telegram("org.telegram.messenger"),
        TelegramWeb("org.telegram.messenger.web"),
        TelegramPlus("org.telegram.plus"),
        TGConnect("com.tgconnect.android"),
        Nagram("xyz.nextalone.nagram", resolverClassName = "com.my.televip.clients.Nagram"),
        Nicegram("app.nicegram", resolverClassName = "com.my.televip.clients.Nicegram"),
        TelegramBeta("org.telegram.messenger.beta"),
        NagramX("nu.gpu.nagram"),
        XPlus("com.xplus.messenger"),
        iMe("com.iMe.android"),
        iMeWeb("com.iMe.android.web"),
        forkgram("org.forkgram.messenger"),
        forkgramBeta("org.forkclient.messenger.beta"),
        Telegraph("ir.ilmili.telegraph", resolverClassName = "com.my.televip.clients.Telegraph"),
        Telega("ru.dahl.messenger"),
        Momogram("nekox.messenger.broken", resolverClassName = "com.my.televip.clients.Momogram"),
        Nekogram("tw.nekomimi.nekogram", isTgnetObfuscated = true),
        Cherrygram("uz.unnarsx.cherrygram", isTgnetObfuscated = true),
        ForkgramClassic("org.forkgram.classic"),
        Turrit("org.telegram.group", resolverClassName = "com.my.televip.clients.Turrit"),
        NagramXF("fork.risin42.nagramx");

        val resolverClass: Class<*>?
            get() = resolverClassName?.let {
                try {
                    Class.forName(it)
                } catch (e: Throwable) {
                    null
                }
            }

        fun hasPackage(pkg: String?): Boolean {
            return this.pkg == pkg
        }
    }

    @JvmStatic
    fun `is`(client: Client): Boolean {
        return `is`(client, Utils.pkgName)
    }

    @JvmStatic
    fun `is`(client: Client, pkg: String?): Boolean {
        return client.hasPackage(pkg)
    }

    @JvmStatic
    fun getCurrent(): Client? {
        val currentPkg = Utils.pkgName
        for (client in Client.entries) {
            if (client.hasPackage(currentPkg)) {
                return client
            }
        }
        return null
    }

    @JvmStatic
    fun isTgnetObfuscated(): Boolean {
        val client = getCurrent()
        return client != null && client.isTgnetObfuscated
    }

    @JvmStatic
    fun containsPackage(pkg: String?, classLoader: ClassLoader?): Boolean {
        for (client in Client.entries) {
            if (client.hasPackage(pkg)) {
                return true
            }
        }
        return ClassLoad.getClass(ClassNames.CONNECTIONS_MANAGER, classLoader, false) != null &&
                ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER, classLoader, false) != null &&
                ClassLoad.getClass(ClassNames.MESSAGES_STORAGE, classLoader, false) != null
    }
}
