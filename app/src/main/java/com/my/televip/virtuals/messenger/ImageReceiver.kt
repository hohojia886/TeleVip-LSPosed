package com.my.televip.virtuals.messenger

import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

class ImageReceiver(@JvmField val imageReceiver: Any?) {
    fun getImageLocation(): ImageLocation {
        return ImageLocation(
            XposedHelpers.callMethod(
                imageReceiver,
                AutomationResolver.resolve("ImageReceiver", "getImageLocation", AutomationResolver.ResolverType.Method)
            )
        )
    }
}
