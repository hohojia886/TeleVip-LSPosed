package com.my.televip.features.otherFeatures

import android.view.View
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.ui.PhotoViewer

object AlwaysSaveMedia {
    @JvmStatic
    fun init() {
        try {
            val photoViewerClass = ClassLoad.getClass(ClassNames.PHOTO_VIEWER)
            if (photoViewerClass != null) {
                HMethod.hookMethod(
                    photoViewerClass,
                    AutomationResolver.resolve("PhotoViewer", "setIsAboutToSwitchToIndex", AutomationResolver.ResolverType.Method),
                    *AutomationResolver.merge(
                        AutomationResolver.resolveObject("setIsAboutToSwitchToIndex", arrayOf<Class<*>>(Int::class.javaPrimitiveType!!, Boolean::class.javaPrimitiveType!!, Boolean::class.javaPrimitiveType!!, Boolean::class.javaPrimitiveType!!)),
                        object : AbstractMethodHook() {
                            override fun afterMethod(param: MethodHookParam) {
                                val photoViewer = PhotoViewer(param.thisObject)
                                val btn = photoViewer.getGalleryButton()
                                if (btn != null) {
                                    btn.visibility = View.VISIBLE
                                }
                            }
                        }
                    )
                )
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }
}
