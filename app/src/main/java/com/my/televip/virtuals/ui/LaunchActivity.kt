package com.my.televip.virtuals.ui

import android.widget.FrameLayout
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

class LaunchActivity(obj: Any?) {

    @JvmField
    var frameLayout: FrameLayout = XposedHelpers.getObjectField(obj, Obfuscate.getFieldName("LaunchActivity", "actionFrameLayout")) as FrameLayout
}
