package com.my.televip.virtuals.ui.Cells

import android.content.Context
import android.view.View
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

class HeaderCell {

    @JvmField
    var headerCell: Any? = null

    constructor(context: Context?) {
        val hcClass = ClassLoad.getClass(ClassNames.HEADER_CELL)
        headerCell = XposedHelpers.newInstance(hcClass, context)
    }

    constructor(obj: Any?) {
        headerCell = obj
    }

    val view: View?
        get() = headerCell as? View

    fun setText(text: CharSequence?) {
        val target = headerCell ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("HeaderCell", "setText"), text)
    }
}
