package com.my.televip.virtuals

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import de.robv.android.xposed.XposedHelpers

object SettingsIconResolver {

    private var cachedIcon: Int? = null

    @JvmStatic
    fun getIconSettings(): Int {
        val drawableClass = ClassLoad.getClass(ClassNames.DRAWABLE) ?: return 0
        cachedIcon?.let { return it }

        val names = arrayOf(
            "msg_settings",
            "msg_settings_old",
            "msg_settings_ny",
            "msg_settings_14",
            "msg_settings_hw"
        )

        for (name in names) {
            try {
                val drawableResource = XposedHelpers.getStaticIntField(drawableClass, name)
                if (drawableResource != 0) {
                    cachedIcon = drawableResource
                    return drawableResource
                }
            } catch (_: Throwable) {}
        }

        return 0
    }
}
