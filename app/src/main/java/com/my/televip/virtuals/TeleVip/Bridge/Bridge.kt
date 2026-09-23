package com.my.televip.virtuals.TeleVip.Bridge

import android.R
import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.base.BaseMethodHook
import com.my.televip.dex.DexInjector
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.settings.controller.SettingsController
import com.my.televip.settings.ui.SettingsAdapter
import com.my.televip.ui.Cells.ExpandableTextCheckCell
import com.my.televip.ui.Cells.TextInfoCell
import com.my.televip.ui.ThemeColors
import com.my.televip.virtuals.ui.Cells.HeaderCell
import com.my.televip.virtuals.ui.Cells.ShadowSectionCell
import com.my.televip.virtuals.ui.Cells.TextCheckCell
import com.my.televip.virtuals.ui.Cells.TextSettingsCell
import de.robv.android.xposed.XposedHelpers

object Bridge {

    @JvmField
    val outValue: TypedValue = TypedValue()

    @JvmStatic
    fun getHeaderCell(holder: Any?): HeaderCell {
        val target = holder ?: return HeaderCell(null)
        val cell = XposedHelpers.getObjectField(target, "headerCell")
        return HeaderCell(cell)
    }

    @JvmStatic
    fun getTextCheckCell(holder: Any?): Any? {
        val target = holder ?: return null
        return XposedHelpers.getObjectField(target, "textCheckCell")
    }

    @JvmStatic
    fun getTextSettingsCell(holder: Any?): Any? {
        val target = holder ?: return null
        return XposedHelpers.getObjectField(target, "textSettingsCell")
    }

    @JvmStatic
    fun getView(holder: Any?): View? {
        val target = holder ?: return null
        return XposedHelpers.getObjectField(target, "view") as? View
    }

    @JvmStatic
    fun getTextInfoCell(holder: Any?): TextInfoCell? {
        val target = holder ?: return null
        return XposedHelpers.getObjectField(target, "view") as? TextInfoCell
    }

    @JvmStatic
    fun getExpandableTextCheckCell(holder: Any?): ExpandableTextCheckCell? {
        val target = holder ?: return null
        return XposedHelpers.getObjectField(target, "expandableTextCheckCell") as? ExpandableTextCheckCell
    }

    @JvmStatic
    fun getLayoutManager(context: Context?): Any? {
        val dexLoader = DexInjector.classLoader ?: return null
        val adapterClass = ClassLoad.getClass(ClassNames.SETTINGS_ADAPTER, dexLoader) ?: return null
        return XposedHelpers.callStaticMethod(adapterClass, "getLayoutManager", context)
    }

