package com.my.televip.virtuals.ui.Components

import com.my.televip.obfuscate.Obfuscate
import com.my.televip.utils.Utils
import de.robv.android.xposed.XposedHelpers

class UItem(@JvmField val uItem: Any?) {

    val id: Int
        get() {
            val target = uItem ?: return 0
            return XposedHelpers.getIntField(target, Obfuscate.getFieldName("UItem", "id"))
        }

    val text: String?
        get() {
            val target = uItem ?: return null
            val fieldVal = XposedHelpers.getObjectField(target, Obfuscate.getFieldName("UItem", "text"))
            return Utils.getFieldAsString(fieldVal)
        }

    val subtext: String?
        get() {
            val target = uItem ?: return null
            val fieldVal = XposedHelpers.getObjectField(target, Obfuscate.getFieldName("UItem", "subtext"))
            return Utils.getFieldAsString(fieldVal)
        }
}
