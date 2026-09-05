package com.my.televip.virtuals.ui

import android.app.Activity
import android.view.View
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.messenger.ImageReceiver
import com.my.televip.virtuals.messenger.MessageObject
import de.robv.android.xposed.XposedHelpers

class PhotoViewer(@JvmField val photoViewer: Any?) {

    fun setParentActivity(activity: Activity) {
        XposedHelpers.callMethod(
            photoViewer,
            AutomationResolver.resolve("PhotoViewer", "setParentActivity", AutomationResolver.ResolverType.Method),
            activity
        )
    }

    fun openPhoto(messageObject: MessageObject, l: Long, l2: Long, l3: Long, provider: PhotoViewerProvider, b: Boolean) {
        XposedHelpers.callMethod(
            photoViewer,
            AutomationResolver.resolve("PhotoViewer", "openPhoto", AutomationResolver.ResolverType.Method),
            messageObject.messageObject, l, l2, l3, provider.getPhotoViewerProvider(), b
        )
    }

    fun getGalleryButton(): View? {
        return XposedHelpers.getObjectField(
            photoViewer,
            AutomationResolver.resolve("PhotoViewer", "galleryButton", AutomationResolver.ResolverType.Field)
        ) as? View
    }

    class PhotoViewerProvider(@JvmField val photoViewerProvider: Any?) {
        fun getPlaceForPhoto(messageObject: MessageObject, fileLocation: Any?, index: Int, needPreview: Boolean, closing: Boolean): PlaceProviderObject {
            return PlaceProviderObject(
                XposedHelpers.callMethod(
                    photoViewerProvider,
                    AutomationResolver.resolve("PhotoViewer\$PhotoViewerProvider", "getPlaceForPhoto", AutomationResolver.ResolverType.Method),
                    messageObject.messageObject, fileLocation, index, needPreview, closing
                )
            )
        }

        fun getPhotoViewerProvider(): Any? = photoViewerProvider
    }

    class PlaceProviderObject(@JvmField val placeProviderObject: Any?) {
        fun getImageReceiver(): ImageReceiver {
            return ImageReceiver(
                XposedHelpers.getObjectField(
                    placeProviderObject,
                    AutomationResolver.resolve("PhotoViewer\$PlaceProviderObject", "imageReceiver", AutomationResolver.ResolverType.Field)
                )
            )
        }
    }

    companion object {
        @JvmStatic
        fun getInstance(): PhotoViewer {
            return PhotoViewer(
                XposedHelpers.callStaticMethod(
                    ClassLoad.getClass(ClassNames.PHOTO_VIEWER),
                    AutomationResolver.resolve("PhotoViewer", "getInstance", AutomationResolver.ResolverType.Method)
                )
            )
        }
    }
}
