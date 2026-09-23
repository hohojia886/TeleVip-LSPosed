package com.my.televip.settings.ui

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import androidx.recyclerview.widget.RecyclerView
import com.my.televip.Audio
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.language.Translator
import com.my.televip.settings.controller.SettingsController
import com.my.televip.ui.ThemeColors
import com.my.televip.utils.DialogUtils
import com.my.televip.virtuals.TeleVip.Bridge.Bridge
import com.my.televip.virtuals.messenger.browser.Browser
import com.my.televip.virtuals.ui.Cells.TextCheckCell
import com.my.televip.virtuals.ui.Cells.TextSettingsCell
import de.robv.android.xposed.XposedHelpers

object SettingsAdapter {

    private var mediaPlayer: MediaPlayer? = null

    @JvmStatic
    fun playAudio(context: Context?) {
        try {
            if (mediaPlayer == null && context != null) {
                val soundClass = ClassLoad.getClass(ClassNames.THEME)
                val soundId = XposedHelpers.getStaticIntField(soundClass, "key_sound_audio")
                mediaPlayer = MediaPlayer.create(context, soundId)
            }
            mediaPlayer?.start()
        } catch (ignored: Throwable) {
        }
    }

    @JvmStatic
    fun getRow(position: Int): Int {
        return ConfigManager.items[position].type
    }

    @JvmStatic
    fun getRowCount(): Int {
        return ConfigManager.items.size
    }

    @JvmStatic
    fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        settingsController: SettingsController,
        position: Int,
        type: Int
    ) {
        val item = ConfigManager.items[position]

        when (type) {
            0 -> {
                val headerCell = Bridge.getHeaderCell(holder)
                headerCell.setText(Translator.get(item.key ?: ""))
            }
            1 -> {
                val cell = TextCheckCell(Bridge.getTextCheckCell(holder))
                cell.setTextAndCheck(Translator.get(item.key ?: ""), item.isEnable, false)
                cell.view?.setOnClickListener {
                    val newChecked = !cell.isChecked
                    cell.setChecked(newChecked)
                    item.setEnable(newChecked)
                    item.run()
                }
            }
            2 -> {
                val cell = TextSettingsCell(Bridge.getTextSettingsCell(holder))
                if (item.value != null) {
                    cell.setTextAndValue(Translator.get(item.key ?: ""), item.value, false, false)
                } else {
                    cell.setText(Translator.get(item.key ?: ""), false)
                }
                cell.view?.setOnClickListener {
                    if (item.key == "Calendar") {
                        val calendarOptions = arrayOf("Gregorian", "Hijri", "Persian")
                        val currentCal = item.getCustomCalendar()
                        val dialog = DialogUtils.createSingleChoiceDialog(
                            settingsController.context as Activity,
                            calendarOptions,
                            "Calendar",
                            currentCal
                        ) { _, which ->
                            item.setCustomCalendar(which)
                            item.run()
                        }
                        dialog?.show()
                    } else if (item.key == "DeveloperChannel") {
                        Browser.openUrl(settingsController.context, "https://t.me/t_l0_e")
                    } else if (item.key == "RestartApp") {
                        val activity = settingsController.context as? Activity
                        activity?.recreate()
                    }
                }
            }
            3 -> {
                val view = Bridge.getView(holder)
                view?.setBackgroundColor(ThemeColors.getBackgroundGrayColor())
            }
            4 -> {
                val textInfoCell = Bridge.getTextInfoCell(holder)
                textInfoCell?.textView?.text = Translator.get(item.key ?: "")
                textInfoCell?.textView?.setTextColor(ThemeColors.getTextGrayColor())
            }
            5 -> {
                val expandableCell = Bridge.getExpandableTextCheckCell(holder)
                expandableCell?.addChildren(item)
            }
        }
    }
}
