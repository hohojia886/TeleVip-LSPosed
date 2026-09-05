package com.my.televip.application

import android.content.Context
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

object ApplicationLoaderHook {

    private var applicationContext: Context? = null

    @JvmStatic
    fun getApplicationContext(): Context {
        if (applicationContext == null) {
            applicationContext = XposedHelpers.getStaticObjectField(
                ClassLoad.getClass(ClassNames.APPLICATION_LOADER),
                AutomationResolver.resolve("ApplicationLoader", "applicationContext", AutomationResolver.ResolverType.Field)
            ) as Context?
        }
        return applicationContext!!
    }
}
