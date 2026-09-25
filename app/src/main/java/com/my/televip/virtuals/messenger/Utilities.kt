package com.my.televip.virtuals.messenger

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

object Utilities {

    @JvmStatic
    fun getStageQueue(): DispatchQueue {
        val utilClass = ClassLoad.getClass(ClassNames.UTILITIES)
        val queueObj = XposedHelpers.getStaticObjectField(utilClass, Obfuscate.getFieldName("Utilities", "stageQueue"))
        return DispatchQueue(queueObj)
    }
}
