package com.my.televip.logging

import android.os.Build
import com.my.televip.application.ApplicationLoaderHook
import com.my.televip.obfuscate.struct.ResolverRegistry
import com.my.televip.utils.Utils
import de.robv.android.xposed.XposedBridge

object Logger {

    @JvmStatic
    fun w(text: String) {
        XposedBridge.log("[TeleVip] [Warning] pkgName: ${Utils.pkgName} $text")
    }

    @JvmStatic
    fun l(text: String) {
        XposedBridge.log("[TeleVip] pkgName: ${Utils.pkgName} $text")
    }

    @JvmStatic
    fun e(throwable: Throwable) {
        try {
            val log = StringBuilder().apply {
                append("[TeleVip] [Error] pkgName: ").append(Utils.pkgName).append(" ").append(throwable).append("\n")
                try {
                    val resolverClass = ResolverRegistry.getResolverClass()
                    if (resolverClass != null) {
                        append("appName = ").append(resolverClass.simpleName).append("\n")
                    }
                } catch (_: Throwable) {
                }

                try {
                    val context = ApplicationLoaderHook.getApplicationContext()
                    if (context != null) {
                        val pm = context.packageManager
                        val info = pm.getPackageInfo(context.packageName, 0)
                        append("versionName: ").append(info.versionName).append("\n")
                        @Suppress("DEPRECATION")
                        append("versionCode: ").append(info.versionCode).append("\n")
                    }
                } catch (_: Throwable) {
                    append("versionName/versionCode: error retrieving\n")
                }

                append("OS Version: ").append(Build.VERSION.RELEASE).append("\n")
                append("SDK: ").append(Build.VERSION.SDK_INT).append("\n")
                append("Manufacturer: ").append(Build.MANUFACTURER).append("\n")
                append("Model: ").append(Build.MODEL).append("\n")

                for (element in throwable.stackTrace) {
                    append("[TeleVip] at ").append(element.toString()).append("\n")
                }
            }

            XposedBridge.log(log.toString())
        } catch (_: Throwable) {
        }
    }
}
