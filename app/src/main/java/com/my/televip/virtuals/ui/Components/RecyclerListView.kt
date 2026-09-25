package com.my.televip.virtuals.ui.Components

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.dex.DexInjector
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.androidx.Adapter
import de.robv.android.xposed.XposedHelpers

class RecyclerListView(context: Context) {

    @JvmField
    val recyclerListView: Any?

    init {
        val dexLoader = DexInjector.classLoader
        recyclerListView = if (dexLoader != null) {
            val listClass = ClassLoad.getClass(ClassNames.SETTINGS_ADAPTER_RECYCLER_LIST_VIEW, dexLoader)
            XposedHelpers.newInstance(listClass, context)
        } else {
            RecyclerView(context)
        }
    }

    fun setAdapter(adapter: Any?) {
        val target = recyclerListView ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("RecyclerListView", "setAdapter"), adapter)
    }

    val adapter: Adapter
        get() {
            val target = recyclerListView ?: return Adapter(null)
            val result = XposedHelpers.callMethod(target, Obfuscate.getMethodName("RecyclerListView", "getAdapter"))
            return Adapter(result)
        }

    fun setBackgroundColor(color: Int) {
        (recyclerListView as? View)?.setBackgroundColor(color)
    }

    fun setVerticalScrollBarEnabled(b: Boolean) {
        (recyclerListView as? View)?.isVerticalScrollBarEnabled = b
    }

    fun setLayoutManager(layout: Any?) {
        val target = recyclerListView ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("RecyclerListView", "setLayoutManager"), layout)
    }

    fun getRecyclerListView(): View? {
        return recyclerListView as? View
    }
}
