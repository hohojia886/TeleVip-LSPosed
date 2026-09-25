package com.my.televip.application

import android.content.Context
import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

object ApplicationLoaderHook {

    private var applicationContext: Context? = null

    @JvmStatic
    fun getApplicationContext(): Context {
        if (applicationContext == null) {
            val appLoaderClass = ClassLoad.getClass(ClassNames.APPLICATION_LOADER)
            val fieldName = Obfuscate.getFieldName("ApplicationLoader", "applicationContext")
            applicationContext = XposedHelpers.getStaticObjectField(appLoaderClass, fieldName) as? Context
        }
        return applicationContext!!
    }
}
