package com.my.televip.virtuals.ui.Cells

import android.content.Context
import android.view.View
import android.widget.TextView
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

class TextSettingsCell {

    @JvmField
    var textSettingsCell: Any?

    constructor(context: Context) {
        textSettingsCell = XposedHelpers.newInstance(ClassLoad.getClass(ClassNames.TEXT_SETTINGS_CELL), context)
    }

    constructor(obj: Any?) {
        textSettingsCell = obj
    }

    fun getView(): View {
        return textSettingsCell as View
    }

    fun setText(text: CharSequence?, divider: Boolean) {
        XposedHelpers.callMethod(
            textSettingsCell,
            AutomationResolver.resolve("TextSettingsCell", "setText", AutomationResolver.ResolverType.Method),
            text, divider
        )
    }

    fun setTextAndValue(text: CharSequence?, value: String?, animated: Boolean, divider: Boolean) {
        XposedHelpers.callMethod(
            textSettingsCell,
            AutomationResolver.resolve("TextSettingsCell", "setTextAndValue", AutomationResolver.ResolverType.Method),
            text, value, animated, divider
        )
    }

    fun getTextView(): TextView {
        return XposedHelpers.getObjectField(
            textSettingsCell,
            AutomationResolver.resolve("TextSettingsCell", "textView", AutomationResolver.ResolverType.Field)
        ) as TextView
    }
}
