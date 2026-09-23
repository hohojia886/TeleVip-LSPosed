package com.my.televip.virtuals.messenger.browser

import android.content.Context
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

object Browser {

    @JvmStatic
    fun openUrl(context: Context?, url: String?) {
        val browserClass = ClassLoad.getClass(ClassNames.BROWSER) ?: return
        XposedHelpers.callStaticMethod(browserClass, Obfuscate.getMethodName("Browser", "openUrl"), context, url)
    }
}
