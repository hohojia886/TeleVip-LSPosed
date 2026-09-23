package com.my.televip.settings.ui

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.televip.Audio
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Drawable.ArrowDrawable
import com.my.televip.base.BaseMethodHook
import com.my.televip.dex.DexInjector
import com.my.televip.hooks.HMethod
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.settings.adapter.ListAdapter
import com.my.televip.settings.controller.SettingsController
import com.my.televip.ui.ThemeColors
import com.my.televip.ui.toolBar.MainToolBar
import com.my.televip.virtuals.TeleVip.Bridge.Bridge
import com.my.televip.virtuals.ui.Components.RecyclerListView
import de.robv.android.xposed.XposedHelpers

class SettingsActivity(private val context: Context) {

    @JvmField
    var listView: RecyclerListView? = null

    init {
        isSettings = true
    }

    fun createView(settingsController: SettingsController): View {
        val layout = LinearLayout(context)

        try {
            layout.orientation = LinearLayout.VERTICAL
            layout.setBackgroundColor(ThemeColors.getBackgroundGrayColor())

            val toolbar = MainToolBar(context)
            toolbar.setColorTitle(ThemeColors.getTextToolBarColor())
            toolbar.setRippleColor(ThemeColors.getToolBarRippleColor())
            toolbar.setTextTitle(Translator.get(Keys.GhostMode))

            val arrow = ArrowDrawable()
            toolbar.setImageDrawable(arrow)
            toolbar.image?.setOnClickListener { settingsController.hide() }

            layout.addView(toolbar)

            val list = RecyclerListView(context)
            listView = list

            val dexLoader = DexInjector.classLoader
            if (dexLoader != null) {
                val adapterClass = ClassLoad.getClass(
                    ClassNames.SETTINGS_ADAPTER_LIST_ADAPTER,
                    dexLoader
                )
                if (adapterClass != null) {
                    val adapter = XposedHelpers.newInstance(adapterClass, context)
                    list.setAdapter(adapter)
                    list.setLayoutManager(Bridge.getLayoutManager(context))
                }
            } else {
                list.setAdapter(ListAdapter(context, settingsController))
                list.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))
            }

            list.setBackgroundColor(ThemeColors.getBackgroundWhiteOrBlueColor())
            list.setVerticalScrollBarEnabled(false)

            val recyclerParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            ).apply {
                setMargins(10, 10, 10, 0)
            }

            layout.addView(list.getRecyclerListView(), recyclerParams)

        } catch (e: Throwable) {
            Logger.e(e)
        }

        return layout
    }

    companion object {
        @JvmField
        var isSettings: Boolean = false

        @JvmStatic
        fun init(settingsController: SettingsController) {
            Audio.init()
            try {
                val launchClass = ClassLoad.getClass(ClassNames.LAUNCH_ACTIVITY)
                if (launchClass != null) {
                    HMethod.hookMethod(launchClass, "onBackPressed", object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            if (isSettings) {
                                settingsController.hide()
                                settingsController.settingsView = null
                                param.result = null
                            }
                        }
                    })
                }

                val androidUtilsClass = ClassLoad.getClass(ClassNames.ANDROID_UTILITIES)
                if (androidUtilsClass != null) {
                    HMethod.hookMethod(
                        androidUtilsClass,
                        Obfuscate.getMethodName("AndroidUtilities", "isTabletInternal"),
                        object : BaseMethodHook() {
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