    @JvmStatic
    fun init(settingsController: SettingsController) {
        val dexLoader = DexInjector.classLoader ?: return
        try {
            val bridgeClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.Bridge", dexLoader)
            val textCheckCellClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.SettingsAdapter\$TextCheckCellHolder", dexLoader)
            val expandableTextCheckCellClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.SettingsAdapter\$ExpandableTextCheckCellHolder", dexLoader)
            val textSettingsCellClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.SettingsAdapter\$TextSettingsCellHolder", dexLoader)
            val headerCellClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.SettingsAdapter\$HeaderCellHolder", dexLoader)
            val shadowSectionCellClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.SettingsAdapter\$ShadowSectionCellHolder", dexLoader)
            val textInfoCellClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.SettingsAdapter\$TextInfoCellHolder", dexLoader)

            if (bridgeClass != null) {
                HMethod.hookMethod(
                    bridgeClass,
                    "getRow",
                    Int::class.javaPrimitiveType!!,
                    object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            param.result = SettingsAdapter.getRow(param.args[0] as Int)
                        }
                    }
                )

                HMethod.hookMethod(
                    bridgeClass,
                    "log",
                    String::class.java,
                    object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            Logger.l(param.args[0] as? String ?: "")
                        }
                    }
                )

                HMethod.hookMethod(
                    bridgeClass,
                    "getRowCount",
                    object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            param.result = SettingsAdapter.getRowCount()
                        }
                    }
                )

                HMethod.hookMethod(
                    bridgeClass,
                    "onBindViewHolder",
                    Any::class.java,
                    Int::class.javaPrimitiveType!!,
                    Int::class.javaPrimitiveType!!,
                    object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            val holder = param.args[0] as? RecyclerView.ViewHolder ?: return
                            SettingsAdapter.onBindViewHolder(
                                holder,
                                settingsController,
                                param.args[1] as Int,
                                param.args[2] as Int
                            )
                        }
                    }
                )
            }

            val context = settingsController.context
            context?.theme?.resolveAttribute(
                R.attr.selectableItemBackground,
                outValue,
                true
            )

            if (textCheckCellClass != null) {
                XposedHelpers.findAndHookConstructor(
                    textCheckCellClass,
                    View::class.java,
                    Any::class.java,
                    object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            val textCheckCell = createTextCheckCell(settingsController.context)
                            param.args[0] = textCheckCell.view
                            param.args[1] = textCheckCell.textCell
                        }
                    }
                )
            }

            if (expandableTextCheckCellClass != null) {
                XposedHelpers.findAndHookConstructor(
                    expandableTextCheckCellClass,
                    View::class.java,
                    Any::class.java,
                    object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            val expandableTextCheckCell = createExpandableTextCheckCell(settingsController.context)
                            param.args[0] = expandableTextCheckCell
                            param.args[1] = expandableTextCheckCell
                        }
                    }
                )
            }

            if (textSettingsCellClass != null) {
                XposedHelpers.findAndHookConstructor(
                    textSettingsCellClass,
                    View::class.java,
                    Any::class.java,
                    object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            val textSettingsCell = createTextSettingsCell(settingsController.context)
                            param.args[0] = textSettingsCell.view
                            param.args[1] = textSettingsCell.textSettingsCell
                        }
                    }
                )
            }

            if (headerCellClass != null) {
                XposedHelpers.findAndHookConstructor(
                    headerCellClass,
                    View::class.java,
                    Any::class.java,
                    object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            val header = createHeaderCell(settingsController.context)
                            param.args[0] = header.view
                            param.args[1] = header.headerCell
                        }
                    }
                )
            }

            if (shadowSectionCellClass != null) {
                XposedHelpers.findAndHookConstructor(
                    shadowSectionCellClass,
                    View::class.java,
                    object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            param.args[0] = ShadowSectionCell(settingsController.context).view
                        }
                    }
                )
            }

            if (textInfoCellClass != null) {
                XposedHelpers.findAndHookConstructor(
                    textInfoCellClass,
                    View::class.java,
                    object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            val textInfoCell = createTextInfoCell(settingsController.context)
                            param.args[0] = textInfoCell
                        }
                    }
                )
            }

        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    @JvmStatic
    fun createTextCheckCell(context: Context?): TextCheckCell {
        val textCheckCell = TextCheckCell(context)
        textCheckCell.view?.setBackgroundColor(ThemeColors.getBackgroundWhiteOrBlueColor())
        textCheckCell.view?.setBackgroundResource(outValue.resourceId)
        textCheckCell.view?.isClickable = true
        textCheckCell.view?.isFocusable = true
        return textCheckCell
    }

    @JvmStatic
    fun createExpandableTextCheckCell(context: Context?): ExpandableTextCheckCell {
        val expandableTextCheckCell = ExpandableTextCheckCell(context!!)
        expandableTextCheckCell.setBackgroundColor(ThemeColors.getBackgroundWhiteOrBlueColor())
        expandableTextCheckCell.setBackgroundResource(outValue.resourceId)
        expandableTextCheckCell.setBChildResource(outValue.resourceId)
        expandableTextCheckCell.isClickable = true
        expandableTextCheckCell.isFocusable = true
        return expandableTextCheckCell
    }

    @JvmStatic
    fun createTextSettingsCell(context: Context?): TextSettingsCell {
        val textSettingsCell = TextSettingsCell(context)
        textSettingsCell.view?.setBackgroundColor(ThemeColors.getBackgroundWhiteOrBlueColor())
        textSettingsCell.view?.setBackgroundResource(outValue.resourceId)
        textSettingsCell.view?.isClickable = true
        textSettingsCell.view?.isFocusable = true
        return textSettingsCell
    }

    @JvmStatic
    fun createHeaderCell(context: Context?): HeaderCell {
        val header = HeaderCell(context)
        header.view?.setBackgroundColor(ThemeColors.getBackgroundWhiteOrBlueColor())
        return header
    }

    @JvmStatic
    fun createTextInfoCell(context: Context?): TextInfoCell {
        val textInfoCell = TextInfoCell(context!!)
        textInfoCell.setBackgroundColor(ThemeColors.getBackgroundGrayColor())
        return textInfoCell
    }
}
