package com.my.televip.virtuals.messenger.browser

import android.content.Context
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

object Browser {
    @JvmStatic
    fun openUrl(context: Context, url: String) {
        XposedHelpers.callStaticMethod(
            ClassLoad.getClass(ClassNames.BROWSER),
            AutomationResolver.resolve("Browser", "openUrl", AutomationResolver.ResolverType.Method),
            context,
            url
        )
    }
}
