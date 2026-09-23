package com.my.televip.virtuals.Adapters

import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers
import java.util.ArrayList

class DrawerLayoutAdapter(private val drawerLayout: Any?) {

    val items: ArrayList<*>?
        get() {
            val target = drawerLayout ?: return null
            return XposedHelpers.getObjectField(target, Obfuscate.getFieldName("DrawerLayoutAdapter", "items")) as? ArrayList<*>
        }
}
