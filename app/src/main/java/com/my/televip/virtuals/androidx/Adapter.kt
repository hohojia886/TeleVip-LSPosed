package com.my.televip.virtuals.androidx

import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

class Adapter(private val adapter: Any?) {
    fun notifyItemChanged(position: Int) {
        XposedHelpers.callMethod(
            adapter,
            AutomationResolver.resolve("RecyclerListView", "notifyItemChanged", AutomationResolver.ResolverType.Method),
            position
        )
    }
}
