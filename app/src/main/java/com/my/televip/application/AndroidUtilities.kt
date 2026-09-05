package com.my.televip.application

import android.content.Context
import android.os.Handler
import com.my.televip.logging.Logger
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
    fun init(context: Context) {
        try {
            applicationHandler = Handler(ApplicationLoaderHook.getApplicationContext().mainLooper)
            density = context.resources.displayMetrics.density
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
