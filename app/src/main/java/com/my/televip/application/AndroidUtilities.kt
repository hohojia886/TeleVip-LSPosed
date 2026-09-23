package com.my.televip.application

import android.os.Handler
import com.my.televip.logging.Logger
import com.my.televip.utils.Utils
import kotlin.math.ceil

object AndroidUtilities {

    @JvmField
    var density: Float = 1f

    @Volatile
    @JvmField
    var applicationHandler: Handler? = null

    @JvmStatic
    fun runOnUIThread(runnable: Runnable) {
        applicationHandler?.post(runnable)
    }

    @JvmStatic
    fun init() {
        try {
            val context = ApplicationLoaderHook.getApplicationContext()
            if (context != null) {
                applicationHandler = Handler(context.mainLooper)
            }
            val activity = Utils.getCurrentActivity()
            if (activity != null) {
                density = activity.resources.displayMetrics.density
            }
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    @JvmStatic
    fun dp(value: Float): Int {
        if (value == 0f) {
            return 0
        }
        return ceil((density * value).toDouble()).toInt()
    }
}
