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
    fun e(text: String) {
        XposedBridge.log("[TeleVip] [Error] Ai: $text")
    }

    @JvmStatic
    fun e(throwable: Throwable) {
        try {
            val log = StringBuilder()
            log.append("[TeleVip] [Error] pkgName: ").append(Utils.pkgName).append(" ").append(throwable).append("\n")
            log.append("appName = ").append(ResolverRegistry.getResolverClass()?.simpleName).append("\n")

            try {
                val context = ApplicationLoaderHook.getApplicationContext()
                val pm = context.packageManager
                val info = pm.getPackageInfo(context.packageName, 0)
                log.append("versionName: ").append(info.versionName).append("\n")
                log.append("versionCode: ").append(info.versionCode).append("\n")
            } catch (e: Throwable) {
                log.append("versionName/versionCode: error retrieving\n")
            }

            log.append("OS Version: ").append(Build.VERSION.RELEASE).append("\n")
            log.append("SDK: ").append(Build.VERSION.SDK_INT).append("\n")
            log.append("Manufacturer: ").append(Build.MANUFACTURER).append("\n")
            log.append("Model: ").append(Build.MODEL).append("\n")

            for (element in throwable.stackTrace) {
                log.append("[TeleVip] at ").append(element.toString()).append("\n")
            }

            XposedBridge.log(log.toString())
        } catch (g: Throwable) {
            // Ignored
        }
    }
}
