package com.my.televip.virtuals.ActionBar

import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

class ActionBarMenuItem(@JvmField val actionBarMenuItem: Any?) {

    fun lazilyAddSubItem(id: Int, resId: Int, name: String?) {
        val target = actionBarMenuItem ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("ActionBarMenuItem", "lazilyAddSubItem"), id, resId, name)
    }

    fun addSubItem(id: Int, resId: Int, name: String?) {
        val target = actionBarMenuItem ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("ActionBarMenuItem", "addSubItem"), id, resId, name)
    }
}
