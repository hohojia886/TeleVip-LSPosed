package com.my.televip.utils

import android.app.Activity
import com.google.gson.Gson
import com.my.televip.logging.Logger
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference
import java.nio.charset.StandardCharsets

object Utils {
    @JvmField
    var pkgName: String? = null

    @JvmField
    var modulePath: String? = null

    @JvmField
    var classLoader: ClassLoader? = null

    @JvmField
    val issue: String = "Your Telegram client may be an incompatible version with TeleVip. Please download the latest version that is compatible with TeleVip."

    private var currentActivityRef: WeakReference<Activity>? = null

    @JvmStatic
    fun setCurrentActivity(activity: Activity?) {
        currentActivityRef = WeakReference(activity)
    }

    @JvmStatic
    fun getCurrentActivity(): Activity? {
        return currentActivityRef?.get()
    }

    @JvmStatic
    fun getFieldAsString(value: Any?): String? {
        if (value == null) {
            return null
        }
        if (value is CharSequence) {
            return value.toString()
        }
        return value.toString()
    }

    @JvmStatic
    fun objectToJson(obj: Any?) {
        if (obj == null) return
        try {
            val activity = getCurrentActivity() ?: return
            val gson = Gson()
            val json = gson.toJson(obj)

            val dir = File(activity.filesDir, "backup")
            if (!dir.exists()) {
                dir.mkdirs()
            }

            val file = File(
                dir,
                "${obj.javaClass.simpleName}_${System.currentTimeMillis()}.json"
            )

            FileOutputStream(file).use { fos ->
                fos.write(json.toByteArray(Charsets.UTF_8))
            }
        } catch (throwable: Throwable) {
            Logger.e(throwable)
        }
    }
}
