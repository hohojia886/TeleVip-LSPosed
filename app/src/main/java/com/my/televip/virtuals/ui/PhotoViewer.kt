package com.my.televip.virtuals.ui

import android.app.Activity
import android.view.View
import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.messenger.ImageReceiver
import de.robv.android.xposed.XposedHelpers

class PhotoViewer(@JvmField val photoViewer: Any?) {

    val galleryButton: View?
        get() {
            val target = photoViewer ?: return null
            return XposedHelpers.getObjectField(target, Obfuscate.getFieldName("PhotoViewer", "galleryButton")) as? View
        }

    fun setParentActivity(activity: Activity?) {
        val target = photoViewer ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("PhotoViewer", "setParentActivity"), activity)
    }

    companion object {
        @JvmStatic
        fun getInstance(): PhotoViewer {
            val pvClass = ClassLoad.getClass(ClassNames.PHOTO_VIEWER)
            val instance = XposedHelpers.callStaticMethod(pvClass, Obfuscate.getMethodName("PhotoViewer", "getInstance"))
            return PhotoViewer(instance)
        }

        @JvmStatic
        fun getPlaceProviderObject(placeProviderObject: Any?): ImageReceiver? {
            if (placeProviderObject == null) return null
            val result = XposedHelpers.getObjectField(placeProviderObject, Obfuscate.getFieldName("PhotoViewer", "placeProviderObject"))
            return ImageReceiver(result)
        }
    }
}
