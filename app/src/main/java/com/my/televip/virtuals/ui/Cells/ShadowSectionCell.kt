package com.my.televip.virtuals.ui.Cells

import android.content.Context
import android.view.View
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import de.robv.android.xposed.XposedHelpers

class ShadowSectionCell(context: Context) {
    @JvmField
    val shadowSectionCell: Any = XposedHelpers.newInstance(ClassLoad.getClass(ClassNames.SHADOW_SECTION_CELL), context)

    fun getView(): View {
        return shadowSectionCell as View
    }
}
