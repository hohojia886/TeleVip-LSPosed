package com.my.televip.virtuals.Adapters

import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

class DrawerLayoutAdapter(private val drawerLayout: Any?) {
    fun getItems(): ArrayList<*>? {
        return XposedHelpers.getObjectField(
            drawerLayout,
            AutomationResolver.resolve("DrawerLayoutAdapter", "items", AutomationResolver.ResolverType.Field)
        ) as? ArrayList<*>
    }
}
