package com.my.televip

import com.my.televip.Configs.ConfigManager
import com.my.televip.application.AndroidUtilities
import com.my.televip.dex.DexInjector
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.settings.SettingsManager
import com.my.televip.settings.controller.SettingsController
import com.my.televip.utils.Utils
import com.my.televip.virtuals.TeleVip.Bridge.Bridge

object TeleVip {

    @JvmStatic
    fun startHook() {
        try {
            ArgsResolver.resolverRegistry.loadParameter()
            Translator.init()
            AndroidUtilities.init()
            DexInjector.injectDex(Utils.classLoader)

            val settingsController = SettingsController()

            Bridge.init(settingsController)
            ConfigManager.loadAndRead()
            SettingsManager.init(settingsController)

        } catch (e: Throwable) {
            Logger.e(e)
        }
    }
}
