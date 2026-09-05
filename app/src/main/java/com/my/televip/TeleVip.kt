package com.my.televip

import android.content.Context
import com.my.televip.Configs.ConfigManager
import com.my.televip.application.AndroidUtilities
import com.my.televip.dex.DexInjector
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.settings.SettingsManager
import com.my.televip.settings.controller.SettingsController
import com.my.televip.utils.Utils
import com.my.televip.virtuals.TeleVip.Bridge.Bridge

object TeleVip {

    @JvmStatic
    fun startHook(context: Context) {
        try {
            AutomationResolver.resolverRegistry.loadParameter()
            Translator.init()
            AndroidUtilities.init(context)
            val classLoader = Utils.classLoader
            if (classLoader != null) {
                DexInjector.injectDex(context, classLoader)
            }

            val settingsController = SettingsController(context)

            Bridge.init(settingsController)
            ConfigManager.loadAndRead(context)
            SettingsManager.init(settingsController)
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }
}
