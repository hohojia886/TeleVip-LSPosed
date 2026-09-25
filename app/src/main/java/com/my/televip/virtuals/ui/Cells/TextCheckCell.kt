package com.my.televip.virtuals.ui.Cells

import android.content.Context
import android.view.View
import android.widget.TextView
import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

class TextCheckCell {

    @JvmField
    var textCell: Any? = null

    constructor(context: Context?) {
        val tcClass = ClassLoad.getClass(ClassNames.TEXT_CHECK_CELL)
        textCell = XposedHelpers.newInstance(tcClass, context)
    }

    constructor(obj: Any?) {
        textCell = obj
    }

    fun setTextAndValueAndCheck(text: CharSequence?, value: String?, checked: Boolean, multiline: Boolean, divider: Boolean) {
        val target = textCell ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("TextCheckCell", "setTextAndValueAndCheck"), text, value, checked, multiline, divider)
    }

    fun setTextAndCheck(text: CharSequence?, checked: Boolean, divider: Boolean) {
        val target = textCell ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("TextCheckCell", "setTextAndCheck"), text, checked, divider)
    }

    fun setChecked(checked: Boolean) {
        val target = textCell ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("TextCheckCell", "setChecked"), checked)
    }

    val isChecked: Boolean
        get() {
            val target = textCell ?: return false
            return XposedHelpers.callMethod(target, Obfuscate.getMethodName("TextCheckCell", "isChecked")) as? Boolean ?: false
        }

    val textView: TextView?
        get() {
            val target = textCell ?: return null
            return XposedHelpers.getObjectField(target, Obfuscate.getFieldName("TextCheckCell", "textView")) as? TextView
        }

    val view: View?
        get() = textCell as? View
}
