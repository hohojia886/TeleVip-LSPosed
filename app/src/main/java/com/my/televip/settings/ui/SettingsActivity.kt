package com.my.televip.settings.ui

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Drawable.ArrowDrawable
import com.my.televip.audio
import com.my.televip.base.AbstractMethodHook
import com.my.televip.dex.DexInjector
import com.my.televip.hooks.HMethod
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.settings.adapter.ListAdapter
import com.my.televip.settings.controller.SettingsController
import com.my.televip.ui.toolBar.MainToolBar
import com.my.televip.virtuals.TeleVip.Bridge.Bridge
import com.my.televip.virtuals.Theme
import com.my.televip.virtuals.ui.Components.RecyclerListView
import de.robv.android.xposed.XposedHelpers

class SettingsActivity(private val context: Context) {

    @JvmField var listView: RecyclerListView? = null

    init {
        isSettings = true
    }

    fun createView(settingsController: SettingsController): View {
        val layout = LinearLayout(context)
        try {
            layout.orientation = LinearLayout.VERTICAL
            layout.setBackgroundColor(Theme.getBackgroundGrayColor())

            val toolbar = MainToolBar(context)
            toolbar.setColorTitle(Theme.getTextToolBarColor())
            toolbar.setRippleColor(Theme.getToolBarRippleColor())
            toolbar.setTextTitle(Translator.get(Keys.GhostMode))

            val arrow = ArrowDrawable()
            toolbar.setImageDrawable(arrow)
            toolbar.image.setOnClickListener { settingsController.hide() }

            layout.addView(toolbar)

            val recycler = RecyclerListView(context)
            listView = recycler
            val loader = DexInjector.classLoader
            if (loader != null) {
                val adapterClass = ClassLoad.getClass(ClassNames.SETTINGS_ADAPTER_LIST_ADAPTER, loader)
                val adapter = XposedHelpers.newInstance(adapterClass, context)
                recycler.setAdapter(adapter)
                recycler.setLayoutManager(Bridge.getLayoutManager(context))
            } else {
                recycler.setAdapter(ListAdapter(context, settingsController))
                recycler.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))
            }

            recycler.setVerticalScrollBarEnabled(false)

            val recyclerParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
            recyclerParams.setMargins(10, 10, 10, 0)

            layout.addView(recycler.getRecyclerListView(), recyclerParams)
        } catch (e: Throwable) {
            Logger.e(e)
        }

        return layout
    }

    companion object {
        @JvmField var isSettings: Boolean = false

        @JvmStatic
        fun init(settingsController: SettingsController) {
            audio.init()
            try {
                val launchActivityClass = ClassLoad.getClass(ClassNames.LAUNCH_ACTIVITY)
                if (launchActivityClass != null) {
                    HMethod.hookMethod(launchActivityClass, "onBackPressed", object : AbstractMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            if (isSettings) {
                                settingsController.hide()
                                settingsController.settingsView = null
                                param.result = null
                            }
                        }
                    })
                }

                val androidUtilitiesClass = ClassLoad.getClass(ClassNames.ANDROID_UTILITIES)
                if (androidUtilitiesClass != null) {
                    HMethod.hookMethod(
                        androidUtilitiesClass,
                        AutomationResolver.resolve("AndroidUtilities", "isTabletInternal", AutomationResolver.ResolverType.Method),
                        object : AbstractMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (isSettings) {
                                    param.result = true
                                }
                            }
                        }
                    )
                }
            } catch (e: Throwable) {
                Logger.e(e)
            }
        }
    }
}
