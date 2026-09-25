package com.my.televip.settings

import com.my.televip.clients.ClientManager
import com.my.televip.clients.Turrit
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.settings.controller.SettingsController
import com.my.televip.settings.hook.SettingsHook
import com.my.televip.settings.ui.SettingsActivity
import com.my.televip.utils.Utils
import de.robv.android.xposed.XposedHelpers

object SettingsManager {

    @JvmStatic
    fun init(settingsController: SettingsController?) {
        if (settingsController == null) return

        SettingsActivity.init(settingsController)

        val settingsActivityClass = XposedHelpers.findClassIfExists(
            Obfuscate.getClassName("org.telegram.ui.SettingsActivity"),
            Utils.classLoader
        )

        val factoryClass = XposedHelpers.findClassIfExists(
            Obfuscate.getClassName("org.telegram.ui.SettingsActivity\$SettingCell\$Factory"),
            Utils.classLoader
        )

        val settings = SettingsHook()
        if (settingsActivityClass != null && factoryClass != null) {
            settings.newSettings(settingsActivityClass, factoryClass, settingsController)
        } else {
            settings.oldSettings(settingsController)
        }
        if (ClientManager.`is`(ClientManager.Client.ForkgramClassic)) {
            settings.oldSettings(settingsController)
        }
        if (ClientManager.`is`(ClientManager.Client.Turrit)) {
            Turrit.showGhostModeDialog(settingsController)
        }
    }
}
