package com.my.televip.virtuals.androidx

import android.view.View
import de.robv.android.xposed.XposedHelpers

class ViewHolder(@JvmField val viewHolder: Any?) {

    val itemView: View?
        get() {
            val target = viewHolder ?: return null
            return XposedHelpers.getObjectField(target, "itemView") as? View
        }
}
