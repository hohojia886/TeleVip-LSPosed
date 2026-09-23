package com.my.televip.features.otherFeatures

import android.view.View
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.base.BaseMethodHook
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.ui.PhotoViewer
import de.robv.android.xposed.XposedHelpers

object AlwaysSaveMedia {

    @JvmStatic
    fun init() {
        try {
            val photoViewerClass = ClassLoad.getClass(ClassNames.PHOTO_VIEWER)
            if (photoViewerClass != null) {
                val types: Array<Class<*>> = arrayOf(
                    Int::class.javaPrimitiveType!!,
                    Boolean::class.javaPrimitiveType!!,
                    Boolean::class.javaPrimitiveType!!,
                    Boolean::class.javaPrimitiveType!!
                )
                val merged = ArgsResolver.merge("setIsAboutToSwitchToIndex", types, object : BaseMethodHook() {
                    override fun afterMethod(param: MethodHookParam) {
                        val photoViewer = PhotoViewer(param.thisObject)
                        val btn = photoViewer.galleryButton
                        if (btn != null) {
                            btn.visibility = View.VISIBLE
                        }
                    }
                })
                if (merged != null) {
                    XposedHelpers.findAndHookMethod(
                        photoViewerClass,
                        Obfuscate.getMethodName("PhotoViewer", "setIsAboutToSwitchToIndex"),
                        *merged
                    )
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }
}
