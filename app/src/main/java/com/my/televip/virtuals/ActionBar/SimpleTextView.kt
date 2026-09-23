package com.my.televip.virtuals.ActionBar

import android.text.Layout
import android.view.View
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

class SimpleTextView(@JvmField val simpleTextView: Any?) {

    fun getText(): CharSequence? {
        val target = simpleTextView ?: return null
        return XposedHelpers.callMethod(target, Obfuscate.getMethodName("SimpleTextView", "getText")) as? CharSequence
    }

    fun setText(text: CharSequence?) {
        val target = simpleTextView ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("SimpleTextView", "setText"), text)
    }

    fun setText(text: CharSequence?, force: Boolean) {
        val target = simpleTextView ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("SimpleTextView", "setText"), text, force)
    }

    fun setAlignment(alignment: Layout.Alignment?) {
        val target = simpleTextView ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("SimpleTextView", "setAlignment"), alignment)
    }

    fun setMaxLines(value: Int) {
        val target = simpleTextView ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("SimpleTextView", "setMaxLines"), value)
    }

    fun getSimpleTextView(): View? {
        return simpleTextView as? View
    }
}
