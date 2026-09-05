package com.my.televip.virtuals.ui.Cells

import android.content.Context
import android.view.View
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

class HeaderCell {

    @JvmField
    var headerCell: Any?

    constructor(context: Context) {
        headerCell = XposedHelpers.newInstance(ClassLoad.getClass(ClassNames.HEADER_CELL), context)
    }

    constructor(obj: Any?) {
        headerCell = obj
    }

    fun getView(): View {
        return headerCell as View
    }

    fun setText(text: CharSequence?) {
        XposedHelpers.callMethod(
            headerCell,
            AutomationResolver.resolve("HeaderCell", "setText", AutomationResolver.ResolverType.Method),
            text
        )
    }
}
