package com.my.televip.virtuals.ui.Components

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.dex.DexInjector
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.androidx.Adapter
import de.robv.android.xposed.XposedHelpers

class RecyclerListView(context: Context) {

    @JvmField
    val recyclerListView: Any = if (DexInjector.classLoader != null) {
        XposedHelpers.newInstance(
            ClassLoad.getClass(ClassNames.SETTINGS_ADAPTER_RECYCLER_LIST_VIEW, DexInjector.classLoader!!),
            context
        )
    } else {
        RecyclerView(context)
    }

    fun setAdapter(adapter: Any?) {
        XposedHelpers.callMethod(
            recyclerListView,
            AutomationResolver.resolve("RecyclerListView", "setAdapter", AutomationResolver.ResolverType.Method),
            adapter
        )
    }

    fun getAdapter(): Adapter {
        return Adapter(
            XposedHelpers.callMethod(
                recyclerListView,
                AutomationResolver.resolve("RecyclerListView", "getAdapter", AutomationResolver.ResolverType.Method)
            )
        )
    }

    fun setBackgroundColor(color: Int) {
        (recyclerListView as View).setBackgroundColor(color)
    }

    fun setVerticalScrollBarEnabled(b: Boolean) {
        (recyclerListView as View).isVerticalScrollBarEnabled = b
    }

    fun setLayoutManager(layout: Any?) {
        XposedHelpers.callMethod(
            recyclerListView,
            AutomationResolver.resolve("RecyclerListView", "setLayoutManager", AutomationResolver.ResolverType.Method),
            layout
        )
    }

    fun getRecyclerListView(): View {
        return recyclerListView as View
    }
}
