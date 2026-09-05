package com.my.televip.virtuals.messenger

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers
import java.io.File

class FileLoader(@JvmField val fileLoader: Any?) {
    fun getLocalFile(location: ImageLocation): File? {
        return XposedHelpers.callMethod(
            fileLoader,
            AutomationResolver.resolve("FileLoader", "getLocalFile", AutomationResolver.ResolverType.Method),
            location.imageLocation
        ) as? File
    }

    companion object {
        @JvmStatic
        fun getInstance(num: Int): FileLoader {
            return FileLoader(
                XposedHelpers.callStaticMethod(
                    ClassLoad.getClass(ClassNames.FILE_LOADER),
                    AutomationResolver.resolve("FileLoader", "getInstance", AutomationResolver.ResolverType.Method),
                    num
                )
            )
        }
    }
}
