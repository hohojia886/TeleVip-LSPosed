package com.my.televip.settings

import com.my.televip.ClientChecker
import com.my.televip.Clients.Turrit
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.settings.controller.SettingsController
import com.my.televip.settings.hook.SettingsHook
import com.my.televip.settings.ui.SettingsActivity
import com.my.televip.utils.Utils
import de.robv.android.xposed.XposedHelpers

object SettingsManager {

    @JvmStatic
    fun init(settingsController: SettingsController) {
        SettingsActivity.init(settingsController)

        val settingsActivityClass = XposedHelpers.findClassIfExists(
            AutomationResolver.resolve("org.telegram.ui.SettingsActivity"),
            Utils.classLoader
        )

        val settingCellFactoryClass = XposedHelpers.findClassIfExists(
            AutomationResolver.resolve("org.telegram.ui.SettingsActivity\$SettingCell\$Factory"),
            Utils.classLoader
        )

        val settings = SettingsHook()
        if (settingsActivityClass != null && settingCellFactoryClass != null) {
            settings.newSettings(settingsActivityClass, settingCellFactoryClass, settingsController)
        } else {
            settings.oldSettings(settingsController)
        }
        if (ClientChecker.check(ClientChecker.ClientType.ForkgramClassic)) {
            settings.oldSettings(settingsController)
        }
        if (ClientChecker.check(ClientChecker.ClientType.Turrit)) {
            Turrit.showGhostModeDialog(settingsController)
        }
    }
}
