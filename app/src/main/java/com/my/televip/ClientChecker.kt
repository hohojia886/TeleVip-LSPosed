package com.my.televip

import com.my.televip.Clients.Cherrygram
import com.my.televip.Clients.Forkgram
import com.my.televip.Clients.ForkgramBeta
import com.my.televip.Clients.ForkgramClassic
import com.my.televip.Clients.IMe
import com.my.televip.Clients.IMeWeb
import com.my.televip.Clients.Momogram
import com.my.televip.Clients.Nagram
import com.my.televip.Clients.NagramX
import com.my.televip.Clients.NagramXF
import com.my.televip.Clients.Nekogram
import com.my.televip.Clients.Nicegram
import com.my.televip.Clients.TGConnect
import com.my.televip.Clients.Telega
import com.my.televip.Clients.Telegram
import com.my.televip.Clients.TelegramBeta
import com.my.televip.Clients.TelegramPlus
import com.my.televip.Clients.TelegramWeb
import com.my.televip.Clients.Telegraph
import com.my.televip.Clients.Turrit
import com.my.televip.Clients.XPlus
import com.my.televip.utils.Utils

object ClientChecker {

    @JvmStatic
    fun check(client: ClientType, pkgName: String?): Boolean {
        return client.packageNames.contains(pkgName)
    }

    @JvmStatic
    fun check(client: ClientType): Boolean {
        return check(client, Utils.pkgName)
    }

    @JvmStatic
    fun isTgnetObfuscated(): Boolean {
        val type = ClientType.fromPackage(Utils.pkgName)
        return type != null && type.isTgnetObfuscated
    }

    enum class ClientType(
        val packageNames: Array<String>,
        val resolverClass: Class<*>,
        val isTgnetObfuscated: Boolean = false
    ) {
        Telegram("org.telegram.messenger", Telegram::class.java),
        TelegramWeb("org.telegram.messenger.web", TelegramWeb::class.java),
        TelegramPlus("org.telegram.plus", TelegramPlus::class.java),
        TGConnect("com.tgconnect.android", TGConnect::class.java),
        Nagram("xyz.nextalone.nagram", Nagram::class.java),
        Nicegram("app.nicegram", Nicegram::class.java),
        TelegramBeta("org.telegram.messenger.beta", TelegramBeta::class.java),
        NagramX("nu.gpu.nagram", NagramX::class.java),
        XPlus("com.xplus.messenger", XPlus::class.java),
        iMe("com.iMe.android", IMe::class.java),
        iMeWeb("com.iMe.android.web", IMeWeb::class.java),
        forkgram("org.forkgram.messenger", Forkgram::class.java),
        forkgramBeta("org.forkclient.messenger.beta", ForkgramBeta::class.java),
        Telegraph("ir.ilmili.telegraph", Telegraph::class.java),
        Telega("ru.dahl.messenger", Telega::class.java),
        Momogram(arrayOf("nekox.messenger.broken", "momo.gram"), Momogram::class.java),
        Nekogram("tw.nekomimi.nekogram", Nekogram::class.java, true),
        Cherrygram("uz.unnarsx.cherrygram", Cherrygram::class.java, true),
        ForkgramClassic("org.forkgram.classic", ForkgramClassic::class.java),
        Turrit("org.telegram.group", Turrit::class.java),
        NagramXF("fork.risin42.nagramx", NagramXF::class.java);

        constructor(packageName: String, resolverClass: Class<*>) : this(arrayOf(packageName), resolverClass, false)
        constructor(packageName: String, resolverClass: Class<*>, tgnetObfuscated: Boolean) : this(arrayOf(packageName), resolverClass, tgnetObfuscated)

        companion object {
            @JvmStatic
            fun fromPackage(pkg: String?): ClientType? {
                if (pkg == null) return null
                return entries.firstOrNull { type -> type.packageNames.contains(pkg) }
            }

            @JvmStatic
            fun containsPackage(pkg: String?): Boolean {
                return fromPackage(pkg) != null
            }
        }
    }
}
