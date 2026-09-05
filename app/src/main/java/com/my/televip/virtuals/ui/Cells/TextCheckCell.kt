package com.my.televip.virtuals.ui.Cells

import android.content.Context
import android.view.View
import android.widget.TextView
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

class TextCheckCell {

    @JvmField
    var textCell: Any?

    constructor(context: Context) {
        textCell = XposedHelpers.newInstance(ClassLoad.getClass(ClassNames.TEXT_CHECK_CELL), context)
    }

    constructor(obj: Any?) {
        textCell = obj
    }

    fun setTextAndValueAndCheck(text: CharSequence?, value: String?, checked: Boolean, multiline: Boolean, divider: Boolean) {
        XposedHelpers.callMethod(
            textCell,
            AutomationResolver.resolve("TextCheckCell", "setTextAndValueAndCheck", AutomationResolver.ResolverType.Method),
            text, value, checked, multiline, divider
        )
    }

    fun setTextAndCheck(text: CharSequence?, checked: Boolean, divider: Boolean) {
        XposedHelpers.callMethod(
            textCell,
            AutomationResolver.resolve("TextCheckCell", "setTextAndCheck", AutomationResolver.ResolverType.Method),
            text, checked, divider
        )
    }

    fun setChecked(checked: Boolean) {
        XposedHelpers.callMethod(
            textCell,
            AutomationResolver.resolve("TextCheckCell", "setChecked", AutomationResolver.ResolverType.Method),
            checked
        )
    }

    fun isChecked(): Boolean {
        return XposedHelpers.callMethod(
            textCell,
            AutomationResolver.resolve("TextCheckCell", "isChecked", AutomationResolver.ResolverType.Method)
        ) as Boolean
    }

    fun getTextView(): TextView {
        return XposedHelpers.getObjectField(
            textCell,
            AutomationResolver.resolve("TextCheckCell", "textView", AutomationResolver.ResolverType.Field)
        ) as TextView
    }

    fun getView(): View {
        return textCell as View
    }
}
