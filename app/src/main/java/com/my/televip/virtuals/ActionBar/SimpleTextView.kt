package com.my.televip.virtuals.ActionBar

import android.text.Layout
import android.view.View
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

class SimpleTextView(@JvmField val simpleTextViewObj: Any?) {

    fun getText(): CharSequence? {
        return XposedHelpers.callMethod(
            simpleTextViewObj,
            AutomationResolver.resolve("SimpleTextView", "getText", AutomationResolver.ResolverType.Method)
        ) as? CharSequence
    }

    fun setText(text: CharSequence?) {
        XposedHelpers.callMethod(
            simpleTextViewObj,
            AutomationResolver.resolve("SimpleTextView", "setText", AutomationResolver.ResolverType.Method),
            text
        )
    }

    fun setText(text: CharSequence?, force: Boolean) {
        XposedHelpers.callMethod(
            simpleTextViewObj,
            AutomationResolver.resolve("SimpleTextView", "setText", AutomationResolver.ResolverType.Method),
            text,
            force
        )
    }

    fun setAlignment(alignment: Layout.Alignment) {
        XposedHelpers.callMethod(
            simpleTextViewObj,
            AutomationResolver.resolve("SimpleTextView", "setAlignment", AutomationResolver.ResolverType.Method),
            alignment
        )
    }

    fun setMaxLines(value: Int) {
        XposedHelpers.callMethod(
            simpleTextViewObj,
            AutomationResolver.resolve("SimpleTextView", "setMaxLines", AutomationResolver.ResolverType.Method),
            value
        )
    }

    fun getSimpleTextView(): View? {
        return simpleTextViewObj as? View
    }
}
