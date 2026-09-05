package com.my.televip.settings.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Process
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.my.televip.Configs.ConfigItem
import com.my.televip.Configs.ConfigManager
import com.my.televip.audio
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.settings.controller.SettingsController
import com.my.televip.utils.DialogUtils
import com.my.televip.utils.Utils
import com.my.televip.virtuals.Theme
import com.my.televip.virtuals.androidx.ViewHolder
import com.my.televip.virtuals.messenger.browser.Browser
import com.my.televip.virtuals.ui.Cells.ExpandableTextCheckCell
import com.my.televip.virtuals.ui.Cells.HeaderCell
import com.my.televip.virtuals.ui.Cells.TextCheckCell
import com.my.televip.virtuals.ui.Cells.TextInfoCell
import com.my.televip.virtuals.ui.Cells.TextSettingsCell
import de.robv.android.xposed.XposedHelpers

object SettingsAdapter {

    private var isLongText = false

    @JvmStatic
    fun getRow(position: Int): Int {
        return ConfigManager.items[position].type
    }

    @JvmStatic
    fun getRowCount(): Int {
        return ConfigManager.items.size
    }

    @JvmStatic
    fun onBindViewHolder(holder: Any, settingsController: SettingsController, position: Int, viewType: Int) {
        try {
            val item = ConfigManager.items[position]

            when (viewType) {
                ConfigItem.HEADER -> {
                    val headerCell = HeaderCellHolder(holder)
                    headerCell.cell.setText(Translator.get(item.key ?: ""))
                }
                ConfigItem.SWITCH -> {
                    val textCheck = TextCheckCellHolder(holder)
                    val value = item.value
                    val key = item.key ?: ""
                    if (value != null) {
                        textCheck.cell.setTextAndValueAndCheck(
                            Translator.get(key),
                            value,
                            item.isEnable,
                            true,
                            false
                        )
                    } else if (item.isRestartRequired) {
                        textCheck.cell.setTextAndValueAndCheck(
                            Translator.get(key),
                            Translator.get(Keys.RestartRequired),
                            item.isEnable,
                            true,
                            false
                        )
                    } else {
                        textCheck.cell.setTextAndCheck(
                            Translator.get(key),
                            item.isEnable,
                            false
                        )
                    }
                    val tv = textCheck.cell.textView
                    tv.setLines(0)
                    tv.maxLines = 0
                    tv.isSingleLine = false
                    tv.ellipsize = null
                }
                ConfigItem.EXPANDABLE_SWITCH -> {
                    val expandableTextCheck = ExpandableTextCheckCellHolder(holder)
                    expandableTextCheck.cell.addChildren(item)
                }
                ConfigItem.TEXT -> {
                    val settingsCell = TextSettingsCellHolder(holder)
                    val key = item.key ?: ""
                    if (key == Keys.Calendar) {
                        val calendarValue = when (item.getCustomCalendar()) {
                            1 -> Translator.get(Keys.Hijri)
                            2 -> Translator.get(Keys.Persian)
                            else -> Translator.get(Keys.Gregorian)
                        }
                        settingsCell.cell.setTextAndValue(Translator.get(key), calendarValue, false, false)
                    } else {
                        settingsCell.cell.setText(Translator.get(key), false)
                        settingsCell.cell.textView.setTextColor(Theme.getTextBlueColor())
                    }
                }
                ConfigItem.DIVIDER -> {
                    val shadowSectionCell = ShadowSectionCellHolder(holder)
                    shadowSectionCell.cell.setBackgroundColor(Theme.getBackgroundGrayColor())
                }
                ConfigItem.INFO -> {
                    val textInfoCell = TextInfoCellHolder(holder)
                    val textView = textInfoCell.text.textView
                    val key = item.key ?: ""
                    if (key == Keys.OfflineVisibilityInfo) {
                        textView.maxLines = 2
                        textView.ellipsize = TextUtils.TruncateAt.END
                        textView.text = Translator.get(Keys.OfflineVisibilityInfo)
                        if (textView.maxLines == 2) {
                            isLongText = false
                        }
                        textView.setOnClickListener {
                            if (!isLongText) {
                                textView.maxLines = Int.MAX_VALUE
                                textView.ellipsize = null
                                isLongText = true
                            } else {
                                textView.maxLines = 2
                                textView.ellipsize = TextUtils.TruncateAt.END
                                textView.text = Translator.get(Keys.OfflineVisibilityInfo)
                                isLongText = false
                            }
                        }
                    }
                }
            }

            val viewHolder = ViewHolder(holder)
            viewHolder.itemView.setOnLongClickListener {
                playAudio(settingsController.getContext())
                true
            }

            viewHolder.itemView.setOnClickListener {
                if (viewType == ConfigItem.SWITCH) {
                    val textCheck = TextCheckCellHolder(holder)
                    val checked = !textCheck.cell.isChecked
                    textCheck.cell.setChecked(checked)
                    item.isEnable = checked
                    item.run()
                } else if (viewType == ConfigItem.TEXT) {
                    when (item.key) {
                        Keys.DeveloperChannel -> {
                            Browser.openUrl(settingsController.getContext(), "https://t.me/t_l0_e")
                            settingsController.hide()
                        }
                        Keys.RestartApp -> {
                            val context = settingsController.getContext()
                            val pkg = Utils.pkgName
                            if (pkg != null) {
                                val intent = context.packageManager.getLaunchIntentForPackage(pkg)
                                if (intent != null) {
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    context.startActivity(intent)
                                }
                            }
                            (context as? Activity)?.finishAffinity()
                            Process.killProcess(Process.myPid())
                        }
                        Keys.Calendar -> {
                            val context = settingsController.getContext() as? Activity ?: return@setOnClickListener
                            val dlg = DialogUtils.createSingleChoiceDialog(
                                context,
                                arrayOf(
                                    Translator.get(Keys.Gregorian),
                                    Translator.get(Keys.Hijri),
                                    Translator.get(Keys.Persian)
                                ),
                                Translator.get(Keys.Calendar),
                                item.getCustomCalendar()
                            ) { _, which ->
                                item.setCustomCalendar(which)
                                item.run()
                                val activity = settingsController.settingsActivity
                                activity?.listView?.getAdapter()?.notifyItemChanged(position)
                            }
                            dlg.show()
                        }
                    }
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }

    class HeaderCellHolder(obj: Any) {
        @JvmField val cell: HeaderCell = HeaderCell(XposedHelpers.getObjectField(obj, "headerCell"))
    }

    class TextCheckCellHolder(obj: Any) {
        @JvmField val cell: TextCheckCell = TextCheckCell(XposedHelpers.getObjectField(obj, "textCheckCell"))
    }

    class ExpandableTextCheckCellHolder(obj: Any) {
        @JvmField val cell: ExpandableTextCheckCell = XposedHelpers.getObjectField(obj, "expandableTextCheckCell") as ExpandableTextCheckCell
    }

    class TextSettingsCellHolder(obj: Any) {
        @JvmField val cell: TextSettingsCell = TextSettingsCell(XposedHelpers.getObjectField(obj, "textSettingsCell"))
    }

    class ShadowSectionCellHolder(obj: Any) {
        @JvmField val cell: View = XposedHelpers.getObjectField(obj, "view") as View
    }

    class TextInfoCellHolder(obj: Any) {
        @JvmField val text: TextInfoCell = XposedHelpers.getObjectField(obj, "view") as TextInfoCell
    }

    @JvmStatic
    fun playAudio(context: Context) {
        if (audio.playing) {
            audio.stop()
        } else {
            audio.start()
            DialogUtils.showQuranAlert(context)
        }
    }
}
