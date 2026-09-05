package com.my.televip.virtuals.androidx

import android.view.View
import de.robv.android.xposed.XposedHelpers

class ViewHolder(@JvmField val viewHolder: Any?) {
    fun getItemView(): View {
        return XposedHelpers.getObjectField(viewHolder, "itemView") as View
    }

    fun getAdapterPosition(): Int {
        return XposedHelpers.callMethod(viewHolder, "getAdapterPosition") as Int
    }
}
