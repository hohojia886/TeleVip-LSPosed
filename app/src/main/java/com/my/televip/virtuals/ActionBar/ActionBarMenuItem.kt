package com.my.televip.virtuals.ActionBar

import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

class ActionBarMenuItem(@JvmField val actionBarMenuItem: Any?) {
    fun lazilyAddSubItem(id: Int, resId: Int, name: String?) {
        XposedHelpers.callMethod(
            actionBarMenuItem,
            AutomationResolver.resolve("ActionBarMenuItem", "lazilyAddSubItem", AutomationResolver.ResolverType.Method),
            id, resId, name
        )
    }

    fun addSubItem(id: Int, resId: Int, name: String?) {
        XposedHelpers.callMethod(
            actionBarMenuItem,
            AutomationResolver.resolve("ActionBarMenuItem", "addSubItem", AutomationResolver.ResolverType.Method),
            id, resId, name
        )
    }
}
