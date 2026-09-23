package com.my.televip.virtuals.ui.Cells

import android.content.Context
import android.view.View
import android.widget.TextView
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

class TextSettingsCell {

    @JvmField
    var textSettingsCell: Any? = null

    constructor(context: Context?) {
        val tsClass = ClassLoad.getClass(ClassNames.TEXT_SETTINGS_CELL)
        textSettingsCell = XposedHelpers.newInstance(tsClass, context)
    }

    constructor(obj: Any?) {
        textSettingsCell = obj
    }

    val view: View?
        get() = textSettingsCell as? View

    fun setText(text: CharSequence?, divider: Boolean) {
        val target = textSettingsCell ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("TextSettingsCell", "setText"), text, divider)
    }

    fun setTextAndValue(text: CharSequence?, value: String?, animated: Boolean, divider: Boolean) {
        val target = textSettingsCell ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("TextSettingsCell", "setTextAndValue"), text, value, animated, divider)
    }

    val textView: TextView?
        get() {
            val target = textSettingsCell ?: return null
            return XposedHelpers.getObjectField(target, Obfuscate.getFieldName("TextSettingsCell", "textView")) as? TextView
        }
}
