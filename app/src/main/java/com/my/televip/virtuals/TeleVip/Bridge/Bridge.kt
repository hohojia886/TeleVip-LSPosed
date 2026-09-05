package com.my.televip.virtuals.TeleVip.Bridge

import android.R
import android.content.Context
import android.util.TypedValue
import android.view.View
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.base.AbstractMethodHook
import com.my.televip.dex.DexInjector
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.settings.controller.SettingsController
import com.my.televip.settings.ui.SettingsAdapter
import com.my.televip.virtuals.Theme
import com.my.televip.virtuals.ui.Cells.ExpandableTextCheckCell
import com.my.televip.virtuals.ui.Cells.HeaderCell
import com.my.televip.virtuals.ui.Cells.ShadowSectionCell
import com.my.televip.virtuals.ui.Cells.TextCheckCell
import com.my.televip.virtuals.ui.Cells.TextInfoCell
import com.my.televip.virtuals.ui.Cells.TextSettingsCell
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object Bridge {

    @JvmField
    val outValue: TypedValue = TypedValue()

    @JvmStatic
    fun getLayoutManager(context: Context): Any? {
        val loader = DexInjector.classLoader ?: return null
        return XposedHelpers.callStaticMethod(
            ClassLoad.getClass(ClassNames.SETTINGS_ADAPTER, loader),
            "getLayoutManager",
            context
        )
    }

    @JvmStatic
    fun init(settingsController: SettingsController) {
        val loader = DexInjector.classLoader ?: return
        try {
            val bridgeClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.Bridge", loader)
            val textCheckCellClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.SettingsAdapter\$TextCheckCellHolder", loader)
            val expandableTextCheckCellClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.SettingsAdapter\$ExpandableTextCheckCellHolder", loader)
            val textSettingsCellClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.SettingsAdapter\$TextSettingsCellHolder", loader)
            val headerCellClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.SettingsAdapter\$HeaderCellHolder", loader)
            val shadowSectionCellClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.SettingsAdapter\$ShadowSectionCellHolder", loader)
            val textInfoCellClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.SettingsAdapter\$TextInfoCellHolder", loader)

            HMethod.hookMethod(
                bridgeClass,
                "getRow",
                Int::class.javaPrimitiveType!!,
                object : AbstractMethodHook() {
                    override fun beforeMethod(param: MethodHookParam) {
                        param.result = SettingsAdapter.getRow(param.args[0] as Int)
                    }
                }
            )

            HMethod.hookMethod(
                bridgeClass,
                "log",
                String::class.java,
                object : AbstractMethodHook() {
                    override fun beforeMethod(param: MethodHookParam) {
                        Logger.l(param.args[0] as String)
                    }
                }
            )

            HMethod.hookMethod(
                bridgeClass,
                "getRowCount",
                object : AbstractMethodHook() {
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
                object : AbstractMethodHook() {
                    override fun beforeMethod(param: MethodHookParam) {
                        SettingsAdapter.onBindViewHolder(
                            param.args[0],
                            settingsController,
                            param.args[1] as Int,
                            param.args[2] as Int
                        )
                    }
                }
            )

            settingsController.getContext().theme.resolveAttribute(
                R.attr.selectableItemBackground,
                outValue,
                true
            )

            XposedHelpers.findAndHookConstructor(
                textCheckCellClass,
                View::class.java,
                Any::class.java,
                object : AbstractMethodHook() {
                    override fun beforeMethod(param: MethodHookParam) {
                        val textCheckCell = createTextCheckCell(settingsController.getContext())
                        param.args[0] = textCheckCell.getView()
                        param.args[1] = textCheckCell.textCell
                    }
                }
            )

            XposedHelpers.findAndHookConstructor(
                expandableTextCheckCellClass,
                View::class.java,
                Any::class.java,
                object : AbstractMethodHook() {
                    override fun beforeMethod(param: MethodHookParam) {
                        val expandableTextCheckCell = createExpandableTextCheckCell(settingsController.getContext())
                        param.args[0] = expandableTextCheckCell
                        param.args[1] = expandableTextCheckCell
                    }
                }
            )

            XposedHelpers.findAndHookConstructor(
                textSettingsCellClass,
                View::class.java,
                Any::class.java,
                object : AbstractMethodHook() {
                    override fun beforeMethod(param: MethodHookParam) {
                        val textSettingsCell = createTextSettingsCell(settingsController.getContext())
                        param.args[0] = textSettingsCell.getView()
                        param.args[1] = textSettingsCell.textSettingsCell
                    }
                }
            )

            XposedHelpers.findAndHookConstructor(
                headerCellClass,
                View::class.java,
                Any::class.java,
                object : AbstractMethodHook() {
                    override fun beforeMethod(param: MethodHookParam) {
                        val header = createHeaderCell(settingsController.getContext())
                        param.args[0] = header.getView()
                        param.args[1] = header.headerCell
                    }
                }
            )

            XposedHelpers.findAndHookConstructor(
                shadowSectionCellClass,
                View::class.java,
                object : AbstractMethodHook() {
                    override fun beforeMethod(param: MethodHookParam) {
                        param.args[0] = ShadowSectionCell(settingsController.getContext()).getView()
                    }
                }
            )

            XposedHelpers.findAndHookConstructor(
                textInfoCellClass,
                View::class.java,
                object : AbstractMethodHook() {
                    override fun beforeMethod(param: MethodHookParam) {
                        val textInfoCell = createTextInfoCell(settingsController.getContext())
                        param.args[0] = textInfoCell
                    }
                }
            )
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    @JvmStatic
    fun createTextCheckCell(context: Context): TextCheckCell {
        val textCheckCell = TextCheckCell(context)
        textCheckCell.getView().setBackgroundColor(Theme.getBackgroundWhiteOrBlueColor())
        textCheckCell.getView().setBackgroundResource(outValue.resourceId)
        textCheckCell.getView().isClickable = true
        textCheckCell.getView().isFocusable = true
        return textCheckCell
    }

    @JvmStatic
    fun createExpandableTextCheckCell(context: Context): ExpandableTextCheckCell {
        val expandableTextCheckCell = ExpandableTextCheckCell(context)
        expandableTextCheckCell.setBackgroundColor(Theme.getBackgroundWhiteOrBlueColor())
        expandableTextCheckCell.setBackgroundResource(outValue.resourceId)
        expandableTextCheckCell.setBChildResource(outValue.resourceId)
        expandableTextCheckCell.isClickable = true
        expandableTextCheckCell.isFocusable = true
        return expandableTextCheckCell
    }

    @JvmStatic
    fun createTextSettingsCell(context: Context): TextSettingsCell {
        val textSettingsCell = TextSettingsCell(context)
        textSettingsCell.getView().setBackgroundColor(Theme.getBackgroundWhiteOrBlueColor())
        textSettingsCell.getView().setBackgroundResource(outValue.resourceId)
        textSettingsCell.getView().isClickable = true
        textSettingsCell.getView().isFocusable = true
        return textSettingsCell
    }

    @JvmStatic
    fun createHeaderCell(context: Context): HeaderCell {
        val header = HeaderCell(context)
        header.getView().setBackgroundColor(Theme.getBackgroundWhiteOrBlueColor())
        return header
    }

    @JvmStatic
    fun createTextInfoCell(context: Context): TextInfoCell {
        val textInfoCell = TextInfoCell(context)
        textInfoCell.setBackgroundColor(Theme.getBackgroundGrayColor())
        return textInfoCell
    }
}
