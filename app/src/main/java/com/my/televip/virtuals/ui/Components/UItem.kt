package com.my.televip.virtuals.ui.Components

import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.utils.Utils
import de.robv.android.xposed.XposedHelpers

class UItem(@JvmField val uItem: Any?) {

    fun getID(): Int {
        return XposedHelpers.getIntField(
            uItem,
            AutomationResolver.resolve("UItem", "id", AutomationResolver.ResolverType.Field)
        )
    }

    fun getText(): String? {
        return Utils.getFieldAsString(
            XposedHelpers.getObjectField(
                uItem,
                AutomationResolver.resolve("UItem", "text", AutomationResolver.ResolverType.Field)
            )
        )
    }

    fun getSubtext(): String? {
        return Utils.getFieldAsString(
            XposedHelpers.getObjectField(
                uItem,
                AutomationResolver.resolve("UItem", "subtext", AutomationResolver.ResolverType.Field)
            )
        )
    }

    fun getUItem(): Any? = uItem
}
