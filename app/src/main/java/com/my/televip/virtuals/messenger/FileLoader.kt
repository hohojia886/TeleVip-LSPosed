package com.my.televip.virtuals.messenger

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

class FileLoader(@JvmField val fileLoader: Any?) {

    companion object {
        @JvmStatic
        fun checkFileName(fileName: String?): String? {
            val fileLoaderClass = ClassLoad.getClass(ClassNames.FILE_LOADER) ?: return fileName
            return XposedHelpers.callStaticMethod(
                fileLoaderClass,
                Obfuscate.getMethodName("FileLoader", "checkFileName"),
                fileName
            ) as? String
        }
    }
}
